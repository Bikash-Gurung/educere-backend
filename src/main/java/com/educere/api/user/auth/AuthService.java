package com.educere.api.user.auth;

import com.educere.api.common.enums.ContactType;
import com.educere.api.common.enums.RoleType;
import com.educere.api.common.enums.UserType;
import com.educere.api.common.exception.BadRequestException;
import com.educere.api.entity.Institution;
import com.educere.api.entity.Member;
import com.educere.api.entity.Tutor;
import com.educere.api.entity.User;
import com.educere.api.redis.AuthToken;
import com.educere.api.redis.AuthTokenService;
import com.educere.api.security.TokenProvider;
import com.educere.api.security.UserPrincipal;
import com.educere.api.user.UserService;
import com.educere.api.user.auth.dto.AccessTokenRequest;
import com.educere.api.user.auth.dto.AccessTokenResponse;
import com.educere.api.user.auth.dto.AuthResponse;
import com.educere.api.user.auth.dto.LoginRequest;
import com.educere.api.user.auth.dto.CompleteSignupRequest;
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.institution.InstitutionService;
import com.educere.api.user.member.MemberMapper;
import com.educere.api.user.member.MemberService;
import com.educere.api.user.member.dto.MemberResponse;
import com.educere.api.user.tutor.TutorService;
import com.educere.api.user.verification.ContactVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ContactVerificationService contactVerificationService;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private InstitutionService institutionService;

    private Logger logger = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse login(LoginRequest loginRequest) {
        String token = authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        User user = userService.findByEmail(loginRequest.getEmail());

        return buildAuthResponse(user, token);
    }

    public AuthResponse signUp(SignUpRequest signUpRequest) {

        if (memberService.isEmailDuplicate(signUpRequest.getEmail())) {
            throw new BadRequestException("User with given email already exists.");
        }

        if (UserType.get(signUpRequest.getUserType()).equals(UserType.TUTOR)) {
            Tutor tutor = tutorService.create(signUpRequest);
            String token = authenticate(signUpRequest.getEmail(), signUpRequest.getPassword());

            contactVerificationService.createDeviceVerification(tutor.getId(), ContactType.EMAIL);

            return buildAuthResponse(tutor, token);
        }

        if (UserType.get(signUpRequest.getUserType()).equals(UserType.INSTITUTION)) {
            Institution institution = institutionService.create(signUpRequest);
            String token = authenticate(signUpRequest.getEmail(), signUpRequest.getPassword());

            contactVerificationService.createDeviceVerification(institution.getId(), ContactType.EMAIL);

            return buildAuthResponse(institution, token);
        }

        throw new BadRequestException("Invalid user type.");
    }

    private String authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return tokenProvider.createAccessToken(userPrincipal.getId());
    }

    public AuthResponse buildAuthResponse(User user, String token) {
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setRoles(user.getRoles().stream()
                .map(role -> RoleType.valueOf(role.getName().toString()).toString().split("_")[1])
                .collect(Collectors.toList()));

        return authResponse;
    }

    public MemberResponse getGuestInfo(Long senderId) {
        Member member = memberService.findById(senderId);

        return memberMapper.toMemberResponse(member);
    }

    public void completeSignUp(CompleteSignupRequest completeSignupRequest,
                                       UserPrincipal userPrincipal) {
        User user = memberService.findById(userPrincipal.getId());
        user = user.getUserType().equals(UserType.TUTOR) ?
                tutorService.updateTutorRole(user) :
                institutionService.updateInstitutionRole(user);
        grantNewAuthentication(user);
        contactVerificationService.createDeviceVerification(user.getId(), ContactType.PHONE);

//        return memberMapper.toMemberResponse(user);
    }

    private void grantNewAuthentication(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
        List<String> privileges = UserPrincipal.getPrivileges(user.getRoles());

        for (String privilege : privileges) {
            updatedAuthorities.add(new SimpleGrantedAuthority(privilege));
        }

        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(),
                updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    public AccessTokenResponse refreshAccessToken(AccessTokenRequest accessTokenRequest) {
        AuthToken authToken = authTokenService.getAuthToken(accessTokenRequest.getReferenceToken());
        String referenceToken = tokenProvider.createAccessToken(authToken.getUserId());

        logger.info("Removing expired pair of auth tokens from redis with referenceId:{}",
                accessTokenRequest.getReferenceToken());
        authTokenService.deleteAuthTokenByReferenceToken(accessTokenRequest.getReferenceToken());

        return new AccessTokenResponse(referenceToken);
    }
}
