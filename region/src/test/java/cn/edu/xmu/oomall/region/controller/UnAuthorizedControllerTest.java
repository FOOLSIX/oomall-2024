package cn.edu.xmu.oomall.region.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.region.RegionApplication;
import cn.edu.xmu.oomall.region.dao.bo.Region;
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
class UnAuthorizedControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    private static final String STATES = "/regions/states";
    private static final String SUB_REGIONS = "/regions/{id}/subregions";
    private static final String REGION = "/regions/{id}";

    @Test
    void retrieveRegionsStates() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(STATES)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code == '0')].name", hasItem("有效")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code == '1')].name", hasItem("停用")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code == '2')].name", hasItem("废弃")));
    }

    @Test
    void retrieveSubRegionsById() throws Exception {
        Mockito.when(redisUtil.get("R4")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(SUB_REGIONS, 4)
                        .param("page","1")
                        .param("pageSize","100")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '5')].name", hasItem("多福巷社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '6')].name", hasItem("银闸社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '7')].name", hasItem("东厂社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].name", hasItem("智德社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '9')].name", hasItem("南池子社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '10')].name", hasItem("黄图岗社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '11')].name", hasItem("灯市口社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '12')].name", hasItem("正义路社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '13')].name", hasItem("甘雨社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '14')].name", hasItem("台基厂社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '15')].name", hasItem("韶九社区居委会")));
    }

    /**
     * @Author 37720222203708
     */
    @Test
    void retrieveSubRegionsById_CacheHit() throws Exception {
        List<Long> childIds = Arrays.asList(5L, 6L, 7L, 8L, 9L,10L,11L,12L,13L,14L,15L);
        Mockito.when(redisUtil.get("R4")).thenReturn(null);
        Mockito.when(redisUtil.get("RC4")).thenReturn((Serializable) childIds);

        this.mockMvc.perform(MockMvcRequestBuilders.get(SUB_REGIONS, 4)
                        .param("page","1")
                        .param("pageSize","100")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(11)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '5')].name", hasItem("多福巷社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '6')].name", hasItem("银闸社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '7')].name", hasItem("东厂社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8')].name", hasItem("智德社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '9')].name", hasItem("南池子社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '10')].name", hasItem("黄图岗社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '11')].name", hasItem("灯市口社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '12')].name", hasItem("正义路社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '13')].name", hasItem("甘雨社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '14')].name", hasItem("台基厂社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '15')].name", hasItem("韶九社区居委会")));
    }

    @Test
    void findRegionByIdGivenFromDatabase() throws Exception {
        Mockito.when(redisUtil.get("R4")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(REGION, 4)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("东华门街道办事处")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shortName", is("东华门")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.mergerName", is("北京,东城,东华门")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lng", is(116.406708)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lat", is(39.914219)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pinyin", is("DongHuaMen")));
    }

    @Test
    void findRegionByIdGivenFromRedis() throws Exception {
        Region region = new Region();
        region.setId(4L);
        region.setName("测试4");
        region.setShortName("风云再起");
        region.setLng(116.416357);
        region.setLat(39.928353);
        region.setPinyin("DongHuaMen");

        Mockito.when(redisUtil.get("R4")).thenReturn(region);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(REGION, 4)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("测试4")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shortName", is("风云再起")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lng", is(116.416357)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.lat", is(39.928353)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pinyin", is("DongHuaMen")));
    }

    @Test
    void findRegionByIdGivenNonExistId() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(REGION, 400122022)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }
}