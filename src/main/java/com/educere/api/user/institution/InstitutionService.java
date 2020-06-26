package com.educere.api.user.institution;

import com.educere.api.common.enums.AuthProvider;
import com.educere.api.common.enums.RoleType;
import com.educere.api.common.enums.UserType;
import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.entity.Institution;
import com.educere.api.entity.Role;
import com.educere.api.entity.User;
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

@Service
public class InstitutionService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private InstitutionMapper institutionMapper;

    public Institution create(SignUpRequest signUpRequest) {
        Role roleUser = roleService.findByName(RoleType.ROLE_USER);
        Institution institution = institutionMapper.toInstitution(signUpRequest);

        institution.setProvider(AuthProvider.SYSTEM);
        institution.setUserType(UserType.INSTITUTION);
        institution.setReferenceId(generateReferenceId());
        institution.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));
        institution.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return save(institution);
    }

    public Institution getById(Long id){
        return institutionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

    }

    public User updateInstitutionRole(User user) {
        Institution institution = getById(user.getId());
        Role roleUser = roleService.findByName(RoleType.ROLE_USER);
        institution.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));

        return save(institution);
    }

    public Institution save(Institution institution) {
        return institutionRepository.save(institution);
    }


    private UUID generateReferenceId() {
        return UUID.randomUUID();
    }
}
