package com.legaoyi.management.rest;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.legaoyi.management.model.CarIcon;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.rest.BaseController;

@RestController("officersCarIconController")
@RequestMapping(produces = {"application/json"})
public class CarIconController extends BaseController {

    @Autowired
    private GeneralService service;

    private static final Map<String, String> contentTypeMap = Maps.newHashMap();
    static {
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("gif", "image/gif");
    }

    @GetMapping(value = "/car/icon/{id}")
    public String downLoad(@PathVariable String id, HttpServletResponse response) throws Exception {
        CarIcon icon = (CarIcon) this.service.get(CarIcon.ENTITY_NAME, id);
        if (icon != null) {
            byte[] data = icon.getIcon();
            if (data != null) {
                String fileName = icon.getFileName();
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

    @PostMapping(value = {"/car/icon"})
    public Result upload(@RequestHeader(name = "_enterpriseId") String enterpriseId, @RequestParam Short carColor, @RequestParam Short brandType, @RequestParam Short brandModel, @RequestParam MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        InputStream in = file.getInputStream();
        byte[] data = new byte[in.available()];
        in.read(data);
        CarIcon icon = new CarIcon();
        icon.setFileName(fileName);
        icon.setBrandType(brandType);
        icon.setBrandModel(brandModel);
        icon.setCarColor(carColor);
        icon.setIcon(data);
        icon.setEnterpriseId(enterpriseId);
        this.service.persist(icon);
        return new Result(icon.getId());
    }
}
