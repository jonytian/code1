package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.AppLogRepository;
import com.reacheng.rc.entity.AppLog;
import com.reacheng.rc.service.AppLogService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service(value = "appLogService")
public class AppLogServiceImpl extends BaseServiceImpl <AppLog, Integer> implements AppLogService {

    @Autowired
    AppLogRepository appLogRepository;

    @Override
    public AppLog findAppInfo(String imei, String projectName, String packageName ,String versionCode) {
        return appLogRepository.findAppInfo(imei,projectName,packageName,versionCode);
    }


    @Override
    public Page<AppLog> findAllLog(String key, String imei,String status, Integer currentPage, Integer pageSize,Sort sort) {
        Pageable pageable = PageRequest.of(currentPage, pageSize,sort);
        Specification<AppLog> specification = (Specification<AppLog>) (root, query, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            if (!StringUtils.isEmpty(imei)) {
                Predicate p1 =criteriaBuilder.like(root.get("imei"), "%" + imei + "%");
                list.add(p1);
            }

            if (!StringUtils.isEmpty(key)) {
                Predicate p2 = criteriaBuilder.like(root.get("projectName"), "%" + key + "%");
                list.add(p2);
            }
            if (!StringUtils.isEmpty(status)) {
                Predicate p3 =criteriaBuilder.equal(root.get("status"),status);
                list.add(p3);
            }
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        };
        return appLogRepository.findAll(specification, pageable);

    }





}
