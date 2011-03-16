package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;
import java.util.EnumSet;
import java.util.Set;

/**
 * A Question that represents a text input.
 */
public final class TextQuestion extends AbstractQuestion {

    @Override
    public Set<Operator> validOperators() {
        return EnumSet.of(Operator.EQUAL, Operator.NOT_EQUAL,
                Operator.IS_NULL, Operator.IS_NOT_NULL,
                Operator.STARTS_WITH, Operator.NOT_START_WITH,
                Operator.CONTAINS, Operator.NOT_CONTAIN);
    }
}
