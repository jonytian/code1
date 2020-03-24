package com.legaoyi.protocol.exception;

/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class IllegalMessageException extends Exception {

    private static final long serialVersionUID = -6101688646811211499L;

    public IllegalMessageException() {
        super("消息有误");
    }

    public IllegalMessageException(Exception e) {
        super(e);
    }

    public IllegalMessageException(String s) {
        super(s);
    }
}
