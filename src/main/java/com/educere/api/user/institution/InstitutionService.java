package com.educere.api.user.institution;

import com.educere.api.address.AddressService;
import com.educere.api.common.enums.AuthProvider;
import com.educere.api.common.enums.RoleType;
import com.educere.api.common.enums.UserType;
import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.entity.Address;
import com.educere.api.entity.Institution;
import com.educere.api.entity.Role;
import com.educere.api.entity.User;
import com.educere.api.user.auth.dto.CompleteSignupRequest;
import com.educere.api.user.auth.dto.CurrentUserResponse;
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.role.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InstitutionService {

    private static final Logger logger = LoggerFactory.getLogger(InstitutionService.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private InstitutionMapper institutionMapper;
    @Autowired
    private AddressService addressService;

    public Institution create(SignUpRequest signUpRequest) {
        Role roleGuest = roleService.findByName(RoleType.ROLE_GUEST);
        Institution institution = institutionMapper.toInstitution(signUpRequest);

        institution.setInstitutionName(signUpRequest.getInstitutionName());
        institution.setProvider(AuthProvider.SYSTEM);
        institution.setUserType(UserType.INSTITUTION);
        institution.setReferenceId(generateReferenceId());
        institution.setRoles(new ArrayList<>(Collections.singletonList(roleGuest)));
        institution.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return save(institution);
    }

    public Institution getById(Long id) {
        return institutionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    }

    public Institution getByEmail(String email) {
        return institutionRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User",
                "email", email));

    }

    public User updateInstitution(CompleteSignupRequest completeSignupRequest, User user) {
        Address address = addressService.create(completeSignupRequest.getAddress());
        Institution institution = getById(user.getId());
        institution.setLinkedin(completeSignupRequest.getLinkedin());
        institution.setGithub(completeSignupRequest.getGithub());
        institution.setTwitter(completeSignupRequest.getTwitter());
        institution.setFacebook(completeSignupRequest.getFacebook());
        institution.setPhoneOne(completeSignupRequest.getPhoneOne());
        institution.setPhoneTwo(completeSignupRequest.getPhoneTwo());
        institution.setPhoneThree(completeSignupRequest.getPhoneThree());
        institution.setBio(completeSignupRequest.getBio());
        institution.setDp(completeSignupRequest.getDp());
        institution.setWall(completeSignupRequest.getWall());
        institution.setAddress(address);

        Role roleUser = roleService.findByName(RoleType.ROLE_INSTITUTION);
        institution.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));

        return save(institution);
    }

    public Institution save(Institution institution) {
        return institutionRepository.save(institution);
    }


    private UUID generateReferenceId() {
        return UUID.randomUUID();
    }

    public CurrentUserResponse getCurrentInstitution(String email) {
        Institution institution = getByEmail(email);
        CurrentUserResponse currentUserResponse = new CurrentUserResponse();
        currentUserResponse.setBio(institution.getBio());
        currentUserResponse.setPhoto(institution.getDp());
        currentUserResponse.setEmail(institution.getEmail());
        currentUserResponse.setGithub(institution.getGithub());
        currentUserResponse.setWebsite(institution.getWebsite());
        currentUserResponse.setTwitter(institution.getTwitter());
        currentUserResponse.setLinkedIn(institution.getLinkedin());
        currentUserResponse.setFacebook(institution.getFacebook());
        currentUserResponse.setPhoneNumber(institution.getPhoneOne());
        currentUserResponse.setName(institution.getInstitutionName());
        currentUserResponse.setRoles(institution.getRoles().stream()
                .map(role -> RoleType.valueOf(role.getName().toString()).toString().split("_")[1])
                .collect(Collectors.toList()));

        return currentUserResponse;
    }
}
