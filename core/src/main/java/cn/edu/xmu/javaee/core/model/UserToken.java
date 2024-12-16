//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.javaee.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
/**
 * 用户token
 */
public class UserToken {
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门id
     * 0 平台管理员
     * 非0 为服务商和商户
     */
    private Long departId;
    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 用户级别
     * 0 平台管理员 1 商户  2 服务商
     */
    private Integer userLevel;

    public void setDepartId(Long departId) {
        this.departId = departId;
    }
}
