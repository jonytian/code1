package com.example.logs.entity;

import lombok.Data;

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
