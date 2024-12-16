package cn.edu.xmu.oomall.wechatpay.util;

/**
 * @author maguoqi
 * @date 2023/12/12
 */
public enum WeChatPayReturnNo {

    //200
    OK(200, "成功"),

    //400
    ORDER_CLOSED(400, "订单已关闭"),
    ORDER_PAID(400, "订单已支付"),
    PARAM_ERROR(400, "参数错误"),
    INVALID_REQUEST(400, "请求参数符合参数格式，但不符合业务规则"),
    INCONSISTENT_AMOUNT(400, "订单金额与记录不一致"),

    //403
    OUT_TRADE_NO_USED(403, "商户订单号重复"),
    OUT_REFUND_NO_USED(403, "商户退款单号重复"),
    OUT_DIVPAY_NO_USED(403, "商户分账单号重复"),
    OUT_DIVREFUND_NO_USED(403, "商户回退单号重复"),
    OUT_RECEIVER_NO_USED(403, "分账接收方重复"),
    ORDER_FAIL(403, "对应支付单未成功支付"),
    REFUND_AMOUNT_ERROR(403, "退款金额错误"),
    DIV_AMOUNT_ERROR(403, "分账金额错误"),
    NO_AUTH(403, "商户无权限"),
    NOT_ENOUGH(403, "分账金额不足"),

    //404
    RESOURCE_NOT_EXISTS(404, "查询的资源不存在"),

    //429
    FREQUENCY_LIMITED(429, "请求频率过高"),
    RATELIMIT_EXCEED(429, "添加/删除接收方频率过高"),

    //500
    SYSTEM_ERROR(500, "系统错误");


    private int status;
    private String message;

    WeChatPayReturnNo(int status, String message){
        this.status = status;
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public int getStatus(){
        return status;
    }
}
