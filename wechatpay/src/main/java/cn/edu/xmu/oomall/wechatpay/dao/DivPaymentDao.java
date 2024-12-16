package cn.edu.xmu.oomall.wechatpay.dao;

import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.wechatpay.dao.bo.DivPayTrans;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivPayTransPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivPayTransPoExample;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.DivPayTransPoMapper;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayException;
import cn.edu.xmu.oomall.wechatpay.util.WeChatPayReturnNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class DivPaymentDao {
    private final DivPayTransPoMapper divPayTransPoMapper;
    private final DivReceiverDao divReceiverDao;

    @Autowired
    public DivPaymentDao(DivPayTransPoMapper divPayTransPoMapper, DivReceiverDao divReceiverDao) {
        this.divPayTransPoMapper = divPayTransPoMapper;
        this.divReceiverDao = divReceiverDao;
    }

    public DivPayTrans createDivPayTrans(DivPayTrans divPayTrans) {
        try {
            DivPayTransPo divPayTransPo = CloneFactory.copy(new DivPayTransPo(), divPayTrans);
            divPayTransPoMapper.insertSelective(divPayTransPo);
            divPayTrans.getReceivers().forEach(divReceiverDao::createDivReceiver);
            return divPayTrans;
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public DivPayTrans getDivPayTransByOutOrderNo(String OutOrderNo) {
        try {
            // 查divPayTrans
            DivPayTransPoExample divPayTransPoExample = new DivPayTransPoExample();
            DivPayTransPoExample.Criteria divPayTransCriteria = divPayTransPoExample.createCriteria();
            divPayTransCriteria.andOutOrderNoEqualTo(OutOrderNo);
            List<DivPayTransPo> list = divPayTransPoMapper.selectByExample(divPayTransPoExample);
            if (list.isEmpty()) {
                return null;
            }
            return CloneFactory.copy(new DivPayTrans(), list.get(0));
        } catch (Exception e) {
            throw  new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }

    public List<DivPayTrans> getDivPayTransByTransId(String transId) {
        try {
            // 查divPayTrans
            DivPayTransPoExample divPayTransPoExample = new DivPayTransPoExample();
            DivPayTransPoExample.Criteria divPayTransCriteria = divPayTransPoExample.createCriteria();
            divPayTransCriteria.andTransactionIdEqualTo(transId);
            List<DivPayTransPo> list = divPayTransPoMapper.selectByExample(divPayTransPoExample);
            if (list.isEmpty()) {
                return null;
            }
            return list.stream().map(po -> CloneFactory.copy(new DivPayTrans(), po)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new WeChatPayException(WeChatPayReturnNo.SYSTEM_ERROR, e.getMessage());
        }
    }
}
