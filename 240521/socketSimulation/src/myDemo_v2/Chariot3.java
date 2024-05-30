package myDemo_v2;

import java.io.*;
import java.net.*;
import java.util.Date;

public class Chariot3 {
    private static final int DEFAULT_PORT = Constant.CHARIOT3_PORT;
    private final int port;
    private ServerSocket serverSocket;

    public Chariot3(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        System.out.println("Chariot3 start...");
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

    private void handleClient(Socket socket) throws IOException, InterruptedException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        while (true) {
            byte[] data = new byte[1024];
            int len = inputStream.read(data);
            if (len == -1) break;

            String fullRequest = new String(data, 0, len);
            System.out.println("Received request: " + fullRequest);

            // 写入日志文件
            File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_chariot3.txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write(fullRequest.getBytes());

            // 模拟耗时操作
            Thread.sleep(3000); // 模拟战车执行任务的耗时

            // 准备响应
            String response = "Chariot3 has executed the command at " + new Date();
            System.out.println("Response: " + response);

            // 发送响应
            outputStream.write(response.getBytes());
            outputStream.flush();

            // 写入日志文件
            fileOutputStream.write(response.getBytes());
        }
    }

    public static void main(String[] args) throws Exception {
        Chariot3 chariot3 = new Chariot3(DEFAULT_PORT);
        chariot3.start();
    }
}