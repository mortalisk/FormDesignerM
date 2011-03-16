package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.openxdata.sharedlib.client.locale.FormsConstants;

/**
 * Standard implementation for ConditionView.
 *
 * @see ConditionView
 */
class ConditionViewImpl extends Composite implements ConditionView {

    private final FormsConstants i18n = GWT.create(FormsConstants.class);
    private List<TextBox> valueFields = Collections.emptyList();
    private ListBox operators = new ListBox();
    private HorizontalPanel panel = new HorizontalPanel();
    private Label andLabel = new Label(i18n.and());
    private Label identifierLabel = new Label(i18n.question() + ":");
    private TextBox identifierField = new TextBox();

    public ConditionViewImpl() {
        initWidget(panel);
        panel.add(identifierLabel);
        panel.add(identifierField);
        panel.add(operators);
    }

    @Override
    public String getValue(final int index) {
        return valueFields.get(index).getText();
    }

    @Override
    public void setValue(final int index, final String value) {
        valueFields.get(index).setText(value);
    }

    @Override
    public void setNumberOfFields(final int number) {
        for (TextBox textBox : valueFields) {
            textBox.removeFromParent();
        }
        andLabel.removeFromParent();
        valueFields = new ArrayList<TextBox>(number);
        for (int i = 0; i < number; i++) {
            if (i > 0) {
                panel.add(andLabel);
            }
            TextBox valueField = new TextBox();
            valueFields.add(valueField);
            panel.add(valueField);
        }
    }

    @Override
    public void setOperatorChoices(final Set<Operator> choices) {
        operators.clear();
        for (Operator operator : choices) {
            operators.addItem(operator.toString(), operator.name());
        }
    }

    @Override
    public void setOperator(final Operator operator) {
        for (int i = 0; i < operators.getItemCount(); i++) {
            String value = operators.getValue(i);
            if (value.equals(operator.name())) {
                operators.setSelectedIndex(i);
                return;
            }
        }
    }

    @Override
    public HasChangeHandlers getOperatorChanger() {
        return operators;
    }

    @Override
    public Operator getOperator() {
        String value = operators.getValue(operators.getSelectedIndex());
        return Operator.valueOf(value);
    }

    @Override
    public HasText getField() {
        return identifierField;
    }
}
