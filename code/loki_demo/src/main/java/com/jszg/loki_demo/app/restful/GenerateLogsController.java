package com.jszg.loki_demo.app.restful;

import com.jszg.loki_demo.app.data.CommonResult;
import com.jszg.loki_demo.app.data.vo.LogTestVO;
import com.jszg.loki_demo.app.service.GenerateLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className generateLogs
 * @Description TODO
 * @Author wangyingcan
 * @DATE 2024/3/28 11:33
 */
@RestController
@RequestMapping("/log")
public class GenerateLogsController {

    @Autowired
    private GenerateLogsService generateLogsService;

    @GetMapping("/generateLogs")
    public CommonResult<LogTestVO> generateLogs(){
        // 1. 由于实现是死循环，所以很难用return来测试是否执行成功，因此用try-catch，其实这里也执行不到return（除非出现异常）
        try{
            // 1.1 执行成功则返回成功信息
            generateLogsService.generateLogs();
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
