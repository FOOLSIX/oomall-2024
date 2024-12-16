package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiver;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiverExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
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

public interface AlipayDivReceiverMapper {
    @DeleteProvider(type=AlipayDivReceiverSqlProvider.class, method="deleteByExample")
    int deleteByExample(AlipayDivReceiverExample example);

    @Delete({
        "delete from alipay_div_receiver",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into alipay_div_receiver (`out_request_no`, `trans_out`, ",
        "`type`, `account`, `bind_login_name`)",
        "values (#{outRequestNo,jdbcType=VARCHAR}, #{transOut,jdbcType=VARCHAR}, ",
        "#{type,jdbcType=VARCHAR}, #{account,jdbcType=VARCHAR}, #{bindLoginName,jdbcType=VARCHAR})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insert(AlipayDivReceiver row);

    @InsertProvider(type=AlipayDivReceiverSqlProvider.class, method="insertSelective")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Long.class)
    int insertSelective(AlipayDivReceiver row);

    @SelectProvider(type=AlipayDivReceiverSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_out", property="transOut", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="account", property="account", jdbcType=JdbcType.VARCHAR),
        @Result(column="bind_login_name", property="bindLoginName", jdbcType=JdbcType.VARCHAR)
    })
    List<AlipayDivReceiver> selectByExample(AlipayDivReceiverExample example);

    @Select({
        "select",
        "`id`, `out_request_no`, `trans_out`, `type`, `account`, `bind_login_name`",
        "from alipay_div_receiver",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="out_request_no", property="outRequestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="trans_out", property="transOut", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="account", property="account", jdbcType=JdbcType.VARCHAR),
        @Result(column="bind_login_name", property="bindLoginName", jdbcType=JdbcType.VARCHAR)
    })
    AlipayDivReceiver selectByPrimaryKey(Long id);

    @UpdateProvider(type=AlipayDivReceiverSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("row") AlipayDivReceiver row, @Param("example") AlipayDivReceiverExample example);

    @UpdateProvider(type=AlipayDivReceiverSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("row") AlipayDivReceiver row, @Param("example") AlipayDivReceiverExample example);

    @UpdateProvider(type=AlipayDivReceiverSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AlipayDivReceiver row);

    @Update({
        "update alipay_div_receiver",
        "set `out_request_no` = #{outRequestNo,jdbcType=VARCHAR},",
          "`trans_out` = #{transOut,jdbcType=VARCHAR},",
          "`type` = #{type,jdbcType=VARCHAR},",
          "`account` = #{account,jdbcType=VARCHAR},",
          "`bind_login_name` = #{bindLoginName,jdbcType=VARCHAR}",
        "where `id` = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(AlipayDivReceiver row);
}