package com.educere.api.experties;

import com.educere.api.entity.Experties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertiesRepository extends JpaRepository<Experties, Long> {

    @Query("select u from Experties u where u.name like %:name% or u.category like %:category%")
    List<Experties> fetchAllExperties(@Param("name") String name, @Param("category") String category);
}
