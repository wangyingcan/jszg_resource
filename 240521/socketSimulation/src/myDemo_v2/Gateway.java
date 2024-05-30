package myDemo_v2;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.*;

public class Gateway {
    private static final int DEFAULT_PORT = Constant.GATEWAY_PORT;
    private final int port;
    private ServerSocket serverSocket;
    private Socket chariotSocket; // 用于与战车通信的socket

    public Gateway(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        System.out.println("Gateway start...");
        this.serverSocket = new ServerSocket(this.port);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    handleClient(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void handleClient(Socket clientSocket) throws IOException, InterruptedException {
        InputStream inputStream = clientSocket.getInputStream();
        OutputStream outputStream = clientSocket.getOutputStream();

        while (true) {
            byte[] data = new byte[1024];
            int len = inputStream.read(data);
            if (len == -1) break;

            String fullRequest = new String(data, 0, len);
            System.out.println("Received from ControlCenter: " + fullRequest);

            // 写入日志文件
            File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_gateway.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(fullRequest.getBytes());

            // 解析请求确定应该发送到哪辆战车
            String chariotIdStr = fullRequest.substring(0, 1);
            System.out.println("解析的战车编号：" + chariotIdStr);

            // 模拟网关对控制中心请求的处理
            Thread.sleep(3000); // 模拟耗时操作

            // 转发请求到战车
            forwardRequestToChariot(chariotIdStr, fullRequest);

            // 等待战车的响应
            byte[] response = new byte[1024];
            int responseLen = chariotSocket.getInputStream().read(response);
            String chariotResponse = new String(response, 0, responseLen);
            System.out.println("Received from Chariot " + chariotIdStr + ": " + chariotResponse);

            // 发送战车的响应回控制中心
            outputStream.write(chariotResponse.getBytes());
            outputStream.flush();

            // 写入日志文件
            fileOutputStream.write(chariotResponse.getBytes());
        }
    }

    private void forwardRequestToChariot(String chariotId, String fullRequest) throws IOException {
        // 根据chariotId连接到对应的战车
        int chariotPort = getChariotPort(chariotId);
        chariotSocket = new Socket("127.0.0.1", chariotPort);

        // 发送请求
        OutputStream chariotOutputStream = chariotSocket.getOutputStream();
        chariotOutputStream.write(fullRequest.getBytes());
        chariotOutputStream.flush();
    }

    private int getChariotPort(String chariotId) {
        int chariotPort;
        switch (chariotId) {
            case "1":
                chariotPort = Constant.CHARIOT1_PORT;
                break;
            case "2":
                chariotPort = Constant.CHARIOT2_PORT;
                break;
            case "3":
                chariotPort = Constant.CHARIOT3_PORT;
                break;
            default:
                throw new IllegalArgumentException("Invalid chariot ID");
        }
        return chariotPort;
    }

    public static void main(String[] args) throws Exception {
        Gateway gateway = new Gateway(DEFAULT_PORT);
        gateway.start();
    }
}