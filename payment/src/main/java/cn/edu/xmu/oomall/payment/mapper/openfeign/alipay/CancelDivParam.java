package cn.edu.xmu.oomall.payment.mapper.openfeign.alipay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 解绑分账请求参数
 * @author huangzian
 * 2023-dgn1-006
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelDivParam {
    /**
     * 外部请求号
     * account.id
     */
    private String out_request_no;
    /**
     * 分账接收方列表，单次传入最多20个
     */
    private List<RoyaltyEntity> receiver_list;
}
