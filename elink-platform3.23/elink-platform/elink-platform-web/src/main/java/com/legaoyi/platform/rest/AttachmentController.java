package com.legaoyi.platform.rest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.legaoyi.platform.util.HttpClientUtil;

@Controller
@RequestMapping(value = "/attachment")
public class AttachmentController {

    @Value("${platform.api.host}")
    private String apiHost;

    @RequestMapping("/upload")
    public void uploadMedia(HttpServletRequest request, HttpServletResponse response, @RequestParam MultipartFile file) throws Exception {
        Map<?, ?> user = (Map<?, ?>) request.getSession().getAttribute("user");
        String userName = (String) user.get("userAccount");
        String password = (String) user.get("password");
        String url = HttpClientUtil.formatUrl(apiHost, request.getParameter("url"));
        String fileName = file.getOriginalFilename();
        HttpClientUtil.render(response, "application/json", HttpClientUtil.uploadFile(url, userName, password, fileName, file.getInputStream()));
    }

    @RequestMapping("/download")
    public void downloadMedia(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String urlStr = HttpClientUtil.formatUrl(apiHost, request.getParameter("url"));
        Map<?, ?> user = (Map<?, ?>) request.getSession().getAttribute("user");
        String userName = (String) user.get("userAccount");
        String password = (String) user.get("password");

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (null != user && null != password) {
            conn.setRequestProperty("Authorization", "Basic " + HttpClientUtil.base64(userName, password));
        }

        conn.connect();
        InputStream inputStream = conn.getInputStream();
        if (conn.getHeaderFields().get("Content-Disposition") != null) {
            response.setHeader("Content-Disposition", conn.getHeaderFields().get("Content-Disposition").get(0));
        }
        response.setContentType(conn.getContentType());
        ServletOutputStream responseOutputStream = response.getOutputStream();
        int numBytes;
        byte[] buffer = new byte[1024];
        while ((numBytes = inputStream.read(buffer)) != -1) {
            if (numBytes == 0) {
                int singleByte = inputStream.read();
                if (singleByte < 0) {
                    break;
                }
                responseOutputStream.write(singleByte);
                continue;
            }
            responseOutputStream.write(buffer, 0, numBytes);
        }
        responseOutputStream.flush();
    }
}
