package cn.edu.xmu.oomall.freight.mapper.openfeign.zt;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * 账号信息类
 * @Author 李子晴
 * 2023-dgn2-001
 */
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountInfo
{
    /*
     * 电子面单账号
     * partnerType为2（非集团客户），orderType传1,2,4时必传（全网件，预约件（返回运单号），星联全网件）
     * 对应ShopLogistics-account
     */
    private String accountType;

    /*
     * 电子面单密码
     * 测试环境传ZTO123
     * 对应ShopLogistics-secret
     */
    private String accountPassword;

    /*
     * 单号类型
     * 1.普通电子面单；74.星联电子面单；默认是1
     */
    private String type="1";

}
    


