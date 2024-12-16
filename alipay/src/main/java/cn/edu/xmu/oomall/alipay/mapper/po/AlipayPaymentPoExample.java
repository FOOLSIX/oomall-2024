package cn.edu.xmu.oomall.alipay.mapper.po;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlipayPaymentPoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlipayPaymentPoExample() {
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

        public Criteria andAppidIsNull() {
            addCriterion("`appid` is null");
            return (Criteria) this;
        }

        public Criteria andAppidIsNotNull() {
            addCriterion("`appid` is not null");
            return (Criteria) this;
        }

        public Criteria andAppidEqualTo(String value) {
            addCriterion("`appid` =", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotEqualTo(String value) {
            addCriterion("`appid` <>", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidGreaterThan(String value) {
            addCriterion("`appid` >", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidGreaterThanOrEqualTo(String value) {
            addCriterion("`appid` >=", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLessThan(String value) {
            addCriterion("`appid` <", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLessThanOrEqualTo(String value) {
            addCriterion("`appid` <=", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLike(String value) {
            addCriterion("`appid` like", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotLike(String value) {
            addCriterion("`appid` not like", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidIn(List<String> values) {
            addCriterion("`appid` in", values, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotIn(List<String> values) {
            addCriterion("`appid` not in", values, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidBetween(String value1, String value2) {
            addCriterion("`appid` between", value1, value2, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotBetween(String value1, String value2) {
            addCriterion("`appid` not between", value1, value2, "appid");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountIsNull() {
            addCriterion("`receiver_account` is null");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountIsNotNull() {
            addCriterion("`receiver_account` is not null");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountEqualTo(String value) {
            addCriterion("`receiver_account` =", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountNotEqualTo(String value) {
            addCriterion("`receiver_account` <>", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountGreaterThan(String value) {
            addCriterion("`receiver_account` >", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountGreaterThanOrEqualTo(String value) {
            addCriterion("`receiver_account` >=", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountLessThan(String value) {
            addCriterion("`receiver_account` <", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountLessThanOrEqualTo(String value) {
            addCriterion("`receiver_account` <=", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountLike(String value) {
            addCriterion("`receiver_account` like", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountNotLike(String value) {
            addCriterion("`receiver_account` not like", value, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountIn(List<String> values) {
            addCriterion("`receiver_account` in", values, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountNotIn(List<String> values) {
            addCriterion("`receiver_account` not in", values, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountBetween(String value1, String value2) {
            addCriterion("`receiver_account` between", value1, value2, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andReceiverAccountNotBetween(String value1, String value2) {
            addCriterion("`receiver_account` not between", value1, value2, "receiverAccount");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdIsNull() {
            addCriterion("`buyer_logon_id` is null");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdIsNotNull() {
            addCriterion("`buyer_logon_id` is not null");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdEqualTo(String value) {
            addCriterion("`buyer_logon_id` =", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdNotEqualTo(String value) {
            addCriterion("`buyer_logon_id` <>", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdGreaterThan(String value) {
            addCriterion("`buyer_logon_id` >", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdGreaterThanOrEqualTo(String value) {
            addCriterion("`buyer_logon_id` >=", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdLessThan(String value) {
            addCriterion("`buyer_logon_id` <", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdLessThanOrEqualTo(String value) {
            addCriterion("`buyer_logon_id` <=", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdLike(String value) {
            addCriterion("`buyer_logon_id` like", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdNotLike(String value) {
            addCriterion("`buyer_logon_id` not like", value, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdIn(List<String> values) {
            addCriterion("`buyer_logon_id` in", values, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdNotIn(List<String> values) {
            addCriterion("`buyer_logon_id` not in", values, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdBetween(String value1, String value2) {
            addCriterion("`buyer_logon_id` between", value1, value2, "buyerLogonId");
            return (Criteria) this;
        }

        public Criteria andBuyerLogonIdNotBetween(String value1, String value2) {
            addCriterion("`buyer_logon_id` not between", value1, value2, "buyerLogonId");
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

        public Criteria andTradeStatusIsNull() {
            addCriterion("`trade_status` is null");
            return (Criteria) this;
        }

        public Criteria andTradeStatusIsNotNull() {
            addCriterion("`trade_status` is not null");
            return (Criteria) this;
        }

        public Criteria andTradeStatusEqualTo(Integer value) {
            addCriterion("`trade_status` =", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusNotEqualTo(Integer value) {
            addCriterion("`trade_status` <>", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusGreaterThan(Integer value) {
            addCriterion("`trade_status` >", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("`trade_status` >=", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusLessThan(Integer value) {
            addCriterion("`trade_status` <", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("`trade_status` <=", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusIn(List<Integer> values) {
            addCriterion("`trade_status` in", values, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusNotIn(List<Integer> values) {
            addCriterion("`trade_status` not in", values, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusBetween(Integer value1, Integer value2) {
            addCriterion("`trade_status` between", value1, value2, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("`trade_status` not between", value1, value2, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNull() {
            addCriterion("`total_amount` is null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNotNull() {
            addCriterion("`total_amount` is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountEqualTo(Integer value) {
            addCriterion("`total_amount` =", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotEqualTo(Integer value) {
            addCriterion("`total_amount` <>", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThan(Integer value) {
            addCriterion("`total_amount` >", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThanOrEqualTo(Integer value) {
            addCriterion("`total_amount` >=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThan(Integer value) {
            addCriterion("`total_amount` <", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThanOrEqualTo(Integer value) {
            addCriterion("`total_amount` <=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIn(List<Integer> values) {
            addCriterion("`total_amount` in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotIn(List<Integer> values) {
            addCriterion("`total_amount` not in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountBetween(Integer value1, Integer value2) {
            addCriterion("`total_amount` between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotBetween(Integer value1, Integer value2) {
            addCriterion("`total_amount` not between", value1, value2, "totalAmount");
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