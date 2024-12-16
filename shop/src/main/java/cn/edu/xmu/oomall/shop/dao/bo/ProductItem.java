//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.shop.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.shop.controller.dto.ProductItemDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom({ProductItemDto.class})
public class ProductItem implements Cloneable {

    /**
     * 产品订单明细Id
     */
    private Long orderItemId;

    /**
     * 产品Id
     */
    private Long productId;

    /**
     * 单价
     * 用不到这个属性
     */
    @JsonIgnore
    private Long price;

    /**
     * 单重
     */
    private Integer weight;

    /**
     * 免邮门槛
     * 用不到这个属性
     */
    @JsonIgnore
    private Long freeShipThreshold;

    /**
     * 数量
     */
    private Integer quantity;

    public ProductItem clone() throws CloneNotSupportedException {
        return (ProductItem) super.clone();
    }

    /**
     * clone一个新ProductItem，设置新的数量
     *
     * @param quantity
     * @return
     * @throws CloneNotSupportedException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-20 10:29
     */
    public ProductItem cloneWithQuantity(Integer quantity) throws CloneNotSupportedException {
        ProductItem ret = this.clone();
        ret.setQuantity(quantity);
        return ret;
    }

    /**
     * @return
     * @author ZhaoDong Wang
     * 2023-dgn1-009
     */
    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }


    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
