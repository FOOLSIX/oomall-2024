package cn.edu.xmu.oomall.jtexpress.dao;

import cn.edu.xmu.oomall.jtexpress.dao.bo.ApiAccount;
import cn.edu.xmu.oomall.jtexpress.mapper.jpa.ApiAccountPoMapper;
import cn.edu.xmu.oomall.jtexpress.mapper.po.ApiAccountPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RefreshScope
public class ApiAccountDao {

    private final static Logger logger = LoggerFactory.getLogger(ApiAccountDao.class);
    private final ApiAccountPoMapper apiAccountPoMapper;

    @Autowired
    public ApiAccountDao(ApiAccountPoMapper apiAccountPoMapper) {
        this.apiAccountPoMapper = apiAccountPoMapper;
    }




    public ApiAccount findByAccount(Long account) {
        logger.debug("findByAccount:{}",account);
        Optional<ApiAccountPo> apiAccountPo=apiAccountPoMapper.findByAccount(account);
        return null;
    }
}
