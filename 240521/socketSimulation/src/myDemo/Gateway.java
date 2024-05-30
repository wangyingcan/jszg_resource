package myDemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Date;

/**
 * @className Gateway
 * @Description 网关对于战车是客户端，对于控制中心是服务端
 * @Author wangyingcan
 * @DATE 2024/5/23 15:47
 */
public class Gateway {
    // 1.网关默认端口
    private static final int DEFAULT_PORT = Constant.GATEWAY_PORT;

    // 2.网关指定端口（对象只会创建一次，所以使用final）
    private final int port;

    // 3.无参构造函数，调用有参构造函数设置好默认端口
    public Gateway() {
        this(DEFAULT_PORT);
    }

    // 4.有参构造函数，设置好port
    public Gateway(int port) {
        this.port = port;
    }

    // 5.服务端开始工作：主要工作函数
    public void start() throws Exception {
        System.out.println("gateway start...");

        // 5.1 创建Socket对象
        ServerSocket serverSocket = new ServerSocket(this.port);

        // 5.2 保持监听
        while (true) {
            // 5.3 阻塞等待连接，accept返回socket对象
            Socket socket = serverSocket.accept();      // socket存在客户端的输出流、服务端的输入流

            // 5.4 连接了客户端，开启一个新的线程：客户端通信  +  IO
            new Thread(() -> {
                // 5.5 获取输入流InputStream对象[字节流形式读取数据]

                try {
                    // 初始化获取数据
                    byte[] data = new byte[1024];

                    // 服务端的输入流，可以从其中读取客户端输出流的写数据
                    InputStream inputStream = socket.getInputStream();

                    // 5.6 获取输出流对象，响应发送客户端
                    OutputStream outputStream = socket.getOutputStream();


                    // 按字节数循环读取直到读完（获取客户端请求的内容）
                    int len;
                    String chariotIdStr = "";
                    String fullRequest = "";
                    while ((len = inputStream.read(data)) != -1) {
                        fullRequest = new String(data, 0, len) + "\n";
                        System.out.print(fullRequest);

                        // 解析请求确定应该发送到哪辆战车
                        chariotIdStr = fullRequest.substring(0, 1);
                        System.out.println("解析的战车编号：" + chariotIdStr);

                        // 写入日志
                        File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_gateway.txt");

                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);   // 追加写文件
                        // FileOutputStream fileOutputStream = new FileOutputStream(file, false);   // 覆盖写文件

                        fileOutputStream.write(fullRequest.getBytes());

                        // 模拟网关对控制中心请求的处理
                        Thread.sleep(3000);

                        // 调用网关作为客户端的函数
                        requestToChariot(chariotIdStr, fullRequest);

                        // 接受到请求之后返回回复信息
                        outputStream.write(new String(new Date() + " 战车" + chariotIdStr + "执行成功").getBytes());
                        outputStream.flush();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }


            }).start();

        }
    }

    // 1.连接战车的IP
    private String chariotHost;

    // 2.连接战车的端口
    private int chariotPort;

    // 3.网关本身的socket对象
    private Socket socket;

    // 6.面向战车的客户端(编号、控制中心请求内容)
    public void connect() {

        System.out.println("gateway connect to chariot");

        try {
            Socket socket = new Socket(this.chariotHost, this.chariotPort);

            this.socket = socket;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 转发请求并接收响应
    public void requestToChariot(String chariotId, String fullRequest) {

        System.out.println("gateway request to chariot");

        // 6.1 根据chariotId获取战车的IP和端口
        String chariotHost = "127.0.0.1";
        int chariotPort = Constant.CHARIOT1_PORT;       // 默认使用战车1的端口

        if (chariotId.equals("1")) {
            chariotPort = Constant.CHARIOT1_PORT;
        } else if (chariotId.equals("2")) {
            chariotPort = Constant.CHARIOT2_PORT;
        } else if (chariotId.equals("3")) {
            chariotPort = Constant.CHARIOT3_PORT;
        }

        // 6.2设置成员变量
        this.chariotHost = chariotHost;
        this.chariotPort = chariotPort;

        // 6.3创建socket
        connect();

        // 6.4网关发送请求
        new Thread(() -> {
            try {

                OutputStream outputStream = this.socket.getOutputStream();

                // 生成日志内容
                Date now = new Date();
                String logData = new String(now + " 已成功将请求转发给战车" + chariotId + "\n");

                System.out.print(logData);

                // 转发请求
                outputStream.write(fullRequest.getBytes());

                // 日志写文件
                File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_gateway.txt");

                FileOutputStream fileOutputStream = new FileOutputStream(file, true);   // 追加写文件
                // FileOutputStream fileOutputStream = new FileOutputStream(file,false);   // 覆盖写文件

                fileOutputStream.write(logData.getBytes());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 6.5网关获取战车响应
        try {
            // 获取客户端的输入流
            InputStream inputStream = socket.getInputStream();
            byte[] data = new byte[1024];

            // 检查服务端是否有数据发送
            while (inputStream.available() == 0) {
                // 如果服务端没有数据发送，就让线程暂停一下，不断查询
                Thread.sleep(100);
            }

            // 按字节数循环读取直到读完
            int len;
            while ((len = inputStream.read(data)) != -1) {
                String controlCenterRequest = new String(data, 0, len) + "\n";
                System.out.print(controlCenterRequest);

                // 写入文件作为日志
                File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_gateway.txt");

                FileOutputStream fileOutputStream = new FileOutputStream(file, true);   // 追加写文件
                // FileOutputStream fileOutputStream = new FileOutputStream(file,false);   // 覆盖写文件

                fileOutputStream.write(controlCenterRequest.getBytes());

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws Exception {
        // 1.初始化默认端口的Gateway对象
        Gateway gateway = new Gateway(Constant.GATEWAY_PORT);

        // 2.开启gateway
        gateway.start();
    }
}
