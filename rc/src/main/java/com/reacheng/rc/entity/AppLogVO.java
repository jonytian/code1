package com.reacheng.rc.entity;

import com.reacheng.rc.tools.ExcelColumn;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class AppLogVO {


    @ExcelColumn(value = "IMEI",col = 1)
    private String imei;

    @ExcelColumn(value = "项目名",col = 2)
    private String projectName;

    @ExcelColumn(value = "应用包名",col = 3)
    private String packageName;

    @ExcelColumn(value = "版本CODE",col = 4)
    private String versionCode;

    @ExcelColumn(value = "状态",col = 5)
    private String status;



}
