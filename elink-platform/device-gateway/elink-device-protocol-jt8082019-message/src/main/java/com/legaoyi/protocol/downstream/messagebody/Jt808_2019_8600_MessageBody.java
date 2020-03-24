package com.legaoyi.protocol.downstream.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 设置圆形区域
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8600_2019" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Jt808_2019_8600_MessageBody extends MessageBody {

    private static final long serialVersionUID = 5704873311863152764L;

    public static final String MESSAGE_ID = "8600";

    /** 设置属性 **/
    @JsonProperty("type")
    private int type;

    /**
     * 区域项列表，key/val值，如 List<Map<String,Object>> areaList = new ArrayList<Map<String,Object>>();<br/>
     * Map<String,Object> map =new HashMap<String,Object>();<br/>
     * map.put("areaId", 1); //区域id<br/>
     * map.put("attribute", "0111110000000000");//区域属性<br/>
     * map.put("centerLng", 39.3d);//中心点经度<br/>
     * map.put("centerLat", 116.3d);//中心点纬度<br/>
     * map.put("radius", 1000);//半径<br/>
     * map.put("startTime", "2014-10-22 22:00:02");//起始时间<br/>
     * map.put("endTime", "2014-11-22 23:00:02");//结束时间<br/>
     * map.put("limitedSpeed",100);//最高速度<br/>
     * map.put("durationTime", 10);//超速持续时间<br/>
     * map.put("nightLimitedSpeed", 10);//夜间最高速度<br/>
     * map.put("name", "高度限速");//区域名称<br/>
     * areaList.add(map);<br/>
     * 
     **/
    @JsonProperty("areaList")
    private List<Map<String, Object>> areaList;

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Map<String, Object>> getAreaList() {
        return areaList;
    }

    public final void setAreaList(List<Map<String, Object>> areaList) {
        this.areaList = areaList;
    }

}
