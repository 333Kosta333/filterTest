package org.example.controller.filter;

public enum SearchOperation {
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    GREATER_THAN_EQUAL("gte"),
    LESS_THAN_EQUAL("lte"),
    NOT_EQUAL("neq"),
    EQUAL("eq"),
    MATCH("like"),
    MATCH_START("start"),
    MATCH_END("end"),
    IN("in"),
    NOT_IN("notin");

    private final String operation;

    SearchOperation(String operation) {
        this.operation = operation;
    }

    public static SearchOperation fromString(String operation) {
        for (SearchOperation op : SearchOperation.values()) {
            if (op.getOperation().equalsIgnoreCase(operation)) {
                return op;
            }
        }
        throw new IllegalArgumentException("Unknown operation: " + operation);
    }

    public String getOperation() {
        return operation;
    }
}