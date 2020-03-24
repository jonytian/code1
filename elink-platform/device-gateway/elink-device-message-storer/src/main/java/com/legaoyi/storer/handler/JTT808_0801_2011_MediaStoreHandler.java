package com.legaoyi.storer.handler;

import java.util.Map;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.storer.handler.MessageHandler;
import com.legaoyi.storer.service.FtpService;
import com.legaoyi.storer.util.Constants;
import com.legaoyi.storer.util.IdGenerator;

import java.io.File;
import java.io.FileInputStream;

/**
 * 存储文件
 * 
 * @author gaoshengbo
 *
 */
@Component(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801_2011_mediaStore" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX)
public class JTT808_0801_2011_MediaStoreHandler extends MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(JTT808_0801_2011_MediaStoreHandler.class);

    @Value("${ftp.server.enable}")
    private boolean ftpServer;

    @Autowired
    @Qualifier("ftpService")
    private FtpService ftpService;

    @Autowired
    public JTT808_0801_2011_MediaStoreHandler(@Qualifier(Constants.ELINK_MESSAGE_STORER_BEAN_PREFIX + "0801_2011_notify" + Constants.ELINK_MESSAGE_STORER_MESSAGE_HANDLER_BEAN_SUFFIX) MessageHandler handler) {
        setSuccessor(handler);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handle(ExchangeMessage message) throws Exception {
        Map<?, ?> map = (Map<?, ?>) message.getMessage();

        Map<?, ?> messageHeader = (Map<?, ?>) map.get(Constants.MAP_KEY_MESSAGE_HEADER);
        String simCode = (String) messageHeader.get(Constants.MAP_KEY_SIM_CODE);

        Map<String, Object> messageBody = (Map<String, Object>) map.get(Constants.MAP_KEY_MESSAGE_BODY);
        messageBody.put("_id", IdGenerator.nextIdStr());
        String filePath = (String) messageBody.get("filePath");
        File srcfile = new File(filePath);

        int index = filePath.indexOf(simCode) - 1;
        filePath = filePath.substring(index);
        messageBody.put("filePath", filePath);
        if (ftpServer) {
            // 上传ftp
            FTPClient ftpClient = ftpService.getFtpClient();
            FileInputStream fis = null;
            try {
                ftpClient.enterLocalPassiveMode();
                String base = ftpClient.printWorkingDirectory();

                String separator = File.separator;
                index = filePath.lastIndexOf(separator);
                String[] paths = null;
                if (separator.equals("\\")) {
                    paths = filePath.substring(0, index).replaceAll("\\\\", "/").split("/");
                } else {
                    paths = filePath.substring(0, index).split(separator);
                }

                String path = base.concat("/");
                for (String p : paths) {
                    path = path.concat(p).concat("/");
                    if (!ftpClient.changeWorkingDirectory(path)) {
                        ftpClient.makeDirectory(path);
                    }
                }

                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                fis = new FileInputStream(srcfile);
                if (ftpClient.storeFile(base.concat(path).concat(srcfile.getName()), fis)) {
                    srcfile.delete();
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                ftpClient.logout();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        this.getSuccessor().handle(message);
    }
}
