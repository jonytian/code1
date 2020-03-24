package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.AppRepository;
import com.reacheng.rc.dao.ApplicationRepository;
import com.reacheng.rc.dao.ProjectRepository;
import com.reacheng.rc.entity.AppLog;
import com.reacheng.rc.entity.Application;
import com.reacheng.rc.entity.Project;
import com.reacheng.rc.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service("applicationService")
public class AppServiceImpl extends BaseServiceImpl <Application, Integer> implements AppService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public List<Application> findByProjectName(String projectName) {
        return applicationRepository.findByProjectName(projectName);
    }

    @Override
    public List<Application> findByProjectNameAndStatus(String projectName, String handleStatus) {
        return applicationRepository.findByProjectNameAndStatus(projectName,handleStatus);
    }

    @Override
    public List<Application> findByAll(String packageName, String projectName, String imei) {
        return applicationRepository.findByAll(packageName,projectName,imei,"待审核");
    }
    @Override
    public List<Application> findByAll( String projectName, String imei) {
        return applicationRepository.findByAll(projectName,imei,"待审核");
    }

    @Override
    public Application findByPackageNameAndProjectNameAndVersion(String packageName, Integer projectId, String version) {
        return applicationRepository.findByPackageNameAndProjectNameAndVersion ( packageName, projectId, version);
    }


    @Override
    public Page<Application> findAllApp(String key, String projectName,String status, Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Specification<Application> specification = (Specification<Application>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(key)) {
                Predicate p1 =criteriaBuilder.like(root.get("appName"), "%" + key + "%");
                list.add(p1);
            }

            if (!StringUtils.isEmpty(status)) {
                Predicate p2 =criteriaBuilder.equal(root.get("status"),status);
                list.add(p2);
            }

            if (!StringUtils.isEmpty(projectName)){
                //两张表关联查询
                Join<Application,Project> join = root.join(root.getModel().getSingularAttribute("project",Project.class), JoinType.LEFT);
                list.add(criteriaBuilder.like(join.get("projectName").as(String.class), "%" + projectName + "%"));
            }


            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        };
        return applicationRepository.findAll(specification, pageable);

    }


}
