package org.example.controller.filter;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SearchCriteria {
    private final String key;
    private final SearchOperation operation;
    private final Object value;

    public SearchCriteria(final String key, final Object value, final SearchOperation operation) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }
}
