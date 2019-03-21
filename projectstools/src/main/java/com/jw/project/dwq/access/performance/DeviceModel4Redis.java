package com.jw.project.dwq.access.performance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * 生成redis数据
 *
 * @author Brook
 */
public class DeviceModel4Redis {
    private static final long START_DEVICE = 19500000000L;
    private static final long END_DEVICE = 19501000000L;

    public static void main(String[] args) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("D://device_modle.txt")));
        for (long i = START_DEVICE; i < END_DEVICE; i++) {
            writer.write("hset device:model " + i + " D160");
            writer.newLine();
        }
        System.out.println("END");
        writer.close();
    }
}
