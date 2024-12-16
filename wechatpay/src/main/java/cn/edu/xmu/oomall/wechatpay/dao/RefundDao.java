package cn.edu.xmu.oomall.wechatpay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.wechatpay.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.RefundTransPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.RefundTransPoExample;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.RefundTransPoMapper;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RefundDao {

    private final RefundTransPoMapper refundTransPoMapper;

    @Autowired
    public RefundDao(RefundTransPoMapper refundTransPoMapper) {
        this.refundTransPoMapper = refundTransPoMapper;
    }

    public RefundTrans createRefund(RefundTrans refundTrans) {
        try {
            RefundTransPo refundTransPo = refundTrans.buildPo(CloneFactory.copy(new RefundTransPo(), refundTrans));
            refundTransPoMapper.insertSelective(refundTransPo);
            return refundTrans;
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public List<RefundTrans> getRefundsByOutTradeNo(String outTradeNo) {
        try {
            RefundTransPoExample example = new RefundTransPoExample();
            RefundTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andOutTradeNoEqualTo(outTradeNo);
            List<RefundTransPo> list = refundTransPoMapper.selectByExample(example);
            if (list.isEmpty()) {
                return null;
            }
            return list.stream().map(po -> CloneFactory.copy(new RefundTrans(), po).buildBo(po)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public RefundTrans getRefundByOutRefundNo(String outRefundNo) {
        try {
            RefundTransPoExample example = new RefundTransPoExample();
            RefundTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andOutRefundNoEqualTo(outRefundNo);
            List<RefundTransPo> list = refundTransPoMapper.selectByExample(example);
            if (list.isEmpty()) {
                return null;
            }
            RefundTransPo refundTransPo = list.get(0);
            return CloneFactory.copy(new RefundTrans(), refundTransPo).buildBo(refundTransPo);
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }
}
