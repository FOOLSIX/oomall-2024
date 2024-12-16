package cn.edu.xmu.oomall.wechatpay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.wechatpay.dao.bo.PayTrans;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.PayTransPoMapper;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.PayTransPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.PayTransPoExample;
import cn.edu.xmu.oomall.wechatpay.mapper.notify.WeChatPayNotifyMapper;
import cn.edu.xmu.oomall.wechatpay.mapper.notify.po.PaymentNotifyPo;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentDao {

    private final PayTransPoMapper payTransPoMapper;
    private final WeChatPayNotifyMapper weChatPayNotifyMapper;

    @Autowired
    public PaymentDao(PayTransPoMapper payTransPoMapper, WeChatPayNotifyMapper weChatPayNotifyMapper) {
        this.payTransPoMapper = payTransPoMapper;
        this.weChatPayNotifyMapper = weChatPayNotifyMapper;
    }

    public PayTrans createPayTrans(PayTrans payTrans) {
        try {
            PayTransPo payTransPo = payTrans.buildPo(CloneFactory.copy(new PayTransPo(), payTrans));
            payTransPoMapper.insertSelective(payTransPo);
            return payTrans;
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public PayTrans getPayTransByOutNo(String outTradeNo) {
        try {
            PayTransPoExample example = new PayTransPoExample();
            PayTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andOutTradeNoEqualTo(outTradeNo);
            List<PayTransPo> list = payTransPoMapper.selectByExample(example);
            if (list.isEmpty()) {
                return null;
            }
            PayTransPo payTransPo = list.get(0);
            return CloneFactory.copy(new PayTrans(), payTransPo).buildBo(payTransPo);
        }
        catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public PayTrans getPayTransByTransId(String transactionId) {
        try {
            PayTransPoExample example = new PayTransPoExample();
            PayTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andTransactionIdEqualTo(transactionId);
            List<PayTransPo> list = payTransPoMapper.selectByExample(example);
            if (list.isEmpty()) {
                return null;
            }
            PayTransPo payTransPo = list.get(0);
            return CloneFactory.copy(new PayTrans(), payTransPo).buildBo(payTransPo);
        }
        catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public void updatePayTrans(PayTrans payTrans) {
        try {
            PayTransPo payTransPo = payTrans.buildPo(CloneFactory.copy(new PayTransPo(), payTrans));
            PayTransPoExample example = new PayTransPoExample();
            PayTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andOutTradeNoEqualTo(payTransPo.getOutTradeNo());
            int ret = payTransPoMapper.updateByExampleSelective(payTransPo, example);
            if (ret == 0) {
                throw new WeChatPayException(WeChatPayReturnNo.RESOURCE_NOT_EXISTS);
            }
        }
        catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public ReturnObject notifyPayment(PayTrans payTrans) {
        String notifyUrl = payTrans.getNotifyUrl();
        PaymentNotifyPo po = new PaymentNotifyPo(payTrans);
        return weChatPayNotifyMapper.notifyPayment(notifyUrl, po);
    }

}
