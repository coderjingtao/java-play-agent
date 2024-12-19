package com.github.coderjingtao.agent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Joseph.Liu
 */
public class MethodTimer{
    private static final ConcurrentHashMap<String,MethodStats> STATS_MAP = new ConcurrentHashMap<>();
    private static final String LOG_FILE = "method_stats.log";

    public static void start(String methodName){
        MethodStats methodStats = STATS_MAP.computeIfAbsent(methodName, k -> new MethodStats());
        methodStats.start();
    }

    public static void end(String methodName){
        MethodStats methodStats = STATS_MAP.get(methodName);
        if(methodStats != null){
            methodStats.end();
        }
    }

    public static void shutdown(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE,true))){
            for(var entry : STATS_MAP.entrySet()){
                String methodName = entry.getKey();
                MethodStats stats = entry.getValue();
                bw.write(String.format("Method: %s, Calls: %d, TotalTime(ms): %d%n",methodName,stats.getCount(),stats.getTotalTime()));
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    static class MethodStats{
        private long startTime;
        private long totalTime;
        private int count;

        public void start(){
            startTime = System.currentTimeMillis();
        }

        public void end(){
            totalTime += System.currentTimeMillis() - startTime;
            count++;
        }

        public int getCount(){
            return count;
        }

        public long getTotalTime(){
            return totalTime;
        }
    }

    static {
        //添加JVM钩子，确保程序退出时，写入日志
        Runtime.getRuntime().addShutdownHook(new Thread(MethodTimer::shutdown));
    }
}
