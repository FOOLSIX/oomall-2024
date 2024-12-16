package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRoyaltyDetail;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRoyaltyDetailExample;
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

public interface AlipayRoyaltyDetailMapper {
    @Delete({
        "delete from alipay_royalty_detail",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into alipay_royalty_detail (`trans_in`, `trans_out`, ",
        "`amount`, `settle_no`)",
        "values (#{transIn,jdbcType=VARCHAR}, #{transOut,jdbcType=VARCHAR}, ",
        "#{amount,jdbcType=INTEGER}, #{settleNo,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AlipayRoyaltyDetail row);

    @InsertProvider(type=AlipayRoyaltyDetailSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(AlipayRoyaltyDetail row);

    @SelectProvider(type=AlipayRoyaltyDetailSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="trans_in", property="transIn", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_out", property="transOut", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
        @Result(column="settle_no", property="settleNo", jdbcType=JdbcType.VARCHAR)
    })
    List<AlipayRoyaltyDetail> selectByExample(AlipayRoyaltyDetailExample example);

    @Select({
        "select",
        "`id`, `trans_in`, `trans_out`, `amount`, `settle_no`",
        "from alipay_royalty_detail",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="trans_in", property="transIn", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_out", property="transOut", jdbcType=JdbcType.VARCHAR),
        @Result(column="amount", property="amount", jdbcType=JdbcType.INTEGER),
        @Result(column="settle_no", property="settleNo", jdbcType=JdbcType.VARCHAR)
    })
    AlipayRoyaltyDetail selectByPrimaryKey(Long id);

    @UpdateProvider(type=AlipayRoyaltyDetailSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") AlipayRoyaltyDetail row, @Param("example") AlipayRoyaltyDetailExample example);

    @UpdateProvider(type=AlipayRoyaltyDetailSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") AlipayRoyaltyDetail row, @Param("example") AlipayRoyaltyDetailExample example);

    @UpdateProvider(type=AlipayRoyaltyDetailSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AlipayRoyaltyDetail row);

    @Update({
        "update alipay_royalty_detail",
        "set `trans_in` = #{transIn,jdbcType=VARCHAR},",
          "`trans_out` = #{transOut,jdbcType=VARCHAR},",
          "`amount` = #{amount,jdbcType=INTEGER},",
          "`settle_no` = #{settleNo,jdbcType=VARCHAR}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AlipayRoyaltyDetail row);
}