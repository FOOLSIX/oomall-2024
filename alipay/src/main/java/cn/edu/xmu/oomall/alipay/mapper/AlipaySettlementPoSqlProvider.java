package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipaySettlementPo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipaySettlementPoExample.Criteria;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipaySettlementPoExample.Criterion;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipaySettlementPoExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class AlipaySettlementPoSqlProvider {
    public String insertSelective(AlipaySettlementPo row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("alipay_settlement");
        
        if (row.getSettleNo() != null) {
            sql.VALUES("`settle_no`", "#{settleNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutRequestNo() != null) {
            sql.VALUES("`out_request_no`", "#{outRequestNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeNo() != null) {
            sql.VALUES("`trade_no`", "#{tradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutTradeNo() != null) {
            sql.VALUES("`out_trade_no`", "#{outTradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getAmountTotal() != null) {
            sql.VALUES("`amount_total`", "#{amountTotal,jdbcType=INTEGER}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.VALUES("`success_time`", "#{successTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String selectByExample(AlipaySettlementPoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`settle_no`");
        sql.SELECT("`out_request_no`");
        sql.SELECT("`trade_no`");
        sql.SELECT("`out_trade_no`");
        sql.SELECT("`amount_total`");
        sql.SELECT("`success_time`");
        sql.FROM("alipay_settlement");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        AlipaySettlementPo row = (AlipaySettlementPo) parameter.get("row");
        AlipaySettlementPoExample example = (AlipaySettlementPoExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("alipay_settlement");
        
        if (row.getId() != null) {
            sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        }
        
        if (row.getSettleNo() != null) {
            sql.SET("`settle_no` = #{row.settleNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutRequestNo() != null) {
            sql.SET("`out_request_no` = #{row.outRequestNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeNo() != null) {
            sql.SET("`trade_no` = #{row.tradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutTradeNo() != null) {
            sql.SET("`out_trade_no` = #{row.outTradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getAmountTotal() != null) {
            sql.SET("`amount_total` = #{row.amountTotal,jdbcType=INTEGER}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_settlement");
        
        sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        sql.SET("`settle_no` = #{row.settleNo,jdbcType=VARCHAR}");
        sql.SET("`out_request_no` = #{row.outRequestNo,jdbcType=VARCHAR}");
        sql.SET("`trade_no` = #{row.tradeNo,jdbcType=VARCHAR}");
        sql.SET("`out_trade_no` = #{row.outTradeNo,jdbcType=VARCHAR}");
        sql.SET("`amount_total` = #{row.amountTotal,jdbcType=INTEGER}");
        sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        
        AlipaySettlementPoExample example = (AlipaySettlementPoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(AlipaySettlementPo row) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_settlement");
        
        if (row.getSettleNo() != null) {
            sql.SET("`settle_no` = #{settleNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutRequestNo() != null) {
            sql.SET("`out_request_no` = #{outRequestNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeNo() != null) {
            sql.SET("`trade_no` = #{tradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutTradeNo() != null) {
            sql.SET("`out_trade_no` = #{outTradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getAmountTotal() != null) {
            sql.SET("`amount_total` = #{amountTotal,jdbcType=INTEGER}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{successTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, AlipaySettlementPoExample example, boolean includeExamplePhrase) {
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