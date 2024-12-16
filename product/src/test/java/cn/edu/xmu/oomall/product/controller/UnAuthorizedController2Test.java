package cn.edu.xmu.oomall.product.controller;


import cn.edu.xmu.javaee.core.model.InternalReturnObject;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;

import cn.edu.xmu.oomall.product.ProductTestApplication;

import cn.edu.xmu.oomall.product.mapper.openfeign.SearchMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.HandlerResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class UnAuthorizedController2Test {

    @Autowired
    private MockMvc mockMvc;
    private static final String PRODUCTS="/products/{id}";

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private ShopMapper shopMapper;

    @MockBean
    private SearchMapper searchMapper;

    JwtHelper jwtHelper = new JwtHelper();

    private static String adminToken;

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
    }

   /**
     * GET
     * TEST  GetProductByCategory GIVEN id=1 page=1 pageSize=10
     * 通过分类获取商品成功
     */
    @Test
    public void testGetProductByCategoryGivenSuccess()throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get("/subcategories/{id}/products",10)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page","1")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

   /**
     * GET
     * TEST  getProducts GIVEN shopId=1 barCode="xxx" name="xxx"
     * page=1 pageSize=10
     * 搜索商品成功
     * 增加Mock 调用elasticsearch模块的searchMapper,修正用例
     */
   @Test
    public void testGetProductSkuListGivenSuccess()throws Exception{
       List<Long> searchResult = new ArrayList<>();
       searchResult.add(1550L);
       InternalReturnObject<List<Long>> ret = new InternalReturnObject<>(ReturnNo.OK.getErrNo(), ReturnNo.OK.getMessage(), searchResult);
       Mockito.when(searchMapper.searchProductsByNameAndBarcodeAndShopId(Mockito.eq("欢乐家久宝桃罐头"), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(ret);

       this.mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("shopId","10")
                        .param("barCode","6924254673572")
                        .param("name","欢乐家久宝桃罐头")
                        .param("page","1")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

   /* *
     * GET
     * TEST  findProductById GIVEN id=150
     * 无对应id的商品 => 搜索商品失败
     */
    @Test
    public void testGetProductProdGivenFail()throws Exception{
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",150)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    /**
     * GET
     * TEST  findProductById GIVEN id=150
     *   搜索商品失败
     */
    @Test
    public void testGetProductProdGivenFail2()throws Exception{
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",150)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }
    /**
     * GET
     * TEST  findProductById GIVEN id=1550
     *   搜索商品成功
     */
    @Test
    public void testGetProductProdGivenSuccess()throws Exception{
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(3L);
        shop.setName("商铺10");
        retObj.setData(shop);
        InternalReturnObject<Template> retTeplate = new InternalReturnObject<>();
        retTeplate.setErrno(0);
        retTeplate.setErrmsg("成功");
        Template template = new Template();
        template.setId(19L);
        template.setName("运费模板啦啦啦");
        retTeplate.setData(template);
        Mockito.when(shopMapper.getShopById(Mockito.any())).thenReturn(retObj);
        Mockito.when(shopMapper.getTemplateById(Mockito.any(),Mockito.any())).thenReturn(retTeplate);

        this.mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS, 1551)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));


    }
    /**
     * GET
     * TEST  findProductById GIVEN id=1550
     *   搜索商品成功
     */
   @Test
    public void testGetOnsaleGivenFail()throws Exception{

        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/onsales/{id}",150)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());


    }
    /**
     * GET
     * TEST  findOnsaleById GIVEN id=600
     *   搜索onsale失败
     */
    @Test
    public void testGetOnsaleGivenFail2()throws Exception{
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",600)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());


    }
    /**
     * GET
     * TEST  findOnsaleById GIVEN id=1
     *   搜索onsale成功
     */
    @Test
    public void testGetOnsaleGivenSuccess()throws Exception{
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(3L);
        shop.setName("商铺10");
        retObj.setData(shop);
        InternalReturnObject<Template> retTeplate = new InternalReturnObject<>();
        retTeplate.setErrno(0);
        retTeplate.setErrmsg("成功");
        Template template = new Template();
        template.setId(1L);
        template.setName("运费模板啦啦啦");
        retTeplate.setData(template);
        Mockito.when(shopMapper.getShopById(Mockito.any())).thenReturn(retObj);
        Mockito.when(shopMapper.getTemplateById(Mockito.any(),Mockito.any())).thenReturn(retTeplate);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/onsales/{id}",2)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }



}