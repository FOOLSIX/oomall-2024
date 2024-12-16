package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPoExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface AlipayPaymentPoMapper {
    @Delete({
        "delete from alipay_payment",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into alipay_payment (`appid`, `receiver_account`, ",
        "`buyer_logon_id`, `trade_no`, ",
        "`out_trade_no`, `trade_status`, ",
        "`total_amount`, `success_time`)",
        "values (#{appid,jdbcType=VARCHAR}, #{receiverAccount,jdbcType=VARCHAR}, ",
        "#{buyerLogonId,jdbcType=VARCHAR}, #{tradeNo,jdbcType=VARCHAR}, ",
        "#{outTradeNo,jdbcType=VARCHAR}, #{tradeStatus,jdbcType=INTEGER}, ",
        "#{totalAmount,jdbcType=INTEGER}, #{successTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AlipayPaymentPo row);

    @InsertProvider(type=AlipayPaymentPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(AlipayPaymentPo row);

    @SelectProvider(type=AlipayPaymentPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="receiver_account", property="receiverAccount", jdbcType=JdbcType.VARCHAR),
        @Result(column="buyer_logon_id", property="buyerLogonId", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_status", property="tradeStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="total_amount", property="totalAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<AlipayPaymentPo> selectByExample(AlipayPaymentPoExample example);

    @Select({
        "select",
        "`id`, `appid`, `receiver_account`, `buyer_logon_id`, `trade_no`, `out_trade_no`, ",
        "`trade_status`, `total_amount`, `success_time`",
        "from alipay_payment",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="receiver_account", property="receiverAccount", jdbcType=JdbcType.VARCHAR),
        @Result(column="buyer_logon_id", property="buyerLogonId", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_status", property="tradeStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="total_amount", property="totalAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP)
    })
    AlipayPaymentPo selectByPrimaryKey(Long id);

    @UpdateProvider(type=AlipayPaymentPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") AlipayPaymentPo row, @Param("example") AlipayPaymentPoExample example);

    @UpdateProvider(type=AlipayPaymentPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") AlipayPaymentPo row, @Param("example") AlipayPaymentPoExample example);

    @UpdateProvider(type=AlipayPaymentPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AlipayPaymentPo row);

    @Update({
        "update alipay_payment",
        "set `appid` = #{appid,jdbcType=VARCHAR},",
          "`receiver_account` = #{receiverAccount,jdbcType=VARCHAR},",
          "`buyer_logon_id` = #{buyerLogonId,jdbcType=VARCHAR},",
          "`trade_no` = #{tradeNo,jdbcType=VARCHAR},",
          "`out_trade_no` = #{outTradeNo,jdbcType=VARCHAR},",
          "`trade_status` = #{tradeStatus,jdbcType=INTEGER},",
          "`total_amount` = #{totalAmount,jdbcType=INTEGER},",
          "`success_time` = #{successTime,jdbcType=TIMESTAMP}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AlipayPaymentPo row);
}