package com.reacheng.rc.entity;

import com.reacheng.rc.tools.ExcelColumn;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
public class DeviceVO implements Serializable {


    @ExcelColumn(value = "IMEI",col = 1)
    private String imei;

    @ExcelColumn(value = "项目名",col = 2)
    private String projectName;

}
