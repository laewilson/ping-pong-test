package org.example.common.limiter.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.*;

@Slf4j
public class CrossProcessRateLimiter extends  AbstractRateLimiter implements IRateLimiter {
    private static final String FILE_PATH_SUFFIX = "_rate_limit_counter.txt";
    private static final long REFILL_INTERVAL_MS = 1000; // 重置计数器的时间间隔
    private String filePath;

    public CrossProcessRateLimiter(long maxRequestsPerSecond,String key) {
        this.filePath = key + FILE_PATH_SUFFIX;
        setMaxRequestsPerSecond(maxRequestsPerSecond);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CrossProcessRateLimiter limiter = new CrossProcessRateLimiter(1,null);
        for (int i = 1; i <= 20; i++) { // 模拟20次请求
            long startTime = System.currentTimeMillis();
            if (limiter.tryAcquire()) {
                System.out.println(startTime+ " Request_" + i + " processed.");
                Thread.sleep(150); // 模拟处理时间
            } else {
                System.out.println(startTime+ " Request " + i + " rejected due to rate limit.");
                Thread.sleep(100);
            }
        }
    }

    public String getFilePath() {
        return this.filePath;
    }
    @Override
    public boolean tryAcquire() {
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(getFilePath(), "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        FileChannel channel = file.getChannel();
        FileLock lock = null;

        try {
            lock = channel.lock(); // 获取文件锁
            long currentTime = System.currentTimeMillis();
            long lastRefillTime = file.length() > 0 ? Long.parseLong(file.readLine()) : 0;
            long requestCount = file.length() > 0 ? Long.parseLong(file.readLine()) : 0;

            // 如果超过重置时间间隔，则重置计数器
            if (currentTime - lastRefillTime > REFILL_INTERVAL_MS) {
//                log.info("Resetting counter due to time interval.");
                lastRefillTime = currentTime;
                requestCount = 0;
            }

            // 检查是否超出限制
            if (requestCount < getMaxRequestsPerSecond()) {
                requestCount++;
                // 更新文件内容
                file.seek(0);
                file.writeBytes(lastRefillTime + "\n" + Long.toString(requestCount));
                file.setLength(file.getFilePointer()); // 截断多余内容
                return true; // 允许请求
            } else {
                return false; // 拒绝请求
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            try {
                if (lock != null) {
                    lock.release(); // 释放锁
                }
                file.close();
                channel.close();
            }catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}