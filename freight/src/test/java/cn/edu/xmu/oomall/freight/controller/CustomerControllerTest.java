package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = FreightApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static String adminToken, customerToken;

    @MockBean
    public RegionMapper regionMapper;

    @MockBean
    private RedisUtil redisUtil;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 0, 3600);
        customerToken = jwtHelper.createToken(1L, "customer", 2L, 2, 3600);
    }

    //@Test
    public void testGetLogisticsCompanyGivenWrongBillCode() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/logistics")
                        .header("authorization", adminToken)
                        .param("billCode", "9999999999")
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    //@Test
    public void testGetLogisticsCompany() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/logistics")
                        .header("authorization", adminToken)
                        .param("billCode", "1234567890")
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1));

        // display:
//        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
//        resultActions.andDo(print());
    }

    @Test
    public void testGetUndeliverable() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        ret.setErrno(ReturnNo.OK.getErrNo());
        RegionPo region = new RegionPo();
        region.setId(483250L);
        region.setName("黄图岗社区居委会");
        ret.setData(region);

        when(regionMapper.findRegionById(483250L)).thenReturn(ret);

        // 模拟顾客请求查看不可送达地区
        this.mockMvc.perform(MockMvcRequestBuilders.get("/logistics/{id}/undeliverableregions", 4L)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id",is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].region.id",is(483250)));
    }

    @Test
    public void testGetUndeliverableWithEmpty() throws Exception {
        // 模拟顾客请求查看不可送达地区，但没有不可送达地区
        this.mockMvc.perform(MockMvcRequestBuilders.get("/logistics/{id}/undeliverableregions", 1L)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()",is(0)));
    }
}
