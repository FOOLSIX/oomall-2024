package cn.edu.xmu.oomall.freight.mapper.openfeign.jt;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 创建运单信息类
 *
 * @author 徐森彬
 * 2023-dgn2-003
 */

@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddExpressOrderParam {

    /**
     * 客户编码
     * 对应ShopLogistics的 account
     */
    private String customerCode;


    /**
     * 客户订单号
     * 对应express的orderCode
     */
    private String txlogisticId;

    /**
     * 快件类型，默认为 EZ
     * EZ(标准快递) TYD(兔优达)
     */
    private String expressType = "EZ";

    /**
     * 订单类型，默认为2
     * 1 散客；2 月结；
     */
    private String orderType = "2";

    /**
     * 服务类型，默认为01
     * 02 门店寄件  01 上门取件
     */
    private String serviceType = "01";

    /**
     * 派送类型，默认为03
     * 06 代收点自提 05 快递柜自提 04 站点自提 03 派送上门
     */
    private String deliveryType = "03";

    /**
     * 支付方式，默认为PP_PM
     * PP_PM("寄付月结"),
     * CC_CASH("到付现结")
     */
    private String payType = "PP_PM";

    /**
     * 寄件信息对象
     */
    private PersonInfo sender;

    /**
     * 收件信息对象
     */
    private PersonInfo receiver;

    /**
     * 物品类型，对应订单主表物品类型。
     * 可选值及其对应的描述如下：
     * - bm000001 文件
     * - bm000002 数码产品
     * - bm000003 生活用品
     * - bm000004 食品
     * - bm000005 服饰
     * - bm000006 其他
     * - bm000007 生鲜类
     * - bm000008 易碎品
     * - bm000009 液体
     */
    private String goodsType;

    /**
     * 重量，单位kg,范围0.01-30
     */
    private String weight;

    /**
     * 物流公司上门取货开始时间 yyyy-MM-dd HH:mm:ss
     */
    private String sendStartTime;

    /**
     * 物流公司上门取货结束时间 yyyy-MM-dd HH:mm:ss
     */
    private String sendEndTime;
}
