package com.tyj.venus.service.impl;


import com.tyj.venus.entity.Gadmin;
import com.tyj.venus.service.GadminService;
import org.springframework.stereotype.Service;

@Service(value = "gadminService")
public class GadminServiceImpl extends BaseServiceImpl<Gadmin,Integer> implements GadminService {
}
