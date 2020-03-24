package com.legaoyi.platform.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.legaoyi.common.exception.BizProcessException;
import com.legaoyi.common.util.Result;
import com.legaoyi.platform.ext.service.ExtendService;
import com.legaoyi.platform.util.SpringContextUtil;

/**
 * @author gaoshengbo
 */
public class BaseController {

    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    private static final String EXTEND_SERVICE_BEAN_ID_SUFFIX = "ExtendService";

    @ExceptionHandler
    public Result exceptionHandler(Exception e) {
        logger.error("exceptionHandler", e);
        String errorMsg;
        if (e instanceof BizProcessException) {
            errorMsg = ((BizProcessException) e).getMessage();
        } else {
            if (e.getCause() != null) {
                errorMsg = e.getCause().getMessage();
            } else {
                errorMsg = e.getLocalizedMessage();
                if (errorMsg == null || "".equals(errorMsg)) {
                    errorMsg = e.getMessage();
                }
            }
        }
        return new Result(Result.RESP_CODE_ERROR, errorMsg, null);
    }

    protected ExtendService getExtendService(String entityName) {
        try {
            return (ExtendService) SpringContextUtil.getBean(entityName.concat(EXTEND_SERVICE_BEAN_ID_SUFFIX));
        } catch (BeansException e) {

        }
        return null;
    }
}
