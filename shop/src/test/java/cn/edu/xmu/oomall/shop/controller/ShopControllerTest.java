package cn.edu.xmu.oomall.shop.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.shop.ShopApplication;
import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.dao.bo.Shop;
import org.apache.ibatis.jdbc.Null;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

/**
 * @author WuTong
 * @Task 2023-dgn1-007
 */
@SpringBootTest(classes = ShopTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ShopControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    private static String shopToken, UnregisteredShopToken, adminToken, WrongShopToken, inAuditShopToken, impossibleToken;
    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        // 未创建过商铺的商户(shopId = -1)
        UnregisteredShopToken = jwtHelper.createToken(3L, "wt", -1L, 1, 3600);
        // 创建过商铺的商户(shopId != -1)
        shopToken = jwtHelper.createToken(2L, "wt", 1L, 1, 3600);
        // 平台管理人员(shopId = 0)
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        WrongShopToken = jwtHelper.createToken(4L, "wt", 100L, 1, 3600);
        inAuditShopToken = jwtHelper.createToken(5L, "wt", 8L, 1, 3600);
        impossibleToken = jwtHelper.createToken(1L, "wt", -1L, 1, 3600);
    }

    /* post API /shops      描述：店家申请店铺 */


    // 平台管理人员创建商铺（不允许）
    @Test
    void testCreateShopsWhenUserIsAdmin() throws Exception {
        String body = "{\"name\":\"测试商铺001\", "
                + "\"consignee\":{\"name\":\"admin\", \"mobile\":\"18888888800\", \"regionId\":\"1\", \"address\":\"厦大\"}"
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.SHOP_USER_HASSHOP.getErrNo())));
    }

    // 成功创建一个商铺
    @Test
    void testCreteShopWhenUserHasNoShop() throws Exception {
        String body = "{\"name\":\"测试商铺001\","
                + "\"consignee\":{\"name\":\"user\",\"mobile\":\"18888888800\",\"regionId\":1,\"address\":\"厦大\"}"
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops")
                        .header("authorization", UnregisteredShopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("测试商铺001")));
    }

    // 已经创建商铺的商户再次创建
    @Test
    void testCreteShopWhenUserHasShop() throws Exception {
        String body = "{\"name\":\"测试商铺001\", "
                + "\"consignee\":{\"name\":\"user\", \"mobile\":\"18888888800\", \"regionId\":\"1\", \"address\":\"厦大\"}"
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops")
                        .header("authorization", impossibleToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.SHOP_USER_HASSHOP.getErrNo())));
    }



    /* get API /shops      描述顾客查询店铺信息 */
    // 带参数的查询
    @Test
    void testRetrieveShopsGivenParam() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops")
                        .param("name", "OOMALL")
                        .param("page", "1")
                        .param("pageSize", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1')].name", hasItem("OOMALL自营商铺")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(5)));
    }

    // 不带参数的查询
    @Test
    void testRetrieveShopsWithoutParam() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops")
                        .param("name", "")
                        .param("page", "1")
                        .param("pageSize", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(7)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '2')].name", hasItem("甜蜜之旅")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1')].name", hasItem("OOMALL自营商铺")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '3')].name", hasItem("向往时刻")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].name", hasItem("努力向前")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '5')].name", hasItem("坚持就是胜利")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '6')].name", hasItem("一口气")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '7')].name", hasItem("商铺7")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(20)));
    }



    /* PUT API  /shops/{id}     描述：店家修改店铺信息 */
    // 成功修改（通过redis）
    @Test
    void testUpdateShopGivenRedis() throws Exception {
        Shop shop = new Shop();
        shop.setId(1L);
        shop.setName("OOMALL自营商铺");
        shop.setDeposit(5000000L);
        shop.setDepositThreshold(1000000L);
        shop.setStatus((byte)2);
        shop.setCreatorId(1L);
        shop.setCreatorName("admin");
        shop.setGmtCreate(LocalDateTime.parse("2021-11-11T13:24:49"));
        shop.setAddress("黄图岗南街112");
        shop.setConsignee("张三");
        shop.setMobile("111111");
        shop.setFreeThreshold(10000);

        Mockito.when(redisUtil.get("S1")).thenReturn(shop);
        Mockito.when(redisUtil.set("S1", shop, 3600)).thenReturn(true);

        String body = "{\"consignee\":{\"name\":\"user\", \"mobile\":\"18888888800\", \"regionId\":\"1\", \"address\":\"厦大\"},"
                + "\"freeThreshold\":1000"
                + "}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}", 1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    // 成功修改
    @Test
    void testUpdateShopWithoutRedis() throws Exception {
        Mockito.when(redisUtil.get("S1")).thenReturn(null);
        Mockito.when(redisUtil.set("S1", new Shop(), 3600)).thenReturn(true);

        String body = "{\"consignee\":{\"name\":\"user\", \"mobile\":\"18888888800\", \"regionId\":\"1\", \"address\":\"厦大\"},"
                + "\"freeThreshold\":1000"
                + "}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}", 1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    // 修改失败（数据库中没有相应的shop）
    @Test
    void testUpdateShopGivenWrongShop() throws Exception {
        Mockito.when(redisUtil.get("S100")).thenReturn(null);

        String body = "{\"consignee\":{\"name\":\"user\", \"mobile\":\"18888888800\", \"regionId\":\"1\", \"address\":\"厦大\"},"
                + "\"freeThreshold\":1000"
                + "}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}", 100)
                        .header("authorization", WrongShopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    // 修改失败，商铺状态为abandon
    @Test
    void testUpdateShopGivenAbandonShop() throws Exception {
        Shop shop = new Shop();
        shop.setId(100L);
        shop.setName("OOMALL自营商铺");
        shop.setDeposit(5000000L);
        shop.setDepositThreshold(1000000L);
        shop.setStatus(Shop.ABANDON);
        shop.setCreatorId(1L);
        shop.setCreatorName("admin");
        shop.setGmtCreate(LocalDateTime.parse("2021-11-11T13:24:49"));
        shop.setAddress("黄图岗南街112");
        shop.setConsignee("张三");
        shop.setMobile("111111");
        shop.setFreeThreshold(10000);

        Mockito.when(redisUtil.get("S100")).thenReturn(shop);

        String body = "{\"consignee\":{\"name\":\"user\", \"mobile\":\"18888888800\", \"regionId\":\"1\", \"address\":\"厦大\"},"
                + "\"freeThreshold\":1000"
                + "}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}", 100)
                        .header("authorization", WrongShopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }



    /* DELETE API: /shops/{id}      描述：平台管理员或店家关闭店铺 */
    // 删除非下线状态的商铺
    @Test
    void testDeleteShopGivenWrongShop() throws Exception{
        Mockito.when(redisUtil.get("S1")).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{id}", 1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }
    // 成功删除
    @Test
    void testDeleteShop() throws Exception{
        Mockito.when(redisUtil.get("S5")).thenReturn(null);
        Mockito.when(redisUtil.set("S5", new Shop(), 3600)).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{id}", 5)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }




    /* GET API: /shops/{id}        描述：商户获取商铺信息 */
    @Test
    void testFindShopById() throws Exception{
        Mockito.when(redisUtil.get("S1")).thenReturn(null);
        Mockito.when(redisUtil.set("S1", new Shop(), 3600)).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{id}", 1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("OOMALL自营商铺")));
    }



    /* GET API: /shops/{id}/shops       描述：平台管理员查询店铺信息 */
    @Test
    void testRetrieveAllShopsGivenAdmin() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{id}/shops", 0)
                        .param("status", Shop.ABANDON.toString())
                        .param("name", "")
                        .param("page", "1")
                        .param("pageSize", "5")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '45')].name", is(List.of("停用商铺1"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '71')].name", is(List.of("停用商铺2"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '72')].name", is(List.of("停用商铺3"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '73')].name", is(List.of("停用商铺4"))));
    }

    @Test
    void testRetrieveAllShopsGivenShop() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{id}/shops", 1)
                        .param("status", Shop.ABANDON.toString())
                        .param("name", "")
                        .param("page", "1")
                        .param("pageSize", "5")
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void testRetrieveAllShops() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{id}/shops", 0)
                        .param("page", "1")
                        .param("pageSize", "10")
                        .param("name", "")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1')].name", is(List.of("OOMALL自营商铺"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '2')].name", is(List.of("甜蜜之旅"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '3')].name", is(List.of("向往时刻"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].name", is(List.of("努力向前"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '5')].name", is(List.of("坚持就是胜利"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '6')].name", is(List.of("一口气"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '7')].name", is(List.of("商铺7"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].name", is(List.of("商铺8"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '9')].name", is(List.of("商铺9"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '10')].name", is(List.of("商铺10"))));
    }



    /* PUT API: /shops/{shopId}/audit       描述：平台管理员审核店铺信息 */
    @Test
    void testUpdateShopAuditGivenShop() throws Exception{
        Mockito.when(redisUtil.get("S8")).thenReturn(null);
        Mockito.when(redisUtil.set("S8", new Shop(), 3600)).thenReturn(true);

        String body = "{\"conclusion\":\"true\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/audit", 8)
                        .header("authorization", inAuditShopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void testUpdateShopAuditGivenAdmin() throws Exception{
        Mockito.when(redisUtil.get("S8")).thenReturn(null);
        Mockito.when(redisUtil.set("S8", new Shop(), 3600)).thenReturn(true);

        String body = "{\"conclusion\":\"true\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/audit", 8)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    // 审核非NEW状态的商铺
    @Test
    void testUpdateShopAuditGivenWrongShopStatus() throws Exception{
        Mockito.when(redisUtil.get("S1")).thenReturn(null);
        Mockito.when(redisUtil.set("S1", new Shop(), 3600)).thenReturn(true);

        String body = "{\"conclusion\":\"true\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/audit", 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }



    /* PUT API: /shops/{id}/online      描述：商户上线店铺 */
    // 上线非下线状态的商铺
    @Test
    void testUpdateShopOnlineWithWrongShopStatus() throws Exception{
        Mockito.when(redisUtil.get("S1")).thenReturn(null);
        Mockito.when(redisUtil.set("S1", new Shop(), 3600)).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}/online", 1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }
    // 成功上线
    @Test
    void testUpdateShopOnline() throws Exception{
        Mockito.when(redisUtil.get("S5")).thenReturn(null);
        Mockito.when(redisUtil.set("S5", new Shop(), 3600)).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}/online", 5)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }



    /* PUT API: /shops/{id}/offline      描述：商户下线店铺 */
    // 下线非上线状态的商铺
    @Test
    void testUpdateShopOfflineWithWrongShopStatus() throws Exception{
        Mockito.when(redisUtil.get("S5")).thenReturn(null);
        Mockito.when(redisUtil.set("S5", new Shop(), 3600)).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}/offline", 5)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }
    // 成功上线
    @Test
    void testUpdateShopOffline() throws Exception{
        Mockito.when(redisUtil.get("S1")).thenReturn(null);
        Mockito.when(redisUtil.set("S1", new Shop(), 3600)).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{id}/offline", 1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}
