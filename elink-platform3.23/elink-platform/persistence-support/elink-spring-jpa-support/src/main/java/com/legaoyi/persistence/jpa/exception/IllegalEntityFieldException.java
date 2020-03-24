package com.legaoyi.persistence.jpa.exception;

/**
 * @author gaoshengbo
 */
public class IllegalEntityFieldException extends RuntimeException {

    private static final long serialVersionUID = 5779209904670622576L;

    public IllegalEntityFieldException(String entityName, String fieldName) {
        super("the field name " + fieldName + " is not exist !");
    }
}
