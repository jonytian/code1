package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.WhitelistRepository;
import com.reacheng.rc.entity.Whitelist;
import com.reacheng.rc.service.WhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("whitelistService")
public class WhitelistServiceImpl extends BaseServiceImpl <Whitelist, Integer> implements WhitelistService {


    @Autowired
    private WhitelistRepository whitelistRepository;

    @Override
    public List<Whitelist> findByProjectIdAndAppId (Integer projectId ,Integer appId){
        return whitelistRepository.findByProjectIdAndAppId(projectId,appId);
    }

    @Override
    public Whitelist findByImeiAndAppId(String imei,Integer appId) {
        return whitelistRepository.findByImeiAndAppId(imei,appId);
    }

    @Override
    public List<Whitelist> findByImei(String imei) {
        return whitelistRepository.findByImei(imei);
    }

    @Override
    public List<Whitelist> findByImeiAndPackageName(String imei, String packageName) {
        return whitelistRepository.findByImeiAndPackageName(imei,packageName);
    }

    @Override
    public Page<Whitelist> findWhiteByProId(Integer projectId,Integer appId, Pageable pageable) {
        return whitelistRepository.findWhiteByProId(projectId,appId,pageable);
    }

    @Override
    public List<Whitelist> findByProjectIdAndStatus(Integer projectId, String status,Integer appId) {
        return whitelistRepository.findByProjectIdAndStatus(projectId,status, appId);
    }

    @Override
    public List<Whitelist> findByImeiAndProjectId(String imei,Integer projectId){
        return whitelistRepository.findByImeiAndProjectId(imei,projectId);
    }
    @Override
    public List<Whitelist> findByImeiAndPackageNameAndProjectId(String imei,String packageName,Integer projectId){
        return whitelistRepository.findByImeiAndPackageNameAndProjectId(imei,packageName,projectId);
    }


}
