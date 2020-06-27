package com.educere.api.user.tutor;

import com.educere.api.common.enums.AuthProvider;
import com.educere.api.common.enums.RoleType;
import com.educere.api.common.enums.UserType;
import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.entity.Role;
import com.educere.api.entity.Tutor;
import com.educere.api.user.auth.dto.SignUpRequest;
import com.educere.api.user.role.RoleService;
import com.educere.api.user.tutor.dto.TutorResponse;
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

    public Tutor create(SignUpRequest signUpRequest) {
        Role roleUser = roleService.findByName(RoleType.ROLE_GUEST);
        Tutor tutor = tutorMapper.toTutor(signUpRequest);

        tutor.setProvider(AuthProvider.SYSTEM);
        tutor.setUserType(UserType.TUTOR);
        tutor.setReferenceId(generateReferenceId());
        tutor.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));
        tutor.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return save(tutor);
    }

    public Tutor getById(Long id){
        return tutorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
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
}
