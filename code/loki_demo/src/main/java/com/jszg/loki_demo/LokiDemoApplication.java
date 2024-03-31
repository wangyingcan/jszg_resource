package com.jszg.loki_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class LokiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LokiDemoApplication.class, args);

        while(true) {
            // 3.模拟获取战车当前的弹药量（0-9）、存活状态（10%死亡状态），之后可以换为真实数据
            boolean isAlive = true;
            int remainAmmunition = (int) (Math.random() * 10);
            // System.out.println("remainAmmunition: " + remainAmmunition);
            if (remainAmmunition == 0) {
                isAlive = false;
            }

            // 4.根据战车弹药量和存活状态，打印不同级别的日志信息
            if (!isAlive) {
                log.error("This is an ERROR message，战车已毁坏，无法继续战斗");
                // 修改成输出到指定文件中（暂时不会logback的重定向），mkdir -p /var/log/Loki_demo，然后创建当日.log文件，最后追加日志内容


            } else {
                if(remainAmmunition >= 7) {
                    log.info("This is an INFO message，战车弹药充足，战车剩余弹药量："+remainAmmunition);
                }else if(remainAmmunition >= 4) {
                    log.debug("This is a DEBUG message，战车弹药较充足，战车剩余弹药量："+remainAmmunition);
                }else {
                    log.warn("This is a WARNING message，战车弹药告急，战车剩余弹药量："+remainAmmunition);
                }
            }

            // 5.每隔1秒循环一次
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
