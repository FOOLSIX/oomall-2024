package cn.edu.xmu.oomall.freight.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Logistics;
import cn.edu.xmu.oomall.freight.dao.bo.Undeliverable;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptor;
import cn.edu.xmu.oomall.freight.dao.logistics.LogisticsAdaptorFactory;
import cn.edu.xmu.oomall.freight.mapper.jpa.LogisticsPoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.LogisticsPo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
@Slf4j
@RequiredArgsConstructor
public class LogisticsDao {

    private final LogisticsPoMapper logisticsPoMapper;

    /**
     * 查询物流
     *
     */
    public Logistics findById(Long id) throws RuntimeException {
        Optional<LogisticsPo> po = logisticsPoMapper.findById(id);
        if (po.isPresent()) {
            return CloneFactory.copy(new Logistics(), po.get());
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 获得所有物流
     */
    public List<Logistics> findAll() throws RuntimeException {
        List<LogisticsPo> pos = logisticsPoMapper.findAll();
        return pos.stream()
                .map(po -> CloneFactory.copy(new Logistics(), po))
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 新增物流
     */
    public void insert(Logistics logistics, UserDto user) throws RuntimeException {
        logistics.setCreator(user);
        logistics.setGmtCreate(LocalDateTime.now());
        LogisticsPo po = CloneFactory.copy(new LogisticsPo(), logistics);
        log.debug("insert Logistics = " + po);
        this.logisticsPoMapper.save(po);
    }
}
