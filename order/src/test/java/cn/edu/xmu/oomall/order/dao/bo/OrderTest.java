package cn.edu.xmu.oomall.order.dao.bo;

import cn.edu.xmu.oomall.order.OrderApplication;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = OrderApplication.class)
@Transactional
public class OrderTest {
    @Autowired
    private OrderDao orderDao;


}
