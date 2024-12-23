package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.CommentApplicationTests;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = CommentApplicationTests.class)
@Transactional
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void testFindById() {
        Customer customer = customerDao.findById(1L);
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(1L);
        assertThat(customer.getName()).isNotEmpty();
    }

    @Test
    public void testGetAllUsers() {
        List<Customer> customers = customerDao.getAllUsers(1, 10);
        assertThat(customers).isNotNull();
        assertThat(customers.size()).isLessThanOrEqualTo(10);
    }


}
