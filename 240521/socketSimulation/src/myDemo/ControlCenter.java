package myDemo;

import java.io.*;
import java.net.*;
import java.util.Date;

/**
 * @className ControlCenter
 * @Description 控制中心作为发送请求的客户端
 * @Author wangyingcan
 * @DATE 2024/5/23 15:46
 */
public class ControlCenter {

    // 1.连接网关的IP
    private final String gatewayHost;

    // 2.连接网关的端口
    private final int gatewayPort;

    // 3.客户端本身的socket对象
    private Socket socket;

    // 随机生成请求内容
    public String generateRequest(){
        // 1.随机请求编号[1,11)
        int randomNumber = (int)((10 * Math.random())+1);

        String requestContext="";

        switch (randomNumber){
            case 1:{
                requestContext = "请执行巡检指令";
                break;
            }

            case 2:{
                requestContext = "请执行开炮指令";
                break;
            }

            case 3:{
                requestContext = "请执行大数据分析指令";
                break;
            }

            case 4:{
                requestContext = "请执行大数据管理指令";
                break;
            }

            case 5:{
                requestContext = "请执行日志检索指令";
                break;
            }

            case 6:{
                requestContext = "请执行集群展示指令";
                break;
            }

            case 7:{
                requestContext = "请执行加油指令";
                break;
            }

            case 8:{
                requestContext = "请执行加水指令";
                break;
            }

            case 9:{
                requestContext = "请执行自爆指令";
                break;
            }

            case 10:{
                requestContext = "请执行同归于尽指令";
                break;
            }

            default:
                requestContext = "不执行任何指令，指挥中心出问题了";
        }

        return requestContext;
    }

     public ControlCenter(String gatewayHost,int gatewayPort){
         this.gatewayHost = gatewayHost;
         this.gatewayPort = gatewayPort;
     }

     public void connect(){

        System.out.println("ControlCenter connect start...");

         try{
             Socket socket = new Socket(this.gatewayHost,this.gatewayPort);

             this.socket = socket;
         }catch (Exception e){
             e.printStackTrace();
         }
     }

     public void request(){

        System.out.println("ControlCenter request start...");

         // 1.新建线程发送循环请求
         new Thread(()->{
             while (true){
                 try {
                     // 2.获取输出流，准备写请求
                     OutputStream outputStream = this.socket.getOutputStream();

                     // 3.生成请求内容：命令时间、战车编号(1,2,3)、请求内容
                     Date now = new Date();

                     int chariotId = (int)((3* Math.random())+1);

                     String requestcontext = generateRequest();

                     String fullRequest = new String("" + chariotId + " " + now + " " + requestcontext+"\n");

                     System.out.print(fullRequest);

                     // 写入文件作为日志
                     File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_controlcenter.txt");

                     FileOutputStream fileOutputStream = new FileOutputStream(file,true);   // 追加写文件
                     // FileOutputStream fileOutputStream = new FileOutputStream(file,false);   // 覆盖写文件

                     fileOutputStream.write(fullRequest.getBytes());

                     outputStream.write(fullRequest.getBytes());

                     // 4. 发了一次休息3s
                     Thread.sleep(3000);

                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
         }).start();

        // 2.循环等待网关的响应
         while (true){
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
                     String controlCenterRequest= new String(data,0,len)+"\n";
                     System.out.print(controlCenterRequest);

                     // 写入文件作为日志
                     File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_controlcenter.txt");

                     FileOutputStream fileOutputStream = new FileOutputStream(file,true);   // 追加写文件
                     // FileOutputStream fileOutputStream = new FileOutputStream(file,false);   // 覆盖写文件

                     fileOutputStream.write(controlCenterRequest.getBytes());

                 }

             } catch (Exception e) {
                 e.printStackTrace();
                 throw new RuntimeException(e);
             }
         }

     }

     public static void main(String[] args){
        ControlCenter controlCenter = new ControlCenter("127.0.0.1",Constant.GATEWAY_PORT);
        controlCenter.connect();
        controlCenter.request();
     }

}
