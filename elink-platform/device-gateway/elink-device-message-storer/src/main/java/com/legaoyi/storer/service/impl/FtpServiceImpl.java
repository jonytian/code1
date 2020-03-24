package com.legaoyi.storer.service.impl;

import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.legaoyi.storer.service.DictionaryService;
import com.legaoyi.storer.service.FtpService;

@Service("ftpService")
public class FtpServiceImpl implements FtpService {

	@Autowired
	@Qualifier("dictionaryService")
	private DictionaryService dictionaryService;

	public FTPClient getFtpClient() throws Exception {
		Map<String, Object> server = dictionaryService.getFtpServer();
		if (server == null) {
			throw new Exception("获取FTP服务器失败,请联系管理员进行配置！");
		}
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("utf-8");
		ftpClient.connect((String) server.get("ip"), Integer.parseInt(String.valueOf(server.get("port")))); // 连接ftp服务器
		ftpClient.login((String) server.get("userName"), (String) server.get("password")); // 登录ftp服务器
		int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
		if (!FTPReply.isPositiveCompletion(replyCode)) {
			throw new Exception("连接FTP服务器失败");
		}
		return ftpClient;
	}

}
