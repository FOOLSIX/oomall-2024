package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.freight.mapper.po.RegionPo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

    @SpyBean
    public RegionMapper regionMapper;

    private static String adminToken, shopToken;

    String requestBody = "{\"beginTime\":\"2020-11-11T11:11:11\", \"endTime\":\"2030-12-12T12:12:12\"}";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 0, 3600);
        shopToken = jwtHelper.createToken(1L, "shopUser", 1L, 1, 3600);
    }


    @Test
    void testAddUndeliverableAsAdminSuccess() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        ret.setErrno(ReturnNo.OK.getErrNo());
        RegionPo region = new RegionPo();
        region.setId(10L);
        region.setName("黄图岗社区居委会");
        ret.setData(region);

        doReturn(ret).when(regionMapper).findRegionById(10L);
        // 模拟平台管理员请求添加不可送达地区
        mockMvc.perform(MockMvcRequestBuilders.post("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 4L, 10L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()));
    }

    @Test
    void testAddUndeliverableAsAdminWithNotExistRegion() throws Exception {
        InternalReturnObject<RegionPo> ret = new InternalReturnObject<>();
        ret.setErrno(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo());
        ret.setErrmsg("Region not found");

        doReturn(ret).when(regionMapper).findRegionById(9999L);
        // 模拟平台管理员请求添加不可送达地区，但传递了不存在的 regionId
        mockMvc.perform(MockMvcRequestBuilders.post("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 4L, 9999L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }

    @Test
    void testAddUndeliverableAsShopUser() throws Exception {
        // 模拟商铺用户请求添加不可送达地区（应返回权限不足）
        mockMvc.perform(MockMvcRequestBuilders.post("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 1L, 4L, 10L)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void testDeleteUndeliverableAsAdminSuccess() throws Exception {
        // 模拟平台管理员请求删除不可送达地区
        mockMvc.perform(MockMvcRequestBuilders.delete("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 4L, 483250L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    void testDeleteUndeliverableAsShopUser() throws Exception {
        // 模拟商铺用户请求删除不可送达地区（应返回权限不足）
        mockMvc.perform(MockMvcRequestBuilders.delete("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 1L, 4L, 483250L)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    void testDeleteUndeliverableAsAdminButNotExist() throws Exception {
        // 模拟平台管理员请求删除不存在的不可送达地区
        mockMvc.perform(MockMvcRequestBuilders.delete("/platforms/{shopId}/logistics/{id}/regions/{rid}/undeliverable", 0L, 4L, 1L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo()));
    }
}
