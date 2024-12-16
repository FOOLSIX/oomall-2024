package cn.edu.xmu.oomall.wechatpay.mapper;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.DivRefundTransPoSqlProvider;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivRefundTransPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivRefundTransPoExample;
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
public class DivRefundTransPoSqlProviderTest {
    @Autowired
    private DivRefundTransPoSqlProvider divRefundTransPoSqlProvider;

    @Test
    public void testUpdateByExampleSelective() {
        DivRefundTransPo row = new DivRefundTransPo();
        row.setId(4L);
        row.setSubMchid("1900008XXX");
        row.setOrderId("1");
        row.setOutOrderNo("1");
        row.setOutReturnNo("1");
        row.setReturnMchid("1900007XXX");
        row.setAmount(55970);
        row.setDescription("退款");
        row.setReturnId("1");
        row.setResult("SUCCESS");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setFinishTime(localDateTime);

        DivRefundTransPoExample example = new DivRefundTransPoExample();
        DivRefundTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(4L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_div_refund_trans\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}, `order_id` = #{row.orderId,jdbcType=VARCHAR}, `out_order_no` = #{row.outOrderNo,jdbcType=VARCHAR}, `out_return_no` = #{row.outReturnNo,jdbcType=VARCHAR}, `return_mchid` = #{row.returnMchid,jdbcType=VARCHAR}, `amount` = #{row.amount,jdbcType=INTEGER}, `description` = #{row.description,jdbcType=VARCHAR}, `return_id` = #{row.returnId,jdbcType=VARCHAR}, `result` = #{row.result,jdbcType=VARCHAR}, `create_time` = #{row.createTime,jdbcType=TIMESTAMP}, `finish_time` = #{row.finishTime,jdbcType=TIMESTAMP}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(divRefundTransPoSqlProvider.updateByExampleSelective(parameter), expectSql);
    }

    @Test
    public void testUpdateByExample() {
        DivRefundTransPo row = new DivRefundTransPo();
        row.setId(4L);
        row.setSubMchid("1900008XXX");
        row.setOrderId("1");
        row.setOutOrderNo("1");
        row.setOutReturnNo("1");
        row.setReturnMchid("1900007XXX");
        row.setAmount(55970);
        row.setDescription("退款");
        row.setReturnId("1");
        row.setResult("SUCCESS");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setFinishTime(localDateTime);

        DivRefundTransPoExample example = new DivRefundTransPoExample();
        DivRefundTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(4L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_div_refund_trans\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}, `order_id` = #{row.orderId,jdbcType=VARCHAR}, `out_order_no` = #{row.outOrderNo,jdbcType=VARCHAR}, `out_return_no` = #{row.outReturnNo,jdbcType=VARCHAR}, `return_mchid` = #{row.returnMchid,jdbcType=VARCHAR}, `amount` = #{row.amount,jdbcType=INTEGER}, `description` = #{row.description,jdbcType=VARCHAR}, `return_id` = #{row.returnId,jdbcType=VARCHAR}, `result` = #{row.result,jdbcType=VARCHAR}, `create_time` = #{row.createTime,jdbcType=TIMESTAMP}, `finish_time` = #{row.finishTime,jdbcType=TIMESTAMP}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(divRefundTransPoSqlProvider.updateByExample(parameter), expectSql);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        DivRefundTransPo row = new DivRefundTransPo();
        row.setId(4L);
        row.setSubMchid("1900008XXX");
        row.setOrderId("1");
        row.setOutOrderNo("1");
        row.setOutReturnNo("1");
        row.setReturnMchid("1900007XXX");
        row.setAmount(55970);
        row.setDescription("退款");
        row.setReturnId("1");
        row.setResult("SUCCESS");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setFinishTime(localDateTime);

        String expectSql = "UPDATE wechatpay_div_refund_trans\n" +
                "SET `sub_mchid` = #{subMchid,jdbcType=VARCHAR}, `order_id` = #{orderId,jdbcType=VARCHAR}, `out_order_no` = #{outOrderNo,jdbcType=VARCHAR}, `out_return_no` = #{outReturnNo,jdbcType=VARCHAR}, `return_mchid` = #{returnMchid,jdbcType=VARCHAR}, `amount` = #{amount,jdbcType=INTEGER}, `description` = #{description,jdbcType=VARCHAR}, `return_id` = #{returnId,jdbcType=VARCHAR}, `result` = #{result,jdbcType=VARCHAR}, `create_time` = #{createTime,jdbcType=TIMESTAMP}, `finish_time` = #{finishTime,jdbcType=TIMESTAMP}\n" +
                "WHERE (`id` = #{id,jdbcType=BIGINT})";

        assert Objects.equals(divRefundTransPoSqlProvider.updateByPrimaryKeySelective(row), expectSql);
    }
}
