package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;

/**
 * @author wuzhicheng
 * @create 2022-12-03 22:12
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class UnAuthorizedControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

    JwtHelper jwtHelper = new JwtHelper();

    private static String adminToken;

    private static final String STATES="/products/states";
    private static final String PRODUCTS="/products/{id}";
    private static final String ONSALES_PRODUCTS="/onsales/{id}";


    @BeforeAll
    public static void setup(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 0, 3600);
    }

    //获得所有的货品状态
    @Test
    public void getStatesTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get(STATES)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
                ////.andDo(MockMvcResultHandlers.print());
    }

    //获得product指定onsale的历史信息
    @Test
    public void getOnsaleTest1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ONSALES_PRODUCTS, "5117")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }

    //获得product指定onsale的历史信息，商品不存在
    @Test
    public void getOnsaleTest2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ONSALES_PRODUCTS, "6000")
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
                //.andDo(MockMvcResultHandlers.print());
    }
    @MockBean
    private ShopMapper shopMapper;
    @Test
    public void testFindProductById() throws Exception{
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
        Mockito.when(shopMapper.getShopById(3L)).thenReturn(retObj);
        Mockito.when(shopMapper.getTemplateById(3L,19L)).thenReturn(retTeplate);

        this.mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS, 1551)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

    }


        /**
     * @Author 37220222203612
     */
    @Test
    void testGetCouponActProduct() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/activities/{id}/onsales", 4L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", is("肖家白胡椒粉30")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", is(1668)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].price", is(40058)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantity", is(34)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}
