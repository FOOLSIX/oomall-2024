package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRefundPo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRefundPoExample;
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

public interface AlipayRefundPoMapper {
    @Delete({
        "delete from alipay_refund",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into alipay_refund (`out_request_no`, `out_trade_no`, ",
        "`trade_no`, `total_amount`, ",
        "`refund_amount`, `gmt_refund_pay`, ",
        "`refund_status`)",
        "values (#{outRequestNo,jdbcType=VARCHAR}, #{outTradeNo,jdbcType=VARCHAR}, ",
        "#{tradeNo,jdbcType=VARCHAR}, #{totalAmount,jdbcType=INTEGER}, ",
        "#{refundAmount,jdbcType=INTEGER}, #{gmtRefundPay,jdbcType=TIMESTAMP}, ",
        "#{refundStatus,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AlipayRefundPo row);

    @InsertProvider(type=AlipayRefundPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(AlipayRefundPo row);

    @SelectProvider(type=AlipayRefundPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_amount", property="totalAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="refund_amount", property="refundAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="gmt_refund_pay", property="gmtRefundPay", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="refund_status", property="refundStatus", jdbcType=JdbcType.INTEGER)
    })
    List<AlipayRefundPo> selectByExample(AlipayRefundPoExample example);

    @Select({
        "select",
        "`id`, `out_request_no`, `out_trade_no`, `trade_no`, `total_amount`, `refund_amount`, ",
        "`gmt_refund_pay`, `refund_status`",
        "from alipay_refund",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_amount", property="totalAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="refund_amount", property="refundAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="gmt_refund_pay", property="gmtRefundPay", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="refund_status", property="refundStatus", jdbcType=JdbcType.INTEGER)
    })
    AlipayRefundPo selectByPrimaryKey(Long id);

    @UpdateProvider(type=AlipayRefundPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") AlipayRefundPo row, @Param("example") AlipayRefundPoExample example);

    @UpdateProvider(type=AlipayRefundPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") AlipayRefundPo row, @Param("example") AlipayRefundPoExample example);

    @UpdateProvider(type=AlipayRefundPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AlipayRefundPo row);

    @Update({
        "update alipay_refund",
        "set `out_request_no` = #{outRequestNo,jdbcType=VARCHAR},",
          "`out_trade_no` = #{outTradeNo,jdbcType=VARCHAR},",
          "`trade_no` = #{tradeNo,jdbcType=VARCHAR},",
          "`total_amount` = #{totalAmount,jdbcType=INTEGER},",
          "`refund_amount` = #{refundAmount,jdbcType=INTEGER},",
          "`gmt_refund_pay` = #{gmtRefundPay,jdbcType=TIMESTAMP},",
          "`refund_status` = #{refundStatus,jdbcType=INTEGER}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AlipayRefundPo row);
}