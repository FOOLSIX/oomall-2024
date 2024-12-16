package cn.edu.xmu.oomall.alipay.mapper.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlipaySettlementPoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlipaySettlementPoExample() {
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

        public Criteria andSettleNoIsNull() {
            addCriterion("`settle_no` is null");
            return (Criteria) this;
        }

        public Criteria andSettleNoIsNotNull() {
            addCriterion("`settle_no` is not null");
            return (Criteria) this;
        }

        public Criteria andSettleNoEqualTo(String value) {
            addCriterion("`settle_no` =", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoNotEqualTo(String value) {
            addCriterion("`settle_no` <>", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoGreaterThan(String value) {
            addCriterion("`settle_no` >", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoGreaterThanOrEqualTo(String value) {
            addCriterion("`settle_no` >=", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoLessThan(String value) {
            addCriterion("`settle_no` <", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoLessThanOrEqualTo(String value) {
            addCriterion("`settle_no` <=", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoLike(String value) {
            addCriterion("`settle_no` like", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoNotLike(String value) {
            addCriterion("`settle_no` not like", value, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoIn(List<String> values) {
            addCriterion("`settle_no` in", values, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoNotIn(List<String> values) {
            addCriterion("`settle_no` not in", values, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoBetween(String value1, String value2) {
            addCriterion("`settle_no` between", value1, value2, "settleNo");
            return (Criteria) this;
        }

        public Criteria andSettleNoNotBetween(String value1, String value2) {
            addCriterion("`settle_no` not between", value1, value2, "settleNo");
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

        public Criteria andAmountTotalIsNull() {
            addCriterion("`amount_total` is null");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIsNotNull() {
            addCriterion("`amount_total` is not null");
            return (Criteria) this;
        }

        public Criteria andAmountTotalEqualTo(Integer value) {
            addCriterion("`amount_total` =", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotEqualTo(Integer value) {
            addCriterion("`amount_total` <>", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalGreaterThan(Integer value) {
            addCriterion("`amount_total` >", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("`amount_total` >=", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalLessThan(Integer value) {
            addCriterion("`amount_total` <", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalLessThanOrEqualTo(Integer value) {
            addCriterion("`amount_total` <=", value, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalIn(List<Integer> values) {
            addCriterion("`amount_total` in", values, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotIn(List<Integer> values) {
            addCriterion("`amount_total` not in", values, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalBetween(Integer value1, Integer value2) {
            addCriterion("`amount_total` between", value1, value2, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andAmountTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("`amount_total` not between", value1, value2, "amountTotal");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeIsNull() {
            addCriterion("`success_time` is null");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeIsNotNull() {
            addCriterion("`success_time` is not null");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeEqualTo(LocalDateTime value) {
            addCriterion("`success_time` =", value, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeNotEqualTo(LocalDateTime value) {
            addCriterion("`success_time` <>", value, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeGreaterThan(LocalDateTime value) {
            addCriterion("`success_time` >", value, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeGreaterThanOrEqualTo(LocalDateTime value) {
            addCriterion("`success_time` >=", value, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeLessThan(LocalDateTime value) {
            addCriterion("`success_time` <", value, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeLessThanOrEqualTo(LocalDateTime value) {
            addCriterion("`success_time` <=", value, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeIn(List<LocalDateTime> values) {
            addCriterion("`success_time` in", values, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeNotIn(List<LocalDateTime> values) {
            addCriterion("`success_time` not in", values, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`success_time` between", value1, value2, "successTime");
            return (Criteria) this;
        }

        public Criteria andSuccessTimeNotBetween(LocalDateTime value1, LocalDateTime value2) {
            addCriterion("`success_time` not between", value1, value2, "successTime");
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