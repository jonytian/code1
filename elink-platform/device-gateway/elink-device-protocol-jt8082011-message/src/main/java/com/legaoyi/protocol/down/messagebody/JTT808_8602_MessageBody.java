package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置矩形区域，例子 List<Map<String,Object>> areaList = new ArrayList<Map<String,Object>>();
 * Map<String,Object> map =new HashMap<String,Object>(); map.put("areaId", 1); //区域id<br/>
 * map.put("attribute", "0111110000000000"); //区域属性<br/>
 * map.put("topLeftLng", 40.025d);//左上点纬度<br/>
 * map.put("topLeftLat", 116.05678d);//左上点经度<br/>
 * map.put("bottomRightLng", 39.58d);//右下点纬度<br/>
 * map.put("bottomRightLat", 116.755d);//右下点经度<br/>
 * map.put("startTime", "2014-10-22 22:00:02");//起始时间<br/>
 * map.put("endTime", "2014-11-22 23:00:02");//结束时间<br/>
 * map.put("limitedSpeed",100);//最高速度<br/>
 * map.put("durationTime", 10);//超速持续时间<br/>
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8602_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8602_MessageBody extends JTT808_8600_MessageBody {

    private static final long serialVersionUID = -3969201888475098320L;

    public static final String MESSAGE_ID = "8602";
}
