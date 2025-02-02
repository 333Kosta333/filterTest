package org.example.controller.filter;

import jakarta.persistence.criteria.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

@Log4j2
public class GenericSpecification<T> implements Specification<T> {

    private final SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        String key = criteria.getKey();
        Object value = criteria.getValue();
        SearchOperation operation = criteria.getOperation();
        log.info("Criteria {}", criteria.toString());

        // Разделяем ключ на части для обработки вложенных полей
        String[] keys = key.split("\\.");

        // Если ключ содержит более одной части, это вложенное поле или связь
        if (keys.length > 1) {
            // Рекурсивно создаем Join для каждого уровня вложенности
            Path<?> path = resolvePath(root, keys);
            return createPredicate(path, value, operation, builder);
        } else {
            // Обычное поле (не вложенное)
            Path<?> path = root.get(key);
            return createPredicate(path, value, operation, builder);
        }
    }

    private Path<?> resolvePath(Root<?> root, String[] keys) {
        Path<?> path = root;
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];

            // Если это последний ключ, возвращаем путь к полю
            if (i == keys.length - 1) {
                return path.get(key);
            }

            // Если текущий путь уже является Join, продолжаем использовать его
            if (path instanceof Join<?, ?>) {
                path = path.get(key);
            } else {
                // Иначе создаем новый Join
                path = ((From<?, ?>) path).join(key, JoinType.INNER);
            }
        }
        return path;
    }

    private Predicate createPredicate(Path<?> path, Object value, SearchOperation operation, CriteriaBuilder builder) {
        // Определяем тип поля
        Class<?> fieldType = path.getJavaType();
        log.info("Field type: {}", fieldType);

        // Обрабатываем в зависимости от типа поля
        if (fieldType.equals(String.class)) {
            return handleStringPredicate(path, value, operation, builder);
        } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
            return handleLongPredicate(path, value, operation, builder);
        } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
            return handleIntegerPredicate(path, value, operation, builder);
        } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
            return handleDoublePredicate(path, value, operation, builder);
        } else if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
            return handleBooleanPredicate(path, value, operation, builder);
        } else {
            throw new IllegalArgumentException("Unsupported field type: " + fieldType);
        }
    }

    private Predicate handleStringPredicate(Path<?> path, Object value, SearchOperation operation, CriteriaBuilder builder) {
        String stringValue = value.toString();
        return switch (operation) {
            case GREATER_THAN -> builder.greaterThan(path.as(String.class), value.toString());
            case LESS_THAN -> builder.lessThan(path.as(String.class), value.toString());
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(path.as(String.class), value.toString());
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(path.as(String.class), value.toString());
            case NOT_EQUAL -> builder.notEqual(path, value);
            case EQUAL -> builder.equal(path, value);
            case MATCH ->
                    builder.like(builder.lower(path.as(String.class)), "%" + value.toString().toLowerCase() + "%");
            case MATCH_START ->
                    builder.like(builder.lower(path.as(String.class)), value.toString().toLowerCase() + "%");
            case MATCH_END -> builder.like(builder.lower(path.as(String.class)), "%" + value.toString().toLowerCase());
            case IN -> {
                if (value instanceof Collection) {
                    yield path.in((Collection<?>) value);
                }
                throw new IllegalArgumentException("Value for IN operation must be a collection");
            }
            case NOT_IN -> {
                if (value instanceof Collection) {
                    yield builder.not(path.in((Collection<?>) value));
                }
                throw new IllegalArgumentException("Value for NOT IN operation must be a collection");
            }
            default -> throw new IllegalArgumentException("Unsupported operation: " + operation);
        };
    }

    private Predicate handleLongPredicate(Path<?> path, Object value, SearchOperation operation, CriteriaBuilder builder) {
        Long longValue = Long.parseLong(value.toString());
        return switch (operation) {
            case GREATER_THAN -> builder.greaterThan(path.as(Long.class), longValue);
            case LESS_THAN -> builder.lessThan(path.as(Long.class), longValue);
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(path.as(Long.class), longValue);
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(path.as(Long.class), longValue);
            case NOT_EQUAL -> builder.notEqual(path, longValue);
            case EQUAL -> builder.equal(path, longValue);
            default -> throw new IllegalArgumentException("Unsupported operation for Long: " + operation);
        };
    }

    private Predicate handleIntegerPredicate(Path<?> path, Object value, SearchOperation operation, CriteriaBuilder builder) {
        Integer intValue = Integer.parseInt(value.toString());
        return switch (operation) {
            case GREATER_THAN -> builder.greaterThan(path.as(Integer.class), intValue);
            case LESS_THAN -> builder.lessThan(path.as(Integer.class), intValue);
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(path.as(Integer.class), intValue);
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(path.as(Integer.class), intValue);
            case NOT_EQUAL -> builder.notEqual(path, intValue);
            case EQUAL -> builder.equal(path, intValue);
            default -> throw new IllegalArgumentException("Unsupported operation for Integer: " + operation);
        };
    }

    private Predicate handleBooleanPredicate(Path<?> path, Object value, SearchOperation operation, CriteriaBuilder builder) {
        Boolean booleanValue = Boolean.parseBoolean(value.toString());
        return switch (operation) {
            case NOT_EQUAL -> builder.notEqual(path, booleanValue);
            case EQUAL -> builder.equal(path, booleanValue);
            default -> throw new IllegalArgumentException("Unsupported operation for Boolean: " + operation);
        };
    }

    private Predicate handleDoublePredicate(Path<?> path, Object value, SearchOperation operation, CriteriaBuilder builder) {
        Double doubleValue = Double.parseDouble(value.toString());
        return switch (operation) {
            case GREATER_THAN -> builder.greaterThan(path.as(Double.class), doubleValue);
            case LESS_THAN -> builder.lessThan(path.as(Double.class), doubleValue);
            case GREATER_THAN_EQUAL -> builder.greaterThanOrEqualTo(path.as(Double.class), doubleValue);
            case LESS_THAN_EQUAL -> builder.lessThanOrEqualTo(path.as(Double.class), doubleValue);
            case NOT_EQUAL -> builder.notEqual(path, doubleValue);
            case EQUAL -> builder.equal(path, doubleValue);
            default -> throw new IllegalArgumentException("Unsupported operation for Double: " + operation);
        };
    }
}