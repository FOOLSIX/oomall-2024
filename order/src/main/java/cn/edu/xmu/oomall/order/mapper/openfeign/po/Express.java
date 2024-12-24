package cn.edu.xmu.oomall.order.mapper.openfeign.po;


import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.mapper.openfeign.ExpressMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class Express {

    private ExpressMapper expressMapper;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Byte status;

    private String address;

    /**
     * 未发货
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte UNSHIPPED = 0;
    /**
     * 在途
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte SHIPPED = 1;
    /**
     * 签收
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte SIGNED = 2;
    /**
     * 取消
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte CANCELED = 3;
    /**
     * 拒收
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte REJECTED = 4;
    /**
     * 已退回
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte RETURNED = 5;
    /**
     * 丢失
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte LOST = 6;
    /**
     * 回收
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte RECEIVED = 7;
    /**
     * 破损
     */
    @ToString.Exclude
    @JsonIgnore
    public static Byte BROKEN = 8;

    /**
     * 状态和名称的对应
     */
    public static final Map<Byte, String> STATUSNAMES = new HashMap() {
        {
            put(UNSHIPPED, "未发货");
            put(SHIPPED, "在途");
            put(SIGNED, "签收");
            put(CANCELED, "取消");
            put(REJECTED, "拒收");
            put(RETURNED, "已退回");
            put(LOST, "丢失");
            put(RECEIVED, "回收");
            put(BROKEN, "破损");
        }
    };


    /**
     * 获得当前状态名称
     *
     * @return
     */
    public String getStatusName() {
        return STATUSNAMES.get(this.status);
    }

    public void cancel(UserDto userDto){
        this.changeStatus(CANCELED);
        this.expressMapper.save(this);
    }

    public void changeStatus(Byte status){
        this.setStatus(status);
    }

    public void changeAddress(String newAddress,UserDto userDto){
        this.setAddress(newAddress);
        this.expressMapper.save(this);
    }

}
