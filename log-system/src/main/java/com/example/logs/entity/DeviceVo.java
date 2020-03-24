package com.example.logs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeviceVo extends Device implements Serializable {

    private List<Log> logList;


}
