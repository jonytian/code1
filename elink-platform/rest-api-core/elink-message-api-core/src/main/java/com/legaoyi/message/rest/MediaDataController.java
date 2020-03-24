package com.legaoyi.message.rest;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Result;
import com.legaoyi.gateway.message.model.DeviceHistoryMedia;
import com.legaoyi.message.service.MessageService;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.rest.BaseController;

/**
 * @author gaoshengbo
 */
@RestController("mediaDataController")
@RequestMapping(value = "/media", produces = {"application/json"})
public class MediaDataController extends BaseController {

    private static final Map<String, String> contentTypeMap = new HashMap<String, String>();

    static {
        contentTypeMap.put("mp4", "video/mpeg4");
        contentTypeMap.put("mp3", "audio/mp3");
        contentTypeMap.put("wav", "audio/wav");
    }

    @Autowired
    @Qualifier("messageService")
    private MessageService messageService;

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    @RequestMapping(value = "/ftp/fileList/{id}", method = RequestMethod.GET)
    public Result getFtpFileList(@PathVariable String id) throws Exception {
        return new Result(messageService.getUploadFileList(id));
    }

    @RequestMapping(value = "/ftp/download/{id}", method = RequestMethod.GET)
    public void download(@PathVariable String id, @RequestParam(required = false) String fileName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DeviceHistoryMedia deviceHistoryMedia = (DeviceHistoryMedia) service.get(DeviceHistoryMedia.ENTITY_NAME, id);
        if (deviceHistoryMedia == null || deviceHistoryMedia.getFilePath().endsWith(File.separator) && StringUtils.isBlank(fileName)) {
            throw new BizProcessException("非法请求,参数不合法");
        }
        String filePath = deviceHistoryMedia.getFilePath();
        if (!filePath.endsWith(File.separator)) {
            int index = filePath.lastIndexOf(File.separator) + 1;
            fileName = filePath.substring(index);
            filePath = filePath.substring(0, index);
        }

        FTPClient ftpClient = messageService.getFtpClient();
        boolean bool = false;
        try {
            ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            String base = ftpClient.printWorkingDirectory();
            if (ftpClient.changeWorkingDirectory(base.concat(filePath))) {
                response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                response.setContentType(contentTypeMap.get(suffix.toLowerCase()));
                ServletOutputStream responseOutputStream = response.getOutputStream();
                ftpClient.retrieveFile(fileName, responseOutputStream);
                responseOutputStream.flush();
                bool = true;
            }
        } finally {
            ftpClient.logout();
        }

        if (!bool) {
            throw new BizProcessException("下载失败");
        }
    }

}
