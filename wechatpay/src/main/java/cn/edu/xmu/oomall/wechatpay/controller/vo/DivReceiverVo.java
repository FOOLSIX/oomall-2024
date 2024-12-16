package cn.edu.xmu.oomall.wechatpay.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DivReceiverVo {
    private String type;
    private String account;
    private Integer amount;
    private String description;
}
