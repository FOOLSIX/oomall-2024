package cn.edu.xmu.oomall.payment.mapper.manual;

import cn.edu.xmu.oomall.payment.mapper.generator.po.RefundTransPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface RefundTransPoManualMapper {
    @SelectProvider(type= RefundTransPoManualSqlProvider.class, method="selectByExample")
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="out_no", property="outNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="trans_no", property="transNo", jdbcType=JdbcType.VARCHAR),
            @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
            @Result(column="status", property="status", jdbcType=JdbcType.TINYINT),
            @Result(column="success_time", property="successTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="adjust_id", property="adjustId", jdbcType=JdbcType.BIGINT),
            @Result(column="adjust_name", property="adjustName", jdbcType=JdbcType.VARCHAR),
            @Result(column="adjust_time", property="adjustTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="user_received_account", property="userReceivedAccount", jdbcType=JdbcType.VARCHAR),
            @Result(column="pay_trans_id", property="payTransId", jdbcType=JdbcType.BIGINT),
            @Result(column="account_id", property="accountId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
            @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
            @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
            @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="div_amount", property="divAmount", jdbcType=JdbcType.BIGINT),
            @Result(column="shop_id", property="shopId", jdbcType=JdbcType.BIGINT),
            @Result(column="ledger_id", property="ledgerId", jdbcType=JdbcType.BIGINT)
    })
    List<RefundTransPo> selectByExample(RefundTransPoManualExample example);
}
