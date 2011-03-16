package org.openxdata.gwt.designer.client.widget.skiprule;

import org.openxdata.sharedlib.client.model.Operator;
import java.util.HashSet;
import java.util.Set;
import org.openxdata.sharedlib.client.model.QuestionDef;

/**
 * Temporary wrapper for QuestionDef in new code.
 * @deprecated Will be replaced by a set of Question classes (TextQuestion,
 * ImageQuestion, etc.)
 * @see Question
 * @see TextQuestion
 */
@Deprecated
public final class LegacyQuestion implements Question {

    private QuestionDef legacyQuestion;

    public LegacyQuestion(final QuestionDef legacyQuestion) {
        this.legacyQuestion = legacyQuestion;
    }

    public Set<Operator> validOperators() {
        Set<Operator> operators = new HashSet<Operator>();
        int dataType = legacyQuestion.getDataType();
        if (!(dataType == QuestionDef.QTN_TYPE_GPS
                || dataType == QuestionDef.QTN_TYPE_VIDEO
                || dataType == QuestionDef.QTN_TYPE_AUDIO
                || dataType == QuestionDef.QTN_TYPE_IMAGE
                || dataType == QuestionDef.QTN_TYPE_BARCODE)) {

            operators.add(Operator.EQUAL);
            operators.add(Operator.NOT_EQUAL);
        }

        if (dataType == QuestionDef.QTN_TYPE_DATE
                || dataType == QuestionDef.QTN_TYPE_DATE_TIME
                || dataType == QuestionDef.QTN_TYPE_DECIMAL
                || dataType == QuestionDef.QTN_TYPE_NUMERIC
                || dataType == QuestionDef.QTN_TYPE_TIME
                || dataType == QuestionDef.QTN_TYPE_REPEAT) {

            operators.add(Operator.GREATER);
            operators.add(Operator.GREATER_EQUAL);
            operators.add(Operator.LESS);
            operators.add(Operator.LESS_EQUAL);
            operators.add(Operator.BETWEEN);
            operators.add(Operator.NOT_BETWEEN);
        }

        if (dataType == QuestionDef.QTN_TYPE_LIST_EXCLUSIVE
                || dataType == QuestionDef.QTN_TYPE_LIST_MULTIPLE) {
            operators.add(Operator.IN_LIST);
            operators.add(Operator.NOT_IN_LIST);
        }

        operators.add(Operator.IS_NULL);
        operators.add(Operator.IS_NOT_NULL);

        return operators;
    }

    public String getIdentifier() {
        return legacyQuestion.getBinding();

    }

    public void setIdentifier(final String string) {
        legacyQuestion.setVariableName(string);
    }

    public Operator defaultOperator() {
        return Operator.IS_NULL;
    }
}
