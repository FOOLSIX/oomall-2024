//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.dao.bo.DivRefundTrans;
import cn.edu.xmu.oomall.payment.dao.bo.Transaction;
import cn.edu.xmu.oomall.payment.mapper.generator.DivRefundTransPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.DivRefundTransPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.DivRefundTransPoExample;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@Repository
@Slf4j
public class DivRefundTransDao extends TransactionDao {

    private final DivRefundTransPoMapper divRefundTransPoMapper;

    private final RefundTransDao refundTransDao;

    private final AccountDao accountDao;

    private final DivPayTransDao divPayTransDao;

    @Autowired
    @Lazy
    public DivRefundTransDao(DivRefundTransPoMapper divRefundTransPoMapper, RefundTransDao refundTransDao, AccountDao accountDao, DivPayTransDao divPayTransDao) {
        this.divRefundTransPoMapper = divRefundTransPoMapper;
        this.refundTransDao = refundTransDao;
        this.accountDao = accountDao;
        this.divPayTransDao = divPayTransDao;
    }

    /**
     * 获得bo对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 23:20
     * @param po
     * @return
     */
    private DivRefundTrans build(DivRefundTransPo po) {
        log.debug("getBo po = {}", po);
        DivRefundTrans ret = CloneFactory.copy(new DivRefundTrans(), po);
        ret.setRefundTransDao(this.refundTransDao);
        ret.setAccountDao(this.accountDao);
        ret.setDivPayTransDao(this.divPayTransDao);
        log.debug("getBo ret = {}", ret);
        return ret;
    }


    /**
     * 由id返回分账退回对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 23:13
     * @param id 分账退回对象id
     * @return 账退回对象
     * @throws RuntimeException
     * @throws BusinessException 无此对象
     */
    public DivRefundTrans findById(Long shopId, Long id) throws RuntimeException{
        DivRefundTrans ret = null;
        if (Objects.nonNull(id)) {
            DivRefundTransPo po = divRefundTransPoMapper.selectByPrimaryKey(id);
            if (null == po) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "分账回退", id));
            }
            if (!PLATFORM.equals(shopId) && !shopId.equals(po.getShopId())){
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "分账回退", id, shopId));
            }
            ret = this.build(po);
        }
        return ret;
    }

    /**
     * 根据退款交易id查找分账退回对象
     * @author Ming Qiu
     * <p>
     * date: 2022-11-06 23:22
     * @param id 退款交易id
     * @return 分账退回对象，无则返回null
     * @throws RuntimeException
     */
    public DivRefundTrans findByRefundTransId(Long id) throws RuntimeException{
        DivRefundTrans ret = null;
        if (Objects.nonNull(id)) {
            DivRefundTransPoExample example = new DivRefundTransPoExample();
            DivRefundTransPoExample.Criteria criteria= example.createCriteria();
            criteria.andRefundTransIdEqualTo(id);
            PageHelper.startPage(1,1,false);
            List<DivRefundTransPo> poList = divRefundTransPoMapper.selectByExample(example);
            if (poList.size() > 0){
                ret = this.build(poList.get(0));
            }
        }
        return ret;
    }

    /**
     * 根据分账的id查询分账退回对象
     * @author Ming Qiu
     * date: 2022-11-15 15:17
     * @param divPayTransId
     * @return
     * @throws RuntimeException
     * modified By Rui Li
     * task 2023-dgn1-005
     */
    public List<DivRefundTrans> retrieveByDivPayTransId(Long divPayTransId) throws RuntimeException{
        List<DivRefundTrans> ret = null;
        if ( Objects.nonNull(divPayTransId)) {
            DivRefundTransPoExample example = new DivRefundTransPoExample();
            DivRefundTransPoExample.Criteria criteria = example.createCriteria();
            criteria.andDivPayTransIdEqualTo(divPayTransId);
            PageHelper.startPage(1,MAX_RETURN,false);
            List<DivRefundTransPo> poList = divRefundTransPoMapper.selectByExample(example);
            if (poList.size() > 0 ){
                ret = poList.stream().map(po -> build(po)).collect(Collectors.toList());
            }else {
                ret = new ArrayList<>(0);
            }
        }
        return ret;
    }

    @Override
    protected Integer save(Transaction trans) {
        DivRefundTrans obj;
        if (trans instanceof DivRefundTrans) {
            obj = (DivRefundTrans) trans;
        }else{
            throw new IllegalArgumentException("save: trans should be DivRefundTrans.");
        }
        DivRefundTransPo po = CloneFactory.copy(new DivRefundTransPo(), obj);
        return this.divRefundTransPoMapper.updateByPrimaryKeySelective(po);
    }

    @Override
    protected Transaction insert(Transaction trans) {
        DivRefundTrans obj;
        if (trans instanceof DivRefundTrans) {
            obj = (DivRefundTrans) trans;
        }else{
            throw new IllegalArgumentException("insert: trans should be DivRefundTrans.");
        }
        DivRefundTransPo po = CloneFactory.copy(new DivRefundTransPo(), obj);
        this.divRefundTransPoMapper.insertSelective(po);
        obj.setId(po.getId());
        return obj;
    }
}
