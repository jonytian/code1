package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.legaoyi.protocol.message.MessageBody;

/**
 * 报警附件上传指令,平台接收到带有附件的报警/事件信息后，向终端下发附件上传指令
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2019-05-20
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "9208_tjsatl" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class Tjsatl_2017_9208_MessageBody extends MessageBody {

    private static final long serialVersionUID = -6174276527381575817L;

    public static final String MESSAGE_ID = "9208";

    /** 附件服务器 IP 地 址 **/
    @JsonProperty("ip")
    private String ip;

    /** 附件服务器端口 （TCP） **/
    @JsonProperty("tcpPort")
    private int tcpPort;

    /** 附件服务器端口 （UDP） **/
    @JsonProperty("udpPort")
    private int udpPort;

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

}
