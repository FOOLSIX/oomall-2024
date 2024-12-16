package cn.edu.xmu.oomall.shop.controller;

import cn.edu.xmu.javaee.core.mapper.RedisUtil;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.shop.ShopTestApplication;
import cn.edu.xmu.oomall.shop.controller.dto.ProductItemDto;
import cn.edu.xmu.oomall.shop.mapper.openfeign.RegionMapper;
import cn.edu.xmu.oomall.shop.mapper.openfeign.po.RegionPo;
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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

/**
 * 不同分包策略和打包算法测试
 * ZhaoDong Wang
 * 2023-dgn1-009
 */
@SpringBootTest(classes = ShopTestApplication.class)
@AutoConfigureMockMvc
@Transactional(propagation = Propagation.REQUIRED)
public class InternalFreightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisUtil redisUtil;
    
    @MockBean
    private RegionMapper regionMapper;
    private static final String PATHS = "/internal/templates/{id}/regions/{rid}/freightprice";

    /**
     * 最大简单分包（计重）
     */
    @Test
    void testGetFreightWhenMaxSimpleWeigh() throws Exception {
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, 50000));
                add(new ProductItemDto(2L, 2L, 2, 50000));
                add(new ProductItemDto(3L, 3L, 3, 50000));
                add(new ProductItemDto(4L, 4L, 4, 50000));
                add(new ProductItemDto(5L, 5L, 3, 50000));
                add(new ProductItemDto(6L, 6L, 2, 50000));
                add(new ProductItemDto(7L, 7L, 1, 50000));
            }
        };
        String body = JacksonUtil.toJson(voList);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 1, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", is(91000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '1')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '2')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '3')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '4')].quantity", hasItem(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '5')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '6')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '7')].quantity", hasItem(1)));

    }

    /**
     * 最大背包分包（计重）
     */
    @Test
    void testGetFreightWhenMaxBackPackWeigh() throws Exception {
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, 50000));
                add(new ProductItemDto(2L, 2L, 2, 50000));
                add(new ProductItemDto(3L, 3L, 3, 50000));
                add(new ProductItemDto(4L, 4L, 4, 50000));
                add(new ProductItemDto(5L, 5L, 3, 50000));
                add(new ProductItemDto(6L, 6L, 2, 50000));
                add(new ProductItemDto(7L, 7L, 1, 50000));
            }
        };
        String body = JacksonUtil.toJson(voList);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 16, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", is(91000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '1')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '2')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '3')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '4')].quantity", hasItem(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '5')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '6')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '7')].quantity", hasItem(1)));
    }

    /**
     * 平均简单分包（计件）
     */
    @Test
    void testGetFreightWhenAverageSimplePiece() throws Exception {
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, null));
                add(new ProductItemDto(2L, 2L, 2, null));
                add(new ProductItemDto(3L, 3L, 3, null));
                add(new ProductItemDto(4L, 4L, 4, null));
                add(new ProductItemDto(5L, 5L, 3, null));
                add(new ProductItemDto(6L, 6L, 2, null));
                add(new ProductItemDto(7L, 7L, 1, null));
            }
        };
        String body = JacksonUtil.toJson(voList);

        // size=16/(16/10+1)=8
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 17, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", is(1600)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '1')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '2')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '3')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '4')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '4')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '5')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '6')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '7')].quantity", hasItem(1)));
    }

    /**
     * 平均背包分包（计件）
     */
    @Test
    void testGetFreightWhenAverageBackPackPiece() throws Exception {
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, null));
                add(new ProductItemDto(2L, 2L, 2, null));
                add(new ProductItemDto(3L, 3L, 3, null));
                add(new ProductItemDto(4L, 4L, 4, null));
                add(new ProductItemDto(5L, 5L, 3, null));
                add(new ProductItemDto(6L, 6L, 2, null));
                add(new ProductItemDto(7L, 7L, 1, null));
            }
        };
        String body = JacksonUtil.toJson(voList);

        // size=16/(16/10+1)=8
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 2, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", is(1600)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '1')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '2')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '3')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '4')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '4')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '5')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '6')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '7')].quantity", hasItem(1)));
    }

    /**
     * 平均背包分包（计件）
     * 查找父级地区模板
     */
    @Test
    void testGetFreightWhenAverageBackPackPieceParentRegion() throws Exception {
        InternalReturnObject<List<RegionPo>> ret = new InternalReturnObject<>(new ArrayList<>() {
            {
                add(new RegionPo(4L, "testRegion"));
                add(new RegionPo(3L, "testRegion"));
                add(new RegionPo(2L, "testRegion"));
                add(new RegionPo(1L, "testRegion"));
                add(new RegionPo(0L, "testRegion"));
            }
        });
        Mockito.when(regionMapper.retrieveParentRegionsById(5L)).thenReturn(ret);
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, null));
                add(new ProductItemDto(2L, 2L, 2, null));
                add(new ProductItemDto(3L, 3L, 3, null));
                add(new ProductItemDto(4L, 4L, 4, null));
                add(new ProductItemDto(5L, 5L, 3, null));
                add(new ProductItemDto(6L, 6L, 2, null));
                add(new ProductItemDto(7L, 7L, 1, null));
            }
        };
        String body = JacksonUtil.toJson(voList);

        // size=16/(16/10+1)=8
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 2, 5)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", is(2800)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '1')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '2')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '3')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '4')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '4')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '5')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '6')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '7')].quantity", hasItem(1)));
    }

    /**
     * 贪心分包（计重）
     */
    @Test
    void testGetFreightWhenGreedyWeighNormal() throws Exception {
        ;
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, 10000));
                add(new ProductItemDto(2L, 2L, 2, 20000));
                add(new ProductItemDto(3L, 3L, 3, 30000));
                add(new ProductItemDto(4L, 4L, 4, 40000));
                add(new ProductItemDto(5L, 5L, 3, 50000));
                add(new ProductItemDto(6L, 6L, 2, 60000));
                add(new ProductItemDto(7L, 7L, 1, 70000));
            }
        };
        String body = JacksonUtil.toJson(voList);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 18, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", is(91000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '3')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '5')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '6')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '7')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '1')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '2')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '3')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '4')].quantity", hasItem(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[1][?(@.productId == '5')].quantity", hasItem(1)));
    }

    /**
     * 贪心分包（计重）
     * 一次性装入一个包裹
     */
    @Test
    void testGetFreightWhenGreedyWeighOnePack() throws Exception {
        ;
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, 1));
                add(new ProductItemDto(2L, 2L, 2, 1));
                add(new ProductItemDto(3L, 3L, 3, 1));
                add(new ProductItemDto(4L, 4L, 4, 1));
                add(new ProductItemDto(5L, 5L, 3, 1));
                add(new ProductItemDto(6L, 6L, 2, 1));
                add(new ProductItemDto(7L, 7L, 1, 1));
            }
        };
        String body = JacksonUtil.toJson(voList);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 18, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '1')].quantity", hasItem(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '2')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '3')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '4')].quantity", hasItem(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '5')].quantity", hasItem(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '6')].quantity", hasItem(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack[0][?(@.productId == '7')].quantity", hasItem(1)));
    }

    /**
     * 最优分包（计件）
     */
    @Test
    void testGetFreightWhenOptimalPieceNormal() throws Exception {
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, null));
                add(new ProductItemDto(2L, 2L, 2, null));
                add(new ProductItemDto(3L, 3L, 3, null));
                add(new ProductItemDto(4L, 4L, 4, null));
                add(new ProductItemDto(5L, 5L, 3, null));
                add(new ProductItemDto(6L, 6L, 2, null));
                add(new ProductItemDto(7L, 7L, 1, null));
            }
        };
        String body = JacksonUtil.toJson(voList);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 19, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", lessThanOrEqualTo(1600)));
    }

    /**
     * 最优分包（计件）
     * 单个物品
     */
    @Test
    void testGetFreightWhenOptimalPieceOnePack() throws Exception {
        List<ProductItemDto> voList = new ArrayList<>() {
            {
                add(new ProductItemDto(1L, 1L, 1, null));
            }
        };
        String body = JacksonUtil.toJson(voList);

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post(PATHS, 19, 248059)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pack.length()", lessThanOrEqualTo(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.freightPrice", lessThanOrEqualTo(500)));
    }
}
