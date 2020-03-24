package com.legaoyi.storer.util;

import org.apache.commons.lang3.StringUtils;

import com.legaoyi.common.util.SnowflakeIdWorker;

public class IdGenerator {

    public static SnowflakeIdWorker idGenerate;

    private static final Object lock = new Object();

    public static String nextIdStr() {
        return String.valueOf(nextId());
    }

    public static long nextId() {
        if (idGenerate == null) {
            synchronized (lock) {
                if (idGenerate == null) {
                    String workerId = ServerRuntimeContext.getProperty("snowflake.workerId");
                    idGenerate = new SnowflakeIdWorker(0, StringUtils.isEmpty(workerId) ? 0 : Long.parseLong(workerId));
                }
            }
        }
        return idGenerate.nextId();
    }
}
