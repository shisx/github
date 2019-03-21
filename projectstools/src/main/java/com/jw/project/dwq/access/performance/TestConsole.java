package com.jw.project.dwq.access.performance;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试桩统一控制台
 *
 * @author Brook
 */
public class TestConsole {

    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyMMddHHmmss");

    private static String server = "192.168.0.168";
    private static int port = 8184;
    private static long startDevice = 19500000000L;
    private static int deviceSize = 20000;
    private static long sendIntervalMs = 15000;
    private static int round = 5;
    private static int printIntervalMs = 5000;
    private static int closeSocketDelayS = 60;

    // 需要运行的服务器列表
    // "test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9", "test10"
    private static final List<String> runServerList = Arrays.asList("test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9", "test10");

    public static void main(String[] args) throws Exception {
        // showJavaPid();
        // updateJavaFile();
        // deleteLogFile();
        // clearDmesg();
        batchStartTest();
    }

    /**
     * 显示java进程
     *
     * @throws Exception
     */
    private static void showJavaPid() throws Exception {
        for (String alias : runServerList) {
            System.out.println(String.format("服务器：%s(%s)", alias, serverAliasMap.get(alias)));

            Session session = getSession(alias);
            execute(session, "jps");
            session.disconnect();
        }
    }

    /**
     * 批量启动测试端
     *
     * @throws Exception
     */
    private static void batchStartTest() throws Exception {
        long currDeviceId = startDevice;
        for (String alias : runServerList) {
            System.out.println(String.format("服务器：%s(%s)，起始设备：%d", alias, serverAliasMap.get(alias), currDeviceId));

            String cmd1 = String.format("echo 'server=%s\n" +
                            "port=%d\n" +
                            "start_device=%s\n" +
                            "device_size=%d\n" +
                            "send_interval_ms=%d\n" +
                            "round=%d\n" +
                            "print_interval_ms=%d\n" +
                            "close_socket_delay_s=%d' > config.properties",
                    server, port, String.valueOf(currDeviceId), deviceSize, sendIntervalMs, round, printIntervalMs, closeSocketDelayS);
            String cmd2 = String.format("/home/access-test/start.sh > %s_%s.log &", alias, format.format(LocalDateTime.now()));

            Session session = getSession(alias);
            execute(session, cmd1);
            execute(session, cmd2);
            session.disconnect();

            currDeviceId += deviceSize;
        }
    }

    /**
     * 更新java文件
     *
     * @throws Exception
     */
    private static void updateJavaFile() throws Exception {
        File file = new File("C:\\Users\\Brook\\Desktop\\PerformanceTest.java");
        String fileContent = FileUtils.readFileToString(file);

        for (String alias : runServerList) {
            System.out.println(String.format("服务器：%s(%s)", alias, serverAliasMap.get(alias)));

            Session session = getSession(alias);
            execute(session, "rm app/PerformanceTest*");
            execute(session, "touch app/PerformanceTest.java");
            execute(session, "echo '" + fileContent + "' > ./app/PerformanceTest.java");
            execute(session, "/home/access-test/javac.sh");
            session.disconnect();
        }
    }

    /**
     * 删除日志文件
     *
     * @throws Exception
     */
    private static void deleteLogFile() throws Exception {
        for (String alias : runServerList) {
            System.out.println(String.format("服务器：%s(%s)", alias, serverAliasMap.get(alias)));

            Session session = getSession(alias);
            execute(session, "rm test*.log");
            session.disconnect();
        }
    }

    /**
     * 清空 dmesg 日志
     *
     * @throws Exception
     */
    private static void clearDmesg() throws Exception {
        for (String alias : runServerList) {
            System.out.println(String.format("服务器：%s(%s)", alias, serverAliasMap.get(alias)));

            Session session = getSessioWithRoot(alias);
            execute(session, "dmesg -C");
            session.disconnect();
        }
    }

    /**
     * 删除日志文件
     *
     * @throws Exception
     */
    private static void updateLinuxSysctl() throws Exception {
        for (String alias : runServerList) {
            System.out.println(String.format("服务器：%s(%s)", alias, serverAliasMap.get(alias)));

            Session session = getSessioWithRoot(alias);
            execute(session, "systemctl stop firewalld");
            execute(session, "systemctl disable firewalld");
            session.disconnect();
        }
    }

    private static Session getSession(String alias) throws Exception {
        return getSession("access-test", serverAliasMap.get(alias), 22, "123456");
    }

    private static Session getSessioWithRoot(String alias) throws Exception {
        return getSession("root", serverAliasMap.get(alias), 22, "root123!");
    }

    private static Session getSession(String username, String ip, int port, String password) throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, ip, port);
        session.setPassword(password);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        return session;
    }

    private static void execute(Session session, String command) throws Exception {
        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        BufferedReader input = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
        BufferedReader errInput = new BufferedReader(new InputStreamReader(channelExec.getErrStream()));
        channelExec.connect();

        //接收远程服务器执行命令的结果
        StringBuilder buf = new StringBuilder();
        String line;
        while ((line = input.readLine()) != null) {
            buf.append(line).append("\n");
        }
        while ((line = errInput.readLine()) != null) {
            buf.append(line).append("\n");
        }
        if (buf.length() > 0) {
            System.out.println(buf);
        }
        input.close();

        //关闭通道
        channelExec.disconnect();
    }

    private static Map<String, String> serverAliasMap = new LinkedHashMap<>();

    static {
        serverAliasMap.put("test1", "192.168.0.130");
        serverAliasMap.put("test2", "192.168.0.131");
        serverAliasMap.put("test3", "192.168.0.132");
        serverAliasMap.put("test4", "192.168.0.133");
        serverAliasMap.put("test5", "192.168.0.134");
        serverAliasMap.put("test6", "192.168.0.135");
        serverAliasMap.put("test7", "192.168.0.136");
        serverAliasMap.put("test8", "192.168.0.137");
        serverAliasMap.put("test9", "192.168.0.138");
        serverAliasMap.put("test10", "192.168.0.139");
    }

}
