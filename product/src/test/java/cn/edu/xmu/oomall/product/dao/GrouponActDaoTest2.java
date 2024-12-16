//package cn.edu.xmu.oomall.product.dao;
//
//import cn.edu.xmu.javaee.core.model.dto.UserDto;
//import cn.edu.xmu.oomall.product.ProductTestApplication;
//import cn.edu.xmu.oomall.product.dao.activity.ActivityDao;
//import cn.edu.xmu.oomall.product.dao.activity.GrouponActDao;
//import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
//import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
//import cn.edu.xmu.oomall.product.model.Threshold;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static cn.edu.xmu.javaee.core.util.Common.cloneObj;
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * @author prophesier
// * @create 2022-12-07 1:59
// */
//@SpringBootTest(classes = ProductTestApplication.class)
//@Transactional
//public class GrouponActDaoTest2 {
//    @Autowired
//    private GrouponActDao grouponActDao;
//
//    @Autowired
//    private ActivityDao activityDao;
//
//    @Test
//    public void insertTest() {
//        List<Threshold> thresholds = new ArrayList<>();
//        thresholds.add(new Threshold(52, (long) 12));
//        GrouponAct grouponAct = new GrouponAct(thresholds);
//        grouponAct.setName("groupon1");
//        grouponAct.setShopId(12L);
//        String objectId = grouponActDao.insert(grouponAct);
//        assertThat(objectId).isNotNull();
//    }
//
//    @Test
//    public void saveTest(){
//        List<Threshold> thresholds = new ArrayList<>();
//        thresholds.add(new Threshold(52, (long) 12));
//        GrouponAct grouponAct = new GrouponAct(thresholds);
//        grouponAct.setObjectId("56064886ade2f21f36b03134");
//        grouponAct.setName("groupon1");
//        grouponAct.setShopId(12L);
//        grouponActDao.save(grouponAct);
//    }
//
//    @Test
//    public void insertActivityOnsaleTest(){
//        UserDto user = new UserDto();
//        user.setId(2L);
//        user.setName("test1");
//        user.setUserLevel(1);
//        activityDao.insertActivityOnsale(1L,10L, user);
//    }
//
//}
