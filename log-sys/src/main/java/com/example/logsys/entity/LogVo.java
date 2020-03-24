package com.example.logsys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LogVo extends Log implements Serializable {

    private Device device;

}
