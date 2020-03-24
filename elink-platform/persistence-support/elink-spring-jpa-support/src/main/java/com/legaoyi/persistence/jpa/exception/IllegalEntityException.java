package com.legaoyi.persistence.jpa.exception;

/**
 * @author gaoshengbo
 */
public class IllegalEntityException extends RuntimeException {

    private static final long serialVersionUID = 5779209904670622576L;

    public IllegalEntityException(String entityName) {
        super("the entity " + entityName + " is not exist !");
    }
}
