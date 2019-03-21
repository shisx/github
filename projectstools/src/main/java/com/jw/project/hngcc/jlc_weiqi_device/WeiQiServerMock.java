package com.jw.project.hngcc.jlc_weiqi_device;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Desc
 *
 * @author Brook
 */
class WeiQiServerMock {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8341);

        while (true) {
            final Socket socket = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 建立转发连接
                        Socket router = null;
                        try {
                            router = new Socket();
                            router.connect(new InetSocketAddress("39.108.136.8", 8123), 3000);
                        } catch (IOException e) {
                            System.out.println("建立转发连接失败！" + e.getMessage());
                        }

                        InputStream inputStream = socket.getInputStream();
                        byte[] buff = new byte[1024];
                        int len;
                        while ((len = inputStream.read(buff)) != -1) {
                            String content = new String(buff, 0, len);
                            System.out.println(FORMATTER.format(LocalTime.now()) + "  收到：" + content);

                            if (content.startsWith("shebeihao")) {
                                String response = String.valueOf(RANDOM.nextInt(65535));
                                socket.getOutputStream().write(response.getBytes());
                                socket.getOutputStream().flush();
                                System.out.println(FORMATTER.format(LocalTime.now()) + "  回复：" + response);
                            }

                            // 转发
                            router.getOutputStream().write(content.getBytes());
                            router.getOutputStream().flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
    }

}
