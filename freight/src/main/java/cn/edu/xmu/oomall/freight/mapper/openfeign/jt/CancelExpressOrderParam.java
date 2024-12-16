package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 取消运单信息类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelExpressOrderParam {
    /**
     * 客户编码
     * 对应ShopLogistics的account
     */
    private String customerCode;

    /**
     * 签名，Base64(Md5(客户编号+密文+privateKey))
     * 密文：MD5(明文密码+jadada236t2) 后大写
     * 明文密码对应ShopLogistics的secret
     * 简化需求,不需要加密，只需要传递明文密码
     */
    private String digest;
    /**
     * 订单类型
     * 1（散客），2（协议客户）
     * 固定为2
     */
    private String orderType = "2";

    /**
     * 客户订单号
     * 传客户自己系统的订单号
     * 对应Express对象的orderCode
     */
    private String txlogisticId;

    /**
     * 取消原因
     */
    private String reason;
}
