//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.payment.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.payment.dao.bo.Ledger;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.mapper.generator.LedgerPoMapper;
import cn.edu.xmu.oomall.payment.mapper.generator.po.LedgerPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.LedgerPoExample;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 台账dao对象
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class LedgerDao {

    private final LedgerPoMapper ledgerPoMapper;
    private final  AccountDao accountDao;

    private Ledger build(LedgerPo po) {
        Ledger bo = CloneFactory.copy(new Ledger(), po);
        bo.setAccountDao(this.accountDao);
        return bo;
    }

    /**
     * 查询台账
     * @param example 查询条件
     * @param page 页
     * @param pageSize 分页
     * @return
     */
    private List<Ledger> retrieveLedgers(LedgerPoExample example, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize, false);
        List<LedgerPo> poList = ledgerPoMapper.selectByExample(example);
        return poList.stream().map(po -> build(po)).collect(Collectors.toList());
    }


    /**
     * 更新错账
     * @param obj
     * @param user
     * @throws RuntimeException
     */
    public void save(Ledger obj, UserDto user) throws RuntimeException{
        obj.setModifier(user);
        obj.setGmtModified(LocalDateTime.now());
        LedgerPo po = CloneFactory.copy(new LedgerPo(), obj);
        int retL = ledgerPoMapper.updateByPrimaryKeySelective(po);
        if (retL==0){
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "台账",po.getId()));
        }
    }

    /**
     * 新增错账
     * @param ledger
     * @param user
     * @return
     * @throws RuntimeException
     */
    public Ledger insert(Ledger ledger, UserDto user) throws RuntimeException{
        log.debug("insertObj: ledger = {}", ledger);
        ledger.setCreator(user);
        ledger.setGmtCreate(LocalDateTime.now());
        ledger.setId(null);
        LedgerPo po = CloneFactory.copy(new LedgerPo(), ledger);
        this.ledgerPoMapper.insert(po);
        ledger.setId(po.getId());
        return ledger;
    }

    /**
     * 按照id找到Ledger对象
     * @param id ledger对象id
     * @return
     */
    public Ledger findById(Long id){

        LedgerPo po = ledgerPoMapper.selectByPrimaryKey(id);
        if (Objects.isNull(po)) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "台账", id));
        }
        return this.build(po);
    }
}
