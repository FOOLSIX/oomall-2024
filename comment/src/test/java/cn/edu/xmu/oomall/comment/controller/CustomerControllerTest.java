package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.comment.CommentTestApplication;
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
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;
    private static String customerToken1;
    private static String customerToken2;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        customerToken1 = jwtHelper.createToken(1L, "customer1", 1L, 2, 3600);
        customerToken2 = jwtHelper.createToken(8L, "customer2", 8L, 2, 3600);
    }

    @Test
    public void testRetrieveComments() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
                .header("authorization", customerToken1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[*].uid").value(Matchers.everyItem(Matchers.is(1))));
    }

    @Test
    public void testCreateComment() throws Exception {

    }

    @Test
    public void testCreateAdditionalComment() throws Exception {
        String body = "{\"content\":\"追评\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/8/additional")
                .header("authorization", customerToken2)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testCreateAdditionalCommentOutScope() throws Exception {
        String body = "{\"content\":\"追评\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/2/additional")
                        .header("authorization", customerToken1)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COMMENT_OUTSCOPE.getErrNo())));
    }

    @Test
    public void testCreateAdditionalCommentOutLimit() throws Exception {
        String body = "{\"content\":\"追评\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/1/additional")
                        .header("authorization", customerToken1)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.ADDITIONAL_COMMENT_OUTLIMIT.getErrNo())));
    }
}
