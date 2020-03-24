package com.example.logsys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DeviceVo extends Device implements Serializable {

    private List<Log> logList;


}
