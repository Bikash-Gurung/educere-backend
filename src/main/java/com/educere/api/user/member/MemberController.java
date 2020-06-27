package com.educere.api.user.member;

import com.educere.api.common.ListResponse;
import com.educere.api.common.enums.UserType;
import com.educere.api.entity.User;
import com.educere.api.security.CurrentUser;
import com.educere.api.security.UserPrincipal;
import com.educere.api.user.UserService;
import com.educere.api.user.institution.InstitutionService;
import com.educere.api.user.member.dto.MemberResponse;
import com.educere.api.user.member.dto.UpdateUserRequest;
import com.educere.api.user.tutor.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/user")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserService userService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private InstitutionService institutionService;

    @GetMapping("")
    @PreAuthorize("hasRole('USER')")
    public MemberResponse getCurrentMember(@CurrentUser UserPrincipal userPrincipal) {
        return memberService.getCurrentMember(userPrincipal.getEmail());
    }

    @GetMapping("/locked")
    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse lockedSendersList() {
        List<MemberResponse> senderResponses = memberService.getLockedSenders();

        return new ListResponse(senderResponses);
    }

    @GetMapping("/{referenceId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public void unLock(@PathVariable("referenceId") UUID referenceId) {
        memberService.unLock(referenceId);
    }

    @PutMapping()
    @PreAuthorize("hasAnyRole('INSTITUTION','TUTOR')")
    public String  updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest,
                                       @CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        if(user.getUserType().equals(UserType.TUTOR)){
            tutorService.updateTutorInfo(updateUserRequest, user);
        }

        if(user.getUserType().equals(UserType.INSTITUTION)){
            institutionService.updateInstitutionInfo(updateUserRequest, user);
        }

        return "User information updated successfully.";
    }

}