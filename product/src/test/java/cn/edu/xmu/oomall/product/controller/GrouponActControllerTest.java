package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
import cn.edu.xmu.oomall.product.model.Threshold;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author WuTong
 * @task 2023-dgn008-wt
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class GrouponActControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;




    /**
     * GET
     * /groupons
     * 查询所有上线态的团购活动
     */
    // 用prodcut_id查询
    @Test
    void testRetrieveGrouponActGivenProductId() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "1559")
                        .param("beginTime", LocalDateTime.now().minusYears(20L).toString())
                        .param("endTime", LocalDateTime.now().plusYears(20L).toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].name", is(List.of("团购活动1"))));
    }

    // 用shop_id查询
    @Test
    void testRetrieveGrouponActGivenShopId() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("shopId", "6")
                        .param("beginTime", LocalDateTime.now().minusYears(20L).toString())
                        .param("endTime", LocalDateTime.now().plusYears(20L).toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].name", is(List.of("团购活动1"))));
    }

    // 不带时间查询
    @Test
    void testRetrieveGrouponActGivenNoTime() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("shopId", "6"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(0)));
    }

    // shop_id和product_id均不使用
    @Test
    void testRetrieveGrouponActGivenNoShopIdNorProductId() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("beginTime", LocalDateTime.now().minusYears(20L).toString())
                        .param("endTime", LocalDateTime.now().plusYears(20L).toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].name", is(List.of("团购活动1"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '4')].name", is(List.of("团购活动2"))));
    }

    // 查询商品对应的优惠活动不存在
    @Test
    void testRetrieveGrouponActGivenProductIdWithNoSale() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "1550")
                        .param("beginTime", LocalDateTime.now().minusYears(20L).toString())
                        .param("endTime", LocalDateTime.now().plusYears(20L).toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(0)));
    }



    /**
     * GET
     * /groupons/{id}
     * 查看团购活动详情
     */
    // 成功返回团购活动详情（with redis）
    @Test
    void testFindGrouponActByIdWithRedis() throws Exception {
        GrouponAct grouponAct = new GrouponAct();
        List<Threshold> thresholdList = new ArrayList<>();
        thresholdList.add(new Threshold(100, 50L));
        thresholdList.add(new Threshold(30, 10L));
        thresholdList.add(new Threshold(20, 5L));
        grouponAct.setThresholds(thresholdList);
        grouponAct.setId(100L);
        grouponAct.setName("团购活动test");

        Mockito.when(redisUtil.get("A100")).thenReturn(grouponAct);
        Mockito.when(redisUtil.hasKey("A100")).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("团购活动test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.thresholds[?(@.quantity == '30')].percentage", is(List.of(10))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.thresholds[?(@.quantity == '100')].percentage", is(List.of(50))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.thresholds[?(@.quantity == '20')].percentage", is(List.of(5))));
    }

    // 返回团购活动详情失败（with redis）（活动类型不对应）
    @Test
    void testFindGrouponActByIdWithRedisGivenWrongClass() throws Exception {
        GrouponAct grouponAct = new GrouponAct();
        List<Threshold> thresholdList = new ArrayList<>();
        thresholdList.add(new Threshold(100, 50L));
        thresholdList.add(new Threshold(30, 10L));
        thresholdList.add(new Threshold(20, 5L));
        grouponAct.setThresholds(thresholdList);
        grouponAct.setId(100L);
        grouponAct.setName("团购活动test");
        grouponAct.setActClass("couponActDao");

        Mockito.when(redisUtil.get("A100")).thenReturn(grouponAct);
        Mockito.when(redisUtil.hasKey("A100")).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    // 成功返回团购活动详情（without redis）
    @Test
    void testFindGrouponActByIdWithoutRedis() throws Exception {
        Mockito.when(redisUtil.get("A4")).thenReturn(null);
        Mockito.when(redisUtil.hasKey("A4")).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons/{id}", 4)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("团购活动2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(4)));
    }

    // 返回团购活动详情失败（without redis，活动类型不匹配）
    @Test
    void testFindGrouponActByIdWithoutRedisGivenWrongClass() throws Exception {
        Mockito.when(redisUtil.get("A5")).thenReturn(null);
        Mockito.when(redisUtil.hasKey("A5")).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons/{id}", 5)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    // 返回团购活动详情失败（without redis，id不存在）
    @Test
    void testFindGrouponActByIdWithoutRedisGivenInvalidId() throws Exception {
        Mockito.when(redisUtil.get("A1000")).thenReturn(null);
        Mockito.when(redisUtil.hasKey("A1000")).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons/{id}", 1000)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }



    /**
     * GET
     * /groupons/{id}/products
     * 查看活动中的商品
     */
    @Test
    void testGetGrouponActProduct() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/groupons/{id}/products", 4)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1668')].name", is(List.of("肖家白胡椒粉30"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1668')].status", is(List.of(1))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1668')].quantity", is(List.of(34))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '1668')].price", is(List.of(40058))));
    }
}
