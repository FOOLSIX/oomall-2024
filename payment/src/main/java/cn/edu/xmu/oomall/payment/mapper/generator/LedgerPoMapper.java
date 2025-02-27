package cn.edu.xmu.oomall.payment.mapper.generator;

import cn.edu.xmu.oomall.payment.mapper.generator.po.LedgerPo;
import cn.edu.xmu.oomall.payment.mapper.generator.po.LedgerPoExample;
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

public interface LedgerPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @Delete({
        "delete from payment_ledger",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @Insert({
        "insert into payment_ledger (`out_no`, `trans_no`, ",
        "`amount`, `account_id`, ",
        "`check_time`, `creator_id`, ",
        "`creator_name`, `modifier_id`, ",
        "`modifier_name`, `gmt_create`, ",
        "`gmt_modified`)",
        "values (#{outNo,jdbcType=VARCHAR}, #{transNo,jdbcType=VARCHAR}, ",
        "#{amount,jdbcType=BIGINT}, #{accountId,jdbcType=BIGINT}, ",
        "#{checkTime,jdbcType=TIMESTAMP}, #{creatorId,jdbcType=BIGINT}, ",
        "#{creatorName,jdbcType=VARCHAR}, #{modifierId,jdbcType=BIGINT}, ",
        "#{modifierName,jdbcType=VARCHAR}, #{gmtCreate,jdbcType=TIMESTAMP}, ",
        "#{gmtModified,jdbcType=TIMESTAMP})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(LedgerPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @InsertProvider(type=LedgerPoSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(LedgerPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @SelectProvider(type=LedgerPoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_no", property="outNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_no", property="transNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
        @Result(column="account_id", property="accountId", jdbcType=JdbcType.BIGINT),
        @Result(column="check_time", property="checkTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    List<LedgerPo> selectByExample(LedgerPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @Select({
        "select",
        "`id`, `out_no`, `trans_no`, `amount`, `account_id`, `check_time`, `creator_id`, ",
        "`creator_name`, `modifier_id`, `modifier_name`, `gmt_create`, `gmt_modified`",
        "from payment_ledger",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_no", property="outNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_no", property="transNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.BIGINT),
        @Result(column="account_id", property="accountId", jdbcType=JdbcType.BIGINT),
        @Result(column="check_time", property="checkTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="creator_id", property="creatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="creator_name", property="creatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="modifier_id", property="modifierId", jdbcType=JdbcType.BIGINT),
        @Result(column="modifier_name", property="modifierName", jdbcType=JdbcType.VARCHAR),
        @Result(column="gmt_create", property="gmtCreate", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="gmt_modified", property="gmtModified", jdbcType=JdbcType.TIMESTAMP)
    })
    LedgerPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @UpdateProvider(type=LedgerPoSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") LedgerPo row, @Param("example") LedgerPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @UpdateProvider(type=LedgerPoSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") LedgerPo row, @Param("example") LedgerPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @UpdateProvider(type=LedgerPoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(LedgerPo row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table payment_ledger
     *
     * @mbg.generated
     */
    @Update({
        "update payment_ledger",
        "set `out_no` = #{outNo,jdbcType=VARCHAR},",
          "`trans_no` = #{transNo,jdbcType=VARCHAR},",
          "`amount` = #{amount,jdbcType=BIGINT},",
          "`account_id` = #{accountId,jdbcType=BIGINT},",
          "`check_time` = #{checkTime,jdbcType=TIMESTAMP},",
          "`creator_id` = #{creatorId,jdbcType=BIGINT},",
          "`creator_name` = #{creatorName,jdbcType=VARCHAR},",
          "`modifier_id` = #{modifierId,jdbcType=BIGINT},",
          "`modifier_name` = #{modifierName,jdbcType=VARCHAR},",
          "`gmt_create` = #{gmtCreate,jdbcType=TIMESTAMP},",
          "`gmt_modified` = #{gmtModified,jdbcType=TIMESTAMP}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(LedgerPo row);
}