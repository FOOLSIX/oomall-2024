package cn.edu.xmu.oomall.product.mapper.mongo;

import cn.edu.xmu.oomall.product.ProductTestApplication;
import cn.edu.xmu.oomall.product.mapper.po.CouponActPo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ProductTestApplication.class)
@Slf4j
public class CouponActMongoTest {

    @Autowired
    private CouponActPoMapper couponActPoMapper;

    @Test
    public void writeData() {
        String objectId = "test123";

        // 写入数据
        CouponActPo couponActPo = new CouponActPo();
        couponActPo.setObjectId(objectId);
        couponActPo.setQuantity(100);
        couponActPoMapper.save(couponActPo);
         couponActPoMapper.delete(couponActPo);
    }

    // 读取数据
    public CouponActPo readData(String objectId) {
        return couponActPoMapper.findById(objectId).orElse(null);
    }

    @Test
    public void testWReadMultipleTimes() {

        String objectId = "657301991ae07d79171442eb";

        // 多次读取数据
        IntStream.range(0, 10).forEach(i -> {
            CouponActPo readData = readData(objectId);
            assertNotNull(readData, "Data should be readable successfully");
        });

    }
}
