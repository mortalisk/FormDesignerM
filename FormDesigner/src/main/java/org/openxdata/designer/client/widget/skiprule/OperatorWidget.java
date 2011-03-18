package org.openxdata.designer.client.widget.skiprule;

import com.google.gwt.core.client.GWT;
import org.openxdata.designer.client.controller.ItemSelectionListener;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import org.openxdata.designer.client.DesignerMessages;
import org.openxdata.sharedlib.client.model.Operator;
import org.openxdata.sharedlib.client.model.QuestionType;

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
    private QuestionType dataType = QuestionType.TEXT;
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
    public OperatorWidget(Operator operator, ItemSelectionListener itemSelectionListener) {
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
    public void setDataType(QuestionType dataType) {
        this.dataType = dataType;
        buildOptionList();
    }

    /**
     * Builds the options to put in the list box. The options are based
     * on the data type of the question
     */
    private void buildOptionList() {
        operators.clear();

        if (!(dataType == QuestionType.GPS || dataType == QuestionType.VIDEO
                || dataType == QuestionType.AUDIO || dataType == QuestionType.IMAGE
                || dataType == QuestionType.BARCODE)) {

            addOperator(i18n.isEqualTo(), Operator.EQUAL);
            addOperator(i18n.isNotEqual(), Operator.NOT_EQUAL);
        }

        if (dataType == QuestionType.DATE || dataType == QuestionType.DATE_TIME
                || dataType == QuestionType.DECIMAL || dataType == QuestionType.NUMERIC
                || dataType == QuestionType.TIME || dataType == QuestionType.REPEAT) {

            addOperator(i18n.isGreaterThan(), Operator.GREATER);
            addOperator(i18n.isGreaterThanOrEqual(), Operator.GREATER_EQUAL);
            addOperator(i18n.isLessThan(), Operator.LESS);
            addOperator(i18n.isLessThanOrEqual(), Operator.LESS_EQUAL);
            addOperator(i18n.isBetween(), Operator.BETWEEN);
            addOperator(i18n.isNotBetween(), Operator.NOT_BETWEEN);
        }

        if (dataType == QuestionType.LIST_EXCLUSIVE || dataType == QuestionType.LIST_MULTIPLE) {
            addOperator(i18n.isInList(), Operator.IN_LIST);
            addOperator(i18n.isNotInList(), Operator.NOT_IN_LIST);
        }

        addOperator(i18n.isNull(), Operator.IS_NULL);
        addOperator(i18n.isNotNull(), Operator.IS_NOT_NULL);
    }

    private void addOperator(String caption, Operator operator) {
        String lookup = operator.name();
        operators.addItem(caption, lookup);
    }

    @Override
    public void onChange(ChangeEvent event) {
        String selected = operators.getValue(operators.getSelectedIndex());
        itemSelectionListener.onItemSelected(this, Operator.valueOf(selected));
    }

    /**
     * Set the operator for the given operator value to its text representation.
     * 
     * @param operator the operator int value.
     */
    public void setOperator(Operator operator) {
        for (int i = 0; i < operators.getItemCount(); i++) {
            if (operators.getValue(i).equals(operator.name())) {
                operators.setSelectedIndex(i);
                return;
            }
        }
    }
}
