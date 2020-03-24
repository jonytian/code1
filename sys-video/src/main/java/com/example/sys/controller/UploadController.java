package com.example.sys.controller;

import com.example.sys.entity.HmVideo;
import com.example.sys.service.HmVideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController extends BaseController {

    @Autowired
    private HmVideoService hmVideoService;

    /**
     * 单文件上传
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam(value="file",required=false) MultipartFile file , HttpServletRequest request){

        log.info("************************开始上传文件****************************");
        String imei = request.getParameter("imei");
        log.info("******imei："+ imei);
        if (StringUtils.isEmpty(imei)){
            return this.outPutErr("参数有误,imei不能为空！");
        }

        if (file == null) {
            return this.outPutErr("上传失败,上传的文件不能为空！");
        }
        // 判断文件是否为空
        if (file.isEmpty()) {
            return this.outPutErr("文件为空");
        }
        // 判断文件是否为空文件
        if (file.getSize() <= 0) {
            return this.outPutErr("文件大小为空，上传失败");
        }

        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        String newFileName  = UUID.randomUUID().toString() + suffixName;
        String filePath= uploadFile(file,newFileName);
        log.info("******文件路径："+ filePath);

        // 保存文件路径
        HmVideo hmVideo = new HmVideo();
        hmVideo.setImei(imei);
        hmVideo.setFileName(newFileName);
        hmVideo.setFilePath(filePath);
        hmVideo.setSerialNumber(UUID.randomUUID().toString());
        hmVideo.setTimeInterval(180);
        hmVideo.setStartTime(new Date());
        hmVideo.setEndTime(new Date());
        hmVideo.setCreateTime(new Date());

        int b = hmVideoService.insert(hmVideo);
        if (b == 1){
            log.info("************************新增文件成功****************************");
        }else {
            log.info("************************新增文件失败****************************");
        }

        log.info("************************上传文件成功****************************");
        return this.outPutData("上传文件成功！");
    }
}
