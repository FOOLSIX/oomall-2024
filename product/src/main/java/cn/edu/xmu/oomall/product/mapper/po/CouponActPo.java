//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.mapper.po;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.product.dao.bo.Activity;
import cn.edu.xmu.oomall.product.dao.bo.CouponAct;
import cn.edu.xmu.oomall.product.model.strategy.BaseCouponDiscount;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "activity")
@CopyFrom({CouponAct.class, Activity.class})
@ToString
public class CouponActPo {
    @MongoId
    private String objectId;
    private LocalDateTime couponTime;
    private Integer quantity;
    private Integer quantityType;
    private Integer validTerm;
    private String strategy;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public LocalDateTime getCouponTime() {
        return couponTime;
    }

    public void setCouponTime(LocalDateTime couponTime) {
        this.couponTime = couponTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(Integer quantityType) {
        this.quantityType = quantityType;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }


    public String getStrategy() {
        return strategy;
    }

    @CopyFrom.Exclude({CouponAct.class})
    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}
