package cn.edu.xmu.oomall.product.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.activity.ActivityDao;
import cn.edu.xmu.oomall.product.dao.bo.Activity;
import cn.edu.xmu.oomall.product.dao.onsale.OnSaleDao;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Liang Nan
 */
@SpringBootTest(classes = ProductTestApplication.class)
//@Transactional
public class ActivityDaoTest {
    @Autowired
    ActivityDao activityDao;

    @Autowired
    OnSaleDao onsaleDao;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 活动查询测试
     * 2023-12-09
     * @author yuhao shi
     */
    @Test
    public void findByIdTestGivenA5(){
        Mockito.when(redisUtil.hasKey("A5")).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Activity activity = activityDao.findById(5L,2L,"couponActDao");
        assertThat(activity.getShopId()).isEqualTo(2L);
        assertThat(activity.getName()).isEqualTo("满3件最便宜的5折");
    }

    @Test
    public void findByIdTestGivenNoShop(){
        Mockito.when(redisUtil.hasKey("A5")).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () ->  activityDao.findById(5L,3L,"couponActDao"));
    }

    @Test
    public void findByIdTestGivenWrongAct(){
        Mockito.when(redisUtil.hasKey("A5")).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () ->  activityDao.findById(5L,2L,"grouponActDao"));
    }

    @Test
    public void findByIdTestGivenLimitMax(){
        Mockito.when(redisUtil.hasKey("A1000000")).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () ->  activityDao.findById(1000000L,2L,"couponActDao"));
    }



    /*@Test
    public void retrieveByShopIdAndProductIdTest1(){
        List<Activity> activityList = this.activityDao.retrieveByShopIdAndProductId(4L,1552L, "advanceSaleActDao", 1, 10);
        assertEquals(2, activityList.size());
        assertThat(activityList).filteredOn(o -> o.getId() == 1).extracting(Activity::getName).containsOnly("预售活动1");
        assertThat(activityList).filteredOn(o -> o.getId() == 1).extracting(o->o.getOnsaleList().get(0).getBeginTime()).containsOnly(LocalDateTime.parse("2021-11-11T14:38:20"));
        assertThat(activityList).filteredOn(o -> o.getId() == 1).extracting(o->o.getOnsaleList().get(0).getEndTime()).containsOnly(LocalDateTime.parse("2027-02-19T14:38:20"));
        assertThat(activityList).filteredOn(o -> o.getId() == 1).extracting(o->o.getOnsaleList().get(0).getQuantity()).containsOnly(26);
        assertThat(activityList).filteredOn(o -> o.getId() == 1).extracting(o->((AdvanceSaleAct) o).getPayTime()).containsOnly(LocalDateTime.parse("2023-12-09T00:00:00"));
        assertThat(activityList).filteredOn(o -> o.getId() == 1).extracting(o->((AdvanceSaleAct) o).getDeposit()).containsOnly(10000L);
        assertThat(activityList).filteredOn(o -> o.getId() == 15).extracting(Activity::getName).containsOnly("天天向上");
        assertThat(activityList).filteredOn(o -> o.getId() == 15).extracting(o->o.getOnsaleList().get(0).getBeginTime()).containsOnly(LocalDateTime.parse("2027-10-21T19:34:50"));
        assertThat(activityList).filteredOn(o -> o.getId() == 15).extracting(o->o.getOnsaleList().get(0).getEndTime()).containsOnly(LocalDateTime.parse("2028-11-21T19:34:50"));
        assertThat(activityList).filteredOn(o -> o.getId() == 15).extracting(o->o.getOnsaleList().get(0).getQuantity()).containsOnly(1000);
        assertThat(activityList).filteredOn(o -> o.getId() == 15).extracting(o->((AdvanceSaleAct) o).getPayTime()).containsOnly(LocalDateTime.parse("2027-12-21T19:34:50.630"));
        assertThat(activityList).filteredOn(o -> o.getId() == 15).extracting(o->((AdvanceSaleAct) o).getDeposit()).containsOnly(1000L);
    }

    @Test
    public void delActivityOnsaleByActIdTest1() {
        activityDao.delActivityOnsaleByActId(12L);


    }

    @Test
    public void delActivityOnsaleByActIdTest2() {
        assertThrows(BusinessException.class, () -> activityDao.delActivityOnsaleByActId(18L));


    }

    //预售与优惠和团购活动不能并存，出215错误
    @Test
    public void addActivityOnsaleTest2() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        Onsale onsale = this.onsaleDao.findById(29L);
        assertThrows(BusinessException.class, () -> activityDao.addActivityOnsale(11L, onsale, user)).getErrno().equals(ReturnNo.ADVSALE_NOTCOEXIST);
    }

    //团购与优惠和预售活动不并存 出216错误
    @Test
    public void addActivityOnsaleTest3() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        Onsale onsale = this.onsaleDao.findById(3L);
        assertThrows(BusinessException.class, () -> activityDao.addActivityOnsale(11L, onsale, user)).getErrno().equals(ReturnNo.GROUPON_NOTCOEXIST);
    }
*/
/*
    @Test
    public void testInsertGivenGrouponAct() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("admin");

        List<Threshold> thresholds = new ArrayList<>();
        Threshold threshold  = new Threshold();
        threshold.setPercentage(10L);
        threshold.setQuantity(10);
        thresholds.add(threshold);

        threshold  = new Threshold();
        threshold.setPercentage(20L);
        threshold.setQuantity(20);
        thresholds.add(threshold);

        threshold  = new Threshold();
        threshold.setPercentage(30L);
        threshold.setQuantity(30);
        thresholds.add(threshold);

        GrouponAct act = new GrouponAct();
        act.setShopId(6L);
        act.setName("团购活动1");
        act.setActClass("grouponActDao");
        act.setThresholds(thresholds);
        Activity newAct = activityDao.insert(act, user);
        assertNotNull(newAct.getId());
    }

    //@Test
    public void testInsertGivenCouponAct(){
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        UserDto user = new UserDto();
        user.setId(1L);
        user.setName("admin");

        AmountCouponLimitation limitation1 = new AmountCouponLimitation(3L);
        CrossCategoryLimitation limitation2 = new CrossCategoryLimitation(2L);
        PriceCouponLimitation limitation3 = new PriceCouponLimitation(10000L);
        ComplexCouponLimitation limitation = new ComplexCouponLimitation(limitation1, limitation3);
        //CheapestPercentageDiscount discount = new CheapestPercentageDiscount(50L, limitation);
        PercentageCouponDiscount discount = new PercentageCouponDiscount(90L, limitation);
        //PriceCouponDiscount discount = new PriceCouponDiscount(1000L, limitation);
        CouponAct act = new CouponAct();
        act.setShopId(2L);
        act.setName("优惠活动10");
        act.setActClass("couponActDao");
        act.setStrategy(discount);
        Activity newAct = activityDao.insert(act, user);
    }*/
}