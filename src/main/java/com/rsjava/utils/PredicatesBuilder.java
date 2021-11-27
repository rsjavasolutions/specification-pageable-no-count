package com.rsjava.utils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class PredicatesBuilder<T> {
    private final Root<T> root;
    private final CriteriaBuilder criteriaBuilder;
    private final List<Predicate> predicates;

    public PredicatesBuilder(Root<T> root, CriteriaBuilder criteriaBuilder) {
        this.root = root;
        this.criteriaBuilder = criteriaBuilder;
        this.predicates = new ArrayList<>();
    }

    public Predicate build() {
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public PredicatesBuilder<T> like(String entityFieldName, String value) {
        if (value != null) {
            predicates.add(criteriaBuilder.like(root.get(entityFieldName), getLikePattern(value)));
        }
        return this;
    }

    public PredicatesBuilder<T> like(String entityFieldName, String value, JoinType joinType, String joinedCollectionName) {

        if (value != null) {
            predicates.add(criteriaBuilder.like(root.join(joinedCollectionName, joinType).get(entityFieldName), value));
        }
        return this;
    }

    public PredicatesBuilder<T> caseInsensitiveLike(String entityFieldName, String value) {
        if (value != null) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(entityFieldName)),
                    getLikePattern(value.toLowerCase())));
        }
        return this;
    }

    public PredicatesBuilder<T> caseInsensitiveLike(String entityFieldName, String value, JoinType joinType, String joinedCollectionName) {
        if (value != null) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.join(joinedCollectionName, joinType).get(entityFieldName)),
                    getLikePattern(value.toLowerCase())));
        }
        return this;
    }

    public PredicatesBuilder<T> equal(String entityFieldName, Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(root.get(entityFieldName), value));
        }
        return this;
    }

    public PredicatesBuilder<T> equal(String entityFieldName, Object value, JoinType joinType, String joinedCollectionName) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(root.join(joinedCollectionName, joinType).get(entityFieldName), value));
        }
        return this;
    }

    public <Y extends Comparable<? super Y>> PredicatesBuilder<T> greaterThanOrEqualTo(Expression<? extends Y> expression, Y value) {
        if (value != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, value));
        }
        return this;
    }

    public <Y extends Comparable<? super Y>> PredicatesBuilder<T> lessThanOrEqualTo(Expression<? extends Y> expression, Y value) {
        if (value != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, value));
        }
        return this;
    }

    public PredicatesBuilder<T> add(Predicate predicate) {
        predicates.add(predicate);
        return this;
    }

    /**
     * Adds predicate if condition is true
     */
    public PredicatesBuilder<T> add(Predicate predicate, boolean condition) {
        return condition ? add(predicate) : this;
    }

    /**
     * Adds predicate if value is not null
     */
    public PredicatesBuilder<T> add(Predicate predicate, Object value) {
        return add(predicate, value != null);
    }

    private String getLikePattern(String value) {
        return "%" + value + "%";
    }
}
