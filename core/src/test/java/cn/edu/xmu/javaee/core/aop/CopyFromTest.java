package cn.edu.xmu.javaee.core.aop;
import cn.edu.xmu.javaee.core.util.CloneFactory;
import cn.edu.xmu.oomall.payment.dao.bo.PayTrans;
import cn.edu.xmu.oomall.payment.dao.bo.RefundTrans;
import cn.edu.xmu.oomall.payment.mapper.generator.po.PayTransPo;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CopyFromTest {

    @Test
    public void testCopyFromAnnotationGeneration() {
        // 创建测试对象
        PayTrans payTrans=new PayTrans();
        payTrans.setSpOpenid("156");
        payTrans.setOutNo("231lwf");
        payTrans.setId(8L);
        PayTransPo po = new PayTransPo();

        // 如果注解处理器工作正常，应该生成了CloneFactory类
        // 使用生成的CloneFactory进行复制
        PayTransPo result = CloneFactory.copy(po, payTrans);
        // 验证复制结果
        assertEquals(payTrans.getSpOpenid(), result.getSpOpenid());
        assertEquals(payTrans.getOutNo(), result.getOutNo());
        assertEquals(payTrans.getId(),result.getId());
    }
}