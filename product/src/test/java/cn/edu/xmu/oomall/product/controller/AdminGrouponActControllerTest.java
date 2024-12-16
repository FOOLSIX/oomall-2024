package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.Activity;
import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.model.Threshold;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;


/**
 * @author WuTong
 * @task 2023-dgn2-008
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class AdminGrouponActControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    @SpyBean
    ShopMapper shopMapper;

    private static String adminToken, shopToken1, shopToken2, shopToken3;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 0, 3600);
        shopToken1 = jwtHelper.createToken(1L, "wt", 10L, 1, 3600);
        shopToken2 = jwtHelper.createToken(1L, "wt", 3L, 1, 3600);
        shopToken3 = jwtHelper.createToken(1L, "wt", 2L, 1, 3600);
    }



    /**
     * GET
     * /shops/{shopId}/groupons
     * 商户查询商铺所有状态的团购活动
     */
    // 不存在的商品
    @Test
    void testRetrieveByShopIdWithNullProduct() throws Exception {


        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons", 2)
                        .header("authorization", shopToken3)
                        .param("productId", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(0)));
    }

    // 商户和活动不匹配
    @Test
    void testRetrieveByShopIdGivenWrongProductId() throws Exception {


        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons", 10)
                        .header("authorization", shopToken1)
                        .param("productId", "1559")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    // 成功返回
    @Test
    void testRetrieveByShopIdGivenProductId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons", 2)
                        .header("authorization", shopToken3)
                        .param("productId", "1668")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].name", is(List.of("团购活动2"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].beginTime", is(List.of(LocalDateTime.parse("2021-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].endTime", is(List.of(LocalDateTime.parse("2027-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))));
    }

    // 不带产品Id，用商户Id查询
    @Test
    void testRetrieveByShopIdGivenWithoutProductId() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons", 2)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].name", is(List.of("团购活动2"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].beginTime", is(List.of(LocalDateTime.parse("2021-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].endTime", is(List.of(LocalDateTime.parse("2027-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))));
    }

    // 不带产品Id，用商户Id查询（平台管理人员查询）
    @Test
    void testRetrieveByShopIdGivenWithoutProductIdWithAdmin() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons", 2)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].name", is(List.of("团购活动2"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].beginTime", is(List.of(LocalDateTime.parse("2021-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].endTime", is(List.of(LocalDateTime.parse("2027-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))));
    }

    // 平台管理人员查询所有
    @Test
    void testRetrieveByShopIdGivenWithoutProductIdWithAdminForAll() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons", 0)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].name", is(List.of("团购活动2"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].beginTime", is(List.of(LocalDateTime.parse("2021-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].endTime", is(List.of(LocalDateTime.parse("2027-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].name", is(List.of("团购活动1"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].beginTime", is(List.of(LocalDateTime.parse("2022-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].endTime", is(List.of(LocalDateTime.parse("2028-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))));
    }

    // 平台管理人员查询所有new状态的
    @Test
    void testRetrieveByShopIdGivenWithoutProductIdWithAdminForAllWithNewStatus() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons", 0)
                        .header("authorization", adminToken)
                        .param("status", "0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '95')].name", is(List.of("团购活动3"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '95')].beginTime", is(List.of(LocalDateTime.parse("2021-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '95')].endTime", is(List.of(LocalDateTime.parse("2027-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '96')].name", is(List.of("团购活动4"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '96')].beginTime", is(List.of(LocalDateTime.parse("2021-11-11T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '96')].endTime", is(List.of(LocalDateTime.parse("2030-02-19T14:38:20", DateTimeFormatter.ISO_DATE_TIME).toString()))));
    }


    /**
     * POST
     * /shops/{shopId}/products/{pid}/groupons
     * 商户新增团购活动
     */

    // 新增失败（不存在对应的商品）
    @Test
    void testCreateShopsWhenUserIsAdmin() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"name\":\"清仓大甩卖\","
                + "\"thresholds\":[{\"quantity\":\"100\", \"percentage\":\"10\"},{\"quantity\":\"50\",\"percentage\":\"5\"}],"
                + "\"price\":\"10000\",\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\","
                + "\"beginTime\":\"2023-12-05T10:09:50.000\",\"endTime\":\"2032-12-05T10:09:50.000\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons", 10, 99999)
                        .header("authorization", shopToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    // 新增失败（商品与商铺不对应）
    @Test
    void testCreateShopsWhenUsingWrongShop() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"name\":\"清仓大甩卖\","
                + "\"thresholds\":[{\"quantity\":\"100\", \"percentage\":\"10\"},{\"quantity\":\"50\",\"percentage\":\"5\"}],"
                + "\"price\":\"10000\",\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\","
                + "\"beginTime\":\"2023-12-05T10:09:50.000\",\"endTime\":\"2032-12-05T10:09:50.000\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons", 10, 1551)
                        .header("authorization", shopToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    // 新增失败（有重叠的onSale）
    @Test
    void testCreateShopsByFlapSale() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"name\":\"清仓大甩卖\","
                + "\"thresholds\":[{\"quantity\":\"100\", \"percentage\":\"10\"},{\"quantity\":\"50\",\"percentage\":\"5\"}],"
                + "\"price\":\"10000\",\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\","
                + "\"beginTime\":\"2023-12-05T10:09:50.000\",\"endTime\":\"2032-12-05T10:09:50.000\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons", 10, 1550)
                        .header("authorization", shopToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.GOODS_ONSALE_CONFLICT.getErrNo())));
    }

    // 新增成功
    @Test
    void testCreateShops() throws Exception {

        String body = "{\"name\":\"清仓大甩卖\","
                + "\"thresholds\":[{\"quantity\":\"100\", \"percentage\":\"10\"},{\"quantity\":\"50\",\"percentage\":\"5\"}],"
                + "\"price\":\"10000\",\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\","
                + "\"beginTime\":\"3030-12-05T10:09:50.000\",\"endTime\":\"3032-12-05T10:09:50.000\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons", 3, 1755)
                        .header("authorization", shopToken2)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("清仓大甩卖")));
    }



    /**
     * POST
     * /shops/{shopId}/products/{pid}/groupons/{id}
     * 商户将产品增加到团购活动
     */
    // shopId与act的shopId不匹配（with redis）
    @Test
    void testCreateGrouponActGivenWrongShopId() throws Exception {
        GrouponAct grouponAct = new GrouponAct();
        List<Threshold> thresholdList = new ArrayList<>();
        thresholdList.add(new Threshold(100, 50L));
        thresholdList.add(new Threshold(30, 10L));
        thresholdList.add(new Threshold(20, 5L));
        grouponAct.setThresholds(thresholdList);
        grouponAct.setId(4L);
        grouponAct.setShopId(3L);
        grouponAct.setName("团购活动test");
        grouponAct.setActClass("grouponActDao");

        Mockito.when(redisUtil.hasKey("A4")).thenReturn(true);
        Mockito.when(redisUtil.get("A4")).thenReturn(grouponAct);

        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 1755, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    // 活动类型不匹配，不是团购活动
    @Test
    void testCreateGrouponActGivenWrongTypeAct() throws Exception {
        GrouponAct grouponAct = new GrouponAct();
        List<Threshold> thresholdList = new ArrayList<>();
        thresholdList.add(new Threshold(100, 50L));
        thresholdList.add(new Threshold(30, 10L));
        thresholdList.add(new Threshold(20, 5L));
        grouponAct.setThresholds(thresholdList);
        grouponAct.setId(4L);
        grouponAct.setShopId(2L);
        grouponAct.setName("团购活动test");
        grouponAct.setActClass("couponActDao");

        Mockito.when(redisUtil.hasKey("A4")).thenReturn(true);
        Mockito.when(redisUtil.get("A4")).thenReturn(grouponAct);

        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 1755, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }



    // 活动ID不合法
    @Test
    void testCreateGrouponActByWrongActivityId() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 1668, 1000)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    // 商户Id和活动Id不匹配
    @Test
    void testCreateGrouponActByWrongActivityIdAndWrongShopId() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 10, 1668, 4)
                        .header("authorization", shopToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    // 活动类型不为团购
    @Test
    void testCreateGrouponActByWrongActivityType() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 1668, 5)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }



    /**
     * DELETE
     * /shops/{shopId}/products/{pid}/groupons/{id}
     * 将产品上的团购活动取消
     */
    // 成功删除
    @Test
    void testCancelProductGrouponAct() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 1668, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    // activity中没有相应的product
    @Test
    void testCancelProductGrouponActGivenWrongProduct() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 9999, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.INTERNAL_SERVER_ERR.getErrNo())));
    }


    /**
     * GET
     * /shops/{shopId}/groupons/{id}
     * 商户查看特定团购详情
     * 使用SpyBean修正用例,以适配OpenFeignMapperAspect
     * @Author 37220222203851
     */
    @Test
    void testFindGrouponByIdByNoShop() throws Exception {
        InternalReturnObject<Shop> ret = new InternalReturnObject<>(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo(), ReturnNo.RESOURCE_ID_NOTEXIST.getMessage());

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.doReturn(ret).when(shopMapper).getShopById(2L);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons/{id}", 2, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    // 成功查看
    @Test
    void testFindGrouponById() throws Exception {
        Shop shop = new Shop();
        shop.setId(2L);
        shop.setName("tongtongShopTest");
        InternalReturnObject<Shop> ret = new InternalReturnObject<>(shop);

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.doReturn(ret).when(shopMapper).getShopById(2L);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/groupons/{id}", 2, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(4)));
    }



    /**
     * PUT
     * /shops/{shopId}/groupons/{id}
     * 商户修改团购活动
     */
    @Test
    void testUpdateGrouponAct() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        String body = "{\"name\":\"团购活动3\","
                + "\"thresholds\":[{\"quantity\":\"100\", \"percentage\":\"10\"},{\"quantity\":\"50\",\"percentage\":\"5\"}],"
                + "\"beginTime\":\"3030-12-05T10:09:50.000\",\"endTime\":\"3032-12-05T10:09:50.000\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/groupons/{id}", 2, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }



    /**
     * PUT
     * /shops/{shopId}/groupons/{id}/publish
     * 平台管理人员审核团购活动
     * 更新url,修正用例
     * @Author 37220222203851
     */
    @Test
    void testPublishGrouponActWithShop() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/groupons/{id}/publish", 2, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }



    /**
    * 发布已经发布过的活动
    * 更新url,修正用例
    * @Author: 37220222203851
    */
    @Test
    void testPublishGrouponActGivenPublishedItem() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/groupons/{id}/publish", 0, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    /**
     * 发布未发布过的团购活动
     * 更新url,修正用例
     * @Author: 37220222203851
     */
    @Test
    void testPublishGrouponActGivenUnpublishedItem() throws Exception {
        GrouponAct grouponAct = new GrouponAct();
//        List<Threshold> thresholdList = new ArrayList<>();
//        thresholdList.add(new Threshold(100, 50L));
//        thresholdList.add(new Threshold(30, 10L));
//        thresholdList.add(new Threshold(20, 5L));
//        grouponAct.setThresholds(thresholdList);
        grouponAct.setId(4L);
        grouponAct.setShopId(2L);
        grouponAct.setName("团购活动test");
        grouponAct.setActClass("grouponActDao");
        grouponAct.setStatus(Activity.NEW);
        grouponAct.setObjectId("650ffad43310e8f1e62c05ce");

        Mockito.when(redisUtil.hasKey("A4")).thenReturn(true);
        Mockito.when(redisUtil.get("A4")).thenReturn(grouponAct);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/groupons/{id}/publish", 0, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }



    /**
     * DELETE
     * /shops/{shopId}/groupons/{id}
     * 商户终止团购活动
     */
    // 平台管理人员终止团购活动
    @Test
    void testDeleteGrouponActByAdmin() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/groupons/{id}", 0, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    // 商户终止团购活动
    @Test
    void testDeleteGrouponActByShop() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/groupons/{id}", 2, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    // 商户终止非自己的团购活动
    @Test
    void testDeleteGrouponActByWrongShop() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/groupons/{id}", 10, 4)
                        .header("authorization", shopToken1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void testCreateGrouponActSuccessWithRedis() throws Exception {
        GrouponAct grouponAct = new GrouponAct();
        List<Threshold> thresholdList = new ArrayList<>();
        thresholdList.add(new Threshold(100, 50L));
        thresholdList.add(new Threshold(30, 10L));
        thresholdList.add(new Threshold(20, 5L));
        grouponAct.setThresholds(thresholdList);
        grouponAct.setId(4L);
        grouponAct.setShopId(2L);
        grouponAct.setName("团购活动test");
        grouponAct.setActClass("grouponActDao");

        Mockito.when(redisUtil.hasKey("A4")).thenReturn(true);
        Mockito.when(redisUtil.get("A4")).thenReturn(grouponAct);

        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 5459, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("团购活动test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(4)));
    }

    // 成功创建（without redis）
    @Test
    void testCreateGrouponAct() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);


        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 5459, 4)
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("团购活动2")));
    }
}
