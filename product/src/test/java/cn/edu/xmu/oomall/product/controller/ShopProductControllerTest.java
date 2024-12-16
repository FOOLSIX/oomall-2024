package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.bo.OnSale;
import cn.edu.xmu.oomall.product.mapper.openfeign.ShopMapper;
import cn.edu.xmu.oomall.product.mapper.openfeign.po.Shop;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import cn.edu.xmu.oomall.product.service.listener.DeleteOnSaleConsumer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Rui Li
 * @task 2023-dgn2-007
 */
@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class ShopProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OnsaleService onsaleService;

    @MockBean
    private RedisUtil redisUtil;

    @MockBean
    private ShopMapper shopMapper;

    @MockBean
    private RocketMQTemplate rocketMQTemplate;

    private static String adminToken;
    private static String shopToken2;
    private static String shopToken4;
    private static String shopToken5;
    private static String shopToken8;

    private static final String ADMIN_ONSALES_ID = "/shops/{shopId}/onsales/{id}";
    private static final String ADMIN_PRODUCT_ID_ONSALES = "/shops/{shopId}/products/{id}/onsales";
    private static final String ADMIN_ONSALES_ID_CANCEL = "/shops/{shopId}/onsales/{id}/cancel";

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 0, 3600);
        shopToken2 = jwtHelper.createToken(1L, "甜蜜之旅", 2L, 1, 3600);
        shopToken4 = jwtHelper.createToken(1L, "努力向前", 4L, 1, 3600);
        shopToken5 = jwtHelper.createToken(1L, "坚持就是胜利", 5L, 1, 3600);
        shopToken8 = jwtHelper.createToken(1L, "商铺8", 8L, 1, 3600);
    }

    /**
     * 01 获取销售详情
     * 快乐路径-Redis中无缓存
     */
    @Test
    void testGetOnsaleByIdWhenNotExistCache() throws Exception{

        when(redisUtil.hasKey(eq("O18"))).thenReturn(false);

        // 模拟返回商户
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        Shop shop = new Shop();
        shop.setId(5L);
        shop.setName("坚持就是胜利");
        shop.setStatus((byte)1);
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        retObj.setData(shop);

        when(shopMapper.getShopById(5L)).thenReturn(retObj);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_ONSALES_ID, 5, 18)
                        .header("authorization",  shopToken5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shop.name", is("坚持就是胜利")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.name", is("六神花露水")));
    }

    /**
     * 01 获取销售详情
     * 快乐路径-Redis中有缓存
     */
    @Test
    void testGetOnsaleByIdWhenExistCache() throws Exception{


        // 模拟返回商户
        InternalReturnObject<Shop> retObj = new InternalReturnObject<>();
        Shop shop = new Shop();
        shop.setId(8L);
        shop.setName("商铺8");
        shop.setStatus((byte)2);
        retObj.setErrno(0);
        retObj.setErrmsg("成功");
        retObj.setData(shop);
        when(shopMapper.getShopById(8L)).thenReturn(retObj);


        // 模拟Redis缓存
        OnSale onSale = new OnSale();
        onSale.setId(5L);
        onSale.setShopId(8L);
        onSale.setProductId(1554L);
        onSale.setPrice(3280L);
        when(redisUtil.hasKey(eq("O5"))).thenReturn(true);
        when(redisUtil.get(eq("O5"))).thenReturn(onSale);


        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_ONSALES_ID, 8, 5)
                        .header("authorization",  shopToken8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shop.name", is("商铺8")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.name", is("黑金刚巧力")));
    }

    /**
     * 01 获取销售详情
     * 快乐路径-无缓存且销售不属于当前商户
     */
    @Test
    void testGetOnsaleByIdWhenOnsaleNotBelongsToThisShop() throws Exception{

        when(redisUtil.hasKey(eq("O18"))).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_ONSALES_ID, 8, 18)
                        .header("authorization",  shopToken8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }



    /**
     * 01 获取销售详情
     * 意外路径-Redis中有缓存，但是不属于当前商户
     */
    @Test
    void testGetOnsaleByIdWhenExistCacheButNotBelongsToThisShop() throws Exception{

        // 模拟Redis缓存
        OnSale onSale = new OnSale();
        onSale.setId(5L);
        onSale.setShopId(8L);
        onSale.setProductId(1554L);
        onSale.setPrice(3280L);
        when(redisUtil.hasKey(eq("O5"))).thenReturn(true);
        when(redisUtil.get(eq("O5"))).thenReturn(onSale);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_ONSALES_ID, 5, 5)
                        .header("authorization",  shopToken5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * 01 获取销售详情
     * 意外路径-销售不存在
     */
    @Test
    void testGetOnsaleByIdWhenOnsaleNotExist() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_ONSALES_ID, 0, 9999)
                        .header("authorization",  adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    /**
     * 02 新增销售
     * 快乐路径
     */
    @Test
    void testAddOnsale() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2028-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2028-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"payTime\": \"2028-12-05T12:30:00\",\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 5, 1555)
                        .header("authorization",  shopToken5)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())));
    }

    /**
     * 02 新增销售
     * 快乐路径-尾款支付时间为空
     */
    @Test
    void testAddOnsaleGivenBeginTimeNull() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2028-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2028-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 5, 1555)
                        .header("authorization",  shopToken5)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())));
    }

    /**
     * 02 新增销售
     * 意外路径-时间冲突
     */
    @Test
    void testAddOnsaleWhenTimeConflict() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2023-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2023-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"payTime\": \"2023-12-05T12:30:00\",\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 5, 1555)
                        .header("authorization",  shopToken5)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.GOODS_ONSALE_CONFLICT.getErrNo())));
    }

    /**
     * 02 新增销售
     * 意外路径-开始时间晚于结束时间
     */
    @Test
    void testAddOnsaleWhenBeginTimeIsAfterEndTime() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2023-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2022-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"payTime\": \"2023-12-05T12:30:00\",\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 5, 1555)
                        .header("authorization",  shopToken5)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.LATE_BEGINTIME.getErrNo())));
    }

    /**
     * 02 新增销售
     * 意外路径-尾款时间早于开始时间
     */
    @Test
    void testAddOnsaleWhenPayTimeIsBeforeBeginTime() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2023-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2023-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"payTime\": \"2022-12-05T12:30:00\",\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 5, 1555)
                        .header("authorization",  shopToken5)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.ADV_SALE_TIMEEARLY.getErrNo())));
    }

    /**
     * 02 新增销售
     * 意外路径-尾款时间晚于于结束时间
     */
    @Test
    void testAddOnsaleWhenPayTimeIsAfterEndTime() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2023-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2023-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"payTime\": \"2024-12-05T12:30:00\",\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 5, 1555)
                        .header("authorization",  shopToken5)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.ADV_SALE_TIMELATE.getErrNo())));
    }

    /**
     * 02 新增销售
     * 意外路径-对应产品不存在
     */
    @Test
    void testAddOnsaleWhenProductNotExist() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2028-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2028-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"payTime\": \"2028-12-05T12:30:00\",\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 5, 9999)
                        .header("authorization",  shopToken5)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())));
    }

    /**
     * 02 新增销售
     * 意外路径-商户越权访问
     */
    @Test
    void testAddOnsaleGivenWrongToken() throws Exception{

        String requestBody = "{\n" +
                "  \"price\": 50,\n" +
                "  \"quantity\": 10,\n" +
                "  \"maxQuantity\": 5,\n" +
                "  \"beginTime\": \"2028-12-01T08:00:00\",\n" +
                "  \"endTime\": \"2028-12-15T18:00:00\",\n" +
                "  \"deposit\": 10,\n" +
                "  \"payTime\": \"2028-12-05T12:30:00\",\n" +
                "  \"type\": 1\n" +
                "}\n";

        this.mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_PRODUCT_ID_ONSALES, 8, 1555)
                        .header("authorization",  shopToken8)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    /**
     * 03 获取所有销售
     * 快乐路径
     */
    @Test
    void testGetAllOnsale() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_PRODUCT_ID_ONSALES, 4, 1552)
                        .header("authorization",  shopToken4)
                        .param("page", "1")
                        .param("pageSize", "100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '3')].price", hasItem(12650)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[?(@.id == '8569')].price", hasItem(2000)));

    }

    /**
     * 03 获取所有销售
     * 意外路径-商户越权访问
     */
    @Test
    void testGetAllOnsaleGivenWrongToken() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_PRODUCT_ID_ONSALES, 8, 1552)
                        .header("authorization",  shopToken8)
                        .param("page", "1")
                        .param("pageSize", "100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));

    }

    /**
     * 04 删除销售
     * 快乐路径-send-delete-onsale-topic消息发送成功
     */
    @Test
    void testDelOnsaleIdWhenSendMessage() throws Exception{
        when(redisUtil.hasKey(eq("O845"))).thenReturn(false);

        // 模拟发送成功
        doAnswer(invocation -> {
            SendCallback callback = invocation.getArgument(2);
            callback.onSuccess(mock(SendResult.class));
            return null;
        }).when(rocketMQTemplate).asyncSend(eq("send-delete-onsale-topic"), any(Message.class), any(SendCallback.class));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_ONSALES_ID, 4, 845)
                        .header("authorization",  shopToken4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

        // 验证是否发送
        verify(rocketMQTemplate).asyncSend(eq("send-delete-onsale-topic"), any(Message.class), any(SendCallback.class));
    }

    /**
     * 04 删除销售
     * 意外路径-send-delete-onsale-topic消息发送异常
     */
    @Test
    void testDelOnsaleIdWhenSendMessageAndSendFailed() throws Exception {
        when(redisUtil.hasKey(eq("O845"))).thenReturn(false);

        // 模拟发送异常
        doAnswer(invocation -> {
            SendCallback callback = invocation.getArgument(2);
            callback.onException(new RuntimeException("Simulated exception"));
            return null;
        }).when(rocketMQTemplate).asyncSend(eq("send-delete-onsale-topic"), any(Message.class), any(SendCallback.class));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADMIN_ONSALES_ID, 4, 845)
                        .header("authorization", shopToken4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

        // 验证是否发送
        verify(rocketMQTemplate).asyncSend(eq("send-delete-onsale-topic"), any(Message.class), any(SendCallback.class));
    }

    /**
     * 04 删除销售
     * 快乐路径-DeleteOnSaleConsumer接受到reply-del-onsale-topic消息并删除成功
     */
    @Test
    void testDelOnsaleIdWhenReceiveMessage() {

        // 模拟消息内容
        Long onSaleId = 845L;
        String packStr = JacksonUtil.toJson(onSaleId);
        byte[] packBytes = packStr.getBytes(StandardCharsets.UTF_8);  // 将字符串转换为字节数组
        UserDto user = UserDto.builder().id(1L).name("努力向前").departId(4L).build();

        // 构造消息
        Message message = MessageBuilder.withPayload(packBytes)
                .setHeader("user", user)
                .build();

        DeleteOnSaleConsumer deleteOnSaleConsumer = new DeleteOnSaleConsumer(onsaleService);

        // 模拟 RocketMQTemplate 的异步发送成功
        doAnswer(invocation -> {
            SendCallback callback = invocation.getArgument(2);
            callback.onSuccess(mock(SendResult.class));
            return null;
        }).when(rocketMQTemplate).asyncSend(eq("reply-del-onsale-topic"), any(Message.class), any(SendCallback.class));

        deleteOnSaleConsumer.onMessage(message);
    }

    /**
     * 05 取消销售
     * 快乐路径-活动进行中
     */
    @Test
    void testCancelOnsaleIdGivenOngoing() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_ONSALES_ID_CANCEL, 4, 845)
                        .header("authorization",  shopToken4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

    }

    /**
     * 05 取消销售
     * 快乐路径-活动未开始
     */
    @Test
    void testCancelOnsaleIdGivenNotStarted() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_ONSALES_ID_CANCEL, 4, 8569)
                        .header("authorization",  shopToken4)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

    }

    /**
     * 05 取消销售
     * 快乐路径-活动已结束
     */
    @Test
    void testCancelOnsaleIdGivenFinished() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_ONSALES_ID_CANCEL, 2, 344)
                        .header("authorization",  shopToken2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));

    }

    /**
     * 05 取消销售
     * 意外路径-商户越权访问
     */
    @Test
    void testCancelOnsaleIdGivenWrongToken() throws Exception{

        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_ONSALES_ID_CANCEL, 8, 845)
                        .header("authorization",  shopToken8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));

    }
}
