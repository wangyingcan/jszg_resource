package com.jszg.loki_demo.app.restful;

import com.jszg.loki_demo.app.data.CommonResult;
import com.jszg.loki_demo.app.data.vo.LogTestVO;
import com.jszg.loki_demo.app.service.GenerateLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @className generateLogs
 * @Description TODO
 * @Author wangyingcan
 * @DATE 2024/3/28 11:33
 */
// @RestController直接返回HTTP响应体的JSON数据
// @Controller返回的是视图，即页面
@RestController
@RequestMapping("/log")
public class GenerateLogsController {

    @Autowired
    private GenerateLogsService generateLogsService;

    /**
       1.  http://localhost:8080/app?count=10  @GetMapping("/generateLogs")  @RequestParam("count") int count
       2.  http://localhost:8080/app/10  @GetMapping("/generateLogs/{count}")  @PathVariable("count") int count
     **/
    @GetMapping("/generateLogs")
    public CommonResult<LogTestVO> generateLogs(@RequestParam("count") int count){
        // 1. 将死循环换成URL指定的日志产生条数
        System.out.println("模拟产生日志条数："+count);
        try{
            // 1.1 执行成功则返回成功信息
            generateLogsService.generateLogs(count);
            // 利用builder链式构造返回的VO对象
            LogTestVO logTestVO=LogTestVO.builder()
                    .status("success")
                    .message("正在不断地生成日志中...")
                    .build();
            return new CommonResult<LogTestVO>().makeSuccess().addData(logTestVO).addMessage("日志生成成功!");
        }catch(Exception e){
            // 1.2 执行失败则返回失败信息
            e.printStackTrace();
            // 利用builder链式构造返回的VO对象
            LogTestVO logTestVO=LogTestVO.builder()
                    .status("failed")
                    .message("日志生成器出现异常")
                    .build();
            return new CommonResult<LogTestVO>().makeFail().addMessage("日志生成失败!").addData(logTestVO);
        }
    }
}
