package cn.edu.xmu.oomall.wechatpay.mapper;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.DivReceiverPoSqlProvider;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivReceiverPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivReceiverPoExample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest(classes = WeChatPayApplication.class)
@AutoConfigureMockMvc
@Transactional
public class DivReceiverPoSqlProviderTest {
    @Autowired
    private DivReceiverPoSqlProvider divReceiverPoSqlProvider;

    @Test
    public void testUpdateByExampleSelective() {
        DivReceiverPo row = new DivReceiverPo();
        row.setId(2L);
        row.setAmount(10);
        row.setDescription("分给OOMALL");
        row.setOrderId("O89769");
        row.setType("MERCHANT_ID");
        row.setResult("PENDING");
        row.setFailReason("");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setFinishTime(localDateTime);
        row.setDetailId("D14445");

        DivReceiverPoExample example = new DivReceiverPoExample();
        DivReceiverPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_div_receiver\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `amount` = #{row.amount,jdbcType=INTEGER}, `description` = #{row.description,jdbcType=VARCHAR}, `order_id` = #{row.orderId,jdbcType=VARCHAR}, `type` = #{row.type,jdbcType=VARCHAR}, `result` = #{row.result,jdbcType=VARCHAR}, `fail_reason` = #{row.failReason,jdbcType=VARCHAR}, `create_time` = #{row.createTime,jdbcType=TIMESTAMP}, `finish_time` = #{row.finishTime,jdbcType=TIMESTAMP}, `detail_id` = #{row.detailId,jdbcType=VARCHAR}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(divReceiverPoSqlProvider.updateByExampleSelective(parameter), expectSql);
    }

    @Test
    public void testUpdateByExample() {
        DivReceiverPo row = new DivReceiverPo();
        row.setId(2L);
        row.setAmount(10);
        row.setDescription("分给OOMALL");
        row.setOrderId("O89769");
        row.setType("MERCHANT_ID");
        row.setResult("PENDING");
        row.setFailReason("");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setFinishTime(localDateTime);
        row.setDetailId("D14445");

        DivReceiverPoExample example = new DivReceiverPoExample();
        DivReceiverPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_div_receiver\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `amount` = #{row.amount,jdbcType=INTEGER}, `description` = #{row.description,jdbcType=VARCHAR}, `order_id` = #{row.orderId,jdbcType=VARCHAR}, `type` = #{row.type,jdbcType=VARCHAR}, `result` = #{row.result,jdbcType=VARCHAR}, `fail_reason` = #{row.failReason,jdbcType=VARCHAR}, `create_time` = #{row.createTime,jdbcType=TIMESTAMP}, `finish_time` = #{row.finishTime,jdbcType=TIMESTAMP}, `detail_id` = #{row.detailId,jdbcType=VARCHAR}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(divReceiverPoSqlProvider.updateByExample(parameter), expectSql);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        DivReceiverPo row = new DivReceiverPo();
        row.setId(2L);
        row.setAmount(10);
        row.setDescription("分给OOMALL");
        row.setOrderId("O89769");
        row.setType("MERCHANT_ID");
        row.setResult("PENDING");
        row.setFailReason("");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setFinishTime(localDateTime);
        row.setDetailId("D14445");

        String expectSql = "UPDATE wechatpay_div_receiver\n" +
                "SET `amount` = #{amount,jdbcType=INTEGER}, `description` = #{description,jdbcType=VARCHAR}, `order_id` = #{orderId,jdbcType=VARCHAR}, `type` = #{type,jdbcType=VARCHAR}, `result` = #{result,jdbcType=VARCHAR}, `fail_reason` = #{failReason,jdbcType=VARCHAR}, `create_time` = #{createTime,jdbcType=TIMESTAMP}, `finish_time` = #{finishTime,jdbcType=TIMESTAMP}, `detail_id` = #{detailId,jdbcType=VARCHAR}\n" +
                "WHERE (`id` = #{id,jdbcType=BIGINT})";

        assert Objects.equals(divReceiverPoSqlProvider.updateByPrimaryKeySelective(row), expectSql);
    }
}

