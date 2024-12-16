package cn.edu.xmu.oomall.alipay.mapper.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlipayRefundRoyaltyDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlipayRefundRoyaltyDetailExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("`id` is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("`id` is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("`id` =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("`id` <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("`id` >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("`id` >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("`id` <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("`id` <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("`id` in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("`id` not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("`id` between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("`id` not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoIsNull() {
            addCriterion("`out_request_no` is null");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoIsNotNull() {
            addCriterion("`out_request_no` is not null");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoEqualTo(String value) {
            addCriterion("`out_request_no` =", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoNotEqualTo(String value) {
            addCriterion("`out_request_no` <>", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoGreaterThan(String value) {
            addCriterion("`out_request_no` >", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoGreaterThanOrEqualTo(String value) {
            addCriterion("`out_request_no` >=", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoLessThan(String value) {
            addCriterion("`out_request_no` <", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoLessThanOrEqualTo(String value) {
            addCriterion("`out_request_no` <=", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoLike(String value) {
            addCriterion("`out_request_no` like", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoNotLike(String value) {
            addCriterion("`out_request_no` not like", value, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoIn(List<String> values) {
            addCriterion("`out_request_no` in", values, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoNotIn(List<String> values) {
            addCriterion("`out_request_no` not in", values, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoBetween(String value1, String value2) {
            addCriterion("`out_request_no` between", value1, value2, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutRequestNoNotBetween(String value1, String value2) {
            addCriterion("`out_request_no` not between", value1, value2, "outRequestNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoIsNull() {
            addCriterion("`out_trade_no` is null");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoIsNotNull() {
            addCriterion("`out_trade_no` is not null");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoEqualTo(String value) {
            addCriterion("`out_trade_no` =", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoNotEqualTo(String value) {
            addCriterion("`out_trade_no` <>", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoGreaterThan(String value) {
            addCriterion("`out_trade_no` >", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoGreaterThanOrEqualTo(String value) {
            addCriterion("`out_trade_no` >=", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoLessThan(String value) {
            addCriterion("`out_trade_no` <", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoLessThanOrEqualTo(String value) {
            addCriterion("`out_trade_no` <=", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoLike(String value) {
            addCriterion("`out_trade_no` like", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoNotLike(String value) {
            addCriterion("`out_trade_no` not like", value, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoIn(List<String> values) {
            addCriterion("`out_trade_no` in", values, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoNotIn(List<String> values) {
            addCriterion("`out_trade_no` not in", values, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoBetween(String value1, String value2) {
            addCriterion("`out_trade_no` between", value1, value2, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andOutTradeNoNotBetween(String value1, String value2) {
            addCriterion("`out_trade_no` not between", value1, value2, "outTradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoIsNull() {
            addCriterion("`trade_no` is null");
            return (Criteria) this;
        }

        public Criteria andTradeNoIsNotNull() {
            addCriterion("`trade_no` is not null");
            return (Criteria) this;
        }

        public Criteria andTradeNoEqualTo(String value) {
            addCriterion("`trade_no` =", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoNotEqualTo(String value) {
            addCriterion("`trade_no` <>", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoGreaterThan(String value) {
            addCriterion("`trade_no` >", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoGreaterThanOrEqualTo(String value) {
            addCriterion("`trade_no` >=", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoLessThan(String value) {
            addCriterion("`trade_no` <", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoLessThanOrEqualTo(String value) {
            addCriterion("`trade_no` <=", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoLike(String value) {
            addCriterion("`trade_no` like", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoNotLike(String value) {
            addCriterion("`trade_no` not like", value, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoIn(List<String> values) {
            addCriterion("`trade_no` in", values, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoNotIn(List<String> values) {
            addCriterion("`trade_no` not in", values, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoBetween(String value1, String value2) {
            addCriterion("`trade_no` between", value1, value2, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTradeNoNotBetween(String value1, String value2) {
            addCriterion("`trade_no` not between", value1, value2, "tradeNo");
            return (Criteria) this;
        }

        public Criteria andTransInIsNull() {
            addCriterion("`trans_in` is null");
            return (Criteria) this;
        }

        public Criteria andTransInIsNotNull() {
            addCriterion("`trans_in` is not null");
            return (Criteria) this;
        }

        public Criteria andTransInEqualTo(String value) {
            addCriterion("`trans_in` =", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInNotEqualTo(String value) {
            addCriterion("`trans_in` <>", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInGreaterThan(String value) {
            addCriterion("`trans_in` >", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInGreaterThanOrEqualTo(String value) {
            addCriterion("`trans_in` >=", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInLessThan(String value) {
            addCriterion("`trans_in` <", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInLessThanOrEqualTo(String value) {
            addCriterion("`trans_in` <=", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInLike(String value) {
            addCriterion("`trans_in` like", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInNotLike(String value) {
            addCriterion("`trans_in` not like", value, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInIn(List<String> values) {
            addCriterion("`trans_in` in", values, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInNotIn(List<String> values) {
            addCriterion("`trans_in` not in", values, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInBetween(String value1, String value2) {
            addCriterion("`trans_in` between", value1, value2, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransInNotBetween(String value1, String value2) {
            addCriterion("`trans_in` not between", value1, value2, "transIn");
            return (Criteria) this;
        }

        public Criteria andTransOutIsNull() {
            addCriterion("`trans_out` is null");
            return (Criteria) this;
        }

        public Criteria andTransOutIsNotNull() {
            addCriterion("`trans_out` is not null");
            return (Criteria) this;
        }

        public Criteria andTransOutEqualTo(String value) {
            addCriterion("`trans_out` =", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutNotEqualTo(String value) {
            addCriterion("`trans_out` <>", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutGreaterThan(String value) {
            addCriterion("`trans_out` >", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutGreaterThanOrEqualTo(String value) {
            addCriterion("`trans_out` >=", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutLessThan(String value) {
            addCriterion("`trans_out` <", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutLessThanOrEqualTo(String value) {
            addCriterion("`trans_out` <=", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutLike(String value) {
            addCriterion("`trans_out` like", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutNotLike(String value) {
            addCriterion("`trans_out` not like", value, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutIn(List<String> values) {
            addCriterion("`trans_out` in", values, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutNotIn(List<String> values) {
            addCriterion("`trans_out` not in", values, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutBetween(String value1, String value2) {
            addCriterion("`trans_out` between", value1, value2, "transOut");
            return (Criteria) this;
        }

        public Criteria andTransOutNotBetween(String value1, String value2) {
            addCriterion("`trans_out` not between", value1, value2, "transOut");
            return (Criteria) this;
        }

        public Criteria andRefundAmountIsNull() {
            addCriterion("`refund_amount` is null");
            return (Criteria) this;
        }

        public Criteria andRefundAmountIsNotNull() {
            addCriterion("`refund_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andRefundAmountEqualTo(Integer value) {
            addCriterion("`refund_amount` =", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountNotEqualTo(Integer value) {
            addCriterion("`refund_amount` <>", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountGreaterThan(Integer value) {
            addCriterion("`refund_amount` >", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountGreaterThanOrEqualTo(Integer value) {
            addCriterion("`refund_amount` >=", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountLessThan(Integer value) {
            addCriterion("`refund_amount` <", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountLessThanOrEqualTo(Integer value) {
            addCriterion("`refund_amount` <=", value, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountIn(List<Integer> values) {
            addCriterion("`refund_amount` in", values, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountNotIn(List<Integer> values) {
            addCriterion("`refund_amount` not in", values, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountBetween(Integer value1, Integer value2) {
            addCriterion("`refund_amount` between", value1, value2, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRefundAmountNotBetween(Integer value1, Integer value2) {
            addCriterion("`refund_amount` not between", value1, value2, "refundAmount");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeIsNull() {
            addCriterion("`royalty_type` is null");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeIsNotNull() {
            addCriterion("`royalty_type` is not null");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeEqualTo(String value) {
            addCriterion("`royalty_type` =", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeNotEqualTo(String value) {
            addCriterion("`royalty_type` <>", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeGreaterThan(String value) {
            addCriterion("`royalty_type` >", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeGreaterThanOrEqualTo(String value) {
            addCriterion("`royalty_type` >=", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeLessThan(String value) {
            addCriterion("`royalty_type` <", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeLessThanOrEqualTo(String value) {
            addCriterion("`royalty_type` <=", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeLike(String value) {
            addCriterion("`royalty_type` like", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeNotLike(String value) {
            addCriterion("`royalty_type` not like", value, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeIn(List<String> values) {
            addCriterion("`royalty_type` in", values, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeNotIn(List<String> values) {
            addCriterion("`royalty_type` not in", values, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeBetween(String value1, String value2) {
            addCriterion("`royalty_type` between", value1, value2, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andRoyaltyTypeNotBetween(String value1, String value2) {
            addCriterion("`royalty_type` not between", value1, value2, "royaltyType");
            return (Criteria) this;
        }

        public Criteria andResultCodeIsNull() {
            addCriterion("`result_code` is null");
            return (Criteria) this;
        }

        public Criteria andResultCodeIsNotNull() {
            addCriterion("`result_code` is not null");
            return (Criteria) this;
        }

        public Criteria andResultCodeEqualTo(String value) {
            addCriterion("`result_code` =", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeNotEqualTo(String value) {
            addCriterion("`result_code` <>", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeGreaterThan(String value) {
            addCriterion("`result_code` >", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeGreaterThanOrEqualTo(String value) {
            addCriterion("`result_code` >=", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeLessThan(String value) {
            addCriterion("`result_code` <", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeLessThanOrEqualTo(String value) {
            addCriterion("`result_code` <=", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeLike(String value) {
            addCriterion("`result_code` like", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeNotLike(String value) {
            addCriterion("`result_code` not like", value, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeIn(List<String> values) {
            addCriterion("`result_code` in", values, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeNotIn(List<String> values) {
            addCriterion("`result_code` not in", values, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeBetween(String value1, String value2) {
            addCriterion("`result_code` between", value1, value2, "resultCode");
            return (Criteria) this;
        }

        public Criteria andResultCodeNotBetween(String value1, String value2) {
            addCriterion("`result_code` not between", value1, value2, "resultCode");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayIsNull() {
            addCriterion("`gmt_refund_pay` is null");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayIsNotNull() {
            addCriterion("`gmt_refund_pay` is not null");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayEqualTo(LocalDateTime value) {
            addCriterion("`gmt_refund_pay` =", value, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayNotEqualTo(LocalDateTime value) {
            addCriterion("`gmt_refund_pay` <>", value, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayGreaterThan(LocalDateTime value) {
            addCriterion("`gmt_refund_pay` >", value, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`gmt_refund_pay` >=", value, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayLessThan(LocalDateTime value) {
            addCriterion("`gmt_refund_pay` <", value, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`gmt_refund_pay` <=", value, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayIn(List<LocalDateTime> values) {
            addCriterion("`gmt_refund_pay` in", values, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayNotIn(List<LocalDateTime> values) {
            addCriterion("`gmt_refund_pay` not in", values, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`gmt_refund_pay` between", value1, value2, "gmtRefundPay");
            return (Criteria) this;
        }

        public Criteria andGmtRefundPayNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`gmt_refund_pay` not between", value1, value2, "gmtRefundPay");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}