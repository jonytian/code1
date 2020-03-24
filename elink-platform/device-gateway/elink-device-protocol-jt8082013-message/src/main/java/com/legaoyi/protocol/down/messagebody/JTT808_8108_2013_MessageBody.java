package com.legaoyi.protocol.down.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 下发终端升级包
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8108_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8108_2013_MessageBody extends MessageBody {

    private static final long serialVersionUID = 4547035064879672661L;

    public static final String MESSAGE_ID = "8108";

    /** 升级类型 **/
    @JsonProperty("upgradeType")
    private int upgradeType;

    /** 制造商id **/
    @JsonProperty("mfrsId")
    private String mfrsId;

    /** 版本号 **/
    @JsonProperty("version")
    private String version;

    /** 升级包,base64编码 **/
    @JsonProperty("upgradePackageData")
    private String upgradePackageData;

    public final int getUpgradeType() {
        return upgradeType;
    }

    public final void setUpgradeType(int upgradeType) {
        this.upgradeType = upgradeType;
    }

    public final String getMfrsId() {
        return mfrsId;
    }

    public final void setMfrsId(String mfrsId) {
        this.mfrsId = mfrsId;
    }

    public final String getVersion() {
        return version;
    }

    public final void setVersion(String version) {
        this.version = version;
    }

    public final String getUpgradePackageData() {
        return upgradePackageData;
    }

    public final void setUpgradePackageData(String upgradePackageData) {
        this.upgradePackageData = upgradePackageData;
    }

}
