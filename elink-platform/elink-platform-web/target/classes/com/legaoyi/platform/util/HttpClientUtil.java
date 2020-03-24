package com.legaoyi.platform.util;

/**
 * @author gaoshengbo
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
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

    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_XML = "application/xml";

    public static final String CHARSET = "UTF-8";

    private static final String URL_SEPARATOR = "/";

    /**
     * 连接超时时间
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 30000;

    /**
     * 读取超时时间
     */
    public static final int DEFAULT_READ_TIMEOUT = 30000;

    private static ObjectMapper mapper = new ObjectMapper();

    public static String send(String uri, String method, int readTimeout, int connectTimeout, Object message, String user, String password) throws Exception {
        if (!uri.startsWith("http")) {
            uri = "http://" + uri;
        }

        long startTime = System.currentTimeMillis();
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            if (null != method && GET.equalsIgnoreCase(method)) {
                if (uri.indexOf("?") == -1) {
                    if (message instanceof Map) {
                        Map<?, ?> m = (Map<?, ?>) message;
                        if (m != null && !m.isEmpty()) {
                            uri += "?";
                            for (Object key : m.keySet()) {
                                uri += key + "=" + m.get(key) + "&";
                            }
                        }
                    }
                } else {
                    if (uri.endsWith("?")) {
                        if (message instanceof Map) {
                            Map<?, ?> m = (Map<?, ?>) message;
                            if (m != null && !m.isEmpty()) {
                                for (Object key : m.keySet()) {
                                    uri += key + "=" + m.get(key) + "&";
                                }
                            }
                        }
                    } else {
                        if (uri.endsWith("&")) {
                            if (message instanceof Map) {
                                Map<?, ?> m = (Map<?, ?>) message;
                                if (m != null && !m.isEmpty()) {
                                    for (Object key : m.keySet()) {
                                        uri += key + "=" + m.get(key) + "&";
                                    }
                                }
                            }
                        } else {
                            if (message instanceof Map) {
                                Map<?, ?> m = (Map<?, ?>) message;
                                if (m != null && !m.isEmpty()) {
                                    for (Object key : m.keySet()) {
                                        uri += "&" + key + "=" + m.get(key);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            logger.info("request  url={},method={},data={}", uri, method, message);
            conn = (HttpURLConnection) new URL(uri).openConnection();
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

            if (readTimeout > 0) {
                conn.setReadTimeout(readTimeout);
            }
            if (connectTimeout > 0) {
                conn.setConnectTimeout(connectTimeout);
            }

            if (!GET.equalsIgnoreCase(conn.getRequestMethod())) {
                conn.setRequestProperty("Content-Type", "application/json; charset=" + CHARSET);
            }

            if (null != user && null != password) {
                conn.setRequestProperty("Authorization", "Basic " + base64(user, password));
            }

            conn.setRequestProperty("Accept", "application/json; charset=" + CHARSET);

            conn.connect();

            if ((POST.equalsIgnoreCase(conn.getRequestMethod()) || PUT.equalsIgnoreCase(conn.getRequestMethod())) && null != message) {
                os = conn.getOutputStream();
                if (message instanceof String) {
                    os.write(((String) message).getBytes(CHARSET));
                } else if (message instanceof byte[]) {
                    os.write((byte[]) message);
                } else {
                    mapper.writeValue(os, message);
                }
            }
            int resCode = conn.getResponseCode();
            if (200 <= resCode && resCode < 300) {
                return convertStreamToString(conn.getInputStream());
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
            logger.info("request time,{} millis", System.currentTimeMillis() - startTime);
        }
    }

    /**
     * 文件上传
     * 
     * @param uri
     * @param user
     * @param password
     * @param fileName
     * @param file
     * @return
     * @throws Exception
     */
    public static String uploadFile(String uri, String user, String password, String fileName, InputStream file) throws Exception {
        if (file == null) {
            return null;
        }
        if (!uri.startsWith("http")) {
            uri = "http://" + uri;
        }
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        DataOutputStream ds = null;
        HttpURLConnection conn = null;
        OutputStream os = null;
        InputStream is = null;
        try {
            conn = (HttpURLConnection) new URL(uri).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            // 设定请求的方法，默认是GET
            conn.setRequestMethod("POST");
            // 设置字符编码连接参数
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            conn.setRequestProperty("Charset", CHARSET);

            // 设置http Basic认证
            if (null != user && null != password) {
                conn.setRequestProperty("Authorization", "Basic " + base64(user, password));
            }
            // 设置请求内容类型
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            // 设置DataOutputStream
            ds = new DataOutputStream(conn.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.write(("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"" + end).getBytes(CHARSET));
            ds.writeBytes(end);

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = file.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();

            int resCode = conn.getResponseCode();
            if (200 <= resCode && resCode < 300) {
                return convertStreamToString(conn.getInputStream());
            } else if (401 == resCode) {
                throw new Exception("鉴权失败，用户名或密码错误！");
            } else {
                throw new Exception("Exception on server, code: " + resCode);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                }
            }
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
            file.close();
        }
    }

    public static String base64(String user, String password) {
        String s = user + ":" + password;
        return new String(Base64.encodeBase64(s.getBytes()));
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /**
     * 发送内容。
     *
     * @param response
     * @param contentType
     * @param text
     */
    public static void render(HttpServletResponse response, String contentType, String text) {
        response.setCharacterEncoding(CHARSET);
        response.setContentType(contentType);
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        if (text != null) {
            try {
                response.getWriter().write(text);
            } catch (IOException e) {
                logger.error("response write ,error={}", e);
            }
        }
    }

    public static String formatUrl(String apiHost, String apiPath) {
        String url;
        if (apiPath.startsWith(URL_SEPARATOR)) {
            if (apiHost.endsWith(URL_SEPARATOR)) {
                url = apiHost.concat(apiPath.substring(1));
            } else {
                url = apiHost.concat(apiPath);
            }
        } else {
            if (apiHost.endsWith(URL_SEPARATOR)) {
                url = apiHost.concat(apiPath);
            } else {
                url = apiHost.concat(URL_SEPARATOR).concat(apiPath);
            }
        }
        return url;
    }
}
