package org.openxdata.designer.client.widget.skiprule;

import com.google.gwt.core.client.GWT;
import org.openxdata.designer.client.controller.ItemSelectionListener;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.openxdata.designer.client.DesignerMessages;

/**
 * Widget used to display the condition operators (eg equal to, less than, greater than, etc)
 * for skip and validation rules.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
final public class OperatorWidget extends Composite implements ChangeHandler {

    private final DesignerMessages i18n = GWT.create(DesignerMessages.class);
    /** The current question data type. */
    private int dataType = QuestionDef.QTN_TYPE_TEXT;
    /** The selection change listener. */
    private ItemSelectionListener itemSelectionListener;
    /** List box that contains the operators the user can select from */
    private final ListBox operators;

    /**
     * Creates a new instance of the operator widget
     * 
     * @param text the default display text.
     * @param itemSelectionListener the listener to selection change events.
     */
    public OperatorWidget(int operator, ItemSelectionListener itemSelectionListener) {
        operators = new ListBox();
        operators.addChangeHandler(this);
        initWidget(operators);
        this.itemSelectionListener = itemSelectionListener;

        buildOptionList();
        setOperator(operator);
    }

    /**
     * Sets the data type of the question to which the operator is being applied.
     * 
     * @param dataType the data type.
     */
    public void setDataType(int dataType) {
        this.dataType = dataType;
        buildOptionList();
    }

    /**
     * Builds the options to put in the list box. The options are based
     * on the data type of the question
     */
    private void buildOptionList() {
        operators.clear();

        if (!(dataType == QuestionDef.QTN_TYPE_GPS || dataType == QuestionDef.QTN_TYPE_VIDEO
                || dataType == QuestionDef.QTN_TYPE_AUDIO || dataType == QuestionDef.QTN_TYPE_IMAGE
                || dataType == QuestionDef.QTN_TYPE_BARCODE)) {

            addOperator(i18n.isEqualTo(), ModelConstants.OPERATOR_EQUAL);
            addOperator(i18n.isNotEqual(), ModelConstants.OPERATOR_NOT_EQUAL);
        }

        if (dataType == QuestionDef.QTN_TYPE_DATE || dataType == QuestionDef.QTN_TYPE_DATE_TIME
                || dataType == QuestionDef.QTN_TYPE_DECIMAL || dataType == QuestionDef.QTN_TYPE_NUMERIC
                || dataType == QuestionDef.QTN_TYPE_TIME || dataType == QuestionDef.QTN_TYPE_REPEAT) {

            addOperator(i18n.isGreaterThan(), ModelConstants.OPERATOR_GREATER);
            addOperator(i18n.isGreaterThanOrEqual(), ModelConstants.OPERATOR_GREATER_EQUAL);
            addOperator(i18n.isLessThan(), ModelConstants.OPERATOR_LESS);
            addOperator(i18n.isLessThanOrEqual(), ModelConstants.OPERATOR_LESS_EQUAL);
            addOperator(i18n.isBetween(), ModelConstants.OPERATOR_BETWEEN);
            addOperator(i18n.isNotBetween(), ModelConstants.OPERATOR_NOT_BETWEEN);
        }

        if (dataType == QuestionDef.QTN_TYPE_LIST_EXCLUSIVE || dataType == QuestionDef.QTN_TYPE_LIST_MULTIPLE) {
            addOperator(i18n.isInList(), ModelConstants.OPERATOR_IN_LIST);
            addOperator(i18n.isNotInList(), ModelConstants.OPERATOR_NOT_IN_LIST);
        }

        addOperator(i18n.isNull(), ModelConstants.OPERATOR_IS_NULL);
        addOperator(i18n.isNotNull(), ModelConstants.OPERATOR_IS_NOT_NULL);
    }

    private void addOperator(String caption, int lookupValue) {
        operators.addItem(caption, Integer.toString(lookupValue));
    }

    @Override
    public void onChange(ChangeEvent event) {
        String selected = operators.getValue(operators.getSelectedIndex());
        itemSelectionListener.onItemSelected(this, Integer.parseInt(selected));
    }

    /**
     * Set the operator for the given operator value to its text representation.
     * 
     * @param operator the operator int value.
     */
    public void setOperator(int operator) {
        for (int i = 0; i < operators.getItemCount(); i++) {
            if (Integer.parseInt(operators.getValue(i)) == operator) {
                operators.setSelectedIndex(i);
                return;
            }
        }
    }
}
