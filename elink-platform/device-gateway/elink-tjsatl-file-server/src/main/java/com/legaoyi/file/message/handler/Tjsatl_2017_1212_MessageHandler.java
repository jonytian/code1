package com.legaoyi.file.message.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.legaoyi.common.message.ExchangeMessage;
import com.legaoyi.file.messagebody.Attachment;
import com.legaoyi.file.messagebody.Tjsatl_2017_1212_MessageBody;
import com.legaoyi.file.messagebody.Tjsatl_2017_9212_MessageBody;
import com.legaoyi.file.server.ServerMessageHandler;
import com.legaoyi.file.server.util.Constants;
import com.legaoyi.protocol.message.Message;
import com.legaoyi.protocol.util.ByteUtils;
import com.legaoyi.protocol.util.DateUtils;

import io.netty.channel.ChannelHandlerContext;

@Component("tjsatl_1212_MessageHandler")
public class Tjsatl_2017_1212_MessageHandler implements MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(Tjsatl_2017_1212_MessageHandler.class);

    public static final int BUFSIZE = 1024 * 8;

    @Value("${file.store.path}")
    private String filePath;

    @Autowired
    @Qualifier("deviceDownMessageDeliverer")
    private DeviceDownMessageDeliverer messageDeliverer;

    @Autowired
    @Qualifier("serverMessageHandler")
    private ServerMessageHandler serverMessageHandler;

    @Override
    public void handle(ChannelHandlerContext ctx, Message message) throws Exception {
        Tjsatl_2017_1212_MessageBody messageBody = (Tjsatl_2017_1212_MessageBody) message.getMessageBody();

        List<Attachment> attachmentList = ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT).get();
        if (attachmentList == null || attachmentList.isEmpty()) {
            logger.error("******非法上行消息，附件数据为空");
            // 非法消息
            return;
        }

        Map<String, Object> fileMap = ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT_LIST).get();

        Collections.sort(attachmentList, new Comparator<Attachment>() {

            @Override
            public int compare(Attachment att1, Attachment att2) {
                long diff = att1.getOffset() - att2.getOffset();
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return 0;
            }
        });

        // 校验附件是否丢包
        long curent = 0;
        List<Map<String, Long>> packageList = new ArrayList<Map<String, Long>>();
        Map<String, Long> map;
        for (Attachment att : attachmentList) {
            long offset = att.getOffset();
            long length = offset - curent;
            if (length > 0) {
                map = new HashMap<String, Long>();
                map.put("offset", curent);
                map.put("length", length);
                packageList.add(map);
            }
            length = att.getLength();
            curent = offset + length;
        }

        long length = messageBody.getFileSize() - curent;
        if (length > 0) {
            map = new HashMap<String, Long>();
            map.put("offset", curent);
            map.put("length", length);
            packageList.add(map);
        }

        Tjsatl_2017_9212_MessageBody resp = new Tjsatl_2017_9212_MessageBody();
        resp.setFileName(messageBody.getFileName());
        resp.setFileType(messageBody.getFileType());
        // 组装文件
        if (packageList.size() == 0) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            String path = null;
            String fileName = attachmentList.get(0).getFileName();
            if (!filePath.endsWith(File.separator)) {
                path = filePath.concat(File.separator);
            }
            path = path.concat(message.getMessageHeader().getSimCode()).concat(File.separator).concat(df.format(new Date())).concat(File.separator).concat(fileName);

            messageBody.setFileName(path);
            mergeFiles(path, attachmentList);
            if (messageBody.getFileType() == 0x03) {
                // 解码后重新写入文件
                decodeFile(path);
            }
            serverMessageHandler.handle(new ExchangeMessage(ExchangeMessage.MESSAGEID_GATEWAY_UP_MESSAGE, message.clone(), message.getMessageHeader().getMessageSeq() + ""));
            ctx.channel().attr(Constants.ATTRIBUTE_SESSION_ATTACHMENT).set(null);
            resp.setResult(0);

            // 删除已上传的附件列表
            fileMap.remove(fileName);
        } else {
            resp.setPackageList(packageList);
            resp.setResult(1);
        }
        message.getMessageHeader().setMessageId(Tjsatl_2017_9212_MessageBody.MESSAGE_ID);
        message.setMessageBody(resp);
        // 应答9212消息
        messageDeliverer.deliver(ctx, message);

        if (fileMap == null || fileMap.isEmpty()) {
            //附件已经上传完毕，关闭终端链接
            logger.info("******附件上传完毕，平台主动断开链接******");
            ctx.close();
        }
    }

    private static void decodeFile(String fileName) {
        List<String> list = new ArrayList<String>();
        InputStream in = null;
        try {
            in = new FileInputStream(fileName);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);

            byte[] arr = new byte[4];
            System.arraycopy(bytes, 0, arr, 0, arr.length);
            int total = (int) ByteUtils.dword2long(arr);

            int offset = 0;
            StringBuilder sb;
            for (int i = 0; i < total; i++) {
                byte[] block = new byte[64];
                System.arraycopy(bytes, offset, block, 0, block.length);
                offset += block.length;

                int crc = 0;
                for (int j = 0, l = block.length - 1; j < l; j++) {
                    crc += ByteUtils.byte2int(block[j]);
                }
                crc = crc & 0xff;
                if (crc != ByteUtils.byte2int(block[block.length - 1])) {
                    // logger.error("******decode bin file error,crc illegal,block={}", ByteUtils.bytes2hex(block));
                    // continue;
                }

                int index = 0;
                sb = new StringBuilder();
                // 数据块总数
                arr = new byte[4];
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.dword2long(arr)).append(",");
                index += arr.length;

                // 当前数据块序号
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.dword2long(arr)).append(",");
                index += arr.length;

                // 报警标志
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.dword2long(arr)).append(",");
                index += arr.length;

                // 车辆状态
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.dword2long(arr)).append(",");
                index += arr.length;

                // 纬度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.6f", ByteUtils.dword2long(arr) * 0.000001))).append(",");
                index += arr.length;

                // 经度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.6f", ByteUtils.dword2long(arr) * 0.000001))).append(",");
                index += arr.length;

                // 卫星高程
                arr = new byte[2];
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.word2int(arr)).append(",");
                index += arr.length;

                // 卫星速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.1f", ByteUtils.word2int(arr) * 0.1))).append(",");
                index += arr.length;

                // 卫星方向
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.word2int(arr)).append(",");
                index += arr.length;

                // 时间
                arr = new byte[6];
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(DateUtils.bcd2Timestamp(ByteUtils.bytes2bcd(arr))).append(",");
                index += arr.length;

                // X 轴加速度
                arr = new byte[2];
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.2f", ByteUtils.word2int(arr) * 0.01))).append(",");
                index += arr.length;

                // Y 轴加速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.2f", ByteUtils.word2int(arr) * 0.01))).append(",");
                index += arr.length;

                // Z 轴加速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.2f", ByteUtils.word2int(arr) * 0.01))).append(",");
                index += arr.length;

                // X 轴角速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.2f", ByteUtils.word2int(arr) * 0.01))).append(",");
                index += arr.length;

                // Y 轴角速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.2f", ByteUtils.word2int(arr) * 0.01))).append(",");
                index += arr.length;

                // Z 轴角速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.2f", ByteUtils.word2int(arr) * 0.01))).append(",");
                index += arr.length;

                // 脉冲速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.1f", ByteUtils.word2int(arr) * 0.1))).append(",");
                index += arr.length;

                // OBD 速度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(Double.valueOf(String.format("%.1f", ByteUtils.word2int(arr) * 0.1))).append(",");
                index += arr.length;

                // 档位状态
                sb.append(ByteUtils.byte2int(block[index++])).append(",");
                // 加速踏板行程值
                sb.append(ByteUtils.byte2int(block[index++])).append(",");
                // 制动踏板行程值
                sb.append(ByteUtils.byte2int(block[index++])).append(",");
                // 制动状态
                sb.append(ByteUtils.byte2int(block[index++])).append(",");

                // 发送机转速
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.word2int(arr)).append(",");
                index += arr.length;

                // 方向盘角度
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.word2int(arr)).append(",");
                index += arr.length;

                // 转向灯状态
                sb.append(ByteUtils.byte2int(block[index++])).append(",");

                // 预留
                System.arraycopy(block, index, arr, 0, arr.length);
                sb.append(ByteUtils.word2int(arr));

                list.add(sb.toString());
            }
        } catch (Exception e) {
            logger.error("******decode file error", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }

        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            if (list != null && list.size() > 0) {
                File file = new File(fileName);
                fos = new FileOutputStream(file);
                bw = new BufferedWriter(new OutputStreamWriter(fos));
                for (String s : list) {
                    bw.write(s);
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            logger.error("******decode file error", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void mergeFiles(String fileName, List<Attachment> attachmentList) {
        FileOutputStream fos = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file.createNewFile();
            fos = new FileOutputStream(file, true);
            for (Attachment att : attachmentList) {
                fos.write(att.getData());
            }
        } catch (IOException e) {
            logger.error("******mergeFiles error", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignore) {
            }
        }
    }
}
