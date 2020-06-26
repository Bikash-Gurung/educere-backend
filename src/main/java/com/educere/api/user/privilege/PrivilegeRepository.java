package com.educere.api.user.privilege;

import com.educere.api.common.enums.PrivilegeType;
import com.educere.api.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(PrivilegeType name);
}
