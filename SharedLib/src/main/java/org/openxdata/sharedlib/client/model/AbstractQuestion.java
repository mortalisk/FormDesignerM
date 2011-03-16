package org.openxdata.sharedlib.client.model;

/**
 * Provide some default behavior for questions.
 */
public abstract class AbstractQuestion implements Question {

    private String identifier = "";

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
}
