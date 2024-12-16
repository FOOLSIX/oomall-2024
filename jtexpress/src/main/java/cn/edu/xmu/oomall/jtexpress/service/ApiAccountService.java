package cn.edu.xmu.oomall.jtexpress.service;

import cn.edu.xmu.oomall.jtexpress.dao.ApiAccountDao;
import cn.edu.xmu.oomall.jtexpress.dao.bo.ApiAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 徐森彬
 * 2023-dgn3-02
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ApiAccountService {
    private final Logger logger = LoggerFactory.getLogger(ApiAccountService.class);

    private final ApiAccountDao apiAccountDao;

    @Autowired
    public ApiAccountService(ApiAccountDao apiAccountDao) {
        this.apiAccountDao = apiAccountDao;
    }

    public ApiAccount getApiAccountPrivateKey(Long account) {
        ApiAccount apiAccount = apiAccountDao.findByAccount(account);
        return apiAccount;
    }
}
