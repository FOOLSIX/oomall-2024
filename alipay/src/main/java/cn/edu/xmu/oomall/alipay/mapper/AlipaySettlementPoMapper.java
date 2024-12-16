package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipaySettlementPo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipaySettlementPoExample;
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

public interface AlipaySettlementPoMapper {
    @Delete({
        "delete from alipay_settlement",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into alipay_settlement (`settle_no`, `out_request_no`, ",
        "`trade_no`, `out_trade_no`, ",
        "`amount_total`, `success_time`)",
        "values (#{settleNo,jdbcType=VARCHAR}, #{outRequestNo,jdbcType=VARCHAR}, ",
        "#{tradeNo,jdbcType=VARCHAR}, #{outTradeNo,jdbcType=VARCHAR}, ",
        "#{amountTotal,jdbcType=INTEGER}, #{successTime,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AlipaySettlementPo row);

    @InsertProvider(type=AlipaySettlementPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(AlipaySettlementPo row);

    @SelectProvider(type=AlipaySettlementPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="settle_no", property="settleNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount_total", property="amountTotal", jdbcType=JdbcType.INTEGER),
        @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<AlipaySettlementPo> selectByExample(AlipaySettlementPoExample example);

    @Select({
        "select",
        "`id`, `settle_no`, `out_request_no`, `trade_no`, `out_trade_no`, `amount_total`, ",
        "`success_time`",
        "from alipay_settlement",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="settle_no", property="settleNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trade_no", property="tradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="out_trade_no", property="outTradeNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount_total", property="amountTotal", jdbcType=JdbcType.INTEGER),
        @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP)
    })
    AlipaySettlementPo selectByPrimaryKey(Long id);

    @UpdateProvider(type=AlipaySettlementPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") AlipaySettlementPo row, @Param("example") AlipaySettlementPoExample example);

    @UpdateProvider(type=AlipaySettlementPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") AlipaySettlementPo row, @Param("example") AlipaySettlementPoExample example);

    @UpdateProvider(type=AlipaySettlementPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AlipaySettlementPo row);

    @Update({
        "update alipay_settlement",
        "set `settle_no` = #{settleNo,jdbcType=VARCHAR},",
          "`out_request_no` = #{outRequestNo,jdbcType=VARCHAR},",
          "`trade_no` = #{tradeNo,jdbcType=VARCHAR},",
          "`out_trade_no` = #{outTradeNo,jdbcType=VARCHAR},",
          "`amount_total` = #{amountTotal,jdbcType=INTEGER},",
          "`success_time` = #{successTime,jdbcType=TIMESTAMP}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AlipaySettlementPo row);
}