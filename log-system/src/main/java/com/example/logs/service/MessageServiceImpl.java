package com.example.logs.service;


import com.example.logs.entity.Message;
import com.example.logs.entity.MessageSearch;
import com.example.logs.mapper.MessageMapper;
import com.example.logs.util.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tyj on 2019/08/15.
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageMapper messageMapper;


	@Override
	public PageDataResult getMessages(MessageSearch messageSearch, int page, int limit) {
		// 时间处理
//		if (null != userSearch) {
//			if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isEmpty(userSearch.getInsertTimeEnd())) {
//				userSearch.setInsertTimeEnd(DateUtil.format(new Date()));
//			} else if (StringUtils.isEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
//				userSearch.setInsertTimeStart(DateUtil.format(new Date()));
//			}
//			if (StringUtils.isNotEmpty(userSearch.getInsertTimeStart())
//					&& StringUtils.isNotEmpty(userSearch.getInsertTimeEnd())) {
//				if (userSearch.getInsertTimeEnd().compareTo(
//						userSearch.getInsertTimeStart()) < 0) {
//					String temp = userSearch.getInsertTimeStart();
//					userSearch
//							.setInsertTimeStart(userSearch.getInsertTimeEnd());
//					userSearch.setInsertTimeEnd(temp);
//				}
//			}
//		}
		PageDataResult pdr = new PageDataResult();
		PageHelper.startPage(page, limit);
		List<Message> prList = messageMapper.getMessages(messageSearch);
		// 获取分页查询后的数据
		PageInfo<Message> pageInfo = new PageInfo<>(prList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
		pdr.setList(prList);
		return pdr;
	}

    @Override
    public int saveMessage(Message message) {
        return messageMapper.insert(message);
    }


}
