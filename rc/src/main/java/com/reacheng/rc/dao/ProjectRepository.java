package com.reacheng.rc.dao;

import com.reacheng.rc.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends BaseRepository<Project, Integer> {

    @Query(value = "FROM Project p where p.projectName =:projectName")
    Project findByName(@Param("projectName") String projectName);
}
