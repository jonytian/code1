package com.legaoyi.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static final int DATA_CHUNK = 1 * 1024 * 1024;

    /**
     * 获取目录下所有文件(按时间排序)
     * 
     * @param path
     * @return
     */
    public static List<File> getFileSort(String path) {
        List<File> list = getFiles(path, new ArrayList<File>());
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<File>() {

                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 
     * 获取目录下所有文件
     * 
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * 
     * 获取当前目录下所有子目录
     * 
     * @param realpath
     * @param files
     * @return
     */
    public static List<File> getDirectorys(String realpath) {
        List<File> files = new ArrayList<File>();
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    public static void writeWithMappedByteBuffer(String filePath, byte[] data) throws IOException {
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
    public static void unmap(final MappedByteBuffer mappedByteBuffer) {
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

    public static void copyFile(File srcFile, File dstFile) throws IOException {
        if (srcFile == null || !srcFile.exists()) {
            return;
        }
        if (dstFile == null || !dstFile.exists()) {
            return;
        }

        FileInputStream fileIns = null;
        FileOutputStream fileOuts = null;
        FileChannel source = null;
        FileChannel destination = null;

        try {
            fileIns = new FileInputStream(srcFile);
            fileOuts = new FileOutputStream(dstFile);
            source = fileIns.getChannel();
            destination = fileOuts.getChannel();
            destination.transferFrom(source, 0, source.size());
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (fileIns != null) {
                fileIns.close();
            }
            if (fileOuts != null) {
                fileOuts.close();
            }
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }
}
