package cn.edu.xmu.oomall.alipay.mapper;

import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiver;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiverExample.Criteria;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiverExample.Criterion;
import cn.edu.xmu.oomall.alipay.mapper.po.AlipayDivReceiverExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class AlipayDivReceiverSqlProvider {
    public String deleteByExample(AlipayDivReceiverExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("alipay_div_receiver");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(AlipayDivReceiver row) {
        SQL sql = new SQL();
        sql.INSERT_INTO("alipay_div_receiver");
        
        if (row.getOutRequestNo() != null) {
            sql.VALUES("`out_request_no`", "#{outRequestNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTransOut() != null) {
            sql.VALUES("`trans_out`", "#{transOut,jdbcType=VARCHAR}");
        }
        
        if (row.getType() != null) {
            sql.VALUES("`type`", "#{type,jdbcType=VARCHAR}");
        }
        
        if (row.getAccount() != null) {
            sql.VALUES("`account`", "#{account,jdbcType=VARCHAR}");
        }
        
        if (row.getBindLoginName() != null) {
            sql.VALUES("`bind_login_name`", "#{bindLoginName,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExample(AlipayDivReceiverExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("`id`");
        } else {
            sql.SELECT("`id`");
        }
        sql.SELECT("`out_request_no`");
        sql.SELECT("`trans_out`");
        sql.SELECT("`type`");
        sql.SELECT("`account`");
        sql.SELECT("`bind_login_name`");
        sql.FROM("alipay_div_receiver");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        AlipayDivReceiver row = (AlipayDivReceiver) parameter.get("row");
        AlipayDivReceiverExample example = (AlipayDivReceiverExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("alipay_div_receiver");
        
        if (row.getId() != null) {
            sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        }
        
        if (row.getOutRequestNo() != null) {
            sql.SET("`out_request_no` = #{row.outRequestNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTransOut() != null) {
            sql.SET("`trans_out` = #{row.transOut,jdbcType=VARCHAR}");
        }
        
        if (row.getType() != null) {
            sql.SET("`type` = #{row.type,jdbcType=VARCHAR}");
        }
        
        if (row.getAccount() != null) {
            sql.SET("`account` = #{row.account,jdbcType=VARCHAR}");
        }
        
        if (row.getBindLoginName() != null) {
            sql.SET("`bind_login_name` = #{row.bindLoginName,jdbcType=VARCHAR}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_div_receiver");
        
        sql.SET("`id` = #{row.id,jdbcType=BIGINT}");
        sql.SET("`out_request_no` = #{row.outRequestNo,jdbcType=VARCHAR}");
        sql.SET("`trans_out` = #{row.transOut,jdbcType=VARCHAR}");
        sql.SET("`type` = #{row.type,jdbcType=VARCHAR}");
        sql.SET("`account` = #{row.account,jdbcType=VARCHAR}");
        sql.SET("`bind_login_name` = #{row.bindLoginName,jdbcType=VARCHAR}");
        
        AlipayDivReceiverExample example = (AlipayDivReceiverExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(AlipayDivReceiver row) {
        SQL sql = new SQL();
        sql.UPDATE("alipay_div_receiver");
        
        if (row.getOutRequestNo() != null) {
            sql.SET("`out_request_no` = #{outRequestNo,jdbcType=VARCHAR}");
        }
        
        if (row.getTransOut() != null) {
            sql.SET("`trans_out` = #{transOut,jdbcType=VARCHAR}");
        }
        
        if (row.getType() != null) {
            sql.SET("`type` = #{type,jdbcType=VARCHAR}");
        }
        
        if (row.getAccount() != null) {
            sql.SET("`account` = #{account,jdbcType=VARCHAR}");
        }
        
        if (row.getBindLoginName() != null) {
            sql.SET("`bind_login_name` = #{bindLoginName,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("`id` = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, AlipayDivReceiverExample example, boolean includeExamplePhrase) {
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