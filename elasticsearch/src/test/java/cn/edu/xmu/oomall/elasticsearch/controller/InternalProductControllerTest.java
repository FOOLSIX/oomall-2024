package cn.edu.xmu.oomall.elasticsearch.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.elasticsearch.ElasticsearchApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.is;


@SpringBootTest(classes = ElasticsearchApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class InternalProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSearchProductsWithAllParams() throws Exception {
        // 调用 Controller 的接口
        mockMvc.perform(MockMvcRequestBuilders.get("/internal/products")
                        .param("name", "罐头")
                        .param("barcode", "6924254673572")
                        .param("shopId", "10")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty());
    }

    @Test
    public void testSearchProductsByNameOnly() throws Exception {
        // 调用 Controller 的接口
        mockMvc.perform(MockMvcRequestBuilders.get("/internal/products")
                        .param("name", "香皂")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray());
    }

    @Test
    public void testSearchProductsNoResults() throws Exception {
        // 调用 Controller 的接口
        mockMvc.perform(MockMvcRequestBuilders.get("/internal/products")
                        .param("name", "不存在的产品")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }
}
