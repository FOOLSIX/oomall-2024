package cn.edu.xmu.oomall.jtexpress.exception;

import java.util.HashMap;
import java.util.Map;

public enum ReturnError {
    // 成功
    SUCCESS(1, "success"),

    // 失败
    FAILURE(0, "失败"),

    // 其他错误码
    HEADERS_SIGN_FAILURE(145003030, "Headers签名失败"),
    INTERNAL_CALL_EXCEPTION(145003040, "内部调用异常"),
    API_ACCOUNT_EMPTY(145003071, "apiAccount为空"),
    DIGEST_EMPTY(145003052, "digest为空"),
    TIMESTAMP_EMPTY(145003053, "timestamp为空"),
    SYSTEM_ERROR(145005000, "系统错误"),

    ILLEGAL_PARAMETER(145003050, "非法参数"),
    BUSINESS_PARAM_SIGN_FAILURE(145002002, "业务参数签名失败"),
    WAYBILL_DUPLICATE(145003042, "运单重复,请勿使用相同运单号！"),
    MODIFY_ORDER_FAILURE(145003041, "修改订单失败"),
    INVALID_BATCH_NO(145003111, "批次号无效！"),
    ILLEGAL_REGION(145003060, "区域不合法"),
    ILLEGAL_CITY(145003061, "城市不合法"),
    ILLEGAL_PROVINCE(145003062, "省份不合法"),
    DATA_NOT_FOUND(145003064, "查不到数据"),
    ORDER_FAILURE(145003041, "下单失败"),
    SENDER_INFO_INCOMPLETE(145003083, "发件人信息不全"),
    INVALID_BATCH_ORDER(145003112, "该批次号运单无效！"),
    ORDER_DUPLICATE(145002001, "订单重复,无需重复下单！"),
    RECEIVER_INFO_INCOMPLETE(145003084, "收件人信息不全"),
    PHONE_NUMBER_EMPTY(145003085, "电话号码不能为空"),
    ADDRESS_INFO_INCOMPLETE(145003086, "地址信息不全"),
    CHECK_ORDER_INFO_INVALID(145003087, "请检查订单类型、服务类型、派送类型、物品类型、快件类型、结算方式是否合法"),
    INVALID_SERVICE_TYPE(145003200, "请检查服务类型是否合法值为 01或 02"),
    DOOR_SERVICE_TIME_INCOMPLETE(145003088, "上门服务时间信息不全"),
    INVALID_WEIGHT(145003092, "重量信息不合法"),
    GOODS_INFO_INCOMPLETE(145003093, "物品信息不全"),
    GOODS_NAME_INCOMPLETE(145003094, "物品名称不全"),
    GOODS_TYPE_INCOMPLETE(145003095, "物品类型不全"),
    INVALID_GOODS_QUANTITY(145003096, "物品数量非法"),
    INVALID_AMOUNT(145003099, "非法金额"),
    INVALID_WAYBILL_NO(145003100, "运单号非法"),
    ORDER_ID_EXISTS(145003101, "客户订单号已存在,无法下单！"),
    INVALID_NAME(145003103, "姓名信息非法"),
    COMPANY_INFO_TOO_LONG(145003104, "公司信息过长"),
    CONTACT_INFO_TOO_LONG(145003105, "联系信息过长"),
    INVALID_POSTCODE_OR_EMAIL(145003106, "邮编或邮箱信息非法"),
    INVALID_PRICE(145003107, "价格信息非法"),
    INVALID_DESCRIPTION(145003108, "备注、描述、链接信息非法"),
    ADDRESS_TOO_LONG(145003109, "地址信息过长"),
    STREET_TOO_LONG(145003110, "街道信息过长"),
    INVALID_TOTAL_ITEMS(145003111, "包裹总件数无效"),
    COD_SERVICE_NOT_AVAILABLE(145003112, "暂未开通代收货款业务"),
    INVALID_PAYMENT_METHOD(145003113, "支付方式不匹配,PP_CASH,CC_CASH,PP_MM"),
    PICKED_STATUS_NOT_MODIFIABLE(145003201, "已取件状态不可修改"),
    CANCELED_STATUS_NOT_MODIFIABLE(145003202, "已取消状态不可修改"),
    UPDATE_ORDER_FAILURE(145003203, "更新订单失败，请稍后重试！"),
    SENDER_LOCATION_INCOMPLETE(145003083, "始发地信息不全"),
    RECEIVER_LOCATION_INCOMPLETE(145003084, "收货地信息不全"),
    STATION_CODE_INVALID(145003340, "驿站编码只能英文与数字"),
    STATION_CODE_TOO_LONG(145003341, "驿站编码最长20字符"),
    STATION_NAME_TOO_LONG(145003342, "驿站名称最长100字符"),
    STATION_ADDRESS_TOO_LONG(145003343, "驿站地址最长200字符"),
    CANCEL_REASON_NOT_NULL(145003089, "取消原因不能为空"),
    TXLOGISTIC_ID_TOO_LONG(145003082,"客户订单号不能为空或过长"),
    BILL_CODE_EXCEED_30(145003502,"运单号个数超过30");




    private final int code;
    private final String message;

    private static final Map<Integer, ReturnError> returnNoMap = new HashMap() {
        {
            for (ReturnError enum1 : ReturnError.values()) {
                put(enum1.getCode(), enum1);
            }
        }
    };

    private static final Map<String, ReturnError> returnNoMapMessage = new HashMap() {
        {
            for (ReturnError enum1 : ReturnError.values()) {
                put(enum1.getMessage(), enum1);
            }
        }
    };

    ReturnError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ReturnError getByMsg(String s) {
        ReturnError returnError = returnNoMapMessage.get(s);
        if (null == returnError) return ReturnError.ILLEGAL_PARAMETER;
        return returnError;
    }

}
