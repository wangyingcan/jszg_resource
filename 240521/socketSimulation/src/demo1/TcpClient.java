package demo1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;


/**
 * @className TcpClient
 * @Description TODO
 * @Author wangyingcan
 * @DATE 2024/5/23 11:48
 */
public class TcpClient {
    public static void main(String[] args) {
        try {
            Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 8001); //需要服务端先开启

            // 同一个通道，服务端的输出流就是客户端的输入流；服务端的输入流就是客户端的输出流
            InputStream ips = s.getInputStream();    //开启通道的输入流
            BufferedReader brNet = new BufferedReader(new InputStreamReader(ips));

            OutputStream ops = s.getOutputStream();  //开启通道的输出流
            DataOutputStream dos = new DataOutputStream(ops);

            BufferedReader brKey = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String strWord = brKey.readLine();
                if (strWord.equalsIgnoreCase("quit")) {
                    break;
                } else {
                    System.out.println("I want to send: " + strWord);
                    // 向服务端发送数据（使用输出流）
                    dos.writeBytes(strWord + System.getProperty("line.separator"));
                    // 打印接收到的数据（使用输入流）
                    System.out.println("Server said: " + brNet.readLine());
                }

            }

            dos.close();
            brNet.close();
            brKey.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
