package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.freight.FreightApplication;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FreightApplication.class)
@AutoConfigureMockMvc
@Transactional
public class WarehouseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static String adminToken;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

     // 测试说明：
     // alter和delete部分检查操作与add相同，不再重复测试

    @Test
    public void testAlterWarehouseLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2030-12-12T12:12:12\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 10L,22L,910L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAlterWarehouseLogisticsGivenUnrealWarehouse() throws Exception {
        // warehouse不存在
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2030-12-12T12:12:12\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 10L,22L,999L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testDeleteWarehouseLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 10L,22L,910L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testDeleteWarehouseLogisticsGivenUnauthorizedShop() throws Exception {
        // shop无权操作
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 5L,22L,910L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAddWarehouseLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2029-11-11T11:11:11\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 10L,26L,29L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()));

        // display:
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(print());
    }

    @Test
    public void testAddWarehouseLogisticsUnauthorizedShop() throws Exception {
        // shopId无对应的shopLogistics
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2029-11-11T11:11:11\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 5L,26L,29L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAddWarehouseLogisticsGivenUnrealShopLogistics() throws Exception {
        // shopLogistics不存在
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2029-11-11T11:11:11\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 10L,26L,99L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAddWarehouseLogisticsGivenUnmatchingWarehouse() throws Exception {
        // warehouse与shopId不对应
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2029-11-11T11:11:11\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 5L,26L,29L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAddWarehouseLogisticsGivenUnrealWarehouse() throws Exception {
        // warehouse不存在
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2029-11-11T11:11:11\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 10L,99L,29L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAddWarehouseLogisticsGivenRepetitive() throws Exception {
        // 重复添加
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2029-11-11T11:11:11\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/warehouses/{id}/shoplogistics/{lid}", 10L,22L,30L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FREIGHT_WAREHOUSELOGISTIC_EXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testGetWarehouseLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2030-12-12T12:12:12\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/warehouses/{id}/shoplogistics", 10L,22L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].shopLogistics.id",is(28)));

        // display:
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(print());
    }
}
