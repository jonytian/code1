package com.legaoyi.storer.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.FfmpegUtil;
import com.legaoyi.storer.handler.MessageHandler;

/**
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801_2011_mediaConver" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0801_2011_MediaConverHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0801_2011_MediaConverHandler.class);

    @Value("${ffmpeg.bin.path}")
    private String ffmpegPath;

    @Autowired
    public JTT808_0801_2011_MediaConverHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801_2011_mediaStore" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();
        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        int mediaType = (Integer) messageBody.get("mediaType");
        if (mediaType == 2) {// 视频
            String srcFile = (String) messageBody.get("filePath");
            String suffix = ".mp4";
            if (!srcFile.endsWith(suffix)) {
                String descFile = srcFile.substring(0, srcFile.lastIndexOf(".")).concat(suffix);
                List<String> command = getFfmpegCommand(ffmpegPath, srcFile, descFile);
                if (FfmpegUtil.process(command)) {
                    messageBody.put("filePath", descFile);
                    logger.info("********* convert to mp4 file success,file={}", descFile);
                } else {
                    logger.info("********* convert to mp4 file failed,file={}", srcFile);
                }
            }
        }

        this.getSuccessor().handle(message);
    }

    private static List<String> getFfmpegCommand(String ffmpegPath, String inputFile, String outputFile) throws Exception {
        List<String> command = new ArrayList<String>();
        command.add(ffmpegPath.concat(File.separator).concat("ffmpeg"));
        command.add("-i");// 输入文件
        command.add(inputFile);
        command.add("-c:v");
        command.add("libx264");
        command.add("-x264opts");
        command.add("force-cfr=1");
        command.add("-f");
        command.add("mp4");
        command.add(outputFile);
        return command;
    }
}
