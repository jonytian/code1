package com.example.sys.service;

import com.example.sys.entity.BusClick;
import com.example.sys.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
public class AsyncTaskService {

    @Autowired
    private ProductService productService;
    List<Product> insertFail = new ArrayList<>();
    @Async
    public void executeAsyncTask(BusClick o , int index) {
        System.out.println("Thread" + Thread.currentThread().getName() + " run：" + index);
        Product product = new Product();
        product.setDate(o.getDate());
        product.setCase_number(o.getCase_number());
        product.setProduct_type(o.getProduct_type());
        product.setMotherboard_sn(o.getMotherboard_sn());
        product.setMachine_sn(o.getMachine_sn());
        product.setImei_1(o.getImei_1());
        product.setImei_2(o.getImei_2());
        product.setIccid1(o.getICCID1());
        product.setIccid2(o.getICCID2());
        product.setBt(o.getBT());
        product.setWifi(o.getWiFi());
        product.setAging_test_temperature(o.getAging_test_temperature());
        product.setSoftware_version(o.getSoftware_version());
        product.setT1_cid(o.getT1_CID());
        product.setT2_cid(o.getT2_CID());
        product.setMotherboard_id(o.getMotherboard_id());
        product.setShipping_address(o.getShipping_address());
        product.setMain_camera(o.getMain_camera());
        product.setSecondary_camera(o.getSecondary_camera());
        product.setUsb_camera(o.getUsb_camera());
        product.setAhd_a_channel(o.getAHD_A_channel());
        product.setAhd_b_channel(o.getAHD_B_channel());
        product.setAhd_c_channel(o.getAHD_C_channel());
        product.setAhd_d_channel(o.getAHD_D_channel());
        product.setTouch_screen(o.getTouch_screen());
        product.setSecondary_touch_screen(o.getSecondary_touch_screen());
        product.setThird_touch_screen(o.getThird_touch_screen());
        product.setDisplay(o.getDisplay());
        product.setSecondary_display(o.getSecondary_display());
        product.setThird_display(o.getThird_display());
        product.setFm_launch(o.getFm_launch());
        product.setAccelerometer(o.getAccelerometer());
        product.setGyro_sensor(o.getGyro_sensor());
        product.setElectronic_compass_sensor(o.getElectronic_compass_sensor());
        product.setLight_perception_and_proximity_sensor(o.getLight_perception_and_proximity_sensor());
        product.setCreateTime(new Date());
        Integer b = productService.insertProducts(product);
        if(b == 1){
            log.info("成功插入第"+index+"数据");
        }else {
            insertFail.add(product);
            log.info("插入第"+index+"数据失败");
        }
    }

    public List initList(){
       return  insertFail;
    }


}
