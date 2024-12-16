package cn.edu.xmu.oomall.region.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.region.RegionApplication;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;


@SpringBootTest(classes = RegionApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AdminRegionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    private static String adminToken;
    private final String ADMIN_SUB_REGIONS = "/platforms/{did}/regions/{id}/subregions";
    private final String ADMIN_REGIONS_ID = "/platforms/{did}/regions/{id}";
    private final String ADMIN_REGIONS_ID_SUSPEND = "/platforms/{did}/regions/{id}/suspend";
    private final String ADMIN_REGIONS_ID_RESUME = "/platforms/{did}/regions/{id}/resume";

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
    }

    @Test
    void updateRegion() throws Exception {
        Mockito.when(redisUtil.hasKey("R4")).thenReturn(false);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"lng\":\"116.416357\", \"lat\":\"39.928353\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID, 0, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    void updateRegionByIdGivenNotPlatform() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"lng\":\"116.416357\", \"lat\":\"39.928353\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID, 1, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                        
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void updateRegionByIdGivenNonExistId() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"lng\":\"116.416357\", \"lat\":\"39.928353\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID, 0, 400122002)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }
    /**
     * @Author 37220222203708
     */

    @Test
    void updateRegionGivenNotAllow() throws Exception {
        Mockito.when(redisUtil.hasKey("R819")).thenReturn(false);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"lng\":\"116.416357\", \"lat\":\"39.928353\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID, 0, 819)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.REGION_ABANDONE.getErrNo())));
    }

    @Test
    public void getShopSubRegionsById() throws Exception {
        Mockito.when(redisUtil.get("R4")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_SUB_REGIONS, 0, 4)
                        .header("authorization", adminToken)
                        .param("page","1")
                        .param("pageSize","100")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(12)))
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '15')].name", hasItem("韶九社区居委会")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '16')].name", hasItem("王府井社区居委会")));
    }

    @Test
    public void getShopSubRegionsByIdGivenNotPlatform() throws Exception {
        Mockito.when(redisUtil.get("R4")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_SUB_REGIONS, 1, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void createSubRegions() throws Exception {
        Mockito.when(redisUtil.get("R3")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"mergerName\":\"北京，东城，风云再起\",\"pinyin\":\"FengYunZaiQi\",\"lng\":\"116.416357\", \"lat\":\"39.928353\",\"areaCode\":\"110101000000\",\"zipCode\":\"00100000\",\"cityCode\":\"010\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_SUB_REGIONS, 0, 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("东城区风云再起")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").exists());
    }

    @Test
    void createSubRegionsGivenNotPlatform() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"mergerName\":\"北京，东城，风云再起\",\"pinyin\":\"FengYunZaiQi\",\"lng\":\"116.416357\", \"lat\":\"39.928353\",\"areaCode\":\"110101000000\",\"zipCode\":\"00100000\",\"cityCode\":\"010\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_SUB_REGIONS, 1, 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void createSubRegionGivenWrongFormat() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"mergerName\":\"北京，东城，风云再起\",\"pinyin\":\"FengYunZaiQi\",\"lng\":\"116.416357\", \"lat\":\"s39.928353\",\"areaCode\":\"110101000000\",\"zipCode\":\"00100000\",\"cityCode\":\"010\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_SUB_REGIONS, 0, 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FIELD_NOTVALID.getErrNo())));
    }

    @Test
    void createSubRegionGivenNullString() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"mergerName\":\"北京，东城，风云再起\",\"pinyin\":\"FengYunZaiQi\",\"lng\":\"\", \"lat\":\"39.928353\",\"areaCode\":\"110101000000\",\"zipCode\":\"00100000\",\"cityCode\":\"010\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_SUB_REGIONS, 0, 3)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FIELD_NOTVALID.getErrNo())));
    }

    /**
     * @Author 37220222203708
     */
    @Test
    void createSubRegionGivenNotAllow() throws Exception {
        Mockito.when(redisUtil.get("R819")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        String body = "{\"name\":\"东城区风云再起\", \"shortName\":\"风云再起\", \"mergerName\":\"北京，东城，风云再起\",\"pinyin\":\"FengYunZaiQi\",\"lng\":\"116.416357\", \"lat\":\"39.928353\",\"areaCode\":\"110101000000\",\"zipCode\":\"00100000\",\"cityCode\":\"010\"}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_SUB_REGIONS, 0, 819)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.REGION_ABANDONE.getErrNo())));

    }

    @Test
    void deleteRegionById() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_REGIONS_ID, 0, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    void deleteRegionByIdGivenNotPlatform() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_REGIONS_ID, 1, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * @Author 37220222203708
     */
    @Test
    void deleteRegionByIdGivenNotAllow() throws Exception {
        Mockito.when(redisUtil.get("R819")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_REGIONS_ID, 0, 819)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    @Test
    void suspendRegionById() throws Exception {
        Mockito.when(redisUtil.get("R4")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID_SUSPEND, 0, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    void suspendRegionByIdGivenNotPlatform() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID_SUSPEND, 1, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * @Author 37220222203708
     */
    @Test
    void suspendRegionByIdGivenNotAllow() throws Exception {
        Mockito.when(redisUtil.get("R16")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID_SUSPEND, 0, 16)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    @Test
    void resumeRegionByIdGivenNotAllow() throws Exception {
        Mockito.when(redisUtil.get("R4")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID_RESUME, 0, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    /**
     * @Author 37220222203708
     */
    @Test
    void resumeRegionByIdGivenNotAllow1() throws Exception {
        Mockito.when(redisUtil.get("R802")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID_RESUME, 0, 802)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.REGION_INVALID.getErrNo())));
    }

    @Test
    void resumeRegionByIdGivenNotPlatform() throws Exception {
        Mockito.when(redisUtil.get(Mockito.anyString())).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID_RESUME, 1, 4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void resumeRegionById() throws Exception {
        Mockito.when(redisUtil.get("R16")).thenReturn(null);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_REGIONS_ID_RESUME, 0, 16)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}