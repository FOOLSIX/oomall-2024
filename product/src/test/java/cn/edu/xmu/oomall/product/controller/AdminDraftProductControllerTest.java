package cn.edu.xmu.oomall.product.controller;

import java.util.Objects;

import cn.edu.xmu.oomall.product.mapper.openfeign.SearchMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.ProductEs;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.transaction.annotation.Propagation;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.controller.dto.ProductDraftDto;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author huang zhong
 * @task 2023-dgn2-005
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class AdminDraftProductControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private ShopMapper shopMapper;
    @MockBean
    private SearchMapper searchMapper;

    ObjectMapper objectMapper = new ObjectMapper();

    final String NEW_DRAFT = "/shops/{shopId}/draftproducts";
    final String FIND_DRAFT = "/shops/{shopId}/draftproducts";
    final String DELETE_DRAFT = "/shops/{shopId}/draftproducts/{id}";
    final String MODIFY_DRAFT = "/shops/{shopId}/draftproducts/{id}";
    final String FIND_DRAFT_BY_ID = "/shops/{shopId}/draftproducts/{id}";

    static String adminToken;
    static String shop1;
    static String shop10;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
        shop1 = jwtHelper.createToken(1L, "1", 1L, 1, 3600);
        shop10 = jwtHelper.createToken(10L, "1", 10L, 1, 3600);

    }

    @Test
    public void testCreateDraftWhenSuccess() throws Exception {
        ProductDraftDto vo = new ProductDraftDto();
        vo.setCategoryId(313l);
        vo.setName("test1");
        vo.setOriginPlace("100");
        vo.setOriginalPrice(1000l);
        vo.setUnit("unit");
        mockMvc.perform(MockMvcRequestBuilders.post(NEW_DRAFT, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("test1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.originPlace", is("100")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.originalPrice", is(1000)));
    }

    @Test
    public void testCreateDraftWhenCategoryIdIsFirst() throws Exception {
        ProductDraftDto vo = new ProductDraftDto();
        vo.setCategoryId(1l);
        vo.setName("test1");
        vo.setOriginPlace("100");
        vo.setOriginalPrice(1000l);
        vo.setUnit("unit");
        mockMvc.perform(MockMvcRequestBuilders.post(NEW_DRAFT, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CATEGORY_NOTALLOW.getErrNo())));
    }

    @Test
    public void testCreateDraftWhenCategoryIdIsNull() throws Exception {
        ProductDraftDto vo = new ProductDraftDto();
        vo.setCategoryId(null);
        vo.setName("test1");
        vo.setOriginPlace("100");
        vo.setOriginalPrice(1000l);
        vo.setUnit("unit");
        mockMvc.perform(MockMvcRequestBuilders.post(NEW_DRAFT, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.FIELD_NOTVALID.getErrNo())));
    }

    @Test
    public void testGetAllProductDraftWhenAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FIND_DRAFT, 0)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(70)));
    }

    @Test
    public void testGetAllProductDraftWhenShop() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FIND_DRAFT, 1)
                        .header("authorization", shop1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(0)));
    }

    @Test
    public void testGetProductDraftWhenSuccess() throws Exception {
        InternalReturnObject internalReturnObject = new InternalReturnObject<>();
        Shop shop = new Shop();
        shop.setId(10l);
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setData(shop);
        Mockito.when(shopMapper.getShopById(10l)).thenReturn(internalReturnObject);
        mockMvc.perform(MockMvcRequestBuilders.get(FIND_DRAFT_BY_ID, 10, 70)
                        .header("authorization", shop10)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(70)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shop.id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id", is(1576)));

    }

    @Test
    public void testGetProductDraftWhenAdmin() throws Exception {
        InternalReturnObject internalReturnObject = new InternalReturnObject<>();
        Shop shop = new Shop();
        shop.setId(10l);
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setData(shop);
        Mockito.when(shopMapper.getShopById(10l)).thenReturn(internalReturnObject);
        mockMvc.perform(MockMvcRequestBuilders.get(FIND_DRAFT_BY_ID, 0, 70)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(70)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shop.id", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id", is(1576)));
    }

    @Test
    public void testGetProductDraftWhenOther() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FIND_DRAFT_BY_ID, 1, 70)
                        .header("authorization", shop1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    public void testModifyDraftWhenSuccess() throws Exception {
        ProductDraftDto vo = new ProductDraftDto();
        vo.setCategoryId(313l);
        vo.setName("test1");
        vo.setOriginPlace("100");
        vo.setOriginalPrice(1000l);
        vo.setUnit("unittest");
        mockMvc.perform(MockMvcRequestBuilders.put(MODIFY_DRAFT, 10, 70)
                        .header("authorization", shop10)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testModifyDraftWhenCategoryIdIsFirst() throws Exception {
        ProductDraftDto vo = new ProductDraftDto();
        vo.setCategoryId(1l);
        vo.setName("test1");
        vo.setOriginPlace("100");
        vo.setOriginalPrice(1000l);
        vo.setUnit("unit");
        mockMvc.perform(MockMvcRequestBuilders.put(MODIFY_DRAFT, 10, 70)
                        .header("authorization", shop10)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CATEGORY_NOTALLOW.getErrNo())));
    }

    @Test
    public void testModifyDraftWhenAdmin() throws Exception {
        ProductDraftDto vo = new ProductDraftDto();
        vo.setCategoryId(313l);
        vo.setName("test1");
        vo.setOriginPlace("100");
        vo.setOriginalPrice(1000l);
        vo.setUnit("unit");
        mockMvc.perform(MockMvcRequestBuilders.put(MODIFY_DRAFT, 0, 70)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testModifyDraftWhenOther() throws Exception {
        ProductDraftDto vo = new ProductDraftDto();
        vo.setCategoryId(313l);
        vo.setName("test1");
        vo.setOriginPlace("100");
        vo.setOriginalPrice(1000l);
        vo.setUnit("unit");
        mockMvc.perform(MockMvcRequestBuilders.put(MODIFY_DRAFT, 1, 70)
                        .header("authorization", shop1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    public void testDelProductsWhenSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DRAFT, 10, 70)
                        .header("authorization", shop10)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testDelProductsWhenAdmin() throws Exception {
        InternalReturnObject internalReturnObject = new InternalReturnObject<>();
        Shop shop = new Shop();
        shop.setId(10l);
        internalReturnObject.setErrno(ReturnNo.OK.getErrNo());
        internalReturnObject.setData(shop);
        Mockito.when(shopMapper.getShopById(10l)).thenReturn(internalReturnObject);
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DRAFT, 0, 70)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testDelProductsWhenOther() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_DRAFT, 1, 70)
                        .header("authorization", shop1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }
    /**
     * POST
     * TEST  Apply GIVEN shopId=100 id=1550
     * "name":"mmm", "originalPrice":123, "categoryId":190, "originPlace":"England", "unit":"England"
     *   申请修改商品信息失败
     */
    @Test
    public void testApplyDraftGivenFail() throws Exception {
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);
        String body = "{\"name\":\"mmm\", \"originalPrice\":123, \"categoryId\":190, \"originPlace\":\"England\", \"unit\":\"England\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}",100,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    /**
     * POST
     * TEST  Apply GIVEN shopId=100 id=1550
     * "name":"mmm", "originalPrice":123, "categoryId":190, "originPlace":"England", "unit":"England
     *   申请修改商品信息失败
     */
    @Test
    public void testApplyDraftGivenFail2() throws Exception {
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);
        String body = "{\"name\":\"mmm\", \"originalPrice\":123, \"categoryId\":190, \"originPlace\":\"England\", \"unit\":\"England\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}",100,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    /**
     * POST
     * TEST  Apply GIVEN shopId=100 id=1550
     * "name":"mmm", "originalPrice":123, "categoryId":12, "originPlace":"England", "unit":"England"
     *   申请修改商品信息成功
     */
    @Test
    public void testApplyDraftGivenSuccess() throws Exception {
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"name\":\"mmm\", \"originalPrice\":123, \"categoryId\":12, \"originPlace\":\"England\", \"unit\":\"England\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}",10,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    /**
     * POST
     * TEST  PublicProduct GIVEN shopId=10 id=700
     *  "commissionRatio":1
     *   发布商品失败  - 只有管理员可以发布商品
     */
    @Test
    public void testPublicProductGivenFail() throws Exception {

        String body = "{\"commissionRatio\":1}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/draftproducts/{id}/publish",10,700)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
    /**
     * POST
     * TEST  PublicProduct GIVEN shopId=0 id=700
     *  "commissionRatio":1
     *   发布商品成功
     *  修正用例
     *  @Author 37220222203851
     *
     */
    @Test
    public void testPublicProductGivenSuccess() throws Exception {
        Mockito.when(searchMapper.saveOrUpdateProduct(Mockito.any(ProductEs.class))).thenReturn(null);

        String body = "{\"commissionRatio\":1}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/draftproducts/{id}/publish",0,70)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
