package cn.edu.xmu.oomall.freight.dao;
import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.freight.dao.bo.Warehouse;
import cn.edu.xmu.oomall.freight.mapper.jpa.WarehousePoMapper;
import cn.edu.xmu.oomall.freight.mapper.po.WarehousePo;
import org.springframework.stereotype.Repository;
import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

import java.util.Optional;
/**
 * @author fan ninghan
 * 2023-dng3-008
 */
@Repository
public class WarehouseDao {

    private WarehousePoMapper warehousePoMapper;

    public WarehouseDao(WarehousePoMapper warehousePoMapper) {
        this.warehousePoMapper = warehousePoMapper;
    }

    public Warehouse findById(Long shopId, Long id) throws RuntimeException {
        Optional<WarehousePo> ret = this.warehousePoMapper.findById(id);
        if (ret.isPresent()) {
            WarehousePo po = ret.get();
            if(PLATFORM.equals(shopId) || po.getShopId().equals(shopId))
                return CloneFactory.copy(new Warehouse(), po);
            else throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE);
        } else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }
    }
}
