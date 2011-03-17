package org.openxdata.sharedlib.client.model;

import org.openxdata.sharedlib.client.model.Operator;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents a question in a form.
 */
public interface Question {

    Question NULL_QUESTION = new Question() {

        @Override
        public Set<Operator> validOperators() {
            return EnumSet.allOf(Operator.class);
        }

        @Override
        public String getIdentifier() {
            return "";
        }

        @Override
        public void setIdentifier(final String string) {
        }

        public Operator defaultOperator() {
            return Operator.EQUAL;
        }

        @Override
        public String getAnswer() {
            return "";
        }

        @Override
        public void setAnswer(String answer) {
        }
    };

    Set<Operator> validOperators();

    Operator defaultOperator();

    String getIdentifier();

    void setIdentifier(String string);

    String getAnswer();

    void setAnswer(String answer);
}
