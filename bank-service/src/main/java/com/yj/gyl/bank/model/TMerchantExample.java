package com.yj.gyl.bank.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TMerchantExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public TMerchantExample() {
        oredCriteria = new ArrayList<Criteria>();
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

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
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
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountIsNull() {
            addCriterion("merchant_account is null");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountIsNotNull() {
            addCriterion("merchant_account is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountEqualTo(String value) {
            addCriterion("merchant_account =", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountNotEqualTo(String value) {
            addCriterion("merchant_account <>", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountGreaterThan(String value) {
            addCriterion("merchant_account >", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountGreaterThanOrEqualTo(String value) {
            addCriterion("merchant_account >=", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountLessThan(String value) {
            addCriterion("merchant_account <", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountLessThanOrEqualTo(String value) {
            addCriterion("merchant_account <=", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountLike(String value) {
            addCriterion("merchant_account like", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountNotLike(String value) {
            addCriterion("merchant_account not like", value, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountIn(List<String> values) {
            addCriterion("merchant_account in", values, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountNotIn(List<String> values) {
            addCriterion("merchant_account not in", values, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountBetween(String value1, String value2) {
            addCriterion("merchant_account between", value1, value2, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantAccountNotBetween(String value1, String value2) {
            addCriterion("merchant_account not between", value1, value2, "merchantAccount");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyIsNull() {
            addCriterion("merchant_public_key is null");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyIsNotNull() {
            addCriterion("merchant_public_key is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyEqualTo(String value) {
            addCriterion("merchant_public_key =", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyNotEqualTo(String value) {
            addCriterion("merchant_public_key <>", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyGreaterThan(String value) {
            addCriterion("merchant_public_key >", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyGreaterThanOrEqualTo(String value) {
            addCriterion("merchant_public_key >=", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyLessThan(String value) {
            addCriterion("merchant_public_key <", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyLessThanOrEqualTo(String value) {
            addCriterion("merchant_public_key <=", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyLike(String value) {
            addCriterion("merchant_public_key like", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyNotLike(String value) {
            addCriterion("merchant_public_key not like", value, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyIn(List<String> values) {
            addCriterion("merchant_public_key in", values, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyNotIn(List<String> values) {
            addCriterion("merchant_public_key not in", values, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyBetween(String value1, String value2) {
            addCriterion("merchant_public_key between", value1, value2, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPublicKeyNotBetween(String value1, String value2) {
            addCriterion("merchant_public_key not between", value1, value2, "merchantPublicKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyIsNull() {
            addCriterion("merchant_private_key is null");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyIsNotNull() {
            addCriterion("merchant_private_key is not null");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyEqualTo(String value) {
            addCriterion("merchant_private_key =", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyNotEqualTo(String value) {
            addCriterion("merchant_private_key <>", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyGreaterThan(String value) {
            addCriterion("merchant_private_key >", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyGreaterThanOrEqualTo(String value) {
            addCriterion("merchant_private_key >=", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyLessThan(String value) {
            addCriterion("merchant_private_key <", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyLessThanOrEqualTo(String value) {
            addCriterion("merchant_private_key <=", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyLike(String value) {
            addCriterion("merchant_private_key like", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyNotLike(String value) {
            addCriterion("merchant_private_key not like", value, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyIn(List<String> values) {
            addCriterion("merchant_private_key in", values, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyNotIn(List<String> values) {
            addCriterion("merchant_private_key not in", values, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyBetween(String value1, String value2) {
            addCriterion("merchant_private_key between", value1, value2, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andMerchantPrivateKeyNotBetween(String value1, String value2) {
            addCriterion("merchant_private_key not between", value1, value2, "merchantPrivateKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyIsNull() {
            addCriterion("public_key is null");
            return (Criteria) this;
        }

        public Criteria andPublicKeyIsNotNull() {
            addCriterion("public_key is not null");
            return (Criteria) this;
        }

        public Criteria andPublicKeyEqualTo(String value) {
            addCriterion("public_key =", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotEqualTo(String value) {
            addCriterion("public_key <>", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyGreaterThan(String value) {
            addCriterion("public_key >", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyGreaterThanOrEqualTo(String value) {
            addCriterion("public_key >=", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyLessThan(String value) {
            addCriterion("public_key <", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyLessThanOrEqualTo(String value) {
            addCriterion("public_key <=", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyLike(String value) {
            addCriterion("public_key like", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotLike(String value) {
            addCriterion("public_key not like", value, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyIn(List<String> values) {
            addCriterion("public_key in", values, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotIn(List<String> values) {
            addCriterion("public_key not in", values, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyBetween(String value1, String value2) {
            addCriterion("public_key between", value1, value2, "publicKey");
            return (Criteria) this;
        }

        public Criteria andPublicKeyNotBetween(String value1, String value2) {
            addCriterion("public_key not between", value1, value2, "publicKey");
            return (Criteria) this;
        }

        public Criteria andChannelIsNull() {
            addCriterion("channel is null");
            return (Criteria) this;
        }

        public Criteria andChannelIsNotNull() {
            addCriterion("channel is not null");
            return (Criteria) this;
        }

        public Criteria andChannelEqualTo(String value) {
            addCriterion("channel =", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotEqualTo(String value) {
            addCriterion("channel <>", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThan(String value) {
            addCriterion("channel >", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThanOrEqualTo(String value) {
            addCriterion("channel >=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThan(String value) {
            addCriterion("channel <", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThanOrEqualTo(String value) {
            addCriterion("channel <=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLike(String value) {
            addCriterion("channel like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotLike(String value) {
            addCriterion("channel not like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelIn(List<String> values) {
            addCriterion("channel in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotIn(List<String> values) {
            addCriterion("channel not in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelBetween(String value1, String value2) {
            addCriterion("channel between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotBetween(String value1, String value2) {
            addCriterion("channel not between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    /**
     */
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