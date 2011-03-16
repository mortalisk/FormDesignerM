package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.TextQuestion;
import org.openxdata.sharedlib.client.model.Condition2;
import org.openxdata.sharedlib.client.model.Question;
import org.openxdata.sharedlib.client.model.Operator;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class ConditionEntryPoint implements EntryPoint {

    @Override
    public void onModuleLoad() {
        RootPanel conditionPanel = RootPanel.get("condition");
        ConditionViewImpl view = new ConditionViewImpl();
        Question question = new TextQuestion();
        question.setIdentifier("pregnant");
        Condition2 condition = new Condition2(Operator.EQUAL, question);
        
        condition.setValue1("1");
        ConditionPresenter presenter = new ConditionPresenter(view, condition);

        conditionPanel.add(view);
    }
}
