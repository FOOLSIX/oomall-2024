package cn.edu.xmu.oomall.freight.service;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.freight.dao.UndeliverableDao;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UndeliverableService {

    private final UndeliverableDao undeliverableDao;

    public void addUndeliverableRegion(Undeliverable undeliverable, UserDto user) {
        undeliverable = undeliverableDao.build(undeliverable);
        undeliverableDao.insert(undeliverable, user);
    }

    public void deleteUndeliverableRegion(Long regionId, Long logisticsId, UserDto user){
        undeliverableDao.delete(regionId, logisticsId, user);
    }

    public List<Undeliverable> retrieveUndeliverableRegion(Long logisticsId, Integer page, Integer pageSize){
        return undeliverableDao.retrieveByLogisticsId(logisticsId, page, pageSize);
    }
}
