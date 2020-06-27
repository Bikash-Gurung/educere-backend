package com.educere.api.user.institution;

import com.educere.api.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findById(Long id);

    Optional<Institution> findByEmail(String email);
}
