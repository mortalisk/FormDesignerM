package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import java.util.Set;

/**
 * The view for the Condition widget. Gives a choice of operator, and shows the
 * values of the Condition.
 */
public interface ConditionView extends IsWidget {

    /**
     * Gets value of a field.
     * @param index number of the field you want get the value of
     * @return the string entered by the user
     */
    String getValue(int index);

    /**
     * Sets one of the fields to a value.
     * @param index number of the field to be set
     * @param value value that will be set
     */
    void setValue(int index, String value);

    /**
     * Sets number of fields. Fields are separated by separator set by
     * setSeparator().
     * @param number number of fields
     */
    void setNumberOfFields(int number);

    /**
     * Sets the choices of operator the user can choose from.
     * @param choices the set of choices
     */
    void setOperatorChoices(Set<Operator> choices);

    /**
     * Set the selected operator.
     * @param operator
     */
    void setOperator(Operator operator);

    /**
     * Returns the current selected operator.
     * @return operator
     */
    Operator getOperator();

    HasChangeHandlers getOperatorChanger();

    /**
     * Return the field that contains the question identifier.
     * @return field
     */
    HasText getField();
}
