package cn.edu.xmu.oomall.sfexpress.exception;

/**
 * 顺丰错误码枚举类
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
public enum SFErrorCodeEnum {
    E1010(1010, "寄件地址不能为空", "Shipper's address is required.", "address不能为空"),
    E1011(1011, "寄件联系人不能为空", "Shipper's contract name is required.", "contact不能为空"),
    E1012(1012, "寄件电话不能为空", "Shipper's telephone number is required.", "mobile和tel不能都为空"),
    E1014(1014, "到件地址不能为空", "Receiver's address is required.", "address不能为空"),
    E1015(1015, "到件联系人不能为空", "Receiver's contact name is required.", "contact不能为空"),
    E1016(1016, "到件电话不能为空", "Receiver's telephone number is required.", "mobile和tel不能都为空"),
    E1020(1020, "出口件邮编不能为空", "Postal code is required for International shipments.", "postCode不能为空"),
    E1023(1023, "拖寄物品名不能为空", "Commodity name is required.", "cargoDetails下面的name不能为空"),
    E1028(1028, "出口件时，拖寄物数量不能为空", "Commodity quantity is required for international shipments.", "cargoDetails下面的count不能为空"),
    E1038(1038, "出口件声明价值不能为空", "The declared value is required for International shipments.", "cargoDeclaredValue不能为空"),
    E6126(6126, "月结卡号不合法", "Invalid credit account number.", "monthlyCard月结卡号必须为10位数字"),
    E6127(6127, "增值服务名不能为空", "AVS name is required.", "serviceList下面的name为空"),
    E6128(6128, "增值服务名不合法", "Invalid AVS name.", "serviceList 下面name传值不正确"),
    E6130(6130, "体积参数不合法", "Invalid Volume Parameters", "volume传参不正确"),
    E6138(6138, "代收货款金额传入错误", "COD amount data error.", "serviceList中name为COD 对应的value为数字"),
    E6139(6139, "代收货款金额小于0错误", "Error! COD amount is less than 0.", "serviceList中name为COD 对应的value必须大于0"),
    E6150(6150, "找不到该订单", "This order cannot be found.", "确认orderId是否正确"),
    E6200(6200, "国际件寄方邮编不能为空", "The shipper postal code is required for International shipment.", "postCode不能为空"),
    E6201(6201, "国际件到方邮编不能为空", "The receiver postal code is required for International shipment.", "postCode不能为空"),
    E6202(6202, "国际件货物数量不能为空", "The cargo quantity is required for International shipment.", "cargoDetails下面的count不能为空"),
    E6203(6203, "国际件货物单位不能为空", "The cargo unit is required for International shipment.", "cargoDetails下面的unit不能为空"),
    E6204(6204, "国际件货物单位重量不能为空", "The cargo unit weight is required for International shipment.", "cargoDetails下面的weight不能为空"),
    E6205(6205, "国际件货物单价不能为空", "The cargo unit value is required for International shipment.", "cargoDetails下面的amount不能为空"),
    E6206(6206, "国际件货物币种不能为空", "The cargo currency is required for International shipment.", "cargoDetails下面的currency不能为空"),
    E6207(6207, "国际件原产地不能为空", "Origin code is required for International shipment.", "cargoDetails下面的sourceArea不能为空"),
    E8003(8003, "查询单号超过最大限制", "The query AWB number exceeds the limit.", "路由查询最多支持10个单号查下"),
    E8013(8013, "未传入查询单号", "AWB number for query is not received.", "确认orderId传值是否正确"),
    E8016(8016, "重复下单", "Duplicated order ID.", "orderId不能重复"),
    E8017(8017, "订单号与运单号不匹配", "Order number does not match with AWB number.", "确认传入的订单号orderId跟运单号是否匹配"),
    E8018(8018, "未获取到订单信息", "Order information is not received.", "确认订单号orderId是否传错"),
    E8024(8024, "未下单", "Order is not yet placed.", "调用下单接口获取订单号"),
    E8252(8252, "订单已确认", "Order already confirmed.", "订单已经确认过了，再次确认会报错"),
    E8037(8037, "订单已消单", "Order already cancelled.", "订单已经取消，再次取消会报错"),
    E8027(8027, "不存在的业务模板", "Business template does not exist.", "bizTemplateCode传入了不存在的模板 或者传空了"),
    E8067(8067, "超过最大能申请子单号数量", "Exceed the maximum number of the available sub waybills.", "下单接口默认最大申请子单号数量我307个"),
    E8096(8096, "您的预约超出今日营业时间，无法上门收件。", "sendStartTm传工作时间。或者isDocall传0", ""),
    E8114(8114, "传入了不可发货的月结卡号", "联系销售经理增加该月结卡号下单权限", ""),
    E8117(8117, "下单包裹不能大于307个", "", "下单接口默认最大申请子单号数量我307个"),
    E8119(8119, "月结卡号不存在或已失效", "", "传入的monthlyCard不存在或已失效"),
    E8168(8168, "订单已生成路由不能申请子单", "", "该订单已揽收"),
    E8191(8191, "运单号格式不正确", "", "waybillType不能都传1"),
    E8194(8194, "跨境件必须包含申明价值和币别", "", "跨境件申明价值（consValue）和申明价值币别（consValueCurrencyCod）e必须要传"),
    E8196(8196, "信息异常", "", "电话号码黑名单"),
    E8247(8247, "运单号不合法", "", "请核实运单号是否是顺丰运单号（注意顺丰生产环境 测试环境 丰桥上面的单不能混用）"),
    E8053(8053, "目的地不在定时派送服务范围内", "", "到件地址不支持定时派送。可以去掉定时派送（IN26）增值服务"),
    E8052(8052, "原寄地不在定时派送服务范围内", "", "寄件地址不支持定时派送。可以去掉定时派送（IN26）增值服务"),
    E8051(8051, "定时派送不在时效范围内，下单失败", "", "传入的时间不在时效范围内，可以根据返回响应的时间段来传值"),
    E8179(8179, "卡号下未查到关联相应协议", "", "需要找销售签订对应的产品协议"),
    E8177(8177, "类似（正值运力高峰期，普通会员（非会员）的寄件通道预约已满，敬请谅解）提示语组成 BPS：策略编号", "", "高峰管控"),
    E20012(20012, "定时派送服务不支持重量超过300KG的快件", "", "totalWeight不能超过300kg"),
    E20011(20011, "产品与定时派送服务时间段不匹配", "", "修改TDELIVERY增值服务value1传值"),
    E8256(8256, "部分快件产品不支持到付和寄付现结，请调整付款方式后下单", "", "付款方式payMethod传1 3 并且monthlyCard需要传值"),
    E20035(20035, "托寄物违禁品不可收寄", "", "修改托寄物cargoDetails的name"),
    E20036(20036, "适用产品不满足", "", "更改产品expressTypeId重新下单，如不行，请联系顺丰销售业务经理处理"),
    E8057(8057, "快件类型为空或未配置", "", "expressTypeId不正确，请参看《快件产品类别表》");

    private final int errorCode;
    private final String errorChineseDesc;
    private final String errorEnglishDesc;
    private final String handlingAdvice;

    SFErrorCodeEnum(int errorCode, String errorChineseDesc, String errorEnglishDesc, String handlingAdvice) {
        this.errorCode = errorCode;
        this.errorChineseDesc = errorChineseDesc;
        this.errorEnglishDesc = errorEnglishDesc;
        this.handlingAdvice = handlingAdvice;
    }

    public static SFErrorCodeEnum getByErrorCode(String errorCodeString) {
        int errorCode = Integer.parseInt(errorCodeString);
        for (SFErrorCodeEnum errorCodeEnum : SFErrorCodeEnum.values()) {
            if (errorCodeEnum.getErrorCode() == errorCode) {
                return errorCodeEnum;
            }
        }
        return null;
    }

    public static SFErrorCodeEnum getByErrorCode(int errorCode) {
        for (SFErrorCodeEnum errorCodeEnum : SFErrorCodeEnum.values()) {
            if (errorCodeEnum.getErrorCode() == errorCode) {
                return errorCodeEnum;
            }
        }
        return null;
    }

    public static SFErrorCodeEnum getByErrorChineseDesc(String errorChineseDesc) {
        for (SFErrorCodeEnum errorCodeEnum : SFErrorCodeEnum.values()) {
            if (errorCodeEnum.getErrorChineseDesc().equals(errorChineseDesc)) {
                return errorCodeEnum;
            }
        }
        return null;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorCodeString() {
        return "S" + errorCode;
    }

    public String getErrorChineseDesc() {
        return errorChineseDesc;
    }

    public String getErrorEnglishDesc() {
        return errorEnglishDesc;
    }

    public String getHandlingAdvice() {
        return handlingAdvice;
    }

    public String getErrorDescAndAdvice() {
        return errorChineseDesc + ", " + handlingAdvice;
    }
}

