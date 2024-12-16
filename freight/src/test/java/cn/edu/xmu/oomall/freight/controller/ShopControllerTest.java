package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.CoreMatchers.is;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;

@SpringBootTest(classes = FreightApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ShopControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public RegionMapper regionMapper;
    private static String adminToken;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void testAddShopLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"logisticsId\":\"2\", \"secret\":\"secret2\", \"priority\":\"6\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/shoplogistics", 2L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.logistics.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.logistics.name").value("中通快递"));
        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAddShopLogisticsGivenWrongLogisticsId() throws Exception {
        // 新增logistics不存在
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"logisticsId\":\"99\", \"secret\":\"secret2\", \"priority\":\"6\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/shoplogistics", 2L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testAddShopLogisticsGivenExistLogisticsId() throws Exception {
        // 重复新增logistics
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"logisticsId\":\"1\", \"secret\":\"secret2\", \"priority\":\"6\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/shoplogistics", 2L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FREIGHT_LOGISTIC_EXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testGetShopLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/shoplogistics", 2L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].logistics.id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].logistics.id",is(3)));

        // display:
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(print());
    }

    @Test
    public void testGetShopLogisticsGivenEmptyShop() throws Exception {
        // 无任何物流信息的商铺
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/shoplogistics", 99L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list").isEmpty());

        // display:
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andDo(print());
    }

    @Test
    public void testChangeLogisticsContract() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"secret\":\"secret2-modify\", \"priority\":\"7\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}", 2L, 6L)
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
    public void testChangeContract() throws Exception {
        // 无权更改
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String json = "{\"secret\":\"secret2-modify\", \"priority\":\"7\"}";
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}", 1L, 6L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testSuspendShopLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}/suspend", 2L, 6L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testSuspendShopLogisticsGivenUnauthorizedShop() throws Exception {
        // 无权更改
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}/suspend", 9L, 6L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testResumeShopLogistics() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/shoplogistics/{id}/resume", 2L, 6L)
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }
}
