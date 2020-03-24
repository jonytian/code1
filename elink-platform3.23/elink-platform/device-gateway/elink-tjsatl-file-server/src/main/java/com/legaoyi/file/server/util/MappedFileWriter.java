package com.legaoyi.file.server.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappedFileWriter {

    private static final Logger logger = LoggerFactory.getLogger(MappedFileWriter.class);

    private static final int DATA_CHUNK = 1 * 1024 * 1024;

    /***
     * 写入文件
     * @param filePath
     * @param data
     * @throws IOException
     */
    public static void write(String filePath, byte[] data) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.createNewFile();

        RandomAccessFile raf = null;
        FileChannel fileChannel = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            fileChannel = raf.getChannel();

            byte[] bytes = null;
            int pos = 0;
            MappedByteBuffer mbb = null;
            long len = data.length;
            int offset = 0;
            while (len >= DATA_CHUNK) {
                mbb = fileChannel.map(MapMode.READ_WRITE, pos, DATA_CHUNK);
                bytes = new byte[DATA_CHUNK];
                System.arraycopy(data, offset, bytes, 0, DATA_CHUNK);
                offset += DATA_CHUNK;
                mbb.put(bytes);
                bytes = null;
                len -= DATA_CHUNK;
                pos += DATA_CHUNK;
            }

            if (len > 0) {
                mbb = fileChannel.map(MapMode.READ_WRITE, pos, len);
                bytes = new byte[(int) len];
                System.arraycopy(data, offset, bytes, 0, bytes.length);
                mbb.put(bytes);
            }

            bytes = null;
            unmap(mbb); // release MappedByteBuffer
        } finally {
            if (fileChannel != null) {
                fileChannel.close();
            }
            if (raf != null) {
                raf.close();
            }
        }
    }

    /**
     * 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生 正在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     * 
     * @param mappedByteBuffer
     */
    private static void unmap(final MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }

            mappedByteBuffer.force();
            AccessController.doPrivileged(new PrivilegedAction<Object>() {

                @Override
                public Object run() {
                    try {
                        Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                        getCleanerMethod.setAccessible(true);
                        sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(mappedByteBuffer, new Object[0]);
                        cleaner.clean();
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
