package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.customer.CommentApplicationTests;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.mapper.jpa.CustomerPoMapper;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = CommentApplicationTests.class)
@AutoConfigureMockMvc
@Transactional
public class CustomerControllerTest {

    @Autowired
    private CustomerController customerController;

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

    @Test
    public void testBanUser(){

    }


}
