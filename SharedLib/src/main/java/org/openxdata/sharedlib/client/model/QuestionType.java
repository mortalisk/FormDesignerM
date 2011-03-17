package org.openxdata.sharedlib.client.model;

public enum QuestionType {

    TEXT(1),
    NUMERIC(2),
    DECIMAL(3),
    DATE(4),
    TIME(5),
    LIST_EXCLUSIVE(6),
    LIST_MULTIPLE(7),
    DATE_TIME(8),
    BOOLEAN(9),
    REPEAT(10),
    IMAGE(11),
    VIDEO(12),
    AUDIO(13),
    LIST_EXCLUSIVE_DYNAMIC(14),
    GPS(15),
    BARCODE(16);
    private int legacyConstant;

    QuestionType(int legacyConstant) {
        this.legacyConstant = legacyConstant;
    }

    public int getLegacyConstant() {
        return legacyConstant;
    }

    public static QuestionType fromLegacyConstant(int legacy) {
        for (QuestionType type : values()) {
            if (type.getLegacyConstant() == legacy) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such QuestionType: " + legacy);
    }
}
