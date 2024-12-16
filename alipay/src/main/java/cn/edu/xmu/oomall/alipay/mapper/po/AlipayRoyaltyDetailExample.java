package cn.edu.xmu.oomall.alipay.mapper.po;

import java.util.ArrayList;
import java.util.List;

public class AlipayRoyaltyDetailExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlipayRoyaltyDetailExample() {
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

        public Criteria andAmountIsNull() {
            addCriterion("`amount` is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("`amount` is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Integer value) {
            addCriterion("`amount` =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Integer value) {
            addCriterion("`amount` <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Integer value) {
            addCriterion("`amount` >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Integer value) {
            addCriterion("`amount` >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Integer value) {
            addCriterion("`amount` <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Integer value) {
            addCriterion("`amount` <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Integer> values) {
            addCriterion("`amount` in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Integer> values) {
            addCriterion("`amount` not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Integer value1, Integer value2) {
            addCriterion("`amount` between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Integer value1, Integer value2) {
            addCriterion("`amount` not between", value1, value2, "amount");
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