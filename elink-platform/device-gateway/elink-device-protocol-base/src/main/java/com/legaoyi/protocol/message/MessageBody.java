package com.legaoyi.protocol.message;

import java.io.Serializable;
import com.legaoyi.common.util.JsonUtil;

/**
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class MessageBody implements Cloneable, Serializable {

    private static final long serialVersionUID = 5118633689637875191L;

    public static final String MESSAGE_BODY_BEAN_PREFIX = "elink_";

    public static final String MESSAGE_BODY_BEAN_SUFFIX = "_messageBody";

    public MessageBody invoke(Object o) throws Exception {
        if (o == null) {
            return this;
        }
        if (o instanceof String) {
            return JsonUtil.convertStringToObject((String) o, this.getClass());
        } else {
            return JsonUtil.convertStringToObject(JsonUtil.covertObjectToString(o), this.getClass());
        }
    }

    @Override
    public MessageBody clone() {
        MessageBody messageBody = null;
        try {
            messageBody = (MessageBody) super.clone();
        } catch (Exception e) {
        }
        return messageBody;
    }
}
