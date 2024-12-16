package cn.edu.xmu.oomall.payment.mapper.manual;

import cn.edu.xmu.oomall.payment.mapper.generator.po.PayTransPo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface PayTransPoManualMapper {

    @SelectProvider(type= PayTransPosManualSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="out_no", property="outNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="trans_no", property="transNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.TINYINT),
            @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="adjust_id", property="adjustId", jdbcType=JdbcType.BIGINT),
            @Result(column="adjust_name", property="adjustName", jdbcType=JdbcType.VARCHAR),
            @Result(column="adjust_time", property="adjustTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="sp_openid", property="spOpenid", jdbcType=JdbcType.VARCHAR),
            @Result(column="time_expire", property="timeExpire", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="time_begin", property="timeBegin", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="account_id", property="accountId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="prepay_id", property="prepayId", jdbcType=JdbcType.VARCHAR),
            @Result(column="div_amount", property="divAmount", jdbcType=JdbcType.BIGINT),
            @Result(column="in_refund", property="inRefund", jdbcType=JdbcType.TINYINT),
            @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
            @Result(column="description", property="description", jdbcType=JdbcType.VARCHAR),
            @Result(column="ledger_id", property="ledgerId", jdbcType=JdbcType.BIGINT)
    })
    List<PayTransPo> selectByExample(PayTransPoManualExample example);
}
