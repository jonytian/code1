package com.legaoyi.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MappedBiggerFileReader {

    private static final int DATA_CHUNK = 2 * 1024 * 1024;

    private MappedByteBuffer[] mappedBufArray;

    private int count = 0;

    private int number;

    private FileInputStream fileIn;

    private long fileLength;

    private byte[] array;

    public MappedBiggerFileReader(File file) throws IOException {
        this.fileIn = new FileInputStream(file);
        FileChannel fileChannel = fileIn.getChannel();
        this.fileLength = fileChannel.size();
        this.number = (int) Math.ceil((double) fileLength / (double) DATA_CHUNK);
        this.mappedBufArray = new MappedByteBuffer[DATA_CHUNK];// 内存文件映射数组
        long preLength = 0;
        long regionSize = DATA_CHUNK;// 映射区域的大小
        for (int i = 0; i < number; i++) {// 将文件的连续区域映射到内存文件映射数组中
            if (fileLength - preLength < DATA_CHUNK) {
                regionSize = fileLength - preLength;// 最后一片区域的大小
            }
            mappedBufArray[i] = fileChannel.map(FileChannel.MapMode.READ_ONLY, preLength, regionSize);
            preLength += regionSize;// 下一片区域的开始
        }
    }

    public MappedBiggerFileReader(String fileName) throws IOException {
        this(new File(fileName));
    }

    public int read() throws IOException {
        if (count >= number) {
            return -1;
        }
        int limit = mappedBufArray[count].limit();
        int position = mappedBufArray[count].position();
        if (limit - position > DATA_CHUNK) {
            array = new byte[DATA_CHUNK];
            mappedBufArray[count].get(array);
            return DATA_CHUNK;
        } else {// 本内存文件映射最后一次读取数据
            array = new byte[limit - position];
            mappedBufArray[count].get(array);
            if (count < number) {
                count++;// 转换到下一个内存文件映射
            }
            return limit - position;
        }
    }

    public void close() throws IOException {
        if (mappedBufArray != null) {
            for (MappedByteBuffer buf : mappedBufArray) {
                FileUtil.unmap(buf);
            }
        }
        fileIn.close();
        array = null;
    }

    public byte[] getArray() {
        return array;
    }

    public long getFileLength() {
        return fileLength;
    }
}
