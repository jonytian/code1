package com.example.logs.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by tyj on 2019/08/14.
 */

@Data
public class MessageSearch implements Serializable {

	private Integer page;

	private Integer limit;

	private String imei;

	private String insertTimeStart;

	private String insertTimeEnd;

}
