package com.jszg.loki_demo.app.data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className CommonResult
 * @Description 响应封装类
 * @Author wangyingcan
 * @DATE 2024/1/15 10:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    // 响应码
    int code;
    // 响应信息
    String message;
    // 响应数据
    T data;

    // 1.请求成功、请求失败的两种情况
    /**
     * @Author wangyingcan
     * @Description  设置请求成功时的响应码
     * @Date 11:29 2024/1/15
     * @Param []
     * @return com.example.knowledgeGraph.knowledgeGraph.app.vo.CommonResult<T>
     **/
    public CommonResult<T> makeSuccess(){
        this.code=200;
        // 允许链式调用
        return this;
    }

    /**
     * @Author wangyingcan
     * @Description  设置请求失败时的响应码（自定义3000）
     * @Date 11:31 2024/1/15
     * @Param []
     * @return com.example.knowledgeGraph.knowledgeGraph.app.vo.CommonResult<T>
     **/
    public CommonResult<T> makeFail(){
        this.code=3000;
        return this;
    }

    /**
     * @Author wangyingcan
     * @Description
     * @Date 11:34 2024/1/15
     * @Param [code]
     * @return com.example.knowledgeGraph.knowledgeGraph.app.vo.CommonResult<T>
     **/
    public CommonResult<T> makeFail(int code){
        if(code==200){
            return null;
        }
        this.code=code;
        return this;
    }

    // 2.设置响应的message

    /**
     * @Author wangyingcan
     * @Description 设置响应的message
     * @Date 11:44 2024/1/15
     * @Param [message]
     * @return com.example.knowledgeGraph.knowledgeGraph.app.vo.CommonResult<T>
     **/
    public CommonResult<T> addMessage(String message){
        this.message=message;
        return this;
    }

    // 3.设置响应的data

    /**
     * @Author wangyingcan
     * @Description 设置响应的data
     * @Date 11:45 2024/1/15
     * @Param [data]
     * @return com.example.knowledgeGraph.knowledgeGraph.app.vo.CommonResult<T>
     **/
    public CommonResult<T> addData(T data){
        this.data=data;
        return this;
    }
}
