package org.openxdata.sharedlib.client.model;

/**
 * Provide some default behavior for questions.
 */
public abstract class AbstractQuestion implements Question {

    private String identifier = "";
    private String answer = "";

    /**
     * Returns the identifier, will never return null.
     * @return identifier
     */
    @Override
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the identifier.
     * @param identifier
     * @throws IllegalArgumentException when identifier is null
     */
    @Override
    public final void setIdentifier(final String identifier) {
        Validate.notNull(identifier, "identifier");
        this.identifier = identifier;
    }

    @Override
    public final Operator defaultOperator() {
        return validOperators().iterator().next();
    }

    /**
     * Sets the answer.
     * @param answer
     * @throws IllegalArgumentException when answer is null
     */
    @Override
    public final void setAnswer(String answer) {
        Validate.notNull(answer, "answer");
        this.answer = answer;
    }

    /**
     *
     * @return the answer, can never be null
     */
    @Override
    public final String getAnswer() {
        return answer;
    }
}
