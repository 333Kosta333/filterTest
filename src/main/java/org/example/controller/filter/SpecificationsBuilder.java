package org.example.controller.filter;

import org.example.entity.RequestEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecificationsBuilder {
    private final List<SearchCriteria> params;

    public SpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public SpecificationsBuilder with(String condition) {
        // Регулярное выражение для разбора условия
        Pattern pattern = Pattern.compile("(\\w+(?:\\.\\w+)*)\\s+(\\w+)\\s+(.+)");
        Matcher matcher = pattern.matcher(condition);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid condition format: " + condition);
        }

        String key = matcher.group(1); // Поле (может быть вложенным, например, orders.product.category.id)
        String operation = matcher.group(2); // Операция
        String value = matcher.group(3).replaceAll("^['\"]|['\"]$", ""); // Убираем кавычки вокруг значения

        // Проверяем, является ли значение коллекцией (например, [1, 2, 3])
        if (value.startsWith("[") && value.endsWith("]")) {
            value = value.substring(1, value.length() - 1); // Убираем квадратные скобки
            List<String> values = Arrays.asList(value.split("\\s*,\\s*")); // Разделяем по запятым
            params.add(new SearchCriteria(key, values, SearchOperation.fromString(operation)));
        } else {
            // Одиночное значение
            params.add(new SearchCriteria(key, value, SearchOperation.fromString(operation)));
        }

        return this;
    }

    public Specification<RequestEntity> build() {
        if (params.isEmpty()) {
            return null;
        }

        Specification<RequestEntity> result = new GenericSpecification<>(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result).and(new GenericSpecification<>(params.get(i)));
        }

        return result;
    }
}

