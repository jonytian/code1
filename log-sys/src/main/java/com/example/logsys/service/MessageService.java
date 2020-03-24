package com.example.logsys.service;

import com.example.logsys.entity.Message;
import com.example.logsys.entity.MessageSearch;
import com.example.logsys.util.PageDataResult;

/**
 * Created by tyj on 2019/08/15.
 */
public interface MessageService {

	/**
	 * 分页查询消息列表
	 * @param page
	 * @param limit
	 * @return
	 */
	PageDataResult getMessages(MessageSearch messageSearch, int page, int limit);

    int saveMessage(Message message);
}
