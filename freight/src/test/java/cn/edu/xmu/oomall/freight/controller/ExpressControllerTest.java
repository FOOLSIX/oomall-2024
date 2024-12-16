package cn.edu.xmu.oomall.freight.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.freight.FreightApplication;
import cn.edu.xmu.oomall.freight.dao.bo.Express;
import cn.edu.xmu.oomall.freight.dao.logistics.JTAdaptor;
import cn.edu.xmu.oomall.freight.dao.logistics.SFAdaptor;
import cn.edu.xmu.oomall.freight.dao.logistics.ZTOAdaptor;
import cn.edu.xmu.oomall.freight.dao.logistics.retObj.PostCreatePackageAdaptorDto;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = FreightApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class ExpressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private ZTOAdaptor ztoAdaptor;
    @MockBean
    private SFAdaptor sfAdaptor;
    @MockBean
    private JTAdaptor jtAdaptor;
    private static String adminToken;
    private static String shop1Token;
    private static String shop2Token;
    private final String CREATE_PACKAGE = "/internal/shops/{shopId}/packages";
    private final String GET_PACKAGE_BY_BILL_CODE = "/internal/shops/{shopId}/packages";
    private final String GET_PACKAGE_BY_ID = "/internal/packages/{id}";
    private final String CONFIRM_PACKAGE = "/internal/shops/{shopId}/packages/{id}/confirm";
    private final String CANCEL_PACKAGE = "/internal/shops/{shopId}/packages/{id}/cancel";
    private final String SEND_PACKAGE = "/internal/shops/{shopId}/packages/{id}/send";


    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        shop1Token = jwtHelper.createToken(1L, "shop1", 1L, 1, 3600);
        shop2Token = jwtHelper.createToken(1L, "shop1", 2L, 1, 3600);
    }

    /**
     * 创建成功，快乐路径
     */
    @Test
    public void testCreatePackage() throws Exception {
        PostCreatePackageAdaptorDto postCreatePackageAdaptorDto = new PostCreatePackageAdaptorDto(1, "202311");
        Mockito.when(ztoAdaptor.createPackage(Mockito.any(), Mockito.any())).thenReturn(postCreatePackageAdaptorDto);
        Mockito.when(jtAdaptor.createPackage(Mockito.any(), Mockito.any())).thenReturn(postCreatePackageAdaptorDto);
        Mockito.when(sfAdaptor.createPackage(Mockito.any(), Mockito.any())).thenReturn(postCreatePackageAdaptorDto);
        String body = "{\"shopLogisticId\": 1,\"sender\": {\"name\": \"oomall\",\"mobile\": \"1234\",\"regionId\": 5," +
                "\"address\": \"testAddress\"},\"delivery\": {\"name\": \"张三\",\"mobile\": \"4321\",\"regionId\": 6," +
                "\"address\": \"翻斗大街\"}}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CREATE_PACKAGE, 0)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.errno").value(ReturnNo.CREATED.getErrNo()));
    }

    /**
     * 根据id查询物流单
     */
    @Test
    public void testGetPackage() throws Exception {
        Express express = Express.builder().status((byte) 4).build();
        Mockito.when(ztoAdaptor.getPackage(Mockito.any(), Mockito.any())).thenReturn(express);
        Mockito.when(sfAdaptor.getPackage(Mockito.any(), Mockito.any())).thenReturn(express);
        Mockito.when(jtAdaptor.getPackage(Mockito.any(), Mockito.any())).thenReturn(express);
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_PACKAGE_BY_ID, 7)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shipper.name", is("李华")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.receiver.name", is("张三")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.billCode", is("202312141")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.logistics.name", is("顺丰快递")));
    }

    /**
     * 根据id查询物流单
     * Id不存在
     */
    @Test
    public void testGetPackageGivenIdNotExist() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_PACKAGE_BY_ID, 114514)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    /**
     * 根据billCode查询物流单
     */
    @Test
    public void testGetPackageByBillCode() throws Exception {
        String billCode = "202312141";
        Express express = Express.builder().status((byte) 4).build();
        Mockito.when(ztoAdaptor.getPackage(Mockito.any(), Mockito.any())).thenReturn(express);
        Mockito.when(sfAdaptor.getPackage(Mockito.any(), Mockito.any())).thenReturn(express);
        Mockito.when(jtAdaptor.getPackage(Mockito.any(), Mockito.any())).thenReturn(express);
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_PACKAGE_BY_BILL_CODE, 0)
                        .header("authorization", adminToken)
                        .param("billCode", billCode)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shipper.name", is("李华")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.receiver.name", is("张三")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.billCode", is("202312141")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.logistics.name", is("顺丰快递")));
    }

    /**
     * 根据billCode查询物流单
     * billCode不存在
     */
    @Test
    public void testGetPackageGivenBillCodeNotExist() throws Exception {
        String billCode = "114514";
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_PACKAGE_BY_BILL_CODE, 0)
                        .header("authorization", adminToken)
                        .param("billCode", billCode)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * 根据billCode查询物流单
     * 商铺不对
     */
    @Test
    public void testGetPackageGivenWrongUser() throws Exception {
        String billCode = "202312141";
        this.mockMvc.perform(MockMvcRequestBuilders.get(GET_PACKAGE_BY_BILL_CODE, 2)
                        .header("authorization", shop2Token)
                        .param("billCode", billCode)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * 通知揽件成功
     */
    @Test
    public void testSendPackage() throws Exception {
        String json = "{\"startTime\":\"2023-12-20T00:00:00\",\"endTime\":\"2023-12-25T00:00:00\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(SEND_PACKAGE, 1, 7)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * 通知揽件失败，不是自己店铺的
     */
    @Test
    public void testSendPackageGivenWrongUser() throws Exception {
        String json = "{\"startTime\":\"2023-12-20T00:00:00\",\"endTime\":\"2023-12-25T00:00:00\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(SEND_PACKAGE, 2, 7)
                        .header("authorization", shop2Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * 通知揽件失败，状态不对
     */
    @Test
    public void testSendPackageGivenWrongStatue() throws Exception {
        String json = "{\"startTime\":\"2023-12-20T00:00:00\",\"endTime\":\"2023-12-25T00:00:00\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(SEND_PACKAGE, 1, 8)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    /**
     * 取消运单
     */
    @Test
    public void testCancelPackage() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(CANCEL_PACKAGE, 1, 7)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * 取消运单
     * 状态不对
     */
    @Test
    public void testCancelPackageGiveWrongStatue() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(CANCEL_PACKAGE, 1, 8)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

    /**
     * 验收退回的快递合格
     */
    @Test
    public void testConfirmPackageReceive() throws Exception {
        String body = "{\"status\": \"1\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CONFIRM_PACKAGE, 1, 25)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    /**
     * 验收退回的快递不合格
     */
    @Test
    public void testConfirmPackageBroken() throws Exception {
        String body = "{\"status\": \"0\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CONFIRM_PACKAGE, 1, 25)
                        .header("authorization", shop1Token)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}
