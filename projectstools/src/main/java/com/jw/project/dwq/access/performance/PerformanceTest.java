package com.jw.project.dwq.access.performance;

import com.jw.component.protocol.j808.parser.entity.AlarmFlag;
import com.jw.component.protocol.j808.parser.entity.PositionAdditional;
import com.jw.component.protocol.j808.parser.entity.PositionInfo;
import com.jw.component.protocol.j808.parser.entity.PositionStatus;
import com.jw.component.protocol.j808.parser.message.J_0200;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 性能测试
 *
 * @author Brook
 */
public class PerformanceTest {

    private static String server = "192.168.01.167";
    private static int port = 8184;
    private static long startDevice = 19500000000L;
    private static int deviceSize = 1;
    private static long sendIntervalMs = 15000;
    private static int round = 2;
    private static int total = deviceSize * round;
    private static int printIntervalMs = 5000;
    private static int closeSocketDelayS = 60;

    private static final ExecutorService pool = Executors.newFixedThreadPool(100);
    private static final List<SocketInfo> socketList = new ArrayList<>(deviceSize);
    private static final AtomicInteger COUNT = new AtomicInteger(0);

    public static void main(String[] args) throws Exception {
        System.out.println("开始");
        configFromFile(args != null && args.length > 0 ? args[0] : null);

        new Thread(new MsgSendThread()).start();
        new Thread(new InfoPrintThread()).start();
    }

    /**
     * 从文件中加载配置
     *
     * @param filePath
     * @throws IOException
     */
    private static void configFromFile(String filePath) throws IOException {
        File file = new File(filePath == null ? "./config.properties" : filePath);
        if (!file.exists()) {
            System.out.println(file + " 文件不存在，将使用默认配置");
            return;
        }

        FileInputStream in = new FileInputStream(file);

        Properties properties = new Properties();
        properties.load(in);
        System.out.println("配置信息：\n" + properties);

        server = properties.getProperty("server", "192.168.0.167");
        port = Integer.parseInt(properties.getProperty("port", "8184"));
        startDevice = Long.parseLong(properties.getProperty("start_device", "19500000000"));
        deviceSize = Integer.parseInt(properties.getProperty("device_size", "100"));
        sendIntervalMs = Integer.parseInt(properties.getProperty("send_interval_ms", "15000"));
        round = Integer.parseInt(properties.getProperty("round", "2"));
        printIntervalMs = Integer.parseInt(properties.getProperty("print_interval_ms", "5000"));
        closeSocketDelayS = Integer.parseInt(properties.getProperty("close_socket_delay_s", "60"));
        total = deviceSize * round;
        in.close();
    }

    private static final class MsgSendThread implements Runnable {
        private int lng = 110300000;
        private int lat = 22300000;

        @Override
        public void run() {
            try {
                // 创建连接
                int connCount = 0;
                for (long j = startDevice; j < startDevice + deviceSize; j++) {
                    Socket client = new Socket();
                    client.connect(new InetSocketAddress(server, port), 10000);
                    socketList.add(new SocketInfo(String.valueOf(j), client));
                    //System.out.print((++connCount) + ".");
                }
                System.out.println("\n创建连接完毕 size: " + socketList.size());

                long sendSizePreSeconed = deviceSize / (sendIntervalMs / 1000);
                System.out.println("每秒发送数量：" + sendSizePreSeconed);
                int hasSendCount = 0;

                // 发送消息
                for (int i = 0; i < round; i++) {
                    long interval = sendIntervalMs;
                    lng += 50;
                    lat += 50;
                    for (SocketInfo socketInfo : socketList) {
                        pool.execute(new SendTask(socketInfo.device, socketInfo.socket, lng, lat));

                        // 每发送一批数据休眠1秒，将N个设备消息平均分散到M时间间隔中
                        if (++hasSendCount >= sendSizePreSeconed) {
                            hasSendCount = 0;
                            interval -= 1000;
                            Thread.sleep(1000);
                        }
                    }
                    if (interval > 0) {
                        Thread.sleep(interval);
                    }
                }
                System.out.println("消息发送完毕");

                // 关闭连接
                pool.shutdown();
                while (true) {
                    if (pool.isTerminated()) {
                        System.out.println(closeSocketDelayS + "秒后关闭所有连接。");
                        Thread.sleep(closeSocketDelayS * 1000);
                        for (SocketInfo socketInfo : socketList) {
                            socketInfo.socket.close();
                        }
                        System.out.println("连接关闭完毕");
                        break;
                    } else {
                        Thread.sleep(2000);
                    }
                }
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                shutdown();
            }
        }
    }

    /**
     * 信息打印线程
     */
    private static final class InfoPrintThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (socketList.size() == deviceSize) {
                    int num = total - COUNT.get();
                    System.out.println(num);
                    if (num <= 0) {
                        break;
                    }
                }
                try {
                    Thread.sleep(printIntervalMs);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.out);
                }
            }
        }
    }


    private static void shutdown() {
        for (SocketInfo socketInfo : socketList) {
            try {
                socketInfo.socket.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
        System.out.println("连接关闭完毕");
        System.exit(1);
    }

    private static final class SendTask implements Runnable {

        private String device;
        private Socket socket;
        private int lng;
        private int lat;

        SendTask(String device, Socket socket, int lng, int lat) {
            this.device = device;
            this.socket = socket;
            this.lng = lng;
            this.lat = lat;
        }

        @Override
        public void run() {
            try {
                byte[] msg = new0200(device, System.currentTimeMillis(), lng, lat);
                socket.getOutputStream().write(msg);
                socket.getOutputStream().flush();
                COUNT.incrementAndGet();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    private static class SocketInfo {
        private String device;
        private Socket socket;

        public SocketInfo(String device, Socket socket) {
            this.device = device;
            this.socket = socket;
        }
    }


    /**
     * 位置
     *
     * @param deviceId
     * @return
     */
    public static byte[] new0200(String deviceId, long time, int lng, int lat) {
        AlarmFlag alarmFlag = new AlarmFlag();
        alarmFlag.setSpeeding(true);

        PositionStatus status = new PositionStatus();
        status.setAccOpen(true);
        status.setValidPosition(true);
        status.setLatFlag((byte) 0x00);
        status.setLngFlag((byte) 0x00);
        status.setUseGPS(true);

        PositionAdditional positionAdditional = new PositionAdditional();
        Map<PositionAdditional.FLAG, Object> info = new HashMap<>();
        info.put(PositionAdditional.FLAG.MILEAGE, 3450);
        positionAdditional.setInfo(info);

        PositionInfo positionInfo = new PositionInfo();
        positionInfo.setAlarmFlag(alarmFlag);
        positionInfo.setPositionStatus(status);
        positionInfo.setLng(lng);
        positionInfo.setLat(lat);
        positionInfo.setAltitude(10);
        positionInfo.setSpeed(230);
        positionInfo.setDirection(63);
        positionInfo.setTime(time);
        positionInfo.setAdditional(positionAdditional);

        J_0200 msg = new J_0200();
        msg.setDeviceId(deviceId);
        msg.setPositionInfo(positionInfo);
        return msg.buildBinaryMessage();
    }

}
