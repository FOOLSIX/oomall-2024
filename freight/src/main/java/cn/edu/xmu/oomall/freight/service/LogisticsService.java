package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.LogisticsDao;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class LogisticsService {

    private final LogisticsDao logisticsDao;


    /**
     * 创建物流
     */
    public Logistics createLogistics(Logistics logistics,UserDto user) {
        logisticsDao.insert(logistics,user);
        return logistics;
    }

    /**
     * 根据物流单号查询属于哪家物流公司
     */
    public Optional<Logistics> getCompanyByBillCode(String billCode) {
        List<Logistics> list=logisticsDao.findAll();//获取所有有效的物流
        Logistics logistics =null;
        for(Logistics logis:list){
            if(billCode.matches(logis.getSnPattern()))//根据物流格式匹配当前物流单，匹配成功则返回
                logistics =logis;
        }
        return Optional.ofNullable(logistics);
    }

    /**
     * 返回所有平台所有支持的物流公司
     */
    public List<String> getAllCompany() {
        return this.logisticsDao.findAll().stream()
                .map(Logistics::getName)
                .collect(java.util.stream.Collectors.toList());
    }
}
