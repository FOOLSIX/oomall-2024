package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.comment.CommentTestApplication;
import cn.edu.xmu.oomall.comment.dao.bo.Comment;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = CommentTestApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static String adminToken;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    @Test
    public void testRetrieveComments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/platforms/0/comments/1")
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[*].status").value(Matchers.everyItem(Matchers.is(1))));
    }

    @Test
    public void testApprove() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/platforms/0/comments/3/approve")
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }
}