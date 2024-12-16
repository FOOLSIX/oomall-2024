package cn.edu.xmu.oomall.wechatpay.mapper;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.DivPayTransPoSqlProvider;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.*;
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
public class DivPayTransPoSqlProviderTest {
    @Autowired
    private DivPayTransPoSqlProvider divPayTransPoSqlProvider;

    @Test
    public void testUpdateByExampleSelective() {
        DivPayTransPo row = new DivPayTransPo();
        row.setId(2L);
        row.setAppid("100001");
        row.setSubMchid("1900008XXX");
        row.setTransactionId("12222");
        row.setOutOrderNo("1");
        row.setOrderId("dasdad");
        row.setState("PROCESSING");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setSuccessTime(localDateTime);

        DivPayTransPoExample example = new DivPayTransPoExample();
        DivPayTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_div_pay_trans\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `appid` = #{row.appid,jdbcType=VARCHAR}, `sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}, `transaction_id` = #{row.transactionId,jdbcType=VARCHAR}, `out_order_no` = #{row.outOrderNo,jdbcType=VARCHAR}, `order_id` = #{row.orderId,jdbcType=VARCHAR}, `state` = #{row.state,jdbcType=VARCHAR}, `success_time` = #{row.successTime,jdbcType=TIMESTAMP}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(divPayTransPoSqlProvider.updateByExampleSelective(parameter), expectSql);
    }

    @Test
    public void testUpdateByExample() {
        DivPayTransPo row = new DivPayTransPo();
        row.setId(2L);
        row.setAppid("100001");
        row.setSubMchid("1900008XXX");
        row.setTransactionId("12222");
        row.setOutOrderNo("1");
        row.setOrderId("dasdad");
        row.setState("PROCESSING");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setSuccessTime(localDateTime);

        DivPayTransPoExample example = new DivPayTransPoExample();
        DivPayTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_div_pay_trans\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `appid` = #{row.appid,jdbcType=VARCHAR}, `sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}, `transaction_id` = #{row.transactionId,jdbcType=VARCHAR}, `out_order_no` = #{row.outOrderNo,jdbcType=VARCHAR}, `order_id` = #{row.orderId,jdbcType=VARCHAR}, `state` = #{row.state,jdbcType=VARCHAR}, `success_time` = #{row.successTime,jdbcType=TIMESTAMP}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(divPayTransPoSqlProvider.updateByExample(parameter), expectSql);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        DivPayTransPo row = new DivPayTransPo();
        row.setId(2L);
        row.setAppid("100001");
        row.setSubMchid("1900008XXX");
        row.setTransactionId("12222");
        row.setOutOrderNo("1");
        row.setOrderId("dasdad");
        row.setState("PROCESSING");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setSuccessTime(localDateTime);

        String expectSql = "UPDATE wechatpay_div_pay_trans\n" +
                "SET `appid` = #{appid,jdbcType=VARCHAR}, `sub_mchid` = #{subMchid,jdbcType=VARCHAR}, `transaction_id` = #{transactionId,jdbcType=VARCHAR}, `out_order_no` = #{outOrderNo,jdbcType=VARCHAR}, `order_id` = #{orderId,jdbcType=VARCHAR}, `state` = #{state,jdbcType=VARCHAR}, `success_time` = #{successTime,jdbcType=TIMESTAMP}\n" +
                "WHERE (`id` = #{id,jdbcType=BIGINT})";

        assert Objects.equals(divPayTransPoSqlProvider.updateByPrimaryKeySelective(row), expectSql);
    }
}

