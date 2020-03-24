package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 音视频录像回放请求
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9201_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_9201_MessageBody extends MessageBody {

    private static final long serialVersionUID = -2222441901600081391L;

    public static final String MESSAGE_ID = "9201";

    /** 服务器IP地址 **/
    @JsonProperty("ip")
    private String ip;

    /** tcp端口号 **/
    @JsonProperty("tcpPort")
    private int tcpPort;

    /** udp端口号 **/
    @JsonProperty("udpPort")
    private int udpPort;

    /** 逻辑通道号 **/
    @JsonProperty("channelId")
    private int channelId;

    /** 数据类型 **/
    @JsonProperty("dataType")
    private int dataType;

    /** 码流类型 **/
    @JsonProperty("streamType")
    private int streamType;

    /** 存储器类型 **/
    @JsonProperty("storeType")
    private int storeType;

    /** 回放方式 **/
    @JsonProperty("playbackType")
    private int playbackType;

    /** 快进或快退倍数 **/
    @JsonProperty("playTimes")
    private int playTimes;

    /** 起始时间 **/
    @JsonProperty("startTime")
    private String startTime;

    /** 结束时间 **/
    @JsonProperty("endTime")
    private String endTime;

    public final String getIp() {
        return ip;
    }

    public final void setIp(String ip) {
        this.ip = ip;
    }

    public final int getTcpPort() {
        return tcpPort;
    }

    public final void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public final int getUdpPort() {
        return udpPort;
    }

    public final void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public final int getChannelId() {
        return channelId;
    }

    public final void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public final int getDataType() {
        return dataType;
    }

    public final void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public final int getStreamType() {
        return streamType;
    }

    public final void setStreamType(int streamType) {
        this.streamType = streamType;
    }

    public final int getStoreType() {
        return storeType;
    }

    public final void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    public final int getPlaybackType() {
        return playbackType;
    }

    public final void setPlaybackType(int playbackType) {
        this.playbackType = playbackType;
    }

    public final int getPlayTimes() {
        return playTimes;
    }

    public final void setPlayTimes(int playTimes) {
        this.playTimes = playTimes;
    }

    public final String getStartTime() {
        return startTime;
    }

    public final void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public final String getEndTime() {
        return endTime;
    }

    public final void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
