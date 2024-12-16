//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.dao.bo.DivPayTrans;
import cn.edu.xmu.oomall.payment.dao.bo.Transaction;
import cn.edu.xmu.oomall.payment.dao.channel.PayAdaptorFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.DivPayTransPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.DivPayTransPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.DivPayTransPoExample;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
@Slf4j
public class DivPayTransDao extends TransactionDao {

    private final PayTransDao payTransDao;

    private final DivPayTransPoMapper divPayTransPoMapper;

    private final DivRefundTransDao divRefundTransDao;

    private final AccountDao accountDao;

    private final PayAdaptorFactory factory;

    @Autowired
    @Lazy
    public DivPayTransDao(PayTransDao payTransDao, DivPayTransPoMapper divPayTransPoMapper, DivRefundTransDao divRefundTransDao, AccountDao accountDao, PayAdaptorFactory factory) {
        this.payTransDao = payTransDao;
        this.divPayTransPoMapper = divPayTransPoMapper;
        this.divRefundTransDao = divRefundTransDao;
        this.accountDao = accountDao;
        this.factory = factory;
    }


    /**
     * 用id返回DivPayTrans对象
     *
     * @param id 分账交易对象id
     * @return 分账交易对象
     * @throws RuntimeException
     * @throws BusinessException 无此对象时
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 23:00
     */
    public DivPayTrans findById(Long shopId, Long id) throws RuntimeException {
        DivPayTransPo po = divPayTransPoMapper.selectByPrimaryKey(id);
        if (Objects.isNull(po)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "分账交易", id));
        }
        if (!shopId.equals(PLATFORM) && !shopId.equals(po.getShopId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "分账交易", id, shopId));
        }
        return build(po);
    }


    /**
     * 查找支付交易对象对应的分账对象
     *
     * @param id 支付交易对象id
     * @return 分账交易对象，无则返回null
     * @throws RuntimeException
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 23:01
     */
    public Optional<DivPayTrans> retrieveByPayTransId(Long id) throws RuntimeException {
        DivPayTrans ret = null;
        if (Objects.nonNull(id)) {
            DivPayTransPoExample example = new DivPayTransPoExample();
            DivPayTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andPayTransIdEqualTo(id);
            PageHelper.startPage(1, 1, false);
            List<DivPayTransPo> poList = this.divPayTransPoMapper.selectByExample(example);
            if (poList.size() > 0) {
                ret = this.build(poList.get(0));
            }
        }
        return Optional.ofNullable(ret);
    }


    /**
     * modified by ych
     * task 2023-dgn1-004
     */
    @Override
    protected Integer save(Transaction trans) {
        DivPayTrans obj;
        if (trans instanceof DivPayTrans) {//应该判断是不是DivPayTrans
            obj = (DivPayTrans) trans;
        }else{
            throw new IllegalArgumentException("PayTransDao.save: trans should be Paytrans.");
        }
        DivPayTransPo po = CloneFactory.copy(new DivPayTransPo(), obj);
        return this.divPayTransPoMapper.updateByPrimaryKeySelective(po);
    }

    /**
     * modified by ych
     * task 2023-dgn1-004
     */
    @Override
    protected Transaction insert(Transaction trans) {
        DivPayTrans obj;
        if (trans instanceof DivPayTrans) {//应该判断是不是DivPayTrans
            obj = (DivPayTrans) trans;
        }else{
            throw new IllegalArgumentException("PayTransDao.save: trans should be Paytrans.");
        }
        DivPayTransPo po = CloneFactory.copy(new DivPayTransPo(), obj);
        this.divPayTransPoMapper.insertSelective(po);
        obj.setId(po.getId());
        return obj;
    }

    /**
     * 由po对象构造bo对象
     *
     * @param po
     * @return
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 22:59
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    private DivPayTrans build(DivPayTransPo po) {
        DivPayTrans ret = CloneFactory.copy(new DivPayTrans(), po);
        ret.setPayTransDao(this.payTransDao);
        ret.setDivRefundTransDao(this.divRefundTransDao);
        ret.setAccountDao(this.accountDao);
        ret.setDivPayTransDao(this);
        ret.setPayAdaptor(this.factory);
        return ret;
    }

}
