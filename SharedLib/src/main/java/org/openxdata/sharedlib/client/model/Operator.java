package org.openxdata.sharedlib.client.model;

/**
 * Represents an operator for a Condition. Operators have a given number of
 * values that it operates on.
 */
public enum Operator {

    NONE(0, 0),
    IS_NULL(0, 9), IS_NOT_NULL(0, 16),
    GREATER(1, 3), GREATER_EQUAL(1, 4),
    LESS(1, 5), LESS_EQUAL(1, 6),
    EQUAL(1, 1), NOT_EQUAL(1, 2),
    STARTS_WITH(1, 14), NOT_START_WITH(1, 15),
    CONTAINS(1, 12), NOT_CONTAIN(1, 13),
    BETWEEN(2, 10), NOT_BETWEEN(2, 11),
    IN_LIST(1, 7), NOT_IN_LIST(1, 8),
    ENDS_WITH(1, 17), NOT_END_WITH(1, 18);
    private final int numberOfValues;
    private final int id;

    private Operator(final int numberOfValues, final int legacyConstant) {
        this.numberOfValues = numberOfValues;
        this.id = legacyConstant;
    }

    public int getNumberOfValues() {
        return numberOfValues;
    }

    public int getId() {
        return id;
    }

    public static Operator fromId(int id) {
        for (Operator operator : Operator.values()) {
            if (operator.getId() == id) {
                return operator;
            }
        }

        throw new IllegalArgumentException("No such operator: " + id);
    }
}
