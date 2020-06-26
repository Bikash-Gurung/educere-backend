package com.educere.api.user.role;

import com.educere.api.common.enums.RoleType;
import com.educere.api.common.exception.ResourceNotFoundException;
import com.educere.api.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(RoleType role) {
        return roleRepository.findByName(role).orElseThrow(() -> new ResourceNotFoundException("Role", "name", role));
    }

    public Boolean existsByName(RoleType role) {
        return roleRepository.existsByName(role);
    }
}
