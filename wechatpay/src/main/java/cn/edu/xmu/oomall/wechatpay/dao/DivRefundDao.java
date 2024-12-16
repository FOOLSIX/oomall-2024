package cn.edu.xmu.oomall.wechatpay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivRefundTrans;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivRefundTransPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivRefundTransPoExample;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.DivRefundTransPoMapper;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DivRefundDao {

    private final DivRefundTransPoMapper divRefundTransPoMapper;

    @Autowired
    public DivRefundDao(DivRefundTransPoMapper divRefundTransPoMapper) {
        this.divRefundTransPoMapper = divRefundTransPoMapper;
    }

    public DivRefundTrans getDivRefundTransByOutReturnNo(String outReturnNo) {
        try {
            DivRefundTransPoExample example = new DivRefundTransPoExample();
            DivRefundTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andOutReturnNoEqualTo(outReturnNo);
            List<DivRefundTransPo> list = divRefundTransPoMapper.selectByExample(example);
            if (list.isEmpty()) {
                return null;
            }
            return CloneFactory.copy(new DivRefundTrans(), list.get(0));
        }
        catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public DivRefundTrans createDivRefundTrans(DivRefundTrans divRefundTrans) {
        try {
            DivRefundTransPo divRefundTransPo = CloneFactory.copy(new DivRefundTransPo(), divRefundTrans);
            divRefundTransPoMapper.insertSelective(divRefundTransPo);
            return divRefundTrans;
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }
}
