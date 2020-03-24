package com.legaoyi.protocol.up.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 上传基本信息
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "1210_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_1210_MessageBody extends MessageBody {

    private static final long serialVersionUID = 7643584698183762496L;

    /** 报警标识号,终端ID **/
    @JsonProperty("terminalId")
    private String terminalId;

    /** 报警标识号,时间 **/
    @JsonProperty("alarmTime")
    private String alarmTime;

    /** 报警标识号,序号 **/
    @JsonProperty("alarmSeq")
    private int alarmSeq;

    /** 报警标识号,预留 **/
    @JsonProperty("alarmExt")
    private int alarmExt;

    /** 报警标识号,附件数量 **/
    @JsonProperty("totalFile")
    private int totalFile;

    /** 报警编号 **/
    @JsonProperty("alarmId")
    private String alarmId;

    /** 信息类型,0x00：正常报警文件信息,0x01：补传报警文件信息 **/
    @JsonProperty("type")
    private int type;

    /** 附件信息列表 **/
    @JsonProperty("fileList")
    private List<Map<String, Object>> fileList;

    public final String getTerminalId() {
        return terminalId;
    }

    public final void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public final String getAlarmTime() {
        return alarmTime;
    }

    public final void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public final int getAlarmSeq() {
        return alarmSeq;
    }

    public final void setAlarmSeq(int alarmSeq) {
        this.alarmSeq = alarmSeq;
    }

    public final int getAlarmExt() {
        return alarmExt;
    }

    public final void setAlarmExt(int alarmExt) {
        this.alarmExt = alarmExt;
    }

    public final int getTotalFile() {
        return totalFile;
    }

    public final void setTotalFile(int totalFile) {
        this.totalFile = totalFile;
    }

    public final String getAlarmId() {
        return alarmId;
    }

    public final void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public final int getType() {
        return type;
    }

    public final void setType(int type) {
        this.type = type;
    }

    public final List<Map<String, Object>> getFileList() {
        return fileList;
    }

    public final void setFileList(List<Map<String, Object>> fileList) {
        this.fileList = fileList;
    }

}
