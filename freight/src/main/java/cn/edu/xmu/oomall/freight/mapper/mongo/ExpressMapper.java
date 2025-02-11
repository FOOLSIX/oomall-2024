package cn.edu.xmu.oomall.freight.mapper.mongo;

import cn.edu.xmu.oomall.freight.dao.bo.Express;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Wu Yiwei
 * @date 2024/12/18
 * @description Mongo ExpressMapper
 */

@Repository
public interface ExpressMapper extends MongoRepository<Express, Long> {
    Express findByBillCode(String billCode);
}
