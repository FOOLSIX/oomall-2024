package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 返回结果类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnObj {
    
    /*
     * 返回结果
     */
    private Object result;

    /*
     * 返回消息，错误详情见相关文档错误码解释。
     */
    private String message;

    /*
     * 返回状态，true：调用成功；false：调用失败
     */
    private boolean status;

    /*
     * 返回状态Code，错误详情见相关文档错误码解释。
     */
    private String statusCode;
}
