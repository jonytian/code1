package com.reacheng.rc.service;

import com.reacheng.rc.entity.Gadmin;

public interface GadminService extends BaseService<Gadmin,Integer> {

    Gadmin findByName (String gname);
}
