package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.DeviceRepository;
import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.Project;
import com.reacheng.rc.service.DeviceService;
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

@Service("deviceService")
public class DeviceServiceImpl extends BaseServiceImpl <Device, Integer> implements DeviceService {

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public Device findByImei(String imei) {
        return deviceRepository.findByImei(imei);
    }

    @Override
    public Device findByImeiAndProject(String imei, Project project) {
        return deviceRepository.findByImeiAndProject(imei,project);
    }

    @Override
    public Page<Device> findAllDev(String key, String projectName, Integer currentPage, Integer pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Specification<Device> specification = (Specification<Device>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(key)) {
                Predicate p1 =criteriaBuilder.like(root.get("imei"), "%" + key + "%");
                list.add(p1);
            }

            if (!StringUtils.isEmpty(projectName)){
                //两张表关联查询
                Join<Device,Project> join = root.join(root.getModel().getSingularAttribute("project",Project.class), JoinType.LEFT);
                list.add(criteriaBuilder.like(join.get("projectName").as(String.class), projectName));
            }


            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        };
        return deviceRepository.findAll(specification, pageable);

    }

    @Override
    public List<Device> findAllDevice(Project project) {
        return deviceRepository.findAllDevice(project);
    }
}
