package com.example.logsys.util;

import lombok.Data;
import java.util.List;

@Data
public class PageDataResult {

	//总记录数量
	private Integer totals;
	//当前页数据列表
	private List<?> list;

	private Integer code = 200;

	public PageDataResult() {
	}

	public PageDataResult( Integer totals, List<?> list) {
		this.totals = totals;
		this.list = list;
	}


}
