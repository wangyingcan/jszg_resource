package com.jszg.loki_demo.app.data.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className LogTestVO
 * @Description TODO
 * @Author wangyingcan
 * @DATE 2024/3/31 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogTestVO {

    private String status;

    private String message;

}
