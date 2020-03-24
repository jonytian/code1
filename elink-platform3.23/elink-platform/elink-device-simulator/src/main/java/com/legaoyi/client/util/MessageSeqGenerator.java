package com.legaoyi.client.util;

public class MessageSeqGenerator {

    private static int seq = 0;

    public synchronized static int getNextSeq() {
        if (seq == Integer.MAX_VALUE) {
            seq = 0;
        }
        return seq++;
    }
}
