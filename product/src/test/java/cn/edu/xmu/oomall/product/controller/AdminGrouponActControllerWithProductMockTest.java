/*
package cn.edu.xmu.oomall.product.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.dao.ProductDao;
import cn.edu.xmu.oomall.product.dao.bo.GrouponAct;
import cn.edu.xmu.oomall.product.model.Threshold;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = ProductTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class AdminGrouponActControllerWithProductMockTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;

*/
/*    @MockBean
    ProductDao productDao;*//*


    private static String  shopToken;
    private static String  shopToken1;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        shopToken = jwtHelper.createToken(1L, "wt", 2L, 1, 3600);
        shopToken1 = jwtHelper.createToken(1L, "wt", 10L, 1, 3600);
    }

    // 成功加入（with redis）
    @Test
    void testCreateGrouponActSuccessWithRedis() throws Exception {
        GrouponAct grouponAct = new GrouponAct();
        List<Threshold> thresholdList = new ArrayList<>();
        thresholdList.add(new Threshold(100, 50L));
        thresholdList.add(new Threshold(30, 10L));
        thresholdList.add(new Threshold(20, 5L));
        grouponAct.setThresholds(thresholdList);
        grouponAct.setId(4L);
        grouponAct.setShopId(2L);
        grouponAct.setName("团购活动test");
        grouponAct.setActClass("grouponActDao");

        Mockito.when(redisUtil.hasKey("A4")).thenReturn(true);
        Mockito.when(redisUtil.get("A4")).thenReturn(grouponAct);

        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 5459, 4)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("团购活动test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(4)));
    }

    // 成功创建（without redis）
    @Test
    void testCreateGrouponAct() throws Exception {

        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);


        String body = "{\"price\":\"10000\",\"quantity\":\"10000\",\"max_quantity\":\"200\""
                + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.post("/shops/{shopId}/products/{pid}/groupons/{id}", 2, 5459, 4)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("团购活动2")));
    }
}
*/
