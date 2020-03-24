package com.example.sys.entity;

import com.example.sys.util.ExcelColumn;
import lombok.Data;

@Data
public class BusClick {

    @ExcelColumn(value = "日期", col = 1)
    private String date;

    @ExcelColumn(value = "箱号", col = 2)
    private String case_number;

    @ExcelColumn(value = "产品类型", col = 3)
    private String product_type;

    @ExcelColumn(value = "主板SN", col = 4)
    private String motherboard_sn;

    @ExcelColumn(value = "整机SN", col = 5)
    private String machine_sn;

    @ExcelColumn(value = "IMEI 1", col = 6)
    private String imei_1;

    @ExcelColumn(value = "IMEI 2", col = 7)
    private String imei_2;

    @ExcelColumn(value = "ICCID1", col = 8)
    private String ICCID1;

    @ExcelColumn(value = "ICCID 2", col = 9)
    private String ICCID2;

    @ExcelColumn(value = "BT号段", col = 10)
    private String BT;

    @ExcelColumn(value = "WiFi号段", col = 11)
    private String WiFi;

    @ExcelColumn(value = "老化测试温度", col = 12)
    private String aging_test_temperature;

    @ExcelColumn(value = "软件版本", col = 13)
    private String software_version;

    @ExcelColumn(value = "T卡1 CID", col = 14)
    private String T1_CID;

    @ExcelColumn(value = "T卡2 CID", col = 15)
    private String T2_CID;

    @ExcelColumn(value = "主板ID", col = 16)
    private String motherboard_id;

    @ExcelColumn(value = "出货地址", col = 17)
    private String shipping_address;

    @ExcelColumn(value = "主摄像头", col = 18)
    private String main_camera;

    @ExcelColumn(value = "副摄像头", col = 19)
    private String secondary_camera;

    @ExcelColumn(value = "USB摄像头", col = 20)
    private String usb_camera;

    @ExcelColumn(value = "AHD A通道", col = 21)
    private String AHD_A_channel;

    @ExcelColumn(value = "AHD B通道", col = 22)
    private String AHD_B_channel;

    @ExcelColumn(value = "AHD C通道", col = 23)
    private String AHD_C_channel;

    @ExcelColumn(value = "AHD D通道", col = 24)
    private String AHD_D_channel;

    @ExcelColumn(value = "触摸屏", col = 25)
    private String touch_screen;

    @ExcelColumn(value = "副触摸屏", col = 26)
    private String secondary_touch_screen;

    @ExcelColumn(value = "第三触摸屏", col = 27)
    private String third_touch_screen;

    @ExcelColumn(value = "显示屏", col = 28)
    private String display;

    @ExcelColumn(value = "副显示屏", col = 29)
    private String secondary_display;

    @ExcelColumn(value = "第三显示屏", col = 30)
    private String third_display;

    @ExcelColumn(value = "FM发射", col = 31)
    private String fm_launch;

    @ExcelColumn(value = "加速度传感器", col = 32)
    private String accelerometer;

    @ExcelColumn(value = "陀螺仪传感器", col = 33)
    private String gyro_sensor;

    @ExcelColumn(value = "电子罗盘传感器", col = 34)
    private String electronic_compass_sensor;

    @ExcelColumn(value = "光感和接近传感器", col = 35)
    private String light_perception_and_proximity_sensor;


}
