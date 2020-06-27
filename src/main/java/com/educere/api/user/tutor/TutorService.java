package com.educere.api.user.tutor;

import com.educere.api.address.AddressService;
import com.educere.api.common.enums.AuthProvider;
import com.educere.api.common.enums.RoleType;
import com.educere.api.common.enums.UserType;
import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.entity.Address;
import com.educere.api.entity.Role;
import com.educere.api.entity.Tutor;
import com.educere.api.entity.User;
import com.educere.api.user.auth.dto.CompleteSignupRequest;
import com.educere.api.user.auth.dto.CurrentUserResponse;
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.member.dto.UpdateUserRequest;
import com.educere.api.user.role.RoleService;
import com.educere.api.user.tutor.dto.TutorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TutorService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private TutorMapper tutorMapper;

    @Autowired
    private AddressService addressService;

    public Tutor create(SignUpRequest signUpRequest) {
        Role roleGuest = roleService.findByName(RoleType.ROLE_GUEST);
        Tutor tutor = tutorMapper.toTutor(signUpRequest);

        tutor.setFirstName(signUpRequest.getFirstName());
        tutor.setMiddleName(signUpRequest.getMiddleName());
        tutor.setLastName(signUpRequest.getLastName());
        tutor.setProvider(AuthProvider.SYSTEM);
        tutor.setUserType(UserType.TUTOR);
        tutor.setReferenceId(generateReferenceId());
        tutor.setRoles(new ArrayList<>(Collections.singletonList(roleGuest)));
        tutor.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return save(tutor);
    }

    public Tutor getById(Long id) {
        return tutorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public Tutor getByEmail(String email) {
        return tutorRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email",
                email));
    }

    public User updateTutor(CompleteSignupRequest completeSignupRequest, User user) {
        Address address = addressService.create(completeSignupRequest.getAddress());
        Tutor tutor = getById(user.getId());
        tutor.setLinkedin(completeSignupRequest.getLinkedin());
        tutor.setGithub(completeSignupRequest.getGithub());
        tutor.setTwitter(completeSignupRequest.getTwitter());
        tutor.setFacebook(completeSignupRequest.getFacebook());
        tutor.setPhoneOne(completeSignupRequest.getPhoneOne());
        tutor.setPhoneTwo(completeSignupRequest.getPhoneTwo());
        tutor.setPhoneThree(completeSignupRequest.getPhoneThree());
        tutor.setBio(completeSignupRequest.getBio());
        tutor.setDp(completeSignupRequest.getDp());
        tutor.setWall(completeSignupRequest.getWall());
        tutor.setAddress(address);

        Role roleUser = roleService.findByName(RoleType.ROLE_TUTOR);
        tutor.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));

        return save(tutor);
    }

    @Transactional
    public void updateTutorInfo(UpdateUserRequest updateUserRequest, User user){
        Tutor tutor = getById(user.getId());
        tutor.setLinkedin(updateUserRequest.getLinkedin());
        tutor.setGithub(updateUserRequest.getGithub());
        tutor.setTwitter(updateUserRequest.getTwitter());
        tutor.setFacebook(updateUserRequest.getFacebook());
        tutor.setPhoneOne(updateUserRequest.getPhoneOne());
        tutor.setPhoneTwo(updateUserRequest.getPhoneTwo());
        tutor.setPhoneThree(updateUserRequest.getPhoneThree());
        tutor.setBio(updateUserRequest.getBio());
        tutor.setDp(updateUserRequest.getDp());
        tutor.setWall(updateUserRequest.getWall());
        save(tutor);

        addressService.update(updateUserRequest.getAddressRequest(), tutor.getAddress().getId());
    }

    public Tutor save(Tutor tutor) {
        return tutorRepository.save(tutor);
    }


    private UUID generateReferenceId() {
        return UUID.randomUUID();
    }

    public List<TutorResponse> getTutors() {
        return tutorMapper.toTutorResponseList(tutorRepository.findAll());
    }

    public CurrentUserResponse getCurrentTutor(String email) {
        Tutor tutor = getByEmail(email);
        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        currentUserResponse.setBio(tutor.getBio());
        currentUserResponse.setPhoto(tutor.getDp());
        currentUserResponse.setEmail(tutor.getEmail());
        currentUserResponse.setName(tutor.getFullName());
        currentUserResponse.setGithub(tutor.getGithub());
        currentUserResponse.setWebsite(tutor.getWebsite());
        currentUserResponse.setTwitter(tutor.getTwitter());
        currentUserResponse.setLinkedIn(tutor.getLinkedin());
        currentUserResponse.setFacebook(tutor.getFacebook());
        currentUserResponse.setPhoneNumber(tutor.getPhoneOne());
        currentUserResponse.setRoles(tutor.getRoles().stream()
                .map(role -> RoleType.valueOf(role.getName().toString()).toString().split("_")[1])
                .collect(Collectors.toList()));

        return currentUserResponse;
    }
}
