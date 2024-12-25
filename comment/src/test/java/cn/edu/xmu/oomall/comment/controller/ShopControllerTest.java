package cn.edu.xmu.oomall.comment.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.comment.CommentTestApplication;
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
public class ShopControllerTest {
    @Autowired
    MockMvc mockMvc;
    private static String adminToken;
    private static String shopToken1;
    private static String shopToken2;
    private static String shopToken3;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        shopToken1 = jwtHelper.createToken(101L, "shop1", 101L, 1, 3600);
        shopToken2 = jwtHelper.createToken(108L, "shop2", 108L, 1, 3600);
        shopToken3 = jwtHelper.createToken(119L, "shop3", 119L, 1, 3600);
    }

    @Test
    public void testCreateReply() throws Exception {
        String body = "{\"id\":8, \"content\":\"回复\", \"shopId\":108, \"productId\":1008}";

        mockMvc.perform(MockMvcRequestBuilders.post("/shops/108/comments/8/reply")
                        .header("authorization", shopToken2)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testCreateReplyOutLimit() throws Exception {
        String body = "{\"content\":\"回复\", \"shopId\":101, \"productId\":1001}";

        mockMvc.perform(MockMvcRequestBuilders.post("/shops/101/comments/1/reply")
                        .header("authorization", shopToken1)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.REPLY_COMMENT_OUTLIMIT.getErrNo())));
    }

    @Test
    public void testCreateReplyOutScope() throws Exception {
        String body = "{\"content\":\"回复\", \"shopId\":101, \"productId\":1001}";

        mockMvc.perform(MockMvcRequestBuilders.post("/shops/101/comments/2/reply")
                        .header("authorization", shopToken1)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COMMENT_OUTSCOPE.getErrNo())));
    }

    @Test
    public void testCreateReplyStatusNotAllow() throws Exception {
        String body = "{\"id\":8, \"content\":\"回复\", \"shopId\":108, \"productId\":1008}";

        mockMvc.perform(MockMvcRequestBuilders.post("/shops/108/comments/18/reply")
                        .header("authorization", shopToken2)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COMMENT_CANNOT_CREATE.getErrNo())));
    }

    @Test
    public void testUpdateComment() throws Exception {
        String body = "{\"content\":\"修改的回复\"}";
        mockMvc.perform(MockMvcRequestBuilders.put("/shops/101/comments/27")
                        .header("authorization", shopToken1)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }


    @Test
    public void testBlockComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/shops/101/comments/1/block")
                        .header("authorization", shopToken1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())));
    }

    @Test
    public void testBlockCommentOutScope() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/shops/101/comments/5/block")
                        .header("authorization", shopToken1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_OUTSCOPE.getErrNo())));
    }

    @Test
    public void testBlockCommentStatusNotAllow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/shops/119/comments/19/block")
                        .header("authorization", shopToken3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())));
    }

}
