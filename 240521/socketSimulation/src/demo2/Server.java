package demo2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @className Server
 * @Description TODO
 * @Author wangyingcan
 * @DATE 2024/5/23 13:04
 */
public class Server {

    // 1.服务端默认端口
    private static final int DEFAULT_PORT = 3333;

    // 2.服务端指定端口（对象只会创建一次，所以使用final）
    private final int port;

    // 3.无参构造函数，调用有参构造函数设置好默认端口
    public Server() {
        this(DEFAULT_PORT);
    }

    // 4.有参构造函数，设置好port
    public Server(int port) {
        this.port = port;
    }

    // 5.服务端开始工作：主要工作函数
    public void start() throws Exception {
        // 5.1 创建Socket对象
        ServerSocket serverSocket = new ServerSocket(this.port);

        // 5.2 保持监听
        while (true) {
            // 5.3 阻塞等待连接，accept返回socket对象
            Socket socket = serverSocket.accept();      // socket存在客户端的输出流、服务端的输入流

            // 5.4 连接了客户端，开启一个新的线程：客户端通信  +  IO
            new Thread(() -> {
                // 5.5 获取输入流InputStream对象[字节流形式读取数据]
                // 一直打开流，时刻监听输入流的数据
                while (true) {
                    try {
                        // 初始化获取数据
                        byte[] data = new byte[1024];

                        // 服务端的输入流，可以从其中读取客户端输出流的写数据
                        InputStream inputStream = socket.getInputStream();

                        // 5.6 获取输出流对象，响应发送客户端
                        OutputStream outputStream = socket.getOutputStream();


                        // 按字节数循环读取直到读完（获取客户端请求的内容）
                        int len;
                        while (inputStream.available() > 0 && (len = inputStream.read(data)) != -1) {
                            System.out.println(new String(data, 0, len));
                        }

                        // 模拟服务端对客户端请求的处理
                        Thread.sleep(3000);

                        // 接受到请求之后返回回复信息
                        outputStream.write(new String(new Date() + " Server received information").getBytes());
                        outputStream.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }).start();

        }
    }

    public static void main(String[] args) throws Exception {
        // 1.初始化默认端口的Server对象
        Server server = new Server();
        // 2.开启server
        server.start();
    }
}
