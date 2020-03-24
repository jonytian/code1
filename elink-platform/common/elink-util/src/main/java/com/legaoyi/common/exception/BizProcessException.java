package com.legaoyi.common.exception;

/**
 * @author gaoshengbo
 */
public class BizProcessException extends Exception {

    private static final long serialVersionUID = 46716004641039231L;

    private int code;

    private String message;

    public BizProcessException(String message) {
        super(message);
        this.message = message;
    }

    public BizProcessException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public final int getCode() {
        return code;
    }

    public final void setCode(int code) {
        this.code = code;
    }

    public final String getMessage() {
        return message;
    }

    public final void setMessage(String message) {
        this.message = message;
    }

}
