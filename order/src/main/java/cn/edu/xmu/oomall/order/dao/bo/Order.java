//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.controller.dto.OrderUpdateDto;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.openfeign.ExpressDao;
import cn.edu.xmu.oomall.order.mapper.openfeign.po.Express;
import cn.edu.xmu.oomall.order.mapper.po.OrderPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jaxb.core.WhiteSpaceProcessor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ToString(callSuper = true, doNotUseGetters = true)
@Slf4j
@NoArgsConstructor
@CopyFrom({OrderPo.class})
@Data
public class Order extends OOMallObject {

    private OrderDao orderDao;

    @Builder
    public Order(Long id, Long creatorId, String creatorName,String status, Long modifierId, String modifierName, LocalDateTime gmtCreate,
                 LocalDateTime gmtModified, Long customerId, Long shopId, String orderSn, Long pid,
                 String consignee, Long regionId, String address, String mobile, String message,
                 Long activityId, Long packageId, List<OrderItem> orderItems) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.customerId = customerId;
        this.shopId = shopId;
        this.orderSn = orderSn;
        this.pid = pid;
        this.consignee = consignee;
        this.regionId = regionId;
        this.address = address;
        this.mobile = mobile;
        this.message = message;
        this.activityId = activityId;
        this.packageId = packageId;
        this.orderItems = orderItems;
        this.status = status;
    }

    @Setter
    @Getter
    private String status;

    @Setter
    @ToString.Exclude
    @JsonIgnore
    private ExpressDao expressDao;

    private Long customerId;

    private Long shopId;

    @Setter
    @Getter
    private String orderSn;

    @Setter
    @Getter
    private Long pid;

    @Setter
    @Getter
    private String consignee;

    @Setter
    private Long regionId;

    @Setter
    @Getter
    private String address;

    @Setter
    @Getter
    private String mobile;

    @Setter
    @Getter
    private String message;

    @Setter
    private Long activityId;

    @Setter
    private Long packageId;

    @Setter
    @Getter
    private List<OrderItem> orderItems;


    /**
     * 无效态0
     * 待接受1
     * 待发货2
     * 已发货3
     * 已完成4
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte INVALID = 0;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte PENDING_ACCEPTANCE = 1;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte PENDING_DISPATCH = 2;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte DISPATCHED = 3;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte COMPLETED = 4;


    @Override
    public void setGmtCreate(LocalDateTime gmtCreate) {

    }

    @Override
    public void setGmtModified(LocalDateTime gmtModified) {

    }

    @Transactional
    public void cancelOrder(UserDto userDto) {
        // 检查订单是否属于当前用户
        if (!userDto.getId().equals(this.customerId)) {
            log.debug("订单不属于当前用户...");
            throw new BusinessException(
                    ReturnNo.ORDER_NOTOWNED,
                    String.format(ReturnNo.ORDER_NOTOWNED.getMessage(), this.getId())
            );
        }

        // 查询订单的物流信息
        Express express = expressDao.findById(this.packageId);
        if (express == null) {
            log.debug("物流信息不存在，无法取消订单...");
            throw new BusinessException(
                    ReturnNo.RESOURCE_NOT_FOUND,
                    String.format(ReturnNo.RESOURCE_NOT_FOUND.getMessage(), this.getId())
            );
        }

        // 检查物流状态
        String statusName = express.getStatusName();
        if (!statusName.equals("未发货")) {
            log.debug("订单已发货，无法取消...");
            throw new BusinessException(
                    ReturnNo.ORDER_ALREADY_SHIPPED,
                    String.format(ReturnNo.ORDER_ALREADY_SHIPPED.getMessage(), this.getId())
            );
        }

        // 修改订单状态
        this.changeStatus(INVALID);

        // 修改运单状态
        express.cancel();
    }

    @Transactional
    public void updateOrder(UserDto userDto, OrderUpdateDto orderUpdateDto){
        // 需要本人才能修改
        if (!userDto.getId().equals(this.customerId)) {
            log.debug("订单不属于当前用户...");
            throw new BusinessException(
                    ReturnNo.ORDER_NOTOWNED,
                    String.format(ReturnNo.ORDER_NOTOWNED.getMessage(), this.getId())
            );
        }

        // 查询订单的物流信息
        Express express = expressDao.findById(this.packageId);
        if (express == null) {
            log.debug("物流信息不存在，无法取消订单...");
            throw new BusinessException(
                    ReturnNo.RESOURCE_NOT_FOUND,
                    String.format(ReturnNo.RESOURCE_NOT_FOUND.getMessage(), this.getId())
            );
        }

        // 检查物流状态,未发货才可以进行修改
        String statusName = express.getStatusName();
        if (!statusName.equals("未发货")) {
            log.debug("订单已发货，无法取消...");
            throw new BusinessException(
                    ReturnNo.ORDER_ALREADY_SHIPPED,
                    String.format(ReturnNo.ORDER_ALREADY_SHIPPED.getMessage(), this.getId())
            );
        }

        // 现在仅支持修改订单收货地址
        this.changeAddress(orderUpdateDto.getAddress());

        // 修改运单的收货地址
        express.changeAddress(orderUpdateDto.getAddress());

    }

    public void confirm(UserDto userDto) {
        // 需要本人才能确认
        if (!this.getShopId().equals(userDto.getId())) {
            log.debug("当前用户无权确认订单, shopId: {}, userId: {}", this.getShopId(), userDto.getId());
            throw new BusinessException(
                    ReturnNo.INCONSISTENT_DATA,
                    String.format(ReturnNo.INCONSISTENT_DATA.getMessage(), "商铺", this.getShopId(), "用户不匹配")
            );
        }

        Long packageId = this.createExpress(this);
        if (packageId == null) {
            log.debug("创建快递信息失败, orderId: {}", this.getId());
            throw new BusinessException(
                    ReturnNo.INTERNAL_SERVER_ERR,
                    "创建快递信息失败"
            );
        }
        this.setPackageId(packageId);
        log.debug("快递信息创建成功, orderId: {}, packageId: {}", this.getId(), packageId);

        this.changeStatus(PENDING_DISPATCH);
        log.debug("订单状态修改为待发货, orderId: {}", this.getId());
    }


    private void changeStatus(byte status){
        this.setStatus(String.valueOf(status));
        log.info("order status = {}",this.getStatus());
        this.orderDao.save(this);
    }

    private void changeAddress(String newAddress){
        this.setAddress(newAddress);
        log.info("order address = {}",newAddress);
        this.orderDao.save(this);
    }

    private Long createExpress(Order order){
        Long packageId = this.expressDao.createExpress(order);
        log.info("creteExpress: packageId = {}",packageId);
        return packageId;
    }


}
