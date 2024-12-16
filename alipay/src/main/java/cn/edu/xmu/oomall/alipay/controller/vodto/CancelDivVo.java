package cn.edu.xmu.oomall.alipay.controller.vodto;

import cn.edu.xmu.oomall.alipay.service.bo.RoyaltyEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * 解绑分账请求参数
 * @author huangzian
 * 2023-dgn1-006
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelDivVo {
    /**
     * 外部请求号
     * shopChannel.id
     */
    private String outRequestNo;
    /**
     * 分账接收方列表，单次传入最多20个
     */
    private List<RoyaltyEntity> receiverList;

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public List<RoyaltyEntity> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<RoyaltyEntity> receiverList) {
        this.receiverList = receiverList;
    }
}
