package com.reacheng.rc.entity;

import com.reacheng.rc.tools.ExcelColumn;
import lombok.Data;
import java.io.Serializable;

@Data
public class AppVO implements Serializable {

    @ExcelColumn(value = "应用名称",col = 1)
    private String appName;

    @ExcelColumn(value = "应用包名",col = 2)
    private String packageName;

    @ExcelColumn(value = "项目名称",col = 3)
    private String projectName;

    @ExcelColumn(value = "MD5",col = 4)
    private String md5;

    @ExcelColumn(value = "状态",col = 5)
    private String status;

    @ExcelColumn(value = "应用启动类名类型",col = 6)
    private Integer type;

    @ExcelColumn(value = "状态",col = 7)
    private Integer classType;

    @ExcelColumn(value = "应用启动类名",col = 8)
    private String className;

    @ExcelColumn(value = "下载地址",col = 9)
    private String downloadUrl;

    @ExcelColumn(value = "处理状态",col = 10)
    private String handleStatus;

    @ExcelColumn(value = "测试IMEI",col = 11)
    private String testImei;

    @ExcelColumn(value = "版本CODE",col = 12)
    private Integer versionCode;

    @ExcelColumn(value = "版本",col = 13)
    private String version;

}
