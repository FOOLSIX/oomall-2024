package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPo;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPoExample.Criteria;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPoExample.Criterion;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayPaymentPoExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class AlipayPaymentPoSqlProvider {
    public String insertSelective(AlipayPaymentPo row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("alipay_payment");
        
        if (row.getAppid() != null) {
            sql.VALUES("`appid`", "#{appid,jdbcType=VARCHAR}");
        }
        
        if (row.getReceiverAccount() != null) {
            sql.VALUES("`receiver_account`", "#{receiverAccount,jdbcType=VARCHAR}");
        }
        
        if (row.getBuyerLogonId() != null) {
            sql.VALUES("`buyer_logon_id`", "#{buyerLogonId,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeNo() != null) {
            sql.VALUES("`trade_no`", "#{tradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutTradeNo() != null) {
            sql.VALUES("`out_trade_no`", "#{outTradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeStatus() != null) {
            sql.VALUES("`trade_status`", "#{tradeStatus,jdbcType=INTEGER}");
        }
        
        if (row.getTotalAmount() != null) {
            sql.VALUES("`total_amount`", "#{totalAmount,jdbcType=INTEGER}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.VALUES("`success_time`", "#{successTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String selectByExample(AlipayPaymentPoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`appid`");
        sql.SELECT("`receiver_account`");
        sql.SELECT("`buyer_logon_id`");
        sql.SELECT("`trade_no`");
        sql.SELECT("`out_trade_no`");
        sql.SELECT("`trade_status`");
        sql.SELECT("`total_amount`");
        sql.SELECT("`success_time`");
        sql.FROM("alipay_payment");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        AlipayPaymentPo row = (AlipayPaymentPo) parameter.get("row");
        AlipayPaymentPoExample example = (AlipayPaymentPoExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("alipay_payment");
        
        if (row.getId() != null) {
            sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        }
        
        if (row.getAppid() != null) {
            sql.SET("`appid` = #{row.appid,jdbcType=VARCHAR}");
        }
        
        if (row.getReceiverAccount() != null) {
            sql.SET("`receiver_account` = #{row.receiverAccount,jdbcType=VARCHAR}");
        }
        
        if (row.getBuyerLogonId() != null) {
            sql.SET("`buyer_logon_id` = #{row.buyerLogonId,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeNo() != null) {
            sql.SET("`trade_no` = #{row.tradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutTradeNo() != null) {
            sql.SET("`out_trade_no` = #{row.outTradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeStatus() != null) {
            sql.SET("`trade_status` = #{row.tradeStatus,jdbcType=INTEGER}");
        }
        
        if (row.getTotalAmount() != null) {
            sql.SET("`total_amount` = #{row.totalAmount,jdbcType=INTEGER}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_payment");
        
        sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        sql.SET("`appid` = #{row.appid,jdbcType=VARCHAR}");
        sql.SET("`receiver_account` = #{row.receiverAccount,jdbcType=VARCHAR}");
        sql.SET("`buyer_logon_id` = #{row.buyerLogonId,jdbcType=VARCHAR}");
        sql.SET("`trade_no` = #{row.tradeNo,jdbcType=VARCHAR}");
        sql.SET("`out_trade_no` = #{row.outTradeNo,jdbcType=VARCHAR}");
        sql.SET("`trade_status` = #{row.tradeStatus,jdbcType=INTEGER}");
        sql.SET("`total_amount` = #{row.totalAmount,jdbcType=INTEGER}");
        sql.SET("`success_time` = #{row.successTime,jdbcType=TIMESTAMP}");
        
        AlipayPaymentPoExample example = (AlipayPaymentPoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(AlipayPaymentPo row) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_payment");
        
        if (row.getAppid() != null) {
            sql.SET("`appid` = #{appid,jdbcType=VARCHAR}");
        }
        
        if (row.getReceiverAccount() != null) {
            sql.SET("`receiver_account` = #{receiverAccount,jdbcType=VARCHAR}");
        }
        
        if (row.getBuyerLogonId() != null) {
            sql.SET("`buyer_logon_id` = #{buyerLogonId,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeNo() != null) {
            sql.SET("`trade_no` = #{tradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getOutTradeNo() != null) {
            sql.SET("`out_trade_no` = #{outTradeNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTradeStatus() != null) {
            sql.SET("`trade_status` = #{tradeStatus,jdbcType=INTEGER}");
        }
        
        if (row.getTotalAmount() != null) {
            sql.SET("`total_amount` = #{totalAmount,jdbcType=INTEGER}");
        }
        
        if (row.getSuccessTime() != null) {
            sql.SET("`success_time` = #{successTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, AlipayPaymentPoExample example, boolean includeExamplePhrase) {
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