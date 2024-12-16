package cn.edu.xmu.oomall.wechatpay.mapper;

import cn.edu.xmu.oomall.wechatpay.WeChatPayApplication;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.PayTransPoSqlProvider;
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
public class PayTransPoSqlProviderTest {
    @Autowired
    private PayTransPoSqlProvider payTransPoSqlProvider;

    @Test
    public void testUpdateByExample() {
        PayTransPo row = new PayTransPo();
        row.setId(2L);
        row.setSpAppid("wx8888888888888888");
        row.setSpMchid("1230000109");
        row.setSubMchid("1900000109");
        row.setDescription("OOMALL-北京厦带-QQ公仔");
        row.setOutTradeNo("OOMALL23323338018");
        row.setPayerSpOpenid("oUpF8uMuAJO_M2123459zNjWeS6o");
        row.setAmountTotal(100);
        row.setTransactionId("T93232");
        row.setTradeState("REFUND");
        row.setTradeStateDesc("转入退款");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setSuccessTime(localDateTime);
        row.setTimeExpire(localDateTime);

        PayTransPoExample example = new PayTransPoExample();
        PayTransPoExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2L);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("row", row);
        parameter.put("example", example);

        String expectSql = "UPDATE wechatpay_pay_trans\n" +
                "SET `id` = #{row.id,jdbcType=BIGINT}, `sp_appid` = #{row.spAppid,jdbcType=VARCHAR}, `sp_mchid` = #{row.spMchid,jdbcType=VARCHAR}, `sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}, `description` = #{row.description,jdbcType=VARCHAR}, `out_trade_no` = #{row.outTradeNo,jdbcType=VARCHAR}, `time_expire` = #{row.timeExpire,jdbcType=TIMESTAMP}, `payer_sp_openid` = #{row.payerSpOpenid,jdbcType=VARCHAR}, `amount_total` = #{row.amountTotal,jdbcType=INTEGER}, `transaction_id` = #{row.transactionId,jdbcType=VARCHAR}, `trade_state` = #{row.tradeState,jdbcType=VARCHAR}, `trade_state_desc` = #{row.tradeStateDesc,jdbcType=VARCHAR}, `success_time` = #{row.successTime,jdbcType=TIMESTAMP}\n" +
                "WHERE ((`id` = #{example.oredCriteria[0].allCriteria[0].value}))";

        assert Objects.equals(payTransPoSqlProvider.updateByExample(parameter), expectSql);
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        PayTransPo row = new PayTransPo();
        row.setId(2L);
        row.setSpAppid("wx8888888888888888");
        row.setSpMchid("1230000109");
        row.setSubMchid("1900000109");
        row.setDescription("OOMALL-北京厦带-QQ公仔");
        row.setOutTradeNo("OOMALL23323338018");
        row.setPayerSpOpenid("oUpF8uMuAJO_M2123459zNjWeS6o");
        row.setAmountTotal(100);
        row.setTransactionId("T93232");
        row.setTradeState("REFUND");
        row.setTradeStateDesc("转入退款");
        LocalDate date = LocalDate.of(2024, 12, 6);
        LocalTime time = LocalTime.of(13, 35);
        LocalDateTime localDateTime = LocalDateTime.of(date, time);
        row.setSuccessTime(localDateTime);
        row.setTimeExpire(localDateTime);

        String expectSql = "UPDATE wechatpay_pay_trans\n" +
                "SET `sp_appid` = #{spAppid,jdbcType=VARCHAR}, `sp_mchid` = #{spMchid,jdbcType=VARCHAR}, `sub_mchid` = #{subMchid,jdbcType=VARCHAR}, `description` = #{description,jdbcType=VARCHAR}, `out_trade_no` = #{outTradeNo,jdbcType=VARCHAR}, `time_expire` = #{timeExpire,jdbcType=TIMESTAMP}, `payer_sp_openid` = #{payerSpOpenid,jdbcType=VARCHAR}, `amount_total` = #{amountTotal,jdbcType=INTEGER}, `transaction_id` = #{transactionId,jdbcType=VARCHAR}, `trade_state` = #{tradeState,jdbcType=VARCHAR}, `trade_state_desc` = #{tradeStateDesc,jdbcType=VARCHAR}, `success_time` = #{successTime,jdbcType=TIMESTAMP}\n" +
                "WHERE (`id` = #{id,jdbcType=BIGINT})";

        assert Objects.equals(payTransPoSqlProvider.updateByPrimaryKeySelective(row), expectSql);
    }
}

