package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.oomall.freight.dao.ExpressDao;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class LogisticsService {

    private final ExpressDao expressDao;

    // 此处面向功能
    public Logistics findByBillCode(String billCode){
        Express express = this.expressDao.findByBillCode(PLATFORM, billCode);
        return express.getContract().getLogistics();
    }

}
