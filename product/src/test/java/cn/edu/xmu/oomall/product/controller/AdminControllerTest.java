package cn.edu.xmu.oomall.product.controller;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;

import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;

import cn.edu.xmu.oomall.product.ProductTestApplication;

import cn.edu.xmu.oomall.product.mapper.openfeign.FreightMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Freight;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Logistics;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static cn.edu.xmu.javaee.core.model.Constants.MAX_RETURN;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private ShopMapper shopMapper;

    @MockBean
    private FreightMapper freightMapper;

    JwtHelper jwtHelper = new JwtHelper();

    private static String adminToken;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
    }
    /**
     * GET
     * test GETProductId Given shopId=1 id=10000
     * 无法找到商品
     */
@Test
    public void testGETProductIdGivenNotFound() throws Exception {
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/products/{id}", 1, 10000)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));

    }
    /**
     * GET
     * test GETProductId Given shopId=100 id=10550
     * 无法找到商品
     */
    @Test
    public void testGETProductIdGivenNotFound2() throws Exception {
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/products/{id}", 100, 10550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));

    }
    /**
     * GET
     * test GETProductId Given shopId=10 id=1550
     * 找到商品
     */
  @Test
    public void testGETProductIdGivenFound() throws Exception {
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(3L);
        shop.setName("商铺10");
        retObj.setData(shop);
        InternalReturnObject<Template> retTemplate = new InternalReturnObject<>();
        retTemplate.setErrno(0);
        retTemplate.setErrmsg("成功");
        Template template = new Template();
        template.setId(19L);
        template.setName("运费模板啦啦啦");
        retTemplate.setData(template);
        InternalReturnObject<Freight> retFreight = new InternalReturnObject<>();
        retFreight.setErrno(0);
        retFreight.setErrmsg("成功sss");
        Freight freight = new Freight();
        freight.setId(0L);
        List<Logistics> logistics=new ArrayList<>();
        logistics.add(new Logistics(1L,"顺丰"));
        logistics.add(new Logistics(2L,"京东"));
        freight.setLogistics(logistics);
        retFreight.setData(freight);
        Mockito.when(freightMapper.getAllLogisticsById(10L,0, MAX_RETURN)).thenReturn(retFreight);
        Mockito.when(shopMapper.getShopById(3L)).thenReturn(retObj);
        Mockito.when(shopMapper.getTemplateById(3L,19L)).thenReturn(retTemplate);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/products/{id}", 3, 1551)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
    /**
     * PUT
     * TEST putProductId GIVEN shopID=10 id=1200
     * 商品不存在 - 无法修改商品
*/
  @Test
    public void testPutProductIdGivenFail()throws Exception{
        String body = "{\"skuSn\":\"1215548\", \"weight\":123, \"barCode\":\"1265166\", \"shopLogisticsId\":123,\"templateId\":39, \"freeThreshold\":3953}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}",10,1200)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

    }
    /**
     * PUT
     * TEST putProductId GIVEN shopID=0 id=1550
     * 修改成功
*/
    @Test
    public void testPutProductGivenSuccess()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        String body = "{\"skuSn\":\"1215548\", \"weight\":123, \"barCode\":\"1265166\", \"shopLogisticsId\":123,\"templateId\":39, \"freeThreshold\":3953}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}",0,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.OK.getErrNo())));

    }

    /**
     * PUT
     * TEST putProductCommissionRatio GIVEN shopID=10 id=1550
     * 修改分账比例成功
     *  更新url,修正用例
     *  @Author 37220222203851
     */
   @Test
    public void testPutProductCommissionGivenFail()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        String body = "{\"commissionRatio\":1}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/products/{id}/commissionratio",10,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

    }

    /**
     * PUT
     * TEST putProductCommissionRatio GIVEN shopID=10 id=1550
     * 修改分账比例成功
     * 更新url,修正用例
     * @Author 37220222203851
     */
    @Test
    public void testPutProductCommissionGivenSuccess()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        String body = "{\"commissionRatio\":1}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/products/{id}/commissionratio",0,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    /**
     * PUT
     * TEST  relateProductId GIVEN shopID=10 id=1550 body{"productId" = 1562}
     * 将两个商品设为相关成功
     */
    @Test
    public void testRelateProductGivenSuccess()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        String body = "{\"productId\":1562}";
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}",10,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.OK.getErrNo())));

    }
    /**
     * PUT
     * TEST  relateProductId GIVEN shopID=10 id=1550
     * 将商品的相关删除成功
     */
    @Test
    public void testDeleteRelateProductGivenSuccess()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/products/{id}/relations",10,1550)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.OK.getErrNo())));

    }

    /**
     * PUT
     * TEST  AllowGoods GIVEN shopID=10 id=1551
     * 解禁商品失败 - 解禁与封禁商品只能由管理员提出 ShopId=0
     */
    @Test
    public void testAllowProductGivenFail()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}/allow",3,1551)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }
   /**
     * PUT
     * TEST  ProhibitProduct GIVEN shopID=0 id=1550
     * 解禁商品成功
     *  更新url,修正用例
     *  @Author 37220222203851
     */
    @Test
    public void testAllowProductGivenSuccess()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/products/{id}/allow",0L,1551L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    /**
     * PUT
     * TEST  AllowGoods GIVEN shopID=0 id=1550
     * 封禁商品失败 - 解禁与封禁商品只能由管理员提出 ShopId=0
     */
    @Test
    public void testProhibitProductGivenFail()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/products/{id}/prohibit",10,1550L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

   /**
     * PUT
     * TEST  AllowGoods GIVEN shopID=10 id=1550
     * 禁用商品成功
     *  修正用例
     *  @Author 37220222203851
     */
  @Test
    public void testProhibitProductGivenSuccess()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/platforms/{shopId}/products/{id}/prohibit",0L,1551L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    /**
     * PUT
     * TEST  AllowGoods GIVEN shopID=0 id=1550
     * 解禁商品成功
     */
    @Test
    public void testGetProductByTemplateGivenSuccess()throws Exception{
        Mockito.when(redisUtil.hasKey("P1550")).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/templates/{id}/products",10,1,1,10)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
    /**
     * GET
     * TEST  GetProductByLogistics GIVEN shopID=10 logisticsId=1 page=1 pageSize=10
     * 通过特殊物流获取商品成功
     */
    @Test
    public void testGetProductByLogisticsGivenSUCCESS()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/10/shoplogistics/1/products")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page","1")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}







