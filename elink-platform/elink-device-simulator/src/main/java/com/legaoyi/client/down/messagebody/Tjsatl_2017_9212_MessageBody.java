package com.legaoyi.client.down.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 文件上传完成消息应答,附件服务器收到终端上报的文件发送完成消息时，向终端发送文件上传完成消息应答
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9212_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_9212_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6174276527381575817L;

    public static final String MESSAGE_ID = "9212";

    /** 文件名称 **/
    @JsonProperty("fileName")
    private String fileName;

    /** 文件类型,0x00：图片 0x01：音频 0x02：视频 0x03：文本 0x04：其它 **/
    @JsonProperty("fileType")
    private int fileType;

    /** 上传结果 **/
    @JsonProperty("result")
    private int result;

    /** 补传数据包列表 **/
    @JsonProperty("packageList")
    private List<Map<String, Long>> packageList;

    public final String getFileName() {
        return fileName;
    }

    public final void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public final int getFileType() {
        return fileType;
    }

    public final void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public final int getResult() {
        return result;
    }

    public final void setResult(int result) {
        this.result = result;
    }

    public final List<Map<String, Long>> getPackageList() {
        return packageList;
    }

    public final void setPackageList(List<Map<String, Long>> packageList) {
        this.packageList = packageList;
    }

}
