package com.legaoyi.protocol.exception;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class MessageDeliveryException extends Exception {

    private static final long serialVersionUID = 3725914824749405587L;

    public MessageDeliveryException() {
        super("消息发送失败");
    }

    public MessageDeliveryException(Exception e) {
        super(e);
    }

    public MessageDeliveryException(String s) {
        super(s);
    }
}
