package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 上传终端音视频属性
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2018-04-09
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "1003_2016" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT1078_1003_MessageBody extends MessageBody {

    private static final long serialVersionUID = 2930376913154134865L;

    public static final String MESSAGE_ID = "1003";

    /** 输入音频编码方式 **/
    @JsonProperty("audioCoding")
    private int audioCoding;

    /** 输入音频声道数量 **/
    @JsonProperty("inputChlNum")
    private int inputChlNum;

    /** 输入音频采样率 **/
    @JsonProperty("samplingRate")
    private int samplingRate;

    /** 输入音频采样位数 **/
    @JsonProperty("samplingNum")
    private int samplingNum;

    /** 音频帧长度 **/
    @JsonProperty("frameLen")
    private int frameLen;

    /** 是否支持音频输出 **/
    @JsonProperty("output")
    private boolean output;

    /** 视频编码方式 **/
    @JsonProperty("videoCoding")
    private int videoCoding;

    /** 最大音频物理通道数量 **/
    @JsonProperty("maxAudioChlNum")
    private int maxAudioChlNum;

    /** 最大视频物理通道数量 **/
    @JsonProperty("maxVideoChlNum")
    private int maxVideoChlNum;

    public final int getAudioCoding() {
        return audioCoding;
    }

    public final void setAudioCoding(int audioCoding) {
        this.audioCoding = audioCoding;
    }

    public final int getInputChlNum() {
        return inputChlNum;
    }

    public final void setInputChlNum(int inputChlNum) {
        this.inputChlNum = inputChlNum;
    }

    public final int getSamplingRate() {
        return samplingRate;
    }

    public final void setSamplingRate(int samplingRate) {
        this.samplingRate = samplingRate;
    }

    public final int getSamplingNum() {
        return samplingNum;
    }

    public final void setSamplingNum(int samplingNum) {
        this.samplingNum = samplingNum;
    }

    public final int getFrameLen() {
        return frameLen;
    }

    public final void setFrameLen(int frameLen) {
        this.frameLen = frameLen;
    }

    public final boolean isOutput() {
        return output;
    }

    public final void setOutput(boolean output) {
        this.output = output;
    }

    public final int getVideoCoding() {
        return videoCoding;
    }

    public final void setVideoCoding(int videoCoding) {
        this.videoCoding = videoCoding;
    }

    public final int getMaxAudioChlNum() {
        return maxAudioChlNum;
    }

    public final void setMaxAudioChlNum(int maxAudioChlNum) {
        this.maxAudioChlNum = maxAudioChlNum;
    }

    public final int getMaxVideoChlNum() {
        return maxVideoChlNum;
    }

    public final void setMaxVideoChlNum(int maxVideoChlNum) {
        this.maxVideoChlNum = maxVideoChlNum;
    }

}
