package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.ProjectRepository;
import com.reacheng.rc.entity.Project;
import com.reacheng.rc.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("projectService")
public class ProjectServiceImpl extends BaseServiceImpl <Project, Integer> implements ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public Project findByName(String projectName) {
        return  projectRepository.findByName(projectName);
    }
}
