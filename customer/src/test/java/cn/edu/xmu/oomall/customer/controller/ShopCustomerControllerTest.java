package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.CommentApplicationTests;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import cn.edu.xmu.oomall.customer.mapper.jpa.CustomerPoMapper;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.CoreMatchers.is;

@Slf4j
@SpringBootTest(classes = CommentApplicationTests.class)
@AutoConfigureMockMvc
@Transactional
public class ShopCustomerControllerTest {

    @Autowired
    private ShopCustomerController shopCustomerController;

    @Autowired
    private CustomerPoMapper customerPoMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;

    private static String adminToken;

    @BeforeAll
    static void setUp() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
    }

    /**
     * 获取用户信息
     * @throws Exception
     */
    @Test
    public void testgetUserById() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/customer/{id}",0L,1)
                .header("authorization",adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("demouser")));
    }

    /**
     * 填入id为null时获取用户信息
     */
    @Test
    public void testgetUserByIdWhenNull() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/customer/{id}", 0L, -1L)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.FIELD_NOTVALID.getErrNo())) // 检查返回错误码
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("填入的id参数为空")); // 检查错误信息
    }

    /**
     * 测试分页获取用户列表
     * @throws Exception
     */
    @Test
    public void testGetCustomers() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/shops/{shopId}/customers", 0L)
                        .header("authorization", adminToken) // 添加必要的 token
                        .param("page", "1") // 模拟分页参数
                        .param("pageSize", "10") // 模拟分页参数
                        .contentType(MediaType.APPLICATION_JSON_VALUE)) // 设置请求类型
                .andExpect(MockMvcResultMatchers.status().isOk()) // 断言状态码 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo())) // 确保返回成功状态
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("成功")) // 确保返回消息正确
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page").value(1)) // 验证分页参数
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize").value(10)) // 验证分页大小
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list").isArray()) // 验证列表是数组
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id").value(3)) // 验证第一个用户 ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].name").value("demouser3")) // 验证第一个用户名称
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].id").value(2)) // 验证第二个用户 ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].name").value("demouser2")) // 验证第二个用户名称
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].id").value(1)) // 验证第三个用户 ID
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[2].name").value("demouser")); // 验证第三个用户名称
    }



    /**
     * 测试管理员禁用顾客
     * @throws Exception
     */
    @Test
    public void testBanUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/customers/{id}/ban", 0, 1)
                        .header("authorization", adminToken)  // 设置认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE))  // 设置请求类型
                .andExpect(MockMvcResultMatchers.status().isOk())  // 确认返回的 HTTP 状态码是 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))  // 确认返回的 errno 为 0
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("成功"));  // 确认返回的 errmsg 是 "成功"
    }

    /**
     * 测试管理员解禁顾客
     * @throws Exception
     */
    @Test
    public void testReleaseUser() throws Exception {
        Long customerId = 2L;
        Long shopId = 0L;

        this.mockMvc.perform(MockMvcRequestBuilders.put("/shops/{shopId}/customers/{id}/release", shopId, customerId)
                        .header("authorization", adminToken) // 设置认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE))  // 设置请求类型
                .andExpect(MockMvcResultMatchers.status().isOk())  // 确认返回的 HTTP 状态码是 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))  // 确认返回的 errno 为 0
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("成功"));  // 确认返回的 errmsg 是 "成功"
    }


    /**
     * 测试管理员删除顾客
     * @throws Exception
     */
    @Test
    public void testDelUserById() throws Exception {
        Long customerId = 3L;
        Long shopId = 0L;

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/shops/{shopId}/customers/{id}", shopId, customerId)
                        .header("authorization", adminToken) // 设置认证 token
                        .contentType(MediaType.APPLICATION_JSON_VALUE))  // 设置请求类型
                .andExpect(MockMvcResultMatchers.status().isOk())  // 确认返回的 HTTP 状态码是 200
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno").value(ReturnNo.OK.getErrNo()))  // 确认返回的 errno 为 0
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg").value("成功"));  // 确认返回的 errmsg 是 "成功"
    }






}
