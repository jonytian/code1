package com.pjb.springbootjjwt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jinbin
 * @date 2018-07-08 20:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    Integer Id;
    String imei;
    String projectName;
    String packageName;
}
