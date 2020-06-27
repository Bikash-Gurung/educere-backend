package com.educere.api.experties;

import com.educere.api.entity.Experties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertiesRepository extends JpaRepository<Experties, Long> {
}
