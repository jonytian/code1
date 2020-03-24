package com.reacheng.rc.service;


import com.reacheng.rc.entity.Project;

public interface ProjectService extends BaseService<Project,Integer> {

    Project findByName(String projectName);
}
