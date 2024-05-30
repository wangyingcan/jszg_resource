package demo1;

import java.net.*;
/**
 * @className TcpServer2
 * @Description 模拟服务端：多客户端可连接并通信
 * @Author wangyingcan
 * @DATE 2024/5/23 11:46
 */
public class TcpServer2 {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8001);
            while (true) {
                Socket socket = serverSocket.accept();      // 阻塞，等到有客户端连接上来
                System.out.println("监听到 client 连接...");
                // 启动一个线程
                new Thread(new Worker(socket)).start();
            }
            //ss.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
