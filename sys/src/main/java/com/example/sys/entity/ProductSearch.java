package com.example.sys.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ProductSearch {


    /**
     * 开始日期
     */
    private String insertTimeStart;
    /**
     * 结束日期
     */
    private String insertTimeEnd;
    /**
     * 用户id
     */
    private Integer id;

    /**
     * 日期
     */
    private String date;

    /**
     * 箱号
     */
    private String case_number;

    /**
     *  产品类型
     */
    private String product_type;


    /**
     *  主板SN
     */
    private String motherboard_sn;

    /**
     *  整机SN
     */
    private String machine_sn;

    /**
     *  imei_1
     */
    private String imei_1;

    /**
     *  imei_2
     */
    private String imei_2;


    /**
     *  ICCID1
     */
    private String ICCID1;

    /**
     *  ICCID2
     */
    private String ICCID2;


    /**
     *  BT
     */
    private String BT;

    /**
     *  WiFi
     */
    private String WiFi;


    /**
     *  老化测试温度
     *
     */
    private String aging_test_temperature;

    /**
     *  软件版本
     */
    private String software_version;

    /**
     *  T1_CID
     */
    private String T1_CID;

    /**
     *  T2_CID
     */
    private String T2_CID;


    /**
     *  主板id
     */
    private String motherboard_id;


    /**
     *  出货地址
     */
    private String shipping_address;


    /**
     *  主摄像头
     */
    private String main_camera;


    /**
     *  副摄像头
     */
    private String secondary_camera;

    /**
     *  USB摄像头
     */
    private String usb_camera;


    /**
     *  AHD_A通道
     */
    private String AHD_A_channel;


    /**
     *  AHD_B通道
     */
    private String AHD_B_channel;


    /**
     *  AHD_C通道
     */
    private String AHD_C_channel;

    /**
     *  AHD_D通道
     */
    private String AHD_D_channel;


    /**
     *  触摸屏
     */
    private String touch_screen;


    /**
     *  副触摸屏
     */
    private String secondary_touch_screen;


    /**
     * 第三触摸屏
     */
    private String third_touch_screen;


    /**
     *  显示屏
     */
    private String display;


    /**
     *  副显示屏
     */
    private String secondary_display;

    /**
     *  第三显示屏
     */
    private String third_display;

    /**
     *  fm发射
     */
    private String fm_launch;


    /**
     *  加速传感计
     */
    private String accelerometer;



    /**
     *  陀螺仪传感器
     */
    private String gyro_sensor;


    /**
     *  电子罗盘传感器
     */
    private String electronic_compass_sensor;



    /**
     *  光感和接近传感器
     */
    private String light_perception_and_proximity_sensor;

    /**
     *  上传时间
     */
    private Date createTime;


}
