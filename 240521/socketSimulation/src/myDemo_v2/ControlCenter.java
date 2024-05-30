package myDemo_v2;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @className ControlCenter
 * @Description 控制中心作为发送请求的客户端
 * @Author wangyingcan
 * @DATE 2024/5/23 15:46
 */
public class ControlCenter {

    // 连接网关的IP和端口
    private final String gatewayHost;
    private final int gatewayPort;

    // 客户端本身的socket对象
    private Socket socket;

    // 用于定时发送请求的线程池
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ControlCenter(String gatewayHost, int gatewayPort) {
        this.gatewayHost = gatewayHost;
        this.gatewayPort = gatewayPort;
    }

    public void connect() {
        System.out.println("ControlCenter connect start...");

        try {
            // 建立连接
            socket = new Socket(this.gatewayHost, this.gatewayPort);
            System.out.println("ControlCenter connected to Gateway.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void request() {
        System.out.println("ControlCenter request start...");

        // 定时任务，每3秒发送一次请求
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // 获取输出流，准备写请求
                OutputStream outputStream = socket.getOutputStream();

                // 生成请求内容：命令时间、战车编号(1,2,3)、请求内容
                Date now = new Date();
                int chariotId = (int) ((3 * Math.random()) + 1);
                String requestContext = generateRequest();
                String fullRequest = chariotId + " " + now + " " + requestContext + "\n";

                System.out.print(fullRequest);

                // 写入文件作为日志
                File file = new File("/Users/wangyingcan/00 工作目录/1 IDC_projects/3 江山重工/3 我的工作/5.21 代码模拟控制中心、网关、战车及其通信/模拟工作/socketSimulation/socketSimulation/src/log/log_controlcenter.txt");
                FileOutputStream fileOutputStream = new FileOutputStream(file, true); // 追加写文件
                fileOutputStream.write(fullRequest.getBytes());

                // 模拟耗时操作
                Thread.sleep(1000); // 模拟发送请求的耗时

                // 发送请求
                outputStream.write(fullRequest.getBytes());
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 3, TimeUnit.SECONDS);
    }

    public String generateRequest() {
        // 随机生成请求内容的逻辑保持不变
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

    public static void main(String[] args) {
        ControlCenter controlCenter = new ControlCenter("127.0.0.1", Constant.GATEWAY_PORT);
        controlCenter.connect();
        controlCenter.request();

        // 为了防止主线程结束导致定时任务线程结束，主线程在这里等待
        try {
            while (true) {
                Thread.sleep(10000); // 每10秒检查一次，防止应用退出
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}