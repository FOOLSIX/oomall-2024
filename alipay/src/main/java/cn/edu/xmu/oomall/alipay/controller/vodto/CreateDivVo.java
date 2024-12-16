package cn.edu.xmu.oomall.alipay.controller.vodto;

import cn.edu.xmu.oomall.alipay.service.bo.RoyaltyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 绑定分账请求参数
 * @author huangzian
 * 2023-dgn1-006
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDivVo {

    /**
     * 外部请求号
     * shopChannel.id
     */
    private String outRequestNo;
    /**
     * 分账接收方列表，单次传入最多20个
     */
    private List<RoyaltyEntity> receiverList;
}
