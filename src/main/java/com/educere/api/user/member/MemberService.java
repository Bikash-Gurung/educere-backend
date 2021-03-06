package com.educere.api.user.member;

import com.educere.api.common.enums.AuthProvider;
import com.educere.api.common.enums.ContactType;
import com.educere.api.common.enums.RoleType;
import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.entity.Member;
import com.educere.api.entity.Role;
import com.educere.api.user.auth.dto.CompleteSignupRequest;
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.member.dto.MemberResponse;
import com.educere.api.user.role.RoleService;
import com.educere.api.user.verification.ContactVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class MemberService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ContactVerificationService contactVerificationService;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", id));
    }

    public Member findByReferenceId(UUID referenceId) {
        return memberRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", referenceId));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "id", email));
    }

    public Boolean isEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member create(SignUpRequest signUpRequest) {
        Role roleUser = roleService.findByName(RoleType.ROLE_USER);
        Member member = memberMapper.toMember(signUpRequest);

        member.setProvider(AuthProvider.SYSTEM);
        member.setReferenceId(generateReferenceId());
        member.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));
        member.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return save(member);
    }

    public Member updateOauth2Member(CompleteSignupRequest completeSignupRequest, Member member) {
        Role roleUser = roleService.findByName(RoleType.ROLE_USER);
        member.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));

        return save(member);
    }

    public Member save(Member sender) {
        return memberRepository.save(sender);
    }

    public MemberResponse getCurrentMember(String email) {
        Member member = findByEmail(email);

        return memberMapper.toMemberResponse(member);
    }

    public List<MemberResponse> getLockedSenders() {
        List<Member> senders = memberRepository.findAllByLocked(true);

        return memberMapper.toMemberResponseList(senders);
    }

    @Transactional
    public void unLock(UUID referenceId) {
        Member member = findByReferenceId(referenceId);

        if (!member.isEmailVerified())
            contactVerificationService.resetResendAttempts(member, ContactType.EMAIL);

        member.setLoginAttempts(0);
        member.setLocked(false);
        memberRepository.save(member);
    }

    private UUID generateReferenceId() {
        return UUID.randomUUID();
    }
}