package com.legaoyi.platform.rest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaoshengbo
 */
public class RequestParams {
    private Map<String, Object> data = new HashMap<String, Object>();

    private String url;

    private int readTimeout = -1;

    private int connectTimeout = -1;

    /**
     * @return the data
     */
    public Map<String, Object> getData() {
        return this.data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the readTimeout
     */
    public int getReadTimeout() {
        return this.readTimeout;
    }

    /**
     * @param readTimeout the readTimeout to set
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * @return the connectTimeout
     */
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    /**
     * @param connectTimeout the connectTimeout to set
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

}
