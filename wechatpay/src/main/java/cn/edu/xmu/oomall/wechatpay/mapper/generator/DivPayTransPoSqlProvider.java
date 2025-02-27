package cn.edu.xmu.oomall.wechatpay.mapper.generator;

import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivPayTransPo;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivPayTransPoExample.Criteria;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivPayTransPoExample.Criterion;
import cn.edu.xmu.oomall.wechatpay.mapper.generator.po.DivPayTransPoExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

@Repository
public class DivPayTransPoSqlProvider {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_div_pay_trans
     *
     * @mbg.generated
     */
    public String insertSelective(DivPayTransPo row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("wechatpay_div_pay_trans");
        
        if (row.getId() != null) {
            sql.VALUES("`id`", "#{id,jdbcType=BIGINT}");
        }
        
        if (row.getAppid() != null) {
            sql.VALUES("`appid`", "#{appid,jdbcType=VARCHAR}");
        }
        
        if (row.getSubMchid() != null) {
            sql.VALUES("`sub_mchid`", "#{subMchid,jdbcType=VARCHAR}");
        }
        
        if (row.getTransactionId() != null) {
            sql.VALUES("`transaction_id`", "#{transactionId,jdbcType=VARCHAR}");
        }
        
        if (row.getOutOrderNo() != null) {
            sql.VALUES("`out_order_no`", "#{outOrderNo,jdbcType=VARCHAR}");
        }
        
        if (row.getState() != null) {
            sql.VALUES("`state`", "#{state,jdbcType=VARCHAR}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.VALUES("`success_time`", "#{successTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_div_pay_trans
     *
     * @mbg.generated
     */
    public String selectByExample(DivPayTransPoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`appid`");
        sql.SELECT("`sub_mchid`");
        sql.SELECT("`transaction_id`");
        sql.SELECT("`out_order_no`");
        sql.SELECT("`order_id`");
        sql.SELECT("`state`");
        sql.SELECT("`success_time`");
        sql.FROM("wechatpay_div_pay_trans");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_div_pay_trans
     *
     * @mbg.generated
     */
    public String updateByExampleSelective(Map<String, Object> parameter) {
        DivPayTransPo row = (DivPayTransPo) parameter.get("row");
        DivPayTransPoExample example = (DivPayTransPoExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("wechatpay_div_pay_trans");
        
        if (row.getId() != null) {
            sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        }
        
        if (row.getAppid() != null) {
            sql.SET("`appid` = #{row.appid,jdbcType=VARCHAR}");
        }
        
        if (row.getSubMchid() != null) {
            sql.SET("`sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}");
        }
        
        if (row.getTransactionId() != null) {
            sql.SET("`transaction_id` = #{row.transactionId,jdbcType=VARCHAR}");
        }
        
        if (row.getOutOrderNo() != null) {
            sql.SET("`out_order_no` = #{row.outOrderNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOrderId() != null) {
            sql.SET("`order_id` = #{row.orderId,jdbcType=VARCHAR}");
        }
        
        if (row.getState() != null) {
            sql.SET("`state` = #{row.state,jdbcType=VARCHAR}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_div_pay_trans
     *
     * @mbg.generated
     */
    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("wechatpay_div_pay_trans");
        
        sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        sql.SET("`appid` = #{row.appid,jdbcType=VARCHAR}");
        sql.SET("`sub_mchid` = #{row.subMchid,jdbcType=VARCHAR}");
        sql.SET("`transaction_id` = #{row.transactionId,jdbcType=VARCHAR}");
        sql.SET("`out_order_no` = #{row.outOrderNo,jdbcType=VARCHAR}");
        sql.SET("`order_id` = #{row.orderId,jdbcType=VARCHAR}");
        sql.SET("`state` = #{row.state,jdbcType=VARCHAR}");
        sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        
        DivPayTransPoExample example = (DivPayTransPoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_div_pay_trans
     *
     * @mbg.generated
     */
    public String updateByPrimaryKeySelective(DivPayTransPo row) {
        SQL sql = new SQL();
        sql.UPDATE("wechatpay_div_pay_trans");
        
        if (row.getAppid() != null) {
            sql.SET("`appid` = #{appid,jdbcType=VARCHAR}");
        }
        
        if (row.getSubMchid() != null) {
            sql.SET("`sub_mchid` = #{subMchid,jdbcType=VARCHAR}");
        }
        
        if (row.getTransactionId() != null) {
            sql.SET("`transaction_id` = #{transactionId,jdbcType=VARCHAR}");
        }
        
        if (row.getOutOrderNo() != null) {
            sql.SET("`out_order_no` = #{outOrderNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOrderId() != null) {
            sql.SET("`order_id` = #{orderId,jdbcType=VARCHAR}");
        }
        
        if (row.getState() != null) {
            sql.SET("`state` = #{state,jdbcType=VARCHAR}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{successTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechatpay_div_pay_trans
     *
     * @mbg.generated
     */
    protected void applyWhere(SQL sql, DivPayTransPoExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}