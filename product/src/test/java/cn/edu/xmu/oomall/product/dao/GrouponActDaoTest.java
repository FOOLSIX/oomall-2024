package cn.edu.xmu.oomall.product.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.activity.GrouponActDao;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author WuTong
 * @task 2023-dgn2-008
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class GrouponActDaoTest {
    private final Logger logger = LoggerFactory.getLogger(GrouponActDaoTest.class);

    @Autowired
    private GrouponActDao grouponActDao;

    @Test
    public void testGetActivityWithWrongObjectId(){
        ActivityPo activityPo = new ActivityPo();
        activityPo.setId(1L);
        activityPo.setActClass("grouponActDao");
        activityPo.setObjectId("123123efsdf12123");

        try {
            grouponActDao.getActivity(activityPo);
        } catch(BusinessException e) {
            logger.info("testGetActivityWithWrongObjectId: {}", e.getMessage());
            assertEquals(ReturnNo.INCONSISTENT_DATA.getErrNo(), e.getErrno().getErrNo());
        }
    }

}
