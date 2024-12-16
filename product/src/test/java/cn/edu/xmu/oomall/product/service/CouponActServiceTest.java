package cn.edu.xmu.oomall.product.service;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.controller.dto.OrderInfoDto;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.model.strategy.Item;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Liang Nan
 */
@SpringBootTest(classes = ProductTestApplication.class)
@Transactional
public class CouponActServiceTest {
    @Autowired
    private CouponActService couponActService;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    @Autowired
    private ShopMapper shopMapper;

    /**
     * 计算商品优惠价格测试
     * 2023-12-09
     * @author yuhao shi
     */
    @Test
    public void cacuCoupoTestGiven5()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(5L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(6015L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(472L);
    }


    @Test
    public void cacuCoupoTestGiven6()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(6L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(5432L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(426L);
    }

    @Test
    public void cacuCoupoTestGiven7()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(7L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(5949L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(467L);
    }

    @Test
    public void cacuCoupoTestGivenCross9()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(9L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(5949L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(467L);
    }


    @Test
    public void cacuCoupoTestGivenCross10()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(10L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(5432L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(426L);
    }

    @Test
    public void cacuCoupoTestGivenCross11()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(11L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(6015L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(472L);
    }

    @Test
    public void cacuCoupoTestGivenUnion97()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(97L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(6015L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(472L);
    }

    @Test
    public void cacuCoupoTestGivenUnion98()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(98L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(5432L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(426L);
    }

    @Test
    public void cacuCoupoTestGivenUnion99()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(10);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(99L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(5949L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(467L);
    }


    @Test
    public void cacuCoupoTestGivenAmount()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(33L);
        orderInfoDto_1.setQuantity(50);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(50);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(99L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(6017L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(472L);
    }


    @Test
    public void cacuCoupoTestGivenCategory()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(1L);
        orderInfoDto_1.setQuantity(1);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(1);

        OrderInfoDto orderInfoDto_3 =new OrderInfoDto();
        orderInfoDto_3.setOnsaleId(3L);
        orderInfoDto_3.setQuantity(1);

        OrderInfoDto orderInfoDto_4 =new OrderInfoDto();
        orderInfoDto_4.setOnsaleId(4L);
        orderInfoDto_4.setQuantity(1);

        OrderInfoDto orderInfoDto_5 =new OrderInfoDto();
        orderInfoDto_5.setOnsaleId(5L);
        orderInfoDto_5.setQuantity(1);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);
        orderInfoDtoList.add(orderInfoDto_3);
        orderInfoDtoList.add(orderInfoDto_4);
        orderInfoDtoList.add(orderInfoDto_5);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(99L, orderInfoDtoList);

        assertThat(itemList.get(0).getDiscount()).isEqualTo(52542L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(467L);
        assertThat(itemList.get(2).getDiscount()).isEqualTo(12472L);
        assertThat(itemList.get(3).getDiscount()).isEqualTo(1013L);
        assertThat(itemList.get(4).getDiscount()).isEqualTo(3234L);
    }


    @Test
    public void cacuCoupoTestGivenComplexNo()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(1L);
        orderInfoDto_1.setQuantity(1);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(1);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);


        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(101L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(null);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(null);

    }

    @Test
    public void cacuCoupoTestGivenComplexOk()
    {
        OrderInfoDto orderInfoDto_1 =new OrderInfoDto();
        orderInfoDto_1.setOnsaleId(1L);
        orderInfoDto_1.setQuantity(20);

        OrderInfoDto orderInfoDto_2 =new OrderInfoDto();
        orderInfoDto_2.setOnsaleId(2L);
        orderInfoDto_2.setQuantity(20);

        List<OrderInfoDto> orderInfoDtoList =new ArrayList<>();
        orderInfoDtoList.add(orderInfoDto_1);
        orderInfoDtoList.add(orderInfoDto_2);


        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        List<Item> itemList=couponActService.cacuCoupon(101L, orderInfoDtoList);
        assertThat(itemList.get(0).getDiscount()).isEqualTo(47966L);
        assertThat(itemList.get(1).getDiscount()).isEqualTo(426L);
    }


    /*    @Test
    public void addCouponactivityTest() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        CouponAct bo = new CouponAct();
        bo.setName("优惠活动3");
        bo.setQuantity(0);
        bo.setQuantityType(1);
        bo.setCouponTime(LocalDateTime.now());
        bo.setValidTerm(0);
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        SimpleCouponActDto dto = couponActService.addCouponactivity(1L, bo, user);
        assertThat(dto.getName()).isEqualTo("优惠活动3");
    }

    @Test
    public void retrieveByShopIdAndProductIdAndOnsaleIdTest() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        PageDto<SimpleCouponActDto> pageDto = couponActService.retrieveByShopIdAndProductId(11L, 1559L, 10L, 1, 10);
        assertThat(pageDto.getList().size()).isEqualTo(1);

    }


    @Test
    public void findCouponActivityByIdTest1() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Shop shop = Shop.builder().id(10L).name("kp小屋").type((byte) 1).build();
        InternalReturnObject<Shop> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(0);
        internalReturnObject.setErrmsg("成功");
        internalReturnObject.setData(shop);
        Mockito.when(shopDao.getShopById(Mockito.anyLong())).thenReturn(internalReturnObject);
        CouponActivityDto dto = couponActService.findCouponActivityById(10L, 12L);
        assertThat(dto.getName()).isEqualTo("优惠活动2");
    }

    @Test
    public void findCouponActivityByIdTest2() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () -> couponActService.findCouponActivityById(10L, 13L));
    }

    @Test
    public void findCouponActivityByIdTest3() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () -> couponActService.findCouponActivityById(11L, 12L));
    }

    @Test
    public void updateCouponActivityByIdTest1() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        CouponAct bo = new CouponAct();
        bo.setName("优惠活动3");
        bo.setQuantity(0);
        bo.setQuantityType(1);
        bo.setCouponTime(LocalDateTime.now());
        bo.setValidTerm(0);
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        couponActService.updateCouponActivityById(10L, 11L, bo, user);
    }

    @Test
    public void updateCouponActivityByIdTest2() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        CouponAct bo = new CouponAct();
        bo.setName("优惠活动3");
        bo.setQuantity(0);
        bo.setQuantityType(1);
        bo.setCouponTime(LocalDateTime.now());
        bo.setValidTerm(0);
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        assertThrows(BusinessException.class, () -> couponActService.updateCouponActivityById(10L, 13L, bo, user));
    }

    @Test
    public void updateCouponActivityByIdTest3() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        CouponAct bo = new CouponAct();
        bo.setName("优惠活动3");
        bo.setQuantity(0);
        bo.setQuantityType(1);
        bo.setCouponTime(LocalDateTime.now());
        bo.setValidTerm(0);
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        assertThrows(BusinessException.class, () -> couponActService.updateCouponActivityById(11L, 11L, bo, user));
    }

    @Test
    public void deleteCouponActivityByIdTest1() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        couponActService.deleteCouponActivityById(10L, 11L);
    }

    @Test
    public void deleteCouponActivityByIdTest2() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () -> couponActService.deleteCouponActivityById(11L, 11L)).getErrno().equals(ReturnNo.RESOURCE_ID_NOTEXIST);
    }

    @Test
    public void deleteCouponActivityByIdTest3() {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        assertThrows(BusinessException.class, () -> couponActService.deleteCouponActivityById(10L, 13L)).getErrno().equals(ReturnNo.RESOURCE_ID_NOTEXIST);
    }
    */

}