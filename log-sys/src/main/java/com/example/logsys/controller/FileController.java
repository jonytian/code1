package com.example.logsys.controller;


import com.example.logsys.entity.Log;
import com.example.logsys.entity.LogVo;
import com.example.logsys.service.LogService;
import com.example.logsys.util.DownloadFileUtil;
import com.example.logsys.util.GetServerRealPathUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController extends BaseController{

//    /** 文件存放路径 */
//    @Value("${file.path}")
//    private String path;

    @Autowired
    private LogService logService;

    /**
     * 下载模板
     * @return 返回excel模板
     */
//    @RequestMapping(value = "/download", method = RequestMethod.POST, produces ="application/json;charset=UTF-8")
//    @ResponseBody
//    public Object download(@RequestBody LogVo logVo ){
//        ResponseEntity<InputStreamResource> response = null;
//        try {
//            if (logVo != null){
//                String  filePath = GetServerRealPathUnit.getPath("upload");
//                String fileName = logVo.getFileName();
//                response = DownloadFileUtil.download(filePath,fileName,"log");
//            }else {
//                log.error("没有可供下载的文件！");
//            }
//
//        } catch (Exception e) {
//            log.error("下载失败");
//        }
////        String fileName = logVo.getFileName();
////        String filePath = GetServerRealPathUnit.getPath("upload");
////        // 获取文件全路径
////        File file = new File(filePath  + fileName);
////        // 判断是否存在磁盘中
////        if (file.exists()) {
////            // 设置强制下载不打开
////            response.setContentType("application/force-download");
////            // 设置文件名
////            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
////            byte[] buffer = new byte[1024];
////            FileInputStream fis = null;
////            BufferedInputStream bis = null;
////            try {
////                fis = new FileInputStream(file);
////                bis = new BufferedInputStream(fis);
////                OutputStream os = response.getOutputStream();
////                int i = bis.read(buffer);
////                while (i != -1) {
////                    os.write(buffer, 0, i);
////                    i = bis.read(buffer);
////                }
////                return this.outPutData("下载成功");
////            } catch (Exception e) {
////                e.printStackTrace();
////            } finally {
////                if (bis != null) {
////                    try {
////                        bis.close();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////                if (fis != null) {
////                    try {
////                        fis.close();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            }
////        } else {
////            return this.outPutErr("数据库查询存在，本地磁盘不存在文件");
////        }
////
////
//        return response;
//    }


    @Transactional(readOnly = false,rollbackFor = Exception.class)
    @RequestMapping(value = "/download", method = RequestMethod.POST, produces ="application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<byte[]> serveFile(@RequestBody LogVo logVo) {

        String fileName = logVo.getFileName();
        if(StringUtils.isEmpty(fileName)){
            log.info("************文件不存在**************");
        }
        String filePath = GetServerRealPathUnit.getPath("upload");
        File file = new File(filePath  + fileName);
        HttpHeaders headers = new HttpHeaders();// 设置一个head
        headers.setContentDispositionFormData("attachment", fileName);// 文件的属性，也就是文件叫什么吧
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);// 内容是字节流
        try {
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);// 开始下载
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
            String exceptionName = request.getParameter("exceptionName");
            log.info("******exceptionName："+ exceptionName);
            if (StringUtils.isEmpty(exceptionName)){
                return this.outPutErr("参数有误,异常名称不能为空！");
            }
            String happenTime = request.getParameter("happenTime");
            log.info("******happenTime："+ happenTime);
            if (StringUtils.isEmpty(happenTime)){
                return this.outPutErr("参数有误,异常发生时间不能为空！");
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
            Log localLog = logService.getOne(exceptionName,happenTime);
            if(localLog !=null){
                localLog.setFileName(newFileName);
                localLog.setFilePath(filePath);
                int b = logService.update(localLog);
                if(b==1){
                    log.info("******日志更新成功******");
                }else {
                    log.info("******日志更新失败******");
                }
            }
        log.info("************************上传文件成功****************************");
        return this.outPutData("上传文件成功！");
    }





}
