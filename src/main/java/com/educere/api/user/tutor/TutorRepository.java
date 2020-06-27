package com.educere.api.user.tutor;

import com.educere.api.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findById(Long id);

    Optional<Tutor> findByEmail(String email);
}
