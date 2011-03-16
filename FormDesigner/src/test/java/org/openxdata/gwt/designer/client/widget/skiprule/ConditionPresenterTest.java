package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Condition2;
import org.openxdata.sharedlib.client.model.Question;
import org.openxdata.sharedlib.client.model.Operator;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.HasText;
import java.util.EnumSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static junit.framework.Assert.*;

/**
 *
 * @author morten
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionPresenterTest {

    @Mock
    ConditionView view;
    Condition2 condition;
    @Mock
    HasText field;
    @Mock
    Question question;

    @Before
    public void createCondition() {
        HasChangeHandlers changeHandler = mock(HasChangeHandlers.class);
        when(view.getOperatorChanger()).thenReturn(changeHandler);

        condition = new Condition2(Operator.BETWEEN, question);
        when(view.getField()).thenReturn(field);
    }

    @Before
    public void setupQuestion() {
        Set<Operator> operators = EnumSet.allOf(Operator.class);
        when(question.validOperators()).thenReturn(operators);
        when(question.getIdentifier()).thenReturn("TEST123");
    }

    @Test
    public void shouldInitializeViewAccordingToGivenCondition() {
        condition.setOperator(Operator.EQUAL);
        condition.setValue1("test");

        new ConditionPresenter(view, condition);

        verify(view).setOperator(Operator.EQUAL);
        verify(view).setNumberOfFields(1);
        verify(view).setValue(0, "test");
    }

    @Test
    public void operatorBetweenShouldGiveTwoValueInputs() {
        condition.setOperator(Operator.BETWEEN);
        condition.setValue1("1");
        condition.setValue2("4");
        new ConditionPresenter(view, condition);

        verify(view).setValue(0, "1");
        verify(view).setValue(1, "4");
        verify(view).setNumberOfFields(2);
    }

    @Test
    public void givenOperatorIsNullThenZeroValueFields() {
        condition.setOperator(Operator.IS_NULL);
        new ConditionPresenter(view, condition);

        verify(view).setNumberOfFields(0);
    }

    @Test
    public void givenOperatorIsNotNullThenZeroValueFields() {
        condition.setOperator(Operator.IS_NOT_NULL);
        new ConditionPresenter(view, condition);

        verify(view).setNumberOfFields(0);
    }

    @Test
    public void questionTypeShouldGiveCorrectOperator() {
        Set<Operator> choices = EnumSet.of(Operator.IS_NOT_NULL, Operator.IS_NULL, Operator.NOT_CONTAIN);
        condition.setOperator(Operator.IS_NULL);
        Question conditionQuestion = mock(Question.class);
        when(conditionQuestion.validOperators()).thenReturn(choices);
        condition.setQuestion(conditionQuestion);

        new ConditionPresenter(view, condition);

        verify(view).setOperatorChoices(choices);
        verify(view).setOperator(Operator.IS_NULL);
    }

    @Test
    public void operatorChangeEventUpdatesViewAndModel() {
        condition.setOperator(Operator.IS_NULL);
        ConditionPresenter cp = new ConditionPresenter(view, condition);
        reset(view);

        Operator operator = Operator.STARTS_WITH;
        when(view.getOperator()).thenReturn(operator);
        cp.getOperatorChangeHandler().onChange(null);

        verify(view).setNumberOfFields(operator.getNumberOfValues());
        assertEquals(operator, condition.getOperator());
    }

    @Test
    public void questionField() {
        when(condition.getQuestion().getIdentifier()).thenReturn("abc");

        ConditionPresenter cp = new ConditionPresenter(view, condition);
        verify(field).setText("abc");
    }
}
