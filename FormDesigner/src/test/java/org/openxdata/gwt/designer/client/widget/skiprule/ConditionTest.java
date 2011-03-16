/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;
import java.util.EnumSet;
import org.junit.Before;
import org.mockito.Mock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static junit.framework.Assert.*;

/**
 *
 * @author morten
 */
@RunWith(MockitoJUnitRunner.class)
public class ConditionTest {

    @Mock
    Question question;
    Condition2 condition;

    @Before
    public void createCondition() {
        EnumSet<Operator> operators = EnumSet.allOf(Operator.class);
        when(question.validOperators()).thenReturn(operators);
        condition = new Condition2(Operator.EQUAL, question);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldCheckConsistentOperator() {
        Question question = new TextQuestion();
        new Condition2(Operator.BETWEEN, question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenQuestionIsNullThrowIllegalArgumentExeption() {
        new Condition2(Operator.IS_NULL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenOperatorIsNullThrowIllegalArgumentExeption() {
        new Condition2(null, question);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setOperatorShouldNotAllowNullInput() {
        condition.setOperator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setQuestionShouldNotAllowNullInput() {
        condition.setQuestion(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setValue1ShouldNotAllowNullInput() {
        condition.setValue1(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setValue2ShouldNotAllowNullInput() {
        condition.setValue2(null);
    }

    @Test
    public void valuesShouldBeEmptyAfterInitialization() {
        assertTrue("Value1 should be the empty string", condition.getValue1().isEmpty());
        assertTrue("Value2 should be the empty string", condition.getValue2().isEmpty());
    }
}
