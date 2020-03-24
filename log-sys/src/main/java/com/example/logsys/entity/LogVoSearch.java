package com.example.logsys.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by tyj on 2019/08/14.
 */

@Data
public class LogVoSearch  {

	private Integer page;

	private Integer limit;

	private String exception;

	private String description;

	private String projectName;

	private String romVersion;

	private String  imei;

	private String insertTimeStart;

	private String insertTimeEnd;

}
