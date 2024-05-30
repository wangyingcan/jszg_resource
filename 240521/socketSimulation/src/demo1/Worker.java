package demo1;

import java.net.*;
import java.io.*;
/**
 * @className Worker
 * @Description TODO
 * @Author wangyingcan
 * @DATE 2024/5/23 11:47
 */
public class Worker implements Runnable {
    Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            System.out.println("Worker 已经启动...");
            InputStream ips = socket.getInputStream();
            OutputStream ops = socket.getOutputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(ips));
            DataOutputStream dos = new DataOutputStream(ops);
            while (true) {
                String strWord = br.readLine();
                System.out.println("client said:" + strWord + ":" + strWord.length());
                if (strWord.equalsIgnoreCase("quit")){
                    break;
                }
                String strEcho = strWord + " 666";
                // dos.writeBytes(strWord +"---->"+ strEcho +"\r\n");
                System.out.println("server said:" + strWord + "---->" + strEcho);
                dos.writeBytes(strWord + "---->" + strEcho + System.getProperty("line.separator"));
            }
            br.close();
            // 关闭包装类，会自动关闭包装类中所包装的底层类。所以不用调用ips.close()
            dos.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
