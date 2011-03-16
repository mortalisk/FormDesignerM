package org.openxdata.gwt.designer.client.widget.skiprule;

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
    };

    Set<Operator> validOperators();

    Operator defaultOperator();

    String getIdentifier();

    void setIdentifier(String string);
}
