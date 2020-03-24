package com.legaoyi.storer.util;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FfmpegUtil {

    /**
     * ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）
     * 
     * @param command
     * @param dto
     * @throws Exception
     */
    public static boolean process(List<String> command) throws Exception {
        if (null == command || command.size() == 0) {
            return false;
        }
        Process videoProcess = new ProcessBuilder(command).redirectErrorStream(true).start();
        new PrintStream(videoProcess.getErrorStream()).start();
        new PrintStream(videoProcess.getInputStream()).start();
        int exitcode = videoProcess.waitFor();
        if (exitcode == 1) {
            return false;
        }
        return true;
    }
}


class PrintStream extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(FfmpegUtil.class);

    java.io.InputStream __is = null;

    public PrintStream(java.io.InputStream is) {
        __is = is;
    }

    public void run() {
        try {
            while (this != null) {
                int _ch = __is.read();
                if (_ch == -1) {
                    break;
                } else {
                    System.out.print((char) _ch);
                }
            }
        } catch (Exception e) {
            logger.error("process ffmpeg error", e);
        }
    }
}
