package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRoyaltyDetail;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRoyaltyDetailExample.Criteria;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRoyaltyDetailExample.Criterion;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayRoyaltyDetailExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class AlipayRoyaltyDetailSqlProvider {
    public String insertSelective(AlipayRoyaltyDetail row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("alipay_royalty_detail");
        
        if (row.getTransIn() != null) {
            sql.VALUES("`trans_in`", "#{transIn,jdbcType=VARCHAR}");
        }
        
        if (row.getTransOut() != null) {
            sql.VALUES("`trans_out`", "#{transOut,jdbcType=VARCHAR}");
        }
        
        if (row.getAmount() != null) {
            sql.VALUES("`amount`", "#{amount,jdbcType=INTEGER}");
        }
        
        if (row.getSettleNo() != null) {
            sql.VALUES("`settle_no`", "#{settleNo,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExample(AlipayRoyaltyDetailExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`trans_in`");
        sql.SELECT("`trans_out`");
        sql.SELECT("`amount`");
        sql.SELECT("`settle_no`");
        sql.FROM("alipay_royalty_detail");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        AlipayRoyaltyDetail row = (AlipayRoyaltyDetail) parameter.get("row");
        AlipayRoyaltyDetailExample example = (AlipayRoyaltyDetailExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("alipay_royalty_detail");
        
        if (row.getId() != null) {
            sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        }
        
        if (row.getTransIn() != null) {
            sql.SET("`trans_in` = #{row.transIn,jdbcType=VARCHAR}");
        }
        
        if (row.getTransOut() != null) {
            sql.SET("`trans_out` = #{row.transOut,jdbcType=VARCHAR}");
        }
        
        if (row.getAmount() != null) {
            sql.SET("`amount` = #{row.amount,jdbcType=INTEGER}");
        }
        
        if (row.getSettleNo() != null) {
            sql.SET("`settle_no` = #{row.settleNo,jdbcType=VARCHAR}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_royalty_detail");
        
        sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        sql.SET("`trans_in` = #{row.transIn,jdbcType=VARCHAR}");
        sql.SET("`trans_out` = #{row.transOut,jdbcType=VARCHAR}");
        sql.SET("`amount` = #{row.amount,jdbcType=INTEGER}");
        sql.SET("`settle_no` = #{row.settleNo,jdbcType=VARCHAR}");
        
        AlipayRoyaltyDetailExample example = (AlipayRoyaltyDetailExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(AlipayRoyaltyDetail row) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_royalty_detail");
        
        if (row.getTransIn() != null) {
            sql.SET("`trans_in` = #{transIn,jdbcType=VARCHAR}");
        }
        
        if (row.getTransOut() != null) {
            sql.SET("`trans_out` = #{transOut,jdbcType=VARCHAR}");
        }
        
        if (row.getAmount() != null) {
            sql.SET("`amount` = #{amount,jdbcType=INTEGER}");
        }
        
        if (row.getSettleNo() != null) {
            sql.SET("`settle_no` = #{settleNo,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, AlipayRoyaltyDetailExample example, boolean includeExamplePhrase) {
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