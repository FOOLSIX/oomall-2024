//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.alipay.controller.vodto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * 取消订单信息参数
 * @author Wenbo Li
 * */

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelOrderVo {
    /*二选一*/
    /**
     * 支付宝交易号
     * payTrans.trans_no
     * */
    private String tradeNo;

    private String outTradeNo;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
