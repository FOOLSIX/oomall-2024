package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
// import cn.edu.xmu.oomall.freight.mapper.jpa.ExpressPoMapper;
import cn.edu.xmu.oomall.freight.mapper.mongo.ExpressMapper;
import cn.edu.xmu.oomall.freight.mapper.po.ExpressPo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.model.Constants.IDNOTEXIST;



/**
 * 2023-dgn3-009
 *
 * @author huangzian,fan ninghan
 */

/**
 * @author Wu Yiwei
 * @date 2024/12/9
 * @description express 改为 mongo 存储
 */
@Repository
@Slf4j
public class ExpressDao {
    // private final ExpressPoMapper expressPoMapper;
    private final ExpressMapper expressMapper;
    @Lazy
    private final  ContractDao contractDao;
    private final LogisticsAdaptorFactory factory;

    @Autowired
    @Lazy
    public ExpressDao(ExpressMapper expressMapper, ContractDao contractDao, LogisticsAdaptorFactory factory) {
        this.expressMapper = expressMapper;
        this.contractDao = contractDao;
        this.factory = factory;
    }

    public Express insert(Express bo, UserDto user) {
        bo.setId(null);
        bo.setCreator(user);
        bo.setGmtCreate(LocalDateTime.now());

        log.debug("save: bo = {}", bo);

        bo = expressMapper.save(bo);
        return bo;
    }

    public Express findById(Long shopId, Long id) throws RuntimeException{
        assert id != null : "id cannot be null";

        log.debug("findById: id = {}", id);

        Optional<Express> ret = expressMapper.findById(id);
        if (ret.isPresent()) {
            Express bo = ret.get();
            log.debug("findById: retrieve from database express = {}", bo);

            if (!PLATFORM.equals(shopId) && !shopId.equals(bo.getShopId())) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "物流单", id, shopId));
            }

            build(bo);
            return bo;
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "物流单", id));
        }
    }

    private void build(Express bo) throws RuntimeException{
        bo.setExpressDao(this);
        bo.setContractDao(this.contractDao);
        bo.setLogisticsAdaptor(this.factory);
    }

    public void save(Express bo, UserDto user) {
        bo.setModifier(user);
        bo.setGmtModified(LocalDateTime.now());

        log.debug("save: bo = {}", bo);

        Express updateBo = expressMapper.save(bo);
        if (IDNOTEXIST.equals(updateBo.getId())) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "物流单", bo.getId()));
        }
    }

    public Express retrieveByBillCode(Long shopId, String billCode) throws RuntimeException{
        Express bo = this.expressMapper.findByBillCode(billCode);
        if (bo != null) {
            log.debug("retrieveByBillCode: retrieve from database express = {}", bo);

            if (!PLATFORM.equals(shopId) && !shopId.equals(bo.getShopId())) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "物流单", bo.getId(), shopId));
            }
            build(bo);
            return bo;
        } else{
            return null;
        }
    }
}
