package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;
import org.junit.Before;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class QuestionTest {

    TextQuestion textQuestion;

    @Before
    public void createTextQuestion() {
        textQuestion = new TextQuestion();
    }

    @Test
    public void textQuestionShouldHaveAtleastOneValidOperator() {
        Set<Operator> operators = textQuestion.validOperators();
        assertThatOperatorIsNotEmpty(operators);
    }

    @Test
    public void imageQuestionShouldHaveAtleastOneValidOperator() {
        ImageQuestion question = new ImageQuestion();
        Set<Operator> operators = question.validOperators();
        assertThatOperatorIsNotEmpty(operators);
    }

    @Test
    public void questionShouldNotReturnNullForDefaultOperator() {
        assertThat(textQuestion.defaultOperator(), notNullValue());
    }

    @Test
    public void identifierShouldNotBeNull() {
        assertThat(textQuestion.getIdentifier(), notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setIdentifierShouldNotAllowNull() {
        textQuestion.setIdentifier(null);
    }

    private void assertThatOperatorIsNotEmpty(Set<Operator> operators) {
        assertThat(operators.size(), greaterThan(0));
    }
}
