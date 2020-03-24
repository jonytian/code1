package com.example.logs.service;


import com.example.logs.entity.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Slf4j
@Service
public class AsyncTaskService {

    @Autowired
    private DeviceService deviceService;

}
