package com.reacheng.rc.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class ApplicationVO implements Serializable {

    private String packageName;

    private String className;

    private Integer type;

    private Integer versionCode;

    private String downloadUrl;

    private String md5;

}
