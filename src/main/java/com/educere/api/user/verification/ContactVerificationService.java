package com.educere.api.user.verification;

import com.educere.api.common.Constants;
import com.educere.api.common.enums.ApplicationEnvironment;
import com.educere.api.common.enums.ContactType;
import com.educere.api.common.enums.ContactVerificationStatus;
import com.educere.api.common.enums.ServerSentEvent;
import com.educere.api.common.exception.BadRequestException;
import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.common.exception.TokenExpiredException;
import com.educere.api.entity.ContactVerification;
import com.educere.api.entity.Member;
import com.educere.api.entity.Tutor;
import com.educere.api.entity.User;
import com.educere.api.redis.AuthTokenService;
import com.educere.api.serversentevent.ServerSentEventService;
import com.educere.api.twilio.TwilioSMSService;
import com.educere.api.twilio.VerificationCodeGenerator;
import com.educere.api.user.UserService;
import com.educere.api.user.member.MemberMailService;
import com.educere.api.user.member.MemberRepository;
import com.educere.api.user.member.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ContactVerificationService {

    private static final Logger logger = LoggerFactory.getLogger(ContactVerificationService.class);

    @Autowired
    private Environment environment;

    @Autowired
    private MemberMailService senderMailService;

    @Autowired
    private ContactVerificationRepository contactVerificationRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TwilioSMSService twilioSmsService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private ServerSentEventService serverSentEventService;

    public void createDeviceVerification(Long userId, ContactType contactType) {
        User user = userService.findById(userId);

        if (!isDeviceVerified(user))
            checkVerificationToken(user, contactType);

        else
            throw new BadRequestException("Your " + contactType.name() + " is already verified.");
    }

    private boolean isDeviceVerified(User user) {
        return user.isEmailVerified();

    }

    private void checkVerificationToken(User user, ContactType contactType) {
        ContactVerification contactVerification = contactVerificationRepository.existsByUserAndType(user, contactType)
                ? checkTwoFAVerificationResendAttempts(user, contactType)
                : createVerificationToken(user, contactType);

        sendVerificationMessage(user, contactVerification.getToken());
    }

    private ContactVerification checkTwoFAVerificationResendAttempts(User user, ContactType contactType) {
        ContactVerification contactVerification = contactVerificationRepository.findByUserIdAndType(user.getId(),
                contactType);

        if (contactVerification.getResendAttempt() >= Constants.RESEND_VERIFICATION_CODE_LIMIT) {
            lockAndInvalidateToken(contactVerification.getUser());

            throw new BadRequestException(Constants.RESEND_VERIFICATION_CODE_LIMIT_EXCEEDED);
        } else {
            return updateVerificationToken(contactVerification, contactType);
        }
    }

    @Transactional
    public ContactVerification createVerificationToken(User user, ContactType contactType) {
        ContactVerification contactVerification = new ContactVerification();

        contactVerification.setUser(user);
        contactVerification.setType(contactType);
        contactVerification.setToken(generateVerificationCode(contactType));
        contactVerification.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        contactVerification.setStatus(ContactVerificationStatus.PENDING);
        contactVerification.setVerificationAttempt(0);

        return contactVerificationRepository.save(contactVerification);
    }

    @Transactional
    public ContactVerification updateVerificationToken(ContactVerification contactVerification,
                                                       ContactType contactType) {
        contactVerification.setToken(generateVerificationCode(contactType));
        contactVerification.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        contactVerification.setVerificationAttempt(0);
        contactVerification.setResendAttempt(contactVerification.getResendAttempt() + 1);

        return contactVerificationRepository.save(contactVerification);
    }

    private void lockAndInvalidateToken(User user) {
        userService.lock(user.getEmail(),
                Constants.RESEND_VERIFICATION_CODE_LIMIT_EXCEEDED);
        authTokenService.deleteAuthTokenByUserId(user.getId());
    }

    @Transactional
    public void resetResendAttempts(Member sender, ContactType contactType) {
        ContactVerification contactVerification =
                contactVerificationRepository.findByUserAndType(sender, contactType).orElseThrow(() -> new ResourceNotFoundException(
                        "TwoFaVerification", "User", sender));
        contactVerification.setResendAttempt(0);
        contactVerificationRepository.save(contactVerification);
    }

    private String generateVerificationCode(ContactType contactType) {
        if (ContactType.PHONE.equals(contactType)) {
            if (environment.acceptsProfiles(Profiles.of(ApplicationEnvironment.PROD.getEnvironment()))) {
                return VerificationCodeGenerator.generate();
            }

            return Constants.DEFAULT_PHONE_VERIFICATION_CODE;
        }


        return VerificationCodeGenerator.generate();
    }

    private void sendVerificationMessage(User user, String token) {
        sendVerificationEmail(user, token);
    }

    private void sendVerificationEmail(User user, String token) {
        senderMailService.sendVerificationMail(user, token);
    }

    @Transactional
    public void verifyToken(Long id, String token, ContactType contactType) {
        ContactVerification contactVerification = contactVerificationRepository.findByUserIdAndType(id, contactType);
        Member member = memberService.findById(id);

        if (contactVerification == null) {
            throw new BadRequestException("User has no verification data");
        }

        if (token.isEmpty()) {
            throw new BadRequestException("Verification token is empty.");
        }

        if (contactVerification.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.info("Verification token: [{}] expired with expiry date [{}] before [{}]", token,
                    contactVerification.getExpiryDate(), LocalDateTime.now());
            throw new TokenExpiredException("Verification token is expired.");
        }

        verifyEmail(contactVerification, token);
        UUID referenceId = memberService.findById(contactVerification.getUser().getId()).getReferenceId();
        serverSentEventService.emitEvent(ServerSentEvent.EMAIL_VERIFIED, referenceId);
    }

    @Transactional
    public void verifyEmail(ContactVerification contactVerification, String token) {
        if (contactVerification.getToken().equals(token)) {
            contactVerification.setVerifiedAt(LocalDateTime.now());
            contactVerification.setStatus(ContactVerificationStatus.VERIFIED);
            contactVerification.getUser().setEmailVerified(true);
            contactVerification.setExpiryDate(LocalDateTime.now());
            contactVerificationRepository.save(contactVerification);
        } else {
            contactVerification.setVerificationAttempt(contactVerification.getVerificationAttempt() + 1);
            contactVerificationRepository.save(contactVerification);

            throw new BadRequestException("Invalid verification token.");
        }
    }
}