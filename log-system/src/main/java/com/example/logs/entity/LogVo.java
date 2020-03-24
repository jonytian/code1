package com.example.logs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class LogVo extends Log implements Serializable {

    private Device device;

}
