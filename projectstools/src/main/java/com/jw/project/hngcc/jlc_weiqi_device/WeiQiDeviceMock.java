package com.jw.project.hngcc.jlc_weiqi_device;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * 模拟尾气数据发送
 *
 * @author Brook
 */
class WeiQiDeviceMock {

    private static final String SERVER = "47.95.245.251";
    // private static final String SERVER = "localhost";
    private static final int PORT = 8341;
    private static final int SEND_INTERVAL = 20; // 发送间隔（秒）
    private static final String DIR = "D:\\jlc_weiqi\\weiqi_dir";

    public static void main(String[] args) throws Exception {
        File dir = new File(DIR);
        File[] files = dir.listFiles((dir1, name) -> name.startsWith("190") && name.endsWith(".txt"));

        CountDownLatch threadCount = new CountDownLatch(files.length);

        for (File file : files) {
            new Thread(() -> {
                sendByFile(file);
                threadCount.countDown();
            }).start();
        }

        threadCount.await();
        System.out.println("发送完毕");
    }

    private static void sendByFile(File file) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file));
                Socket socket = new Socket(SERVER, PORT);
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String msg = line.split("：")[1];
                System.out.println(".>>> " + msg);
                socket.getOutputStream().write(msg.getBytes());
                socket.getOutputStream().flush();
                try {
                    Thread.sleep(SEND_INTERVAL * 1000);
                } catch (InterruptedException e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
