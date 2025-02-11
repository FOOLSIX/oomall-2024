package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.activity.CouponActDao;
import cn.edu.xmu.oomall.product.dao.bo.Activity;
import cn.edu.xmu.oomall.product.dao.bo.CouponAct;
import cn.edu.xmu.oomall.product.mapper.jpa.ActivityPoMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Template;
import cn.edu.xmu.oomall.product.mapper.po.ActivityPo;
import net.bytebuddy.dynamic.DynamicType;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doReturn;

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
    @SpyBean
    private ActivityPoMapper activityPoMapper;
    JwtHelper jwtHelper = new JwtHelper();

    private static String adminToken;

    private static final String STATES="/products/states";
    private static final String PRODUCTS="/products/{id}";
    private static final String ONSALES_PRODUCTS="/onsales/{id}";
    @Autowired
    private CouponActDao couponActDao;


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
     * @Author 37720222205040
     * 在ActivityDao中测试获取List时Redis命中，获取Activity时Redis不命中,在Mapper查找时不为空
     */
    @Test
    public void testFindProductByIdWithoutRedis() throws Exception{
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
        ArrayList<Long> list = new ArrayList<>();
        list.add(98L);
        Mockito.when(redisUtil.get("ACTIVITYLIST2")).thenReturn(list);
        this.mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS, 1551)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

    }


    /**
     * @Author 37720222205040
     * 在ActivityDao中测试获取List时Redis命中，获取Activity时Redis不命中,且在Mapper查找时为空
     */
    @Test
    public void testFindProductByIdActivityIsNull() throws Exception{
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
        Optional<ActivityPo> activityPo=Optional.empty();
        Mockito.when(shopMapper.getShopById(3L)).thenReturn(retObj);
        Mockito.when(shopMapper.getTemplateById(3L,19L)).thenReturn(retTeplate);
        doReturn(activityPo).when(activityPoMapper).findById(98L);
        ArrayList<Long> list = new ArrayList<>();
        list.add(98L);
        Mockito.when(redisUtil.get("ACTIVITYLIST2")).thenReturn(list);
        this.mockMvc.perform(MockMvcRequestBuilders.get(PRODUCTS, 1551)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

    }

    /**
     * @Author 37720222205040
     * 在ActivityDao中测试获取List时Redis命中，获取Activity时Redis也命中
     */
    @Test
    public void testFindProductByIdWithRedis() throws Exception{
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
        ArrayList<Long> list = new ArrayList<>();
        list.add(98L);
        CouponAct activity=new CouponAct();
        activity.setId(98L);
        activity.setCreatorId(1L);
        activity.setCreatorName("admin");
        activity.setShopId(3L);
        activity.setObjectId("6573028729d39517cce1e03b");
        activity.setActClass("couponActDao");
        Mockito.when(shopMapper.getShopById(3L)).thenReturn(retObj);
        Mockito.when(shopMapper.getTemplateById(3L,19L)).thenReturn(retTeplate);
        Mockito.when(redisUtil.get("ACTIVITYLIST2")).thenReturn(list);
        Mockito.when(redisUtil.get("A98")).thenReturn(activity);
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
