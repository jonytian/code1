package com.legaoyi.protocol.util;


/**
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
public class MessageBuilder {

    private int capacity = 20;

    private int length = 0;

    private byte[] array = new byte[this.capacity];

    private int index = 0;

    public MessageBuilder() {

    }

    public MessageBuilder(int capacity) {
        setCapacity(capacity);
    }

    public void setCapacity(int capacity) {
        if (capacity > 0) {
            this.capacity = capacity;
        }
    }

    public MessageBuilder append(byte b) {
        if (this.index >= this.array.length) {
            byte[] newArray = new byte[this.array.length + this.capacity];
            System.arraycopy(this.array, 0, newArray, 0, this.array.length);
            this.array = newArray;
        }
        this.array[this.index] = b;
        this.index += 1;
        return this;
    }

    public MessageBuilder append(byte[] array) {
        for (byte b : array) {
            append(b);
        }
        return this;
    }

    public MessageBuilder append(String string) {
        return append(string.getBytes());
    }

    public void addByte(int i) {
        append(ByteUtils.int2byte(i));
    }

    public void addDword(int i) {
        append(ByteUtils.int2dword(i));
    }

    public void addWord(int i) {
        append(ByteUtils.int2word(i));
    }

    public void append(int paramId, String string) {
        append(paramId, string, true);
    }

    public void append(int paramId, String string, boolean fillLength) {
        this.length += 1;
        addWord(paramId);
        if (string == null || "".equals(string)) {
            if (fillLength) {
                addByte(0);
            }
            return;
        }
        addByte(string.getBytes().length);
        append(string);
    }

    public void insertFirst(byte b) {
        byte[] arr = getBytes();
        byte[] newArray = new byte[arr.length + this.capacity];
        System.arraycopy(arr, 0, newArray, 1, arr.length);
        newArray[0] = b;
        this.array = newArray;
        this.index += 1;
    }

    public byte[] getBytes() {
        byte[] newArray = new byte[this.index];
        System.arraycopy(this.array, 0, newArray, 0, this.index);
        return newArray;
    }

    public byte[] getNewBytes(byte[] by) {
        byte[] oldArray = getBytes();
        byte[] newArray = new byte[oldArray.length - by.length];
        System.arraycopy(oldArray, 0, newArray, 0, newArray.length);
        return newArray;
    }

    public int getLength() {
        return this.length;
    }
}
