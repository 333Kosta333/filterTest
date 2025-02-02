package org.example.controller.filter;

import org.example.entity.RequestEntity;
import org.example.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
class SpecificationsBuilderTest {
    @Autowired
    RequestRepository repository;

    @BeforeEach
    void setUp() {

    }

    @Test
    @Sql(
            scripts = "create-test-data.sql",
            executionPhase = BEFORE_TEST_METHOD
    )
    void testBuild() {
        String filter = "templateId like '%equ%';partners.name eq 'P2'";
        String[] filterArray = filter.split(";");

        // Создаем спецификацию на основе всех условий
        SpecificationsBuilder builder = new SpecificationsBuilder();
        for (String f : filterArray) {
            builder.with(f.trim()); // Убираем лишние пробелы
        }

        Specification<RequestEntity> spec = builder.build();
        // Парсим параметр сортировки
        String sort = "partners.name asc";
        // Создаем объект Sort.Order
        Sort sortObj = parseSortParameter(sort);

        // Создаем объект Pageable для пагинации и сортировки
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Page<RequestEntity> all = repository.findAll(spec, pageable);
        for (RequestEntity r : all) {
            System.out.println(r.toString());
        }
    }

    /**
     * Парсит параметр сортировки в формате "field1 direction1, field2 direction2".
     *
     * @param sort Параметр сортировки (например, "name asc, orders.product.name desc").
     * @return Объект Sort.
     */
    private Sort parseSortParameter(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.unsorted();
        }

        // Разделяем параметр сортировки по запятым
        List<Sort.Order> orders = Arrays.stream(sort.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(this::parseOrder)
                .collect(Collectors.toList());

        return Sort.by(orders);
    }

    /**
     * Парсит отдельное условие сортировки в формате "field direction".
     *
     * @param order Параметр сортировки (например, "orders.product.name desc").
     * @return Объект Sort.Order.
     */
    private Sort.Order parseOrder(String order) {
        String[] parts = order.split("\\s+");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid sort parameter format: " + order);
        }

        String field = parts[0];
        Sort.Direction direction = Sort.Direction.fromString(parts[1]);

        // Если поле содержит вложенные сущности (например, "orders.product.name"),
        // создаем Sort.Order с использованием PropertyPath
        if (field.contains(".")) {
            return new Sort.Order(direction, field);
        } else {
            return new Sort.Order(direction, field);
        }
    }
}
