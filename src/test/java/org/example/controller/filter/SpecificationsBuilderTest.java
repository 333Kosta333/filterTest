package org.example.controller.filter;

import org.example.entity.RequestEntity;
import org.example.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
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
        SpecificationsBuilder parser = new SpecificationsBuilder();
        String filter = "templateId like '%equ%';requestType.id eq 'U1'";
        String[] filterArray = filter.split(";");

        // Создаем спецификацию на основе всех условий
        SpecificationsBuilder builder = new SpecificationsBuilder();
        for (String f : filterArray) {
            builder.with(f.trim()); // Убираем лишние пробелы
        }

        Specification<RequestEntity> spec = builder.build();
        List<RequestEntity> all = repository.findAll(spec);
        for (RequestEntity r : all) {
            System.out.println(r.toString());
        }
    }
}
