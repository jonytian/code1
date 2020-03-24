package com.reacheng.rc.service.impl;

import com.reacheng.rc.dao.GadminRepository;
import com.reacheng.rc.entity.Gadmin;
import com.reacheng.rc.service.GadminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "gadminService")
public class GadminServiceImpl extends BaseServiceImpl<Gadmin,Integer> implements GadminService {

    @Autowired
    GadminRepository gadminRepository;

    @Override
    public Gadmin findByName(String gname) {
        return gadminRepository.findByName(gname);
    }
}
