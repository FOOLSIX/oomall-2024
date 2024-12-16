package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.controller.dto.OnSaleDto;
import cn.edu.xmu.oomall.product.controller.dto.RelateProductDto;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author wuzhicheng
 * @create 2022-12-04 19:44
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ShopProductController2Test {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    JwtHelper jwtHelper = new JwtHelper();

    private static String adminToken;

    private static final String ALLOW_GOODS="/platforms/{shopId}/products/{id}/allow";

    private static final String PROHIBIT_GOODS="/platforms/{shopId}/products/{id}/prohibit";

    private static final String DRAFT="/shops/{shopId}/draftproducts";

    private static final String DRAFT_DET="/shops/{shopId}/draftproducts/{id}";

    private static final String RELATE_PRODUCT="/shops/{shopId}/products/{id}/relations";


    @MockBean
    private ShopMapper shopMapper;


    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
    }

    /**
     * 测试异常1
     * @throws Exception 代码299
     */
    @Test
    public void addOnsaleTest2()throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        retObj.setData(shop);

        Mockito.when(shopMapper.getShopById(10L)).thenReturn(retObj);
        OnSaleDto vo = new OnSaleDto();
        vo.setPrice(1L);
        vo.setBeginTime(LocalDateTime.parse("2022-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        vo.setEndTime(LocalDateTime.parse("2023-11-09T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        vo.setType(Byte.valueOf("0"));
        vo.setMaxQuantity(50);
        vo.setQuantity(2000);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/10/products/1550/onsales")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.GOODS_ONSALE_CONFLICT.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试异常2
     * @throws Exception RESOURCE_ID_NOTEXIST
     */
    @Test
    public void addOnsaleTest3()throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        retObj.setData(shop);

        Mockito.when(shopMapper.getShopById(10L)).thenReturn(retObj);
        OnSaleDto vo = new OnSaleDto();
        vo.setPrice(1L);
        vo.setBeginTime(LocalDateTime.parse("2030-11-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        vo.setEndTime(LocalDateTime.parse("2031-11-09T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        vo.setType(Byte.valueOf("0"));
        vo.setMaxQuantity(50);
        vo.setQuantity(2000);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/10/products/111550/onsales")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(Objects.requireNonNull(JacksonUtil.toJson(vo))))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 正常获取
     * @throws Exception
     */
    @Test
    public void getAllSalesTest1() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        retObj.setData(shop);

        Mockito.when(shopMapper.getShopById(10L)).thenReturn(retObj);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/10/products/1550/onsales")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page","0")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 测试异常
     * @throws Exception RESOURCE_ID_NOTEXIST
     */
    @Test
    public void getAllSalesTest2() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        retObj.setData(shop);

        Mockito.when(shopMapper.getShopById(10L)).thenReturn(retObj);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/10/products/1522350/onsales")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page","0")
                        .param("pageSize","10"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

      /**
     * 测试正常商品下线 endTime<now
     * 更新endTime的值
     * @throws Exception
     */
    @Test
    public void delOnsaleIdTest2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        retObj.setData(shop);

        Mockito.when(shopMapper.getShopById(4L)).thenReturn(retObj);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/onsales/{id}",4,19)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }


    /**
     * @throws Exception
     */
    @Test
    public void delOnsaleIdTest3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        retObj.setData(shop);

        Mockito.when(shopMapper.getShopById(9L)).thenReturn(retObj);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/4/onsales/14")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    /**
     * 异常情况处理
     * @throws Exception
     */
    @Test
    public void delOnsaleIdTest4() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(redisUtil.bfExist(Mockito.anyString(), (Long) Mockito.any())).thenReturn(false);
        Mockito.when(redisUtil.bfAdd(Mockito.anyString(), Mockito.any())).thenReturn(true);
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        retObj.setData(shop);

        Mockito.when(shopMapper.getShopById(10L)).thenReturn(retObj);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/10/onsales/23456")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //解禁商品失败，没有权限
    @Test
    public void allowGoodsTest2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put(ALLOW_GOODS, "1", "5107")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //封禁商品失败，没有权限
    @Test
    public void prohibitGoodsTest2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put(PROHIBIT_GOODS, "1", "5108")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //店家查看草稿，平台管理员
    @Test
    public void getAllProductDraftTest1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        Mockito.when(shopMapper.getShopById(10L)).thenReturn(new InternalReturnObject<>(shop));

        this.mockMvc.perform(MockMvcRequestBuilders.get(DRAFT, "0")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //店家查看草稿
    @Test
    public void getAllProductDraftTest2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Shop shop = new Shop();
        shop.setId(10L);
        shop.setName("商铺10");
        Mockito.when(shopMapper.getShopById(10L)).thenReturn(new InternalReturnObject<>(shop));
        this.mockMvc.perform(MockMvcRequestBuilders.get(DRAFT, "10")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //店家查看草稿详情，草稿不存在
    @Test
    public void getProductDraftTest2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get(DRAFT_DET, "10", "80")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //店家查看草稿详情，没有权限
    @Test
    public void getProductDraftTest3() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get(DRAFT_DET, "1", "70")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //关联商品失败，商品不存在
    @Test
    public void relateProductIdTest2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        RelateProductDto body = new RelateProductDto();
        body.setProductId(5108555L);

        this.mockMvc.perform(MockMvcRequestBuilders.post(RELATE_PRODUCT, "10", "5107")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JacksonUtil.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //关联商品失败，没有权限
    @Test
    public void relateProductIdTest3() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        RelateProductDto body = new RelateProductDto();
        body.setProductId(5108L);
        this.mockMvc.perform(MockMvcRequestBuilders.post(RELATE_PRODUCT, "11", "5107")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(JacksonUtil.toJson(body)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //取消关联失败，没有权限
    @Test
    public void delRelateProductTest2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(RELATE_PRODUCT, "11", "5211")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "2261"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //取消关联失败，商品不存在
    @Test
    public void delRelateProductTest3() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(RELATE_PRODUCT, "10", "52111")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("productId", "2261"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }
}
