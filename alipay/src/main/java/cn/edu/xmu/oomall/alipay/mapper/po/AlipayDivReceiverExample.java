package cn.edu.xmu.oomall.alipay.mapper.po;

import java.util.ArrayList;
import java.util.List;

public class AlipayDivReceiverExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AlipayDivReceiverExample() {
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

        public Criteria andTypeIsNull() {
            addCriterion("`type` is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("`type` is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("`type` =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("`type` <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("`type` >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("`type` >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("`type` <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("`type` <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("`type` like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("`type` not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("`type` in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("`type` not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("`type` between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("`type` not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andAccountIsNull() {
            addCriterion("`account` is null");
            return (Criteria) this;
        }

        public Criteria andAccountIsNotNull() {
            addCriterion("`account` is not null");
            return (Criteria) this;
        }

        public Criteria andAccountEqualTo(String value) {
            addCriterion("`account` =", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotEqualTo(String value) {
            addCriterion("`account` <>", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThan(String value) {
            addCriterion("`account` >", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThanOrEqualTo(String value) {
            addCriterion("`account` >=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThan(String value) {
            addCriterion("`account` <", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThanOrEqualTo(String value) {
            addCriterion("`account` <=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLike(String value) {
            addCriterion("`account` like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotLike(String value) {
            addCriterion("`account` not like", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountIn(List<String> values) {
            addCriterion("`account` in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotIn(List<String> values) {
            addCriterion("`account` not in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountBetween(String value1, String value2) {
            addCriterion("`account` between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotBetween(String value1, String value2) {
            addCriterion("`account` not between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameIsNull() {
            addCriterion("`bind_login_name` is null");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameIsNotNull() {
            addCriterion("`bind_login_name` is not null");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameEqualTo(String value) {
            addCriterion("`bind_login_name` =", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameNotEqualTo(String value) {
            addCriterion("`bind_login_name` <>", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameGreaterThan(String value) {
            addCriterion("`bind_login_name` >", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameGreaterThanOrEqualTo(String value) {
            addCriterion("`bind_login_name` >=", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameLessThan(String value) {
            addCriterion("`bind_login_name` <", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameLessThanOrEqualTo(String value) {
            addCriterion("`bind_login_name` <=", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameLike(String value) {
            addCriterion("`bind_login_name` like", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameNotLike(String value) {
            addCriterion("`bind_login_name` not like", value, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameIn(List<String> values) {
            addCriterion("`bind_login_name` in", values, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameNotIn(List<String> values) {
            addCriterion("`bind_login_name` not in", values, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameBetween(String value1, String value2) {
            addCriterion("`bind_login_name` between", value1, value2, "bindLoginName");
            return (Criteria) this;
        }

        public Criteria andBindLoginNameNotBetween(String value1, String value2) {
            addCriterion("`bind_login_name` not between", value1, value2, "bindLoginName");
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