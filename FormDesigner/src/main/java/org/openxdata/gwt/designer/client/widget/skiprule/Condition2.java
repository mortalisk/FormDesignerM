package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;

/**
 * Model for a Condition. Usually used in a skip logic rule.
 * Contains an operator and its value(s).
 *
 * @apiviz.composedOf org.openxdata.gwt.designer.client.widget.skiprule.Operator
 * @apiviz.composedOf org.openxdata.gwt.designer.client.widget.skiprule.Question
 */
public final class Condition2 {

    private Operator operator;
    private Question question = Question.NULL_QUESTION;
    private String value1 = "";
    private String value2 = "";

    /**
     * Initializes a valid Condition, does not allow null, or inconsistent
     * operator for the question.
     * @param operator null is not allowed
     * @param question null is not allowed
     */
    public Condition2(final Operator operator, final Question question) {
        setOperator(operator);
        setQuestion(question);
    }

    public Operator getOperator() {
        return operator;
    }

    /**
     * sets the operator and checks its validity.
     * @param operator null is not allowed
     * @throws IllegalArgumentException if operator is null
     */
    public void setOperator(final Operator operator) {
        validateCorrectOperator(question, operator);
        this.operator = operator;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(final String value1) {
        Validate.notNull(value1, "value1");
        this.value1 = value1;
    }

    void setQuestion(final Question question) {
        validateCorrectOperator(question, operator);
        this.question = question;
    }

    Question getQuestion() {
        return question;
    }

    String getValue2() {
        return value2;
    }

    void setValue2(final String value2) {
        Validate.notNull(value2, "value2");
        this.value2 = value2;
    }

    private void validateCorrectOperator(final Question newQuestion,
            final Operator newOperator) {
        Validate.notNull(newQuestion, "question");
        Validate.notNull(newOperator, "operator");

        if (!newQuestion.validOperators().contains(newOperator)) {
            throw new IllegalArgumentException("Operator " + newOperator
                    + " not allowed for question type " + newQuestion);
        }
    }
}
