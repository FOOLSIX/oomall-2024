package cn.edu.xmu.oomall.region.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.region.RegionApplication;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;


@SpringBootTest(classes = RegionApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
class InternalRegionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    private static final String PARENTS = "/internal/regions/{id}/parents";

    @Test
    void getParentsRegions() throws Exception {
        Mockito.when(redisUtil.get("R6")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(PARENTS, 6)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '4')].name", hasItem("东华门街道办事处")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '3')].name", hasItem("东城区")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '2')].name", hasItem("直辖区")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '1')].name", hasItem("北京市")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == '0')].name", hasItem("中华人民共和国")));
    }

    /**
     * @Author 37720222205040
     */
    @Test
    void getParentsRegions_CacheHit() throws Exception {
        // 准备缓存中的父区域ID列表
        List<Long> parentIds = Arrays.asList(4L, 3L, 2L, 1L, 0L);
        // 模拟Redis缓存返回父区域ID列表
        Mockito.when(redisUtil.get("R6")).thenReturn(null);
        Mockito.when(redisUtil.get("RP6")).thenReturn((Serializable) parentIds);

        // 执行请求
        this.mockMvc.perform(MockMvcRequestBuilders.get(PARENTS, 6)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == 4)].name", hasItem("东华门街道办事处")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == 3)].name", hasItem("东城区")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == 2)].name", hasItem("直辖区")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == 1)].name", hasItem("北京市")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.id == 0)].name", hasItem("中华人民共和国")));
    }
}