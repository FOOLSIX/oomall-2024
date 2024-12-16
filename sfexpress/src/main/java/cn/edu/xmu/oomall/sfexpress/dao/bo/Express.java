package cn.edu.xmu.oomall.sfexpress.dao.bo;

import cn.edu.xmu.oomall.sfexpress.dao.ExpressStatusEnum;
import cn.edu.xmu.oomall.sfexpress.exception.SFErrorCodeEnum;
import cn.edu.xmu.oomall.sfexpress.exception.SFException;
import cn.edu.xmu.oomall.sfexpress.utils.IdWorker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * searchOrderResp的请求msgData
 *
 * @author Zhouzhe Fan
 * dgn3-004-fzz
 */
@NoArgsConstructor
@Data
public class Express {

    /**
     * 语言，默认zh-CN，必填
     */
    private String language = "zh-CN";

    /**
     * 客户订单号，必填，须唯一
     */
    private String orderId;

    /**
     * 货物详细信息
     */
    private List<CargoDetail> cargoDetails;

    /**
     * 拖寄物类型描述,如： 文件，电子产品，衣服等，非必填
     */
    private String cargoDesc;

    /**
     * 收寄双方的信息
     */
    private List<ContactInfo> contactInfoList;

    /**
     * 月结卡号
     */
    private String monthlyCard;

    /**
     * 付款方式，支持以下值： 1:寄方付 2:收方付 3:第三方付
     * 默认1
     */
    private Integer payMethod = 1;

    /**
     * 快件产品类别， 支持附录 《快件产品类别表》 的产品编码值，仅可使用与顺丰销售约定的快件产品
     * 《快件产品类别表》 :
     * https://open.sf-express.com/developSupport/734349?activeIndex=324604
     */
    private Integer expressTypeId;

    private int status;
    private Double totalHeight;
    private Double totalLength;
    private Double totalVolume;
    private Double totalWeight;
    private Double totalWidth;
    private String waybillNo;
    private Integer waybillType;
    private Timestamp sendStartTm;
    private Timestamp pickupAppointEndtime;
    private Timestamp createTime;
    private String originCode;
    private String destCode;

    //methods

    /**
     * 检查物流订单状态，异常的状态转移会抛出异常
     *
     * @param origin
     * @param target
     */
    public void checkStatus(int origin, int target) {
        SFErrorCodeEnum returnError = ExpressStatusEnum.getByStatus(origin, target);
        if (null != returnError) throw new SFException(returnError);
    }

    public void generateWaybillNo() {
        IdWorker idWorker = new IdWorker(1L, 1L);
        Long id = idWorker.nextId();
        this.waybillNo = "SF" + id;
    }
}
