package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.down.messagebody.JTT808_8701_2013_MessageBody;
import com.legaoyi.protocol.message.MessageBody;
/**
 * 设置状态量配置信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8701_84H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8701_84H_2013_MessageBody extends JTT808_8701_2013_MessageBody {

    private static final long serialVersionUID = -1965386260107206087L;

    /** D0 的状态信号名称 **/
    @JsonProperty("d0Name")
    private String d0Name;

    /** D1 的状态信号名称 **/
    @JsonProperty("d1Name")
    private String d1Name;

    /** D2 的状态信号名称 **/
    @JsonProperty("d2Name")
    private String d2Name;

    /** D3 的状态信号名称 **/
    @JsonProperty("d3Name")
    private String d3Name;

    /** D4 的状态信号名称 **/
    @JsonProperty("d4Name")
    private String d4Name;

    /** D5 的状态信号名称 **/
    @JsonProperty("d5Name")
    private String d5Name;

    /** D6 的状态信号名称 **/
    @JsonProperty("d6Name")
    private String d6Name;

    /** D7 的状态信号名称 **/
    @JsonProperty("d7Name")
    private String d7Name;

    public final String getD0Name() {
        return d0Name;
    }

    public final void setD0Name(String d0Name) {
        this.d0Name = d0Name;
    }

    public final String getD1Name() {
        return d1Name;
    }

    public final void setD1Name(String d1Name) {
        this.d1Name = d1Name;
    }

    public final String getD2Name() {
        return d2Name;
    }

    public final void setD2Name(String d2Name) {
        this.d2Name = d2Name;
    }

    public final String getD3Name() {
        return d3Name;
    }

    public final void setD3Name(String d3Name) {
        this.d3Name = d3Name;
    }

    public final String getD4Name() {
        return d4Name;
    }

    public final void setD4Name(String d4Name) {
        this.d4Name = d4Name;
    }

    public final String getD5Name() {
        return d5Name;
    }

    public final void setD5Name(String d5Name) {
        this.d5Name = d5Name;
    }

    public final String getD6Name() {
        return d6Name;
    }

    public final void setD6Name(String d6Name) {
        this.d6Name = d6Name;
    }

    public final String getD7Name() {
        return d7Name;
    }

    public final void setD7Name(String d7Name) {
        this.d7Name = d7Name;
    }

}
