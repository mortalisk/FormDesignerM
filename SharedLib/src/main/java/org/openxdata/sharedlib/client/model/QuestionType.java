package org.openxdata.sharedlib.client.model;

public enum QuestionType {
    /** Text question type. */
    TEXT(1),
    /** Numeric question type. These are numbers without decimal points*/
    NUMERIC(2),
    /** Decimal question type. These are numbers with decimals */
    DECIMAL(3),
    /** Date question type. This has only date component without time. */
    DATE(4),
    /** Time question type. This has only time element without date*/
    TIME(5),
     /** This is a question with alist of options where not more than one option can be selected at a time. */
    LIST_EXCLUSIVE(6),
    /** This is a question with alist of options where more than one option can be selected at a time. */
    LIST_MULTIPLE(7),
    /** Date and Time question type. This has both the date and time components*/
    DATE_TIME(8),
    /** Question with true and false answers. */
    BOOLEAN(9),
    /** Question with repeat sets of questions. */
    REPEAT(10),
    /** Question with image. */
    IMAGE(11),
    /** Question with recorded video. */
    VIDEO(12),
    /** Question with recoded audio. */
    AUDIO(13),
    /** Question whose list of options varies basing on the value selected from another question.
     * An example of such a question would be countries where the list depends on the continent
     * selected in the continent question.
     */
    LIST_EXCLUSIVE_DYNAMIC(14),
    /** Question with GPS cordinates. */
    GPS(15),
    /** Question with barcode cordinates. */
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
