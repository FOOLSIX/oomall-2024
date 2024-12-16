package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRefundRoyaltyDetail;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRefundRoyaltyDetailExample;
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

public interface AlipayRefundRoyaltyDetailMapper {
    @Delete({
        "delete from alipay_refund_royalty_detail",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into alipay_refund_royalty_detail (`out_request_no`, `out_trade_no`, ",
        "`trade_no`, `trans_in`, ",
        "`trans_out`, `refund_amount`, ",
        "`royalty_type`, `result_code`, ",
        "`gmt_refund_pay`)",
        "values (#{outRequestNo,jdbcType=VARCHAR}, #{outTradeNo,jdbcType=VARCHAR}, ",
        "#{tradeNo,jdbcType=VARCHAR}, #{transIn,jdbcType=VARCHAR}, ",
        "#{transOut,jdbcType=VARCHAR}, #{refundAmount,jdbcType=INTEGER}, ",
        "#{royaltyType,jdbcType=VARCHAR}, #{resultCode,jdbcType=VARCHAR}, ",
        "#{gmtRefundPay,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AlipayRefundRoyaltyDetail row);

    @InsertProvider(type=AlipayRefundRoyaltyDetailSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(AlipayRefundRoyaltyDetail row);

    @SelectProvider(type=AlipayRefundRoyaltyDetailSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_in", property="transIn", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_out", property="transOut", jdbcType=JdbcType.VARCHAR),
        @Result(column="refund_amount", property="refundAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="royalty_type", property="royaltyType", jdbcType=JdbcType.VARCHAR),
        @Result(column="result_code", property="resultCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_refund_pay", property="gmtRefundPay", jdbcType=JdbcType.TIMESTAMP)
    })
    List<AlipayRefundRoyaltyDetail> selectByExample(AlipayRefundRoyaltyDetailExample example);

    @Select({
        "select",
        "`id`, `out_request_no`, `out_trade_no`, `trade_no`, `trans_in`, `trans_out`, ",
        "`refund_amount`, `royalty_type`, `result_code`, `gmt_refund_pay`",
        "from alipay_refund_royalty_detail",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_in", property="transIn", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_out", property="transOut", jdbcType=JdbcType.VARCHAR),
        @Result(column="refund_amount", property="refundAmount", jdbcType=JdbcType.INTEGER),
        @Result(column="royalty_type", property="royaltyType", jdbcType=JdbcType.VARCHAR),
        @Result(column="result_code", property="resultCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_refund_pay", property="gmtRefundPay", jdbcType=JdbcType.TIMESTAMP)
    })
    AlipayRefundRoyaltyDetail selectByPrimaryKey(Long id);

    @UpdateProvider(type=AlipayRefundRoyaltyDetailSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") AlipayRefundRoyaltyDetail row, @Param("example") AlipayRefundRoyaltyDetailExample example);

    @UpdateProvider(type=AlipayRefundRoyaltyDetailSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") AlipayRefundRoyaltyDetail row, @Param("example") AlipayRefundRoyaltyDetailExample example);

    @UpdateProvider(type=AlipayRefundRoyaltyDetailSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AlipayRefundRoyaltyDetail row);

    @Update({
        "update alipay_refund_royalty_detail",
        "set `out_request_no` = #{outRequestNo,jdbcType=VARCHAR},",
          "`out_trade_no` = #{outTradeNo,jdbcType=VARCHAR},",
          "`trade_no` = #{tradeNo,jdbcType=VARCHAR},",
          "`trans_in` = #{transIn,jdbcType=VARCHAR},",
          "`trans_out` = #{transOut,jdbcType=VARCHAR},",
          "`refund_amount` = #{refundAmount,jdbcType=INTEGER},",
          "`royalty_type` = #{royaltyType,jdbcType=VARCHAR},",
          "`result_code` = #{resultCode,jdbcType=VARCHAR},",
          "`gmt_refund_pay` = #{gmtRefundPay,jdbcType=TIMESTAMP}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AlipayRefundRoyaltyDetail row);
}