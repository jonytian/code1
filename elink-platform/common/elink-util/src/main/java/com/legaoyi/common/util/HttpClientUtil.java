package com.legaoyi.common.util;

/**
 * @author gaoshengbo
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String PUT = "PUT";

    public static final String PATCH = "PATCH";

    public static final String DELETE = "DELETE";

    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String CHARSET_GBK = "gbk";

    public static final String CHARSET_GB2312 = "gb2312";

    /**
     * 连接超时时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 30000;

    /**
     * 读取超时时间
     */
    public static final int DEFAULT_READ_TIMEOUT = 30000;

    private static ObjectMapper mapper = new ObjectMapper();

    public static String send(String url) throws Exception {
        return send(url, GET, null, CHARSET_UTF8);
    }

    public static String send(String url, String charset) throws Exception {
        return send(url, GET, null, charset);
    }

    public static String send(String url, String method, Object data, String charset) throws Exception {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }

        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            if (null != method && GET.equalsIgnoreCase(method)) {
                if (url.indexOf("?") == -1) {
                    if (data instanceof Map) {
                        Map<?, ?> m = (Map<?, ?>) data;
                        if (m != null && !m.isEmpty()) {
                            url += "?";
                            for (Object key : m.keySet()) {
                                url += key + "=" + m.get(key) + "&";
                            }
                        }
                    }
                } else {
                    if (url.endsWith("?")) {
                        if (data instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) data;
                            if (m != null && !m.isEmpty()) {
                                for (Object key : m.keySet()) {
                                    url += key + "=" + m.get(key) + "&";
                                }
                            }
                        }
                    } else {
                        if (url.endsWith("&")) {
                            if (data instanceof Map) {
                                Map<?, ?> m = (Map<?, ?>) data;
                                if (m != null && !m.isEmpty()) {
                                    for (Object key : m.keySet()) {
                                        url += key + "=" + m.get(key) + "&";
                                    }
                                }
                            }
                        } else {
                            if (data instanceof Map) {
                                Map<?, ?> m = (Map<?, ?>) data;
                                if (m != null && !m.isEmpty()) {
                                    for (Object key : m.keySet()) {
                                        url += "&" + key + "=" + m.get(key);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            if (null != method) {
                if (PATCH.equalsIgnoreCase(method)) {
                    conn.setRequestProperty("X-HTTP-Method-Override", PATCH);
                    conn.setRequestMethod(POST);
                } else {
                    conn.setRequestMethod(method.toString().toUpperCase());
                }
            } else {
                conn.setRequestMethod(POST);
            }

            conn.setReadTimeout(DEFAULT_READ_TIMEOUT);
            conn.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);

            if (!GET.equalsIgnoreCase(conn.getRequestMethod())) {
                conn.setRequestProperty("Content-Type", "text/html; charset=" + charset);
            }

            conn.setRequestProperty("Accept", "text/html; charset=" + charset);

            conn.connect();

            if ((POST.equalsIgnoreCase(conn.getRequestMethod()) || PUT.equalsIgnoreCase(conn.getRequestMethod())) && null != data) {
                os = conn.getOutputStream();
                if (data instanceof String) {
                    os.write(((String) data).getBytes(charset));
                } else if (data instanceof byte[]) {
                    os.write((byte[]) data);
                } else {
                    mapper.writeValue(os, data);
                }
            }
            int resCode = conn.getResponseCode();
            if (200 <= resCode && resCode < 300) {
                return convertStreamToString(conn.getInputStream(), charset);
            } else if (401 == resCode) {
                throw new Exception("鉴权失败，用户名或密码错误！");
            } else {
                throw new Exception("Exception on server, code: " + resCode);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != os) {
                try {
                    os.flush();
                } catch (Exception e) {
                } finally {
                    try {
                        os.close();
                    } catch (Exception e) {
                    }
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
            if (null != conn) {
                conn.disconnect();
            }
        }
    }

    private static String convertStreamToString(InputStream is, String charset) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, charset));
        } catch (UnsupportedEncodingException e) {
            logger.error("***********convertStreamToString error", e);
        }

        if (reader == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            logger.error("***********convertStreamToString error", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }
}
