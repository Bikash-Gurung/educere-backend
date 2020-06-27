package com.educere.api.experties;

import com.educere.api.entity.Experties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertiesRepository extends JpaRepository<Experties, Long> {
    @Query("SELECT e FROM Experties e WHERE e.name like %?1 or e.category like %?1")
    List<Experties> fetchAllExperties(String name, String category);
}
