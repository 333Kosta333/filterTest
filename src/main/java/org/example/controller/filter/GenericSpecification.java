package org.example.controller.filter;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;

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
                path = ((Join<?, ?>) path).get(key);
            } else {
                // Иначе создаем новый Join
                path = ((From<?, ?>) path).join(key, JoinType.INNER);
            }
        }
        return path;
    }

    private Predicate createPredicate(Path<?> path, Object value, SearchOperation operation, CriteriaBuilder builder) {
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
}