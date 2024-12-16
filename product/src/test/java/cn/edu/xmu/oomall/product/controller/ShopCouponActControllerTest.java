package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductApplication;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ShopCouponActControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    @Autowired
    private ShopMapper shopMapper;


    private static String adminToken;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
    }

    @Test
    public void addCouponactivityTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
                String json = "{\"name\": \"优惠测试1\", \"quantity\": 1000, \"quantityType\": 1, \"validTerm\": 0, \"couponTime\": \"2022-12-10T20:38:20\", " +
                "\"strategy\": " +
                "{\"value\": 90," +
                "\"couponLimitation\": {" +
                "\"value\": 0," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.ComplexCouponLimitation\"," +
                "\"couponLimitationList\": [{" +
                "\"value\": 3," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.AmountCouponLimitation\"" +
                "}, {" +
                "\"value\": 10000," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.PriceCouponLimitation\"" +
                "}]" +
                "}," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.PercentageCouponDiscount\"" +
                "} }";
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/couponactivities", 2L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("优惠测试1")))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    public void addCouponactivityTest2() throws Exception {
//        // 确认validTerm正常工作，不完整属性
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        String json = "{\"quantityType\": 1, \"validTerm\": 0, \"couponTime\": \"2022-12-10T20:38:20\", " +
//                "\"strategy\": " +
//                "{\"value\": 90," +
//                "\"couponLimitation\": {" +
//                "\"value\": 0," +
//                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.ComplexCouponLimitation\"," +
//                "\"couponLimitationList\": [{" +
//                "\"value\": 3," +
//                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.AmountCouponLimitation\"" +
//                "}, {" +
//                "\"value\": 10000," +
//                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.PriceCouponLimitation\"" +
//                "}]" +
//                "}," +
//                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.PercentageCouponDiscount\"" +
//                "} }";
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/couponactivities", 2L)
//                        .header("authorization", adminToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(json))
//                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
//    }

    @Test
    public void getCouponActIdTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Shop shop = new Shop();
        shop.setId(2L);
        shop.setName("helloShop");
        shop.setStatus((byte) 1);

        InternalReturnObject<Shop> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(0);
        internalReturnObject.setErrmsg("成功");
        internalReturnObject.setData(shop);
        Mockito.when(shopMapper.getShopById(Mockito.anyLong())).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/couponactivities", 2L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void publishCouponActTest1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/couponactivities/{id}/publish", 0L, 102L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void publishCouponActTest2() throws Exception{
        // 非管理员审核
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/couponactivities/{id}/publish", 2L, 102L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
        //.andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void publishCouponActTest3() throws Exception{
        // status已经为上线状态
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/couponactivities/{id}/publish", 0L, 101L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
        //.andDo(MockMvcResultHandlers.print());
    }

    // CouponActivityId不合法的情况：
    // publish和put都使用findById查找activity，在putCouponActProductTest中已经进行测试，在publish不重复测试

    @Test
    public void putCouponActProductTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String json = "{\"name\":\"test\", \"quantity\": 100, \"quantityType\": 1, \"validTerm\": 0, " +
                "\"strategy\": {" +
                "\"value\": 1000," +
                "\"couponLimitation\": {" +
                "\"value\": 0," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.ComplexCouponLimitation\"," +
                "\"couponLimitationList\": [{" +
                "\"value\": 2," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.CrossCategoryLimitation\"" +
                "}, {" +
                "\"value\": 10000," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.PriceCouponLimitation\"" +
                "}]" +
                "}," +
                "\"className\": \"cn.edu.xmu.oomall.product.model.strategy.impl.PriceCouponDiscount\"" +
                "}}";

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/couponactivities/{id}", 2L, 101L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    public void putCouponActProductTest2() throws Exception {
//        // 确保选择性validTerm正常工作
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        String json = "{\"name\":\"test2\", \"quantity\": -1}";
//
//        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/couponactivities/{id}", 2L, 101L)
//                        .header("authorization", adminToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(json))
//                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
//        //.andDo(MockMvcResultHandlers.print());
//    }

//    @Test
//    public void putCouponActProductTest3() throws Exception {
//        // 确保选择性validTerm正常工作
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        String json = "{\"name\":\"test2\"}";
//
//        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/couponactivities/{id}", 2L, 101L)
//                        .header("authorization", adminToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(json))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
//        //.andDo(MockMvcResultHandlers.print());
//    }

    @Test
    public void putCouponActProductTest2() throws Exception {
        // 修改目标CouponAct不存在
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String json = "{\"name\":\"test\", \"quantity\": 100, \"quantityType\": 1, \"validTerm\": 0}";

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/couponactivities/{id}", 2L, 110L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
        //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delCouponActTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/couponactivities/{id}", 2L, 101L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    // CouponActivityId不合法的情况：
    // delete和put都使用findById查找activity，在putCouponActProductTest中已经进行测试，在delete不重复测试

    @Test
    public void getCouponActProductTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        Shop shop = new Shop();
        shop.setId(7L);
        shop.setName("helloShop");
        shop.setStatus((byte) 1);

        InternalReturnObject<Shop> internalReturnObject = new InternalReturnObject<>();
        internalReturnObject.setErrno(0);
        internalReturnObject.setErrmsg("成功");
        internalReturnObject.setData(shop);
        Mockito.when(shopMapper.getShopById(Mockito.anyLong())).thenReturn(internalReturnObject);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/couponactivities/{id}", 2L, 101L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
        //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addCouponActToOnsaleTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/couponactivities/{id}/onsales/{sid}", 2L, 101L, 9L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addCouponActToOnsaleTest2() throws Exception {
        // shopId 与 couponActivityId 不匹配
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/couponactivities/{id}/onsales/{sid}", 8L, 101L, 9L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addCouponActToOnsaleTest3() throws Exception {
        // couponActivityId 不存在
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/couponactivities/{id}/onsales/{sid}", 2L, 110L, 9L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delCouponActFromOnsaleTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/couponactivities/{id}/onsales/{sid}", 2L, 101L, 129L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
        //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delCouponActFromOnsaleTest2() throws Exception {
        // couponActivityId 不存在
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/couponactivities/{id}/onsales/{sid}", 2L, 110L, 129L)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delCouponActFromOnsaleTest3() throws Exception {
        // shopId 与 couponActivityId 不匹配
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/couponactivities/{id}/onsales/{sid}", 7L, 101L, 129L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
        //.andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delCouponActFromOnsaleTest4() throws Exception {
        // shopId 与 onsaleId 不匹配
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/couponactivities/{id}/onsales/{sid}", 7L, 101L, 130L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
        //.andDo(MockMvcResultHandlers.print());
    }


//    // 关于 findAllOnlineCouponActivities 的测试说明V1：
//    // 在不添加添加endTime参数时，后端得到endTime为当前时间，故获取到的优惠活动个数为0，如test1，test2
//    // 若想获得非空结果，需要将endTime设置为当前时间之后的时间，如test3
//
//    @Test
//    public void findAllOnlineCouponActivitiesTest1() throws Exception {
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/couponactivities")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
//
//    }
//
//    @Test
//    public void findAllOnlineCouponActivitiesTest2() throws Exception {
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/couponactivities")
//                        .param("shopId", "7")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
//    }
//
//    @Test
//    public void findAllOnlineCouponActivitiesTest3() throws Exception {
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/couponactivities")
//                        .param("endTime", "2027-10-10 11:11:11")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
//    }
//
//    @Test
//    public void findCouponActByIdTest1() throws Exception {
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/couponactivities/{id}", 11L)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
//    }
//
//    @Test
//    public void findCouponActProductByIdTest1() throws Exception {
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/couponactivities/{id}/products", 11L)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
//    }
//
//    @Test
//    public void findCouponActProductById2() throws Exception {
//        // 当couponActivity不存在时
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/couponactivities/{id}/products", 99L)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
//    }
}