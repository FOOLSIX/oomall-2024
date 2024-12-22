package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.oomall.customer.CommentApplicationTests;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = CommentApplicationTests.class)
@Transactional
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

}
