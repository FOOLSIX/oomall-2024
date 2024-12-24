package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.oomall.comment.CommentTestApplication;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = CommentTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class UnAuthorizedControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRetrieveCommentsByProductId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1001/comments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[*].pid").value(Matchers.everyItem(Matchers.is(0))));
    }

    @Test
    void retrieveRegionsStates() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/comments/states")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code == '0')].name", hasItem("待审")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code == '1')].name", hasItem("审核通过")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code == '2')].name", hasItem("请求屏蔽")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code == '3')].name", hasItem("封禁")));
    }
}
