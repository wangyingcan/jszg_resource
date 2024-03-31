package com.jszg.loki_demo.app.restful;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className HelloWorld
 * @Description 测试 Hello World 能否打印到web界面上   ---   OK
 * @Author wangyingcan
 * @DATE 2024/3/26 22:09
 */
@RestController
@RequestMapping("/test")
public class HelloWorldController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

}
