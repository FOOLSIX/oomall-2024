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

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 新增不可达地区
     */
    public void addUndeliverableRegion(Undeliverable undeliverable, UserDto user) {
        undeliverable = undeliverableDao.build(undeliverable);
        undeliverableDao.insert(undeliverable, user);
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 删除无法配送的地区
     */
    public void deleteUndeliverableRegion(Long regionId, Long logisticsId, UserDto user){
        undeliverableDao.delete(regionId, logisticsId, user);
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 获取无法配送的地区
     */
    public List<Undeliverable> retrieveUndeliverableRegion(Long logisticsId, Integer page, Integer pageSize){
        return undeliverableDao.retrieveByLogisticsId(logisticsId, page, pageSize);
    }
}
