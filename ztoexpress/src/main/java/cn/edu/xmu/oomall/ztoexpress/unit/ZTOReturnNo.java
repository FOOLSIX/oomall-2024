package cn.edu.xmu.oomall.ztoexpress.unit;


/*
    错误码
 */
public enum ZTOReturnNo {
    //公共错误码
    SYS000("请求成功",true,"SYS000"),
    S200("请求超时",false,"S200"),
    S202("发生错误",false,"S202"),
    S203("服务暂不可用",false,"S203"),
    S206("api qos limit",false,"S206"),
    S207("API不存在",false,"S207"),
    S208("XX不能为空",false,"S208"),
    S210("无权限访问",false,"S210"),
    S211("签名错误",false,"S211"),
    S212("IP黑白名单限制",false,"S212"),
    S214("时间戳非法",false,"S214"),



    //创建订单错误码
    DEF001("电子面单账号或者集团客户编码不能为空",false,"DEF001"),
    F002("电子面单账户余额不足",false,"F002"),
    po001("没有找到可以处理的订单类型",false,"p-o001"),
    po003("电子面单或者集团客户编码不存在或者没有绑定 ",false,"p-o003"),
    pow002("c端状态码返回成功，但是结果返回为空 ",false,"p-ow002"),
    //物流轨迹查询错误码
    E403("参数校验失败",false,"E403"),
    E404("鉴权失败,未绑定电子面单账号",false,"E404"),
    E405("找不到运单",false,"E405"),
    E406("找不到网点",false,"E406"),
    E409("鉴权失败,收寄人电话号码校验不一致",false,"E409"),
    E413("鉴权失败,请输入收寄人任一方电话号码后4位",false,"E413"),
    // 取消订单错误码
    E414("找不到订单",false,"E414"),
    E415("订单已取消",false,"E415"),
    E500("未知系统异常",false,"E500");

    private String message;
    private boolean status;
    private String statusCode;
    public String getMessage() {
        return message;
    }
    public boolean getStatus() {
        return status;
    }
    public String getStatusCode() {
        return statusCode;
    }
    ZTOReturnNo(ZTOReturnNo ZTOReturnNo){
        this.message= ZTOReturnNo.message;
        this.status= ZTOReturnNo.status;
        this.statusCode= ZTOReturnNo.statusCode;
    }
    ZTOReturnNo(String message, boolean status, String statusCode) {
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
    }
}
