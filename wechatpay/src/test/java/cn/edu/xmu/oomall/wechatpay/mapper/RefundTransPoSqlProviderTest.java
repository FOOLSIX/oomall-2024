package cn.edu.xmu.oomall.wechatpay.mapper;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.RefundTransPoSqlProvider;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.RefundTransPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.RefundTransPoExample;
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
public class RefundTransPoSqlProviderTest {
    @Autowired
    private RefundTransPoSqlProvider refundTransPoSqlProvider;

    @Test
    public void testUpdateByExampleSelective() {
        RefundTransPo row = new RefundTransPo();
        row.setId(4L);
        row.setSubMchid("1900008XXX");
        row.setTransactionId("2111");
        row.setAmountTotal(1000);
        row.setAmountPayerTotal(1000);
        row.setAmountSettlementTotal(1000);
        row.setAmountRefund(200);
        row.setAmountPayerRefund(200);
        row.setAmountSettlementRefund(200);
        row.setAmountDiscountRefund(0);
        row.setOutRefundNo("597");
        row.setRefundId("12121");
        row.setOutTradeNo("768");
        row.setUserReceivedAccount("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setStatus("SUCCESS");

        RefundTransPoExample example = new RefundTransPoExample();
        RefundTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(4L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_refund_trans\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}, `transaction_id` = #{row.transactionId,jdbcType=VARCHAR}, `amount_total` = #{row.amountTotal,jdbcType=INTEGER}, `amount_payer_total` = #{row.amountPayerTotal,jdbcType=INTEGER}, `amount_settlement_total` = #{row.amountSettlementTotal,jdbcType=INTEGER}, `amount_refund` = #{row.amountRefund,jdbcType=INTEGER}, `amount_payer_refund` = #{row.amountPayerRefund,jdbcType=INTEGER}, `amount_settlement_refund` = #{row.amountSettlementRefund,jdbcType=INTEGER}, `amount_discount_refund` = #{row.amountDiscountRefund,jdbcType=INTEGER}, `out_refund_no` = #{row.outRefundNo,jdbcType=VARCHAR}, `refund_id` = #{row.refundId,jdbcType=VARCHAR}, `out_trade_no` = #{row.outTradeNo,jdbcType=VARCHAR}, `user_received_account` = #{row.userReceivedAccount,jdbcType=VARCHAR}, `create_time` = #{row.createTime,jdbcType=TIMESTAMP}, `status` = #{row.status,jdbcType=VARCHAR}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(refundTransPoSqlProvider.updateByExampleSelective(parameter), expectSql);
    }

    @Test
    public void testUpdateByExample() {
        RefundTransPo row = new RefundTransPo();
        row.setId(4L);
        row.setSubMchid("1900008XXX");
        row.setTransactionId("2111");
        row.setAmountTotal(1000);
        row.setAmountPayerTotal(1000);
        row.setAmountSettlementTotal(1000);
        row.setAmountRefund(200);
        row.setAmountPayerRefund(200);
        row.setAmountSettlementRefund(200);
        row.setAmountDiscountRefund(0);
        row.setOutRefundNo("597");
        row.setRefundId("12121");
        row.setOutTradeNo("768");
        row.setUserReceivedAccount("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setStatus("SUCCESS");

        RefundTransPoExample example = new RefundTransPoExample();
        RefundTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(4L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_refund_trans\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}, `transaction_id` = #{row.transactionId,jdbcType=VARCHAR}, `amount_total` = #{row.amountTotal,jdbcType=INTEGER}, `amount_payer_total` = #{row.amountPayerTotal,jdbcType=INTEGER}, `amount_settlement_total` = #{row.amountSettlementTotal,jdbcType=INTEGER}, `amount_refund` = #{row.amountRefund,jdbcType=INTEGER}, `amount_payer_refund` = #{row.amountPayerRefund,jdbcType=INTEGER}, `amount_settlement_refund` = #{row.amountSettlementRefund,jdbcType=INTEGER}, `amount_discount_refund` = #{row.amountDiscountRefund,jdbcType=INTEGER}, `out_refund_no` = #{row.outRefundNo,jdbcType=VARCHAR}, `refund_id` = #{row.refundId,jdbcType=VARCHAR}, `out_trade_no` = #{row.outTradeNo,jdbcType=VARCHAR}, `user_received_account` = #{row.userReceivedAccount,jdbcType=VARCHAR}, `create_time` = #{row.createTime,jdbcType=TIMESTAMP}, `status` = #{row.status,jdbcType=VARCHAR}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(refundTransPoSqlProvider.updateByExample(parameter), expectSql);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        RefundTransPo row = new RefundTransPo();
        row.setId(4L);
        row.setSubMchid("1900008XXX");
        row.setTransactionId("2111");
        row.setAmountTotal(1000);
        row.setAmountPayerTotal(1000);
        row.setAmountSettlementTotal(1000);
        row.setAmountRefund(200);
        row.setAmountPayerRefund(200);
        row.setAmountSettlementRefund(200);
        row.setAmountDiscountRefund(0);
        row.setOutRefundNo("597");
        row.setRefundId("12121");
        row.setOutTradeNo("768");
        row.setUserReceivedAccount("oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setCreateTime(localDateTime);
        row.setStatus("SUCCESS");

        String expectSql = "UPDATE wechatpay_refund_trans\n" +
                "SET `sub_mchid` = #{subMchid,jdbcType=VARCHAR}, `transaction_id` = #{transactionId,jdbcType=VARCHAR}, `amount_total` = #{amountTotal,jdbcType=INTEGER}, `amount_payer_total` = #{amountPayerTotal,jdbcType=INTEGER}, `amount_settlement_total` = #{amountSettlementTotal,jdbcType=INTEGER}, `amount_refund` = #{amountRefund,jdbcType=INTEGER}, `amount_payer_refund` = #{amountPayerRefund,jdbcType=INTEGER}, `amount_settlement_refund` = #{amountSettlementRefund,jdbcType=INTEGER}, `amount_discount_refund` = #{amountDiscountRefund,jdbcType=INTEGER}, `out_refund_no` = #{outRefundNo,jdbcType=VARCHAR}, `refund_id` = #{refundId,jdbcType=VARCHAR}, `out_trade_no` = #{outTradeNo,jdbcType=VARCHAR}, `user_received_account` = #{userReceivedAccount,jdbcType=VARCHAR}, `create_time` = #{createTime,jdbcType=TIMESTAMP}, `status` = #{status,jdbcType=VARCHAR}\n" +
                "WHERE (`id` = #{id,jdbcType=BIGINT})";

        assert Objects.equals(refundTransPoSqlProvider.updateByPrimaryKeySelective(row), expectSql);
    }
}
