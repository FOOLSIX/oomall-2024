package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ContractDao;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

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

    /**
     * 新增商铺的物流合作
     *
     */
    public Contract addContract(Contract contract, UserDto user){

        return contractDao.insert(contract, user);
    }

    /**
     * 修改商铺的物流合作
     *
     */
    public void modifyContract(Contract bo, UserDto user){
        // find原因：检查shopId与shopLogisticsId是否匹配
        contractDao.findById(bo.getShopId(), bo.getId());
        contractDao.save(bo, user);
    }

}
