package myDemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @className Chariot3
 * @Description 战车3 服务端之一
 * @Author wangyingcan
 * @DATE 2024/5/23 15:48
 */
public class Chariot2 {
    // 1.服务端默认端口
    private static final int DEFAULT_PORT = Constant.CHARIOT2_PORT;

    // 2.服务端指定端口（对象只会创建一次，所以使用final）
    private final int port;

    // 3.无参构造函数，调用有参构造函数设置好默认端口
    public Chariot2() {
        this(DEFAULT_PORT);
    }

    // 4.有参构造函数，设置好port
    public Chariot2(int port) {
        this.port = port;
    }

    // 5.服务端开始工作：主要工作函数
    public void start() throws Exception {

        System.out.println("战车2 start...");

        ServerSocket serverSocket = new ServerSocket(this.port);

        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(() -> {
                try {
                    while(true) {
                        byte[] data = new byte[1024];
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();

                        int len;
                        String fullRequest = "";
                        while ((len = inputStream.read(data)) != -1) {
                            fullRequest = new String(data, 0, len) + "\n";
                            System.out.print(fullRequest);

                            // 写入日志
                            File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_chariot2.txt");

                            FileOutputStream fileOutputStream = new FileOutputStream(file,true);   // 追加写文件
                            // FileOutputStream fileOutputStream = new FileOutputStream(file, false);   // 覆盖写文件

                            fileOutputStream.write(fullRequest.getBytes());
                        }

                        Thread.sleep(3000);

                        String logData = new String(new Date() + " 战车2" + "执行指令" + "成功\n");
                        System.out.print(logData);

                        outputStream.write(logData.getBytes());
                        File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_chariot2.txt");

                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                        fileOutputStream.write(logData.getBytes());

                        outputStream.flush();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);

                }

            }).start();
        }
    }

    public static void main(String[] args) throws Exception {
        Chariot2 chariot2 = new Chariot2(Constant.CHARIOT2_PORT);

        chariot2.start();
    }
}
