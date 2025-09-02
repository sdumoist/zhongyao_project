package com.xpxp.util;

public class SnowflakeIdGenerator {
    // 起始时间戳（可设置为项目启动时间）
    private final static long START_TIMESTAMP = 1609459200000L; // 2021-01-01 00:00:00
    
    // 机器ID所占位数
    private final static long WORKER_ID_BITS = 5L;
    // 数据中心ID所占位数
    private final static long DATACENTER_ID_BITS = 5L;
    // 序列号所占位数
    private final static long SEQUENCE_BITS = 12L;
    
    // 最大机器ID (2^5-1)
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    // 最大数据中心ID (2^5-1)
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    
    // 各部分左移位数
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;
    
    private final long workerId;
    private final long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeIdGenerator(long workerId, long datacenterId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID 必须介于0和" + MAX_WORKER_ID + "之间");
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("Datacenter ID 必须介于0和" + MAX_DATACENTER_ID + "之间");
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        
        if (timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨异常，拒绝生成ID");
        }
        
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }
        
        lastTimestamp = timestamp;
        
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }
    
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}