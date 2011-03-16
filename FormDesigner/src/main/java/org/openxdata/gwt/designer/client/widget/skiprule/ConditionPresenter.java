package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import java.util.Set;

/**
 * Presenter for Condition widget.
 */
public final class ConditionPresenter {

    private final ConditionView view;
    private final Condition2 condition;
    private ChangeHandler operatorChangeHandler = new ChangeHandler() {

        @Override
        public void onChange(final ChangeEvent event) {
            condition.setOperator(view.getOperator());
            setValueFields();
        }
    };

    public ConditionPresenter(final ConditionView view,
            final Condition2 condition) {

        HasChangeHandlers operatorChanger = view.getOperatorChanger();
        operatorChanger.addChangeHandler(operatorChangeHandler);
        this.view = view;
        this.condition = condition;
        updateView();
    }

    private void updateView() {
        setQuestionIdentifier();
        setValueFields();
        setOperatorChoices();
    }

    private void setQuestionIdentifier() {
        String identifier = condition.getQuestion().getIdentifier();
        view.getField().setText(identifier);
    }

    private void setValueFields() {
        Operator operator = condition.getOperator();
        int numberOfValues = operator.getNumberOfValues();
        view.setNumberOfFields(numberOfValues);

        if (numberOfValues == 2) {
            view.setValue(0, condition.getValue1());
            view.setValue(1, condition.getValue2());
        } else if (numberOfValues == 1) {
            view.setValue(0, condition.getValue1());
        }
    }

    /**
     * Sets the correct operator choices on the view.
     */
    private void setOperatorChoices() {
        Set<Operator> operators = condition.getQuestion().validOperators();
        view.setOperatorChoices(operators);
        view.setOperator(condition.getOperator());
    }

    ChangeHandler getOperatorChangeHandler() {
        return operatorChangeHandler;
    }

    public Condition2 getCondition() {
        return condition;
    }

    public ConditionView getView() {
        return view;
    }
}
