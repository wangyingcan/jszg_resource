package demo2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * @className Client
 * @Description TODO
 * @Author wangyingcan
 * @DATE 2024/5/23 13:35
 */
public class Client {

    // 1.连接的服务端的IP
    private final String serverHost;

    // 2.连接的服务端的端口
    private final int serverPort;

    // 3.客户端本身的socket对象
    private Socket socket;

    // 4.构造函数
    public Client(String serverHost,int serverPort){
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    // 5.客户端连接函数
    public void connect(){
        // 5.1创建客户端的socket对象，完成客户端对指定服务端的连接
        try {
            Socket socket = new Socket(this.serverHost,this.serverPort);

            // 5.2 成员变量的设置
            this.socket = socket;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // 6.客户端请求函数
    public void request(){
        new Thread(() -> {
            while(true){
                try {
                    // 获取客户端的输出流
                    OutputStream outputStream = this.socket.getOutputStream();

                    // 写入字节流数据
                    outputStream.write((new Date() + " Hello Server").getBytes());

                    // 刷新
                    outputStream.flush();

                    // 暂停一下，然后进行下一轮的写数据
                    // Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while(true){
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
                while((len = inputStream.read(data))!=-1){
                    System.out.println(new String(data,0,len));
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    // 7.主函数
    public static void main(String[] args) throws Exception{
        // 7.1初始化客户端
        Client client = new Client("127.0.0.1",3333);
        client.connect();
        client.request();
    }
}