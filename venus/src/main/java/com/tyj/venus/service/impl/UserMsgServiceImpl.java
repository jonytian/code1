package com.tyj.venus.service.impl;


import com.tyj.venus.entity.UserMsg;
import com.tyj.venus.service.UserMsgService;
import org.springframework.stereotype.Service;

@Service("userMsgService")
public class UserMsgServiceImpl extends BaseServiceImpl<UserMsg,Integer> implements UserMsgService {
}
