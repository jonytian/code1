package com.legaoyi.file.messagebody;

public class Attachment {

    private String fileName;

    private Long offset;

    private Long length;

    private byte[] data;

    public final String getFileName() {
        return fileName;
    }

    public final void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public final Long getOffset() {
        return offset;
    }

    public final void setOffset(Long offset) {
        this.offset = offset;
    }

    public final Long getLength() {
        return length;
    }

    public final void setLength(Long length) {
        this.length = length;
    }

    public final byte[] getData() {
        return data;
    }

    public final void setData(byte[] data) {
        this.data = data;
    }

}
