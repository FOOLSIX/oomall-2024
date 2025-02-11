package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
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
import java.util.List;
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
        // contractId == 0，根据优先级选择合适的物流合同
        // if (express.getContractId() == 0) {
        //
        // }
        try {
            Contract contract = this.contractDao.findById(shopId, express.getContractId());
            log.debug("shopLogistics: shopLogistics = {}", contract);
            return contract.createExpress(shopId, express, user);
        } catch (BusinessException e) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, "创建运单失败");
        }
    }

    public Express findExpressById(Long shopId, Long id) {
        Express express = this.expressDao.findById(shopId, id);

        if (Objects.nonNull(express)) {
            // 验证运单是否属于该商铺
            if (!Objects.equals(express.getShopId(), shopId)) {
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE,
                        String.format("运单不属于商铺 %d", shopId));
            }

            Byte status = express.getStatus();
            if (Objects.equals(status, Express.UNSHIPPED) || Objects.equals(status, Express.SHIPPED)
                    || Objects.equals(status, Express.REJECTED)) {
                express.getNewStatus();
            }
        }
        return express;
    }

    public Express retrieveExpressByBillCode(Long shopId, String billCode) {
        Express express = this.expressDao.retrieveByBillCode(shopId, billCode);

        if (Objects.nonNull(express)) {
            Byte status = express.getStatus();

            if (Objects.equals(status, Express.UNSHIPPED) || Objects.equals(status, Express.SHIPPED)
                    || Objects.equals(status, Express.REJECTED)) {
                express.getNewStatus();
            }
        }
        return express;
    }

    public void sendExpress(Long shopId, Long id, UserDto user, LocalDateTime startTime, LocalDateTime endTime) {
        Express express = this.expressDao.findById(shopId, id);

        express.getNewStatus();

        // 当前状态不允许揽收
        if (!express.allowStatus(Express.SHIPPED)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW,
                    String.format(ReturnNo.STATENOTALLOW.getMessage() ,"物流单", id, express.getStatusName()));
        }

        express.send(user,startTime,endTime);
    }

    public void cancelExpress(Long shopId, Long id, UserDto user) {
        Express express = this.expressDao.findById(shopId, id);

        express.getNewStatus();

        // 当前状态不允许取消
        if (!express.allowStatus(Express.CANCELED)) {
            throw new BusinessException(ReturnNo.STATENOTALLOW,
                    String.format(ReturnNo.STATENOTALLOW.getMessage() ,"物流单", id, express.getStatusName()));
        }

        express.cancel(user);
    }

    public void confirmExpress(Long shopId, Long id, Byte status, UserDto user) {
        Express express = this.expressDao.findById(shopId, id);
        express.confirm(status, user);
    }
}
