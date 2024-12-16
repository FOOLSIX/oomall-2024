package cn.edu.xmu.oomall.wechatpay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivReceiver;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.DivReceiverPoMapper;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivReceiverPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivReceiverPoExample;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DivReceiverDao {
    private final DivReceiverPoMapper divReceiverPoMapper;

    @Autowired
    DivReceiverDao(DivReceiverPoMapper divReceiverPoMapper) {
        this.divReceiverPoMapper = divReceiverPoMapper;
    }

    public List<DivReceiver> getDivReceiversByOrderId(String orderId) {
        DivReceiverPoExample divReceiverPoExample = new DivReceiverPoExample();
        DivReceiverPoExample.Criteria divReceiverPoCriteria = divReceiverPoExample.createCriteria();
        divReceiverPoCriteria.andOrderIdEqualTo(orderId);
        List<DivReceiverPo> receiverPoList = divReceiverPoMapper.selectByExample(divReceiverPoExample);
        if (receiverPoList.isEmpty()) {
            return null;
        }
        return receiverPoList.stream().map(po -> CloneFactory.copy(new DivReceiver(), po)).collect(Collectors.toCollection(ArrayList::new));
    }

    public DivReceiver createDivReceiver(DivReceiver divReceiver) {
        try {
            DivReceiverPo divReceiverPo = CloneFactory.copy(new DivReceiverPo(), divReceiver);
            divReceiverPoMapper.insertSelective(divReceiverPo);
            return divReceiver;
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }
}
