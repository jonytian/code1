package com.legaoyi.protocol.up.messagebody;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;
import com.legaoyi.protocol.up.messagebody.JTT808_0700_MessageBody;
/**
 * 采集记录仪唯一性编号 (07H)
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "0700_07H_2013" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_0700_07H_2013_MessageBody extends JTT808_0700_MessageBody {

    private static final long serialVersionUID = 1761370471709345924L;

    /** 生产厂 CCC 认证代码 **/
    @JsonProperty("cccAuthcode")
    private String cccAuthcode;

    /** 认证产品型号 **/
    @JsonProperty("productModel")
    private String productModel;

    /** 记录仪的生产日期 **/
    @JsonProperty("mfgdate")
    private String mfgdate;

    /** 产品生产流水号 **/
    @JsonProperty("batchNo")
    private String batchNo;

    public final String getCccAuthcode() {
        return cccAuthcode;
    }

    public final void setCccAuthcode(String cccAuthcode) {
        this.cccAuthcode = cccAuthcode;
    }

    public final String getProductModel() {
        return productModel;
    }

    public final void setProductModel(String productModel) {
        this.productModel = productModel;
    }

    public final String getMfgdate() {
        return mfgdate;
    }

    public final void setMfgdate(String mfgdate) {
        this.mfgdate = mfgdate;
    }

    public final String getBatchNo() {
        return batchNo;
    }

    public final void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

}
