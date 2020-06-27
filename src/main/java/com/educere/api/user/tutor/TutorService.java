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
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
        tutor.setMiddleName(signUpRequest.getLastName());
        tutor.setProvider(AuthProvider.SYSTEM);
        tutor.setUserType(UserType.TUTOR);
        tutor.setReferenceId(generateReferenceId());
        tutor.setRoles(new ArrayList<>(Collections.singletonList(roleGuest)));
        tutor.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return save(tutor);
    }

    public Tutor getById(Long id){
        return tutorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
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

    public Tutor save(Tutor tutor) {
        return tutorRepository.save(tutor);
    }


    private UUID generateReferenceId() {
        return UUID.randomUUID();
    }

    public List<Tutor> getTutors() {
        return tutorRepository.findAll();
    }
}
