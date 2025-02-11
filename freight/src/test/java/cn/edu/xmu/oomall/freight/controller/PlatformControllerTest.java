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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = FreightApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class PlatformControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    @SpyBean
    public RegionMapper regionMapper;

    private static String adminToken, shopToken,customerToken;

    String requestBody = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2030-12-12T12:12:12\"}";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 0, 3600);
        shopToken = jwtHelper.createToken(1L, "shopUser", 1L, 1, 3600);
        customerToken = jwtHelper.createToken(1L, "customer", 2L, 2, 3600);
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 测试平台管理员添加不可送达地区，happy path
     */
    @Test
    void testAddUndeliverableAsAdminSuccess() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        ret.setErrno(ReturnNo.OK.getErrNo());
        RegionPo region = new RegionPo();
        region.setId(483250L);
        region.setName("黄图岗社区居委会");
        ret.setData(region);

        doReturn(ret).when(regionMapper).findRegionById(483250L);
        // 模拟平台管理员请求添加不可送达地区
        mockMvc.perform(MockMvcRequestBuilders.post("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 4L, 483250L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()));
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 测试平台管理员添加不可送达地区，但传递了不存在的 regionId，应返回资源不存在
     */
    @Test
    void testAddUndeliverableAsAdminWithNotExistRegion() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        ret.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo());
        ret.setErrmsg("Region not found");

        doReturn(ret).when(regionMapper).findRegionById(9999L);
        mockMvc.perform(MockMvcRequestBuilders.post("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 3L, 9999L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 测试商铺用户添加不可送达地区，应返回权限不足
     */
    @Test
    void testAddUndeliverableAsShopUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 1L, 3L, 483250L)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 测试平台管理员删除不可送达地区，happy path
     */
    @Test
    void testDeleteUndeliverableAsAdminSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 1L, 483250L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 测试商铺用户删除不可送达地区，应返回权限不足
     */
    @Test
    void testDeleteUndeliverableAsShopUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 1L, 4L, 483250L)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * 2024-dsg-112
     *
     * @author Hao Chen
     * 测试商铺用户删除不可送达地区，但传递了不存在的不可送达地区，应返回资源不存在
     */
    @Test
    void testDeleteUndeliverableAsAdminButNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 2L, 483250L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    @Test
    public void testCreateLogistics() throws Exception {
        String body="{\"name\": \"京东快递\"," +
                "\"appId\": \"JD1002\"," +
                "\"appAccount\": \"adwawdw\"," +
                "\"secret\": \"secret4\"," +
                "\"snPattern\":\"^JD[A-Za-z0-9-]{4,35}$\"," +
                "\"logisticsClass\": \"jDAdaptor\"}";

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/platforms/0/logistics")
                        .header("authorization", adminToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()));
    }

    @Test
    public void testCreateLogisticsWithWrongValidation() throws Exception {
        String body="{\"name\": \"京东快递\"," +
                "\"appId\": \"JD1002\"," +
                "\"appAccount\": \"adwawdw\"," +
                "\"secret\": \"secret4\"," +
                "\"snPattern\":\"^JD[A-Za-z0-9-]{4,35}$\"," +
                "\"logisticsClass\": \"jDAdaptor\"}";

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/platforms/2/logistics")
                        .header("authorization",customerToken)
                        .contentType("application/json;charset=UTF-8")
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.AUTH_NO_RIGHT.getErrNo()));
    }

}
