package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.ContractDao;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 2023-dgn3-009
 *
 * @author huangzian
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
@RequiredArgsConstructor
public class ExpressService {
    private final ContractDao contractDao;
    private final ExpressDao expressDao;

    public Express createExpress(Long shopId, Express express, UserDto user) {
        Contract contract = this.contractDao.findById(shopId, express.getContractId());
        log.debug("shopLogistics: shopLogistics = {}", contract);
        return contract.createExpress(shopId, express, user);
    }

    public Express findExpressById(Long shopId, Long id) {
        Express express = this.expressDao.findById(shopId, id);
        express.getNewStatus();
        return express;
    }

    public Express findExpressByBillCode(Long shopId, String billCode) {
        Express express = this.expressDao.findByBillCode(shopId, billCode);
        if (Objects.nonNull(express)) {
            express.getNewStatus();
        }
        return express;
    }

    public void sendExpress(Long shopId, Long id, UserDto user, LocalDateTime startTime, LocalDateTime endTime) {
        Express express = this.expressDao.findById(shopId, id);
        express.send(user,startTime,endTime);
    }

    public void cancelExpress(Long shopId, Long id, UserDto user) {
        Express express = this.expressDao.findById(shopId, id);
        express.cancel(user);
    }

    public void confirmExpress(Long shopId, Long id, Byte status, UserDto user) {
        Express express = this.expressDao.findById(shopId, id);
        express.confirm(status, user);
    }
}
