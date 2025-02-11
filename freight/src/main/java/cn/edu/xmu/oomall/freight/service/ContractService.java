package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ContractDao;
import cn.edu.xmu.oomall.freight.dao.WarehouseDao;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class ContractService {
    private final ContractDao contractDao;
    private final WarehouseDao warehouseDao;

    /**
     * 新增商铺的物流合作
     *
     */
    public Contract addContract(Contract contract, UserDto user){
        contract.setInvalid(Contract.VALID);
        Contract bo=contractDao.insert(contract,user);
        bo=contractDao.findById(bo.getShopId(),bo.getId());
        return bo;
    }

    /**
     * 暂停或者恢复商铺的物流合作
     *
     */
    public void resumeAndSuspendContract(Long shopId,Long id,UserDto user,Byte invalid){
        Contract bo=contractDao.findById(shopId,id);
        if(bo.getInvalid().equals(invalid))
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "物流合同", id,invalid));
        bo.setInvalid(invalid);
        contractDao.save(bo,user);
    }

    /**
     * 修改商铺的物流合作
     *
     */
    public void modifyContract(Long shopId,Long id,UserDto user,Contract contract){
        Contract bo=contractDao.findById(shopId,id);
        warehouseDao.findById(shopId,bo.getWarehouseId());
        contractDao.save(contract, user);
    }

    /**
     * 取消商铺的物流合作
     *
     */
    public void deleteContract(Long shopId,Long id,UserDto user){
        // find原因：检查shopId与shopLogisticsId是否匹配
        contractDao.findById(shopId,id);
        contractDao.deleteContract(shopId,id);
    }

    /**
     * 2024-dsg-115
     * 获得仓库物流合同
     * @author liboyang
     *
     * @param shopId
     * @param warehouseId
     * @param page
     * @param pageSize
     * @return
     */
    public List<Contract> getContractsByWareHouseId(Long shopId, Long warehouseId, Integer page, Integer pageSize) {
        return contractDao.retrieveByWareHouseId(shopId,warehouseId,page, pageSize);
    }

}
