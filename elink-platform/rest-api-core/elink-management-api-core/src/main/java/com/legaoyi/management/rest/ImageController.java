package com.legaoyi.management.rest;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Result;
import com.legaoyi.management.model.Image;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.rest.BaseController;

@RestController("imageController")
@RequestMapping(produces = {"application/json"})
public class ImageController extends BaseController {

    @Autowired
    @Qualifier("generalService")
    private GeneralService service;

    private static final Map<String, String> contentTypeMap = Maps.newHashMap();
    static {
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("gif", "image/gif");
    }

    @GetMapping(value = "/image/{id}")
    public String downLoad(@PathVariable String id, HttpServletResponse response) throws Exception {
        Image image = (Image) this.service.get(Image.ENTITY_NAME, id);
        if (image != null) {
            byte[] data = image.getImage();
            if (data != null) {
                String fileName = image.getFileName();
                String contentType = contentTypeMap.get(fileName.substring(fileName.indexOf(".") + 1).toLowerCase());
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                response.setContentType(contentType);
                response.setHeader("Content-Disposition", "attachment; filename=".concat(fileName));
                response.setContentLength(data.length);
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(data);
                outputStream.flush();
                return null;
            }
        }
        throw new BizProcessException("no record for this id ".concat(id));
    }

    @PostMapping(value = {"/image"})
    public Result upload(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream in = file.getInputStream();
        byte[] data = new byte[in.available()];
        in.read(data);
        Image image = new Image();
        image.setFileName(fileName);
        image.setImage(data);
        image.setEnterpriseId(enterpriseId);
        this.service.persist(image);
        return new Result(image.getId());
    }
}
