package com.example.logsys.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by tyj on 2019/08/14.
 */

@Data
public class DeviceVoSearch implements Serializable {

	private Integer page;

	private Integer limit;

	private String exception;

	private String insertTimeStart;

	private String insertTimeEnd;

}
