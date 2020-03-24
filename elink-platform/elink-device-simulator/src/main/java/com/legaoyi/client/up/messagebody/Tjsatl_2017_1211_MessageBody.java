package com.legaoyi.client.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 文件信息上传,终端向附件服务器发送报警附件信息指令并得到应答后，向附件服务器发送附件文件信息消息，
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "1211_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_1211_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7643584698183762496L;

    public static final String JTT808_MESSAGE_ID = "1211";

    /** 文件名称 **/
    @JsonProperty("fileName")
    private String fileName;

    /**
     * 文件类型,0x00：图片 0x01：音频 0x02：视频 0x03：文本 0x04：其它
     **/
    @JsonProperty("fileType")
    private int fileType;

    /** 文件大小 **/
    @JsonProperty("fileSize")
    private Long fileSize;

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

    public final Long getFileSize() {
        return fileSize;
    }

    public final void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

}
