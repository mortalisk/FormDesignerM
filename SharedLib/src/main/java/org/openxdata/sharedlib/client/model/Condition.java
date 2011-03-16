package org.openxdata.sharedlib.client.model;

import java.io.Serializable;
import java.util.Date;

import org.openxdata.sharedlib.client.util.FormUtil;

import com.google.gwt.i18n.client.DateTimeFormat;


/**
 * A condition which is part of a rule. For definition of a rule, go to the Rule class.
 * E.g. If sex is Male. If age is greater than than 4. etc
 *
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 */
public class Condition implements Serializable{
	
	private static final long serialVersionUID = 26938503530506663L;
	
	/** expression functions **/
	private static final int FUNC_SUM = 1;
	private static final int FUNC_MAX = 2;
	private static final int FUNC_MIN = 3;
	private static final int FUNC_AVG = 4;

	/** The unique identifier of the question referenced by this condition. */
	private int questionId = ModelConstants.NULL_ID;

	/** The operator of the condition. Eg Equal to, Greater than, etc. */
	private Operator operator = Operator.NONE;

	/** The aggregate function. Eg Length, Value. */
	private int function = ModelConstants.FUNCTION_VALUE;

	/** The value checked to see if the condition is true or false.
	 * For the above example, the value would be 4 or the id of the Male option.
	 * For a list of options this value is the option id, not the value or text value.
	 */
	private String value = ModelConstants.EMPTY_STRING;

	/** The the question whose value is dynamically put in the value property. 
	 * This is useful for only design mode when the question variablename changes
	 * and hence we need to change the value or the value property.
	 * */
	private QuestionDef valueQtnDef;

	/**
	 * The second value checked to see if the condition is true. This has values or
	 * for contitions that have the OR operator or AND operator. Eg weight > 0 and weight < 100
	 */
	private String secondValue = ModelConstants.EMPTY_STRING;

	/** The unique identifier of a condition. */
	private int id = ModelConstants.NULL_ID;

	/** Creates a new condition object. */
	public Condition(){

	}

	/** Copy constructor. */
	public Condition(Condition condition){
		this(condition.getId(),condition.getQuestionId(),condition.getOperator(),condition.getFunction(),condition.getValue());
	}

	/**
	 * Creates a new condition object from its parameters. 
	 * 
	 * @param id - the numeric identifier of the condition.
	 * @param questionId - the numeric identifier of the question.
	 * @param operator - the condition operator.
	 * @param value - the value to be equated to.
	 */
	public Condition(int id,int questionId, Operator operator, int function, String value) {
		this();
		setQuestionId(questionId);
		setOperator(operator);
		setFunction(function);
		setValue(value);
		setId(id);
	}

	public Operator getOperator() {
		return operator;
	}
	public void setOperator(Operator operator) {
		this.operator = operator;
	}
	public int getFunction() {
		return function;
	}

	public void setFunction(int function) {
		this.function = function;
	}

	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getId() {
		return id;
	}
	public void setId(int conditionId) {
		this.id = conditionId;
	}

	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}

	/**
	 * Checks if this condition is true or false.
	 * 
	 * @param formDef the form definition object.
	 * @param validation set to true if this is a validation rule condition, else false if skip rule condition.
	 * @return true if the condition is true, else false.
	 */
	public boolean isTrue(FormDef formDef, boolean validation){
		String tempValue = value;
		boolean ret = true;

		try{
			QuestionDef qn = formDef.getQuestion(this.questionId);
			if(qn == null)
				return false; //possibly question deleted
			
			String realValue = getRealValue(formDef);
			if(realValue == null || realValue.trim().length() == 0){
				return (qn.getAnswer() == null || qn.getAnswer().trim().length() == 0);
			} else if(qn.getAnswer() == null || qn.getAnswer().trim().length() == 0){
				return false;
			}

			value = realValue;
			switch(qn.getDataType()){
			case QuestionDef.QTN_TYPE_TEXT:
				ret = isTextTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_REPEAT:
			case QuestionDef.QTN_TYPE_NUMERIC:
				ret = isNumericTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_DATE:
				ret = isDateTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_DATE_TIME:
				ret = isDateTimeTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_DECIMAL:
				ret = isDecimalTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_LIST_EXCLUSIVE:
			case QuestionDef.QTN_TYPE_LIST_EXCLUSIVE_DYNAMIC:
				ret = isListExclusiveTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_LIST_MULTIPLE:
				ret = isListMultipleTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_TIME:
				ret = isTimeTrue(qn,validation);
				break;
			case QuestionDef.QTN_TYPE_BOOLEAN:
				ret = isTextTrue(qn,validation);
				break;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		value = tempValue;

		return ret;
	}
	
	/**
	 * Attempt to convert the condition expression into a real value. Supported features:
	 * <ul>
	 * <li>Fetch the value of a referenced node e.g. root/question1
	 * <li>Sum the values of various nodes e.g. sum(root/q1 | root/q2 | root/q3)
	 * <li>Avg the values of various nodes e.g. avg(root/q1 | root/q2 | root/q3)
	 * <li>Min the values of various nodes e.g. min(root/q1 | root/q2 | root/q3)
	 * <li>Max the values of various nodes e.g. max(root/q1 | root/q2 | root/q3)
	 * 
	 * @param data the form data containing the current forms data
	 * @return String representing the real value of the expression
	 */
	private String getRealValue(FormDef formDef) {
		String rootNode = formDef.getBinding();
		int expressionFunction = getExpressionFunction();
		if(value.startsWith(rootNode+"/")){
			QuestionDef qn2 = formDef.getQuestion(value.substring(value.indexOf('/')+1));
			if(qn2 != null){
				return qn2.getAnswer();
			} else {
				return null;
			}
		} else if (expressionFunction > 0){
			int lastIndexOf = value.lastIndexOf(')');
			if (lastIndexOf < 0) {
				lastIndexOf = value.length();
			}
			String expression = value.substring(4, lastIndexOf);
			int indexOf = 0;
			double answer = expressionFunction == FUNC_MIN ? Double.MAX_VALUE : 0d;
			int count = 0;
			while (indexOf >= 0){
				int indexOf2 = expression.indexOf('|', indexOf + 1);
				String expressionArgRef = expression.substring(indexOf, indexOf2 > 0 ? indexOf2 : expression.length()).trim();
				String expressionArg = expressionArgRef;
				if(expressionArgRef.startsWith(rootNode+"/")){
					QuestionDef qn2 = formDef.getQuestion(expressionArgRef.substring(expressionArgRef.indexOf('/')+1));
					if(qn2 != null){
						expressionArg = qn2.getAnswer();
					}
				}
				if(expressionArg != null && expressionArg.trim().length() > 0){
					try {
							double argVal = Double.parseDouble(expressionArg);
							answer = processAggregate(answer, argVal, expressionFunction);
					} catch (NumberFormatException e) {
						return null; //unable to sum values
					}
				}
				indexOf = indexOf2 >= 0 ? indexOf2 + 1 : indexOf2;
				count++;
			}
			if (expressionFunction == FUNC_AVG){
				answer = answer / count;
			}
			return String.valueOf(answer);
		}
		return value;
	}

	private int getExpressionFunction() {
		if (value.startsWith("sum(")){
			return FUNC_SUM;
		} else if (value.startsWith("avg(")){
			return FUNC_AVG;
		} else if (value.startsWith("min(")){
			return FUNC_MIN;
		} else if (value.startsWith("max(")){
			return FUNC_MAX;
		}
		return -1;
	}
			
	private double processAggregate(double answer, double nextArg, int function) {
		switch (function) {
		case (FUNC_SUM):
		case (FUNC_AVG):
			return answer + nextArg;
		case (FUNC_MAX):
			return answer > nextArg ? answer : nextArg;
		case (FUNC_MIN):
			return answer < nextArg ? answer : nextArg;
		}
		return 0;
	}

	/**
	 * Check to see if a condition, attached to a numeric question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isNumericTrue(QuestionDef qtn, boolean validation){
		try{
			if(qtn.getAnswer() == null || qtn.getAnswer().trim().length() == 0){
				if(validation && operator == Operator.IS_NOT_NULL)
					return false;
				else if(validation || operator == Operator.NOT_EQUAL ||
						operator == Operator.NOT_BETWEEN)
					return true;
				return operator == Operator.IS_NULL;
			}
			else if(operator == Operator.IS_NOT_NULL)
				return true;

			String answerString = qtn.getAnswer();
			long answerLong;

			if (answerString.equals("-"))
				return false;
			else
				answerLong = Long.parseLong(qtn.getAnswer());
			
			long valueLong = Long.parseLong(value);

			long secondValueLong = valueLong;
			if(secondValue != null && secondValue.trim().length() > 0)
				secondValueLong = Long.parseLong(secondValue);

			if(operator == Operator.EQUAL)
				return valueLong == answerLong;
			else if(operator == Operator.NOT_EQUAL)
				return valueLong != answerLong;
			else if(operator == Operator.LESS)
				return answerLong < valueLong;
			else if(operator == Operator.LESS_EQUAL)
				return answerLong < valueLong || valueLong == answerLong;
			else if(operator == Operator.GREATER)
				return answerLong > valueLong;
			else if(operator == Operator.GREATER_EQUAL)
				return answerLong > valueLong || valueLong == answerLong;
			else if(operator == Operator.BETWEEN)
				return answerLong > valueLong && valueLong < secondValueLong;
			else if(operator == Operator.NOT_BETWEEN)
				return !(answerLong > valueLong && valueLong < secondValueLong);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		return false;
	}

	/**
	 * Check to see if a condition, attached to a text question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isTextTrue(QuestionDef qtn, boolean validation){
		String answer = qtn.getAnswer();

		if(function == ModelConstants.FUNCTION_VALUE){
			if(answer == null || answer.trim().length() == 0){
				if(validation && operator == Operator.IS_NOT_NULL)
					return false;
				else if(validation || operator == Operator.NOT_EQUAL ||
						operator == Operator.NOT_START_WITH ||
						operator == Operator.NOT_CONTAIN)
					return true;

				return operator == Operator.IS_NULL;
			}
			else if(operator == Operator.IS_NOT_NULL)
				return true;

			if(operator == Operator.EQUAL)
				return value.equals(qtn.getAnswer());
			else if(operator == Operator.NOT_EQUAL)
				return !value.equals(qtn.getAnswer());
			else if(operator == Operator.STARTS_WITH)
				return answer.startsWith(value);
			else if(operator == Operator.NOT_START_WITH)
				return !answer.startsWith(value);
			else if(operator == Operator.CONTAINS)
				return answer.contains(value);
			else if(operator == Operator.NOT_CONTAIN)
				return !answer.contains(value);
		}
		else{
			if(answer == null || answer.trim().length() == 0)
				return true;

			long len1 = 0, len2 = 0, len = 0;
			if(value != null && value.trim().length() > 0)
				len1 = Long.parseLong(value);
			if(secondValue != null && secondValue.trim().length() > 0)
				len2 = Long.parseLong(secondValue);

			len = answer.trim().length();

            if (operator == Operator.EQUAL) {
                return len == len1;
            } else if (operator == Operator.NOT_EQUAL) {
                return len != len1;
            } else if (operator == Operator.LESS) {
                return len < len1;
            } else if (operator == Operator.LESS_EQUAL) {
                return len <= len1;
            } else if (operator == Operator.GREATER) {
                return len > len1;
            } else if (operator == Operator.GREATER_EQUAL) {
                return len >= len1;
            } else if (operator == Operator.BETWEEN) {
                return len > len1 && len < len2;
            } else if (operator == Operator.NOT_BETWEEN) {
                return !(len > len1 && len < len2);
            }
		}

		return false;
	}

	/**
	 * Check to see if a condition, attached to a date question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isDateTrue(QuestionDef qtn, boolean validation){
		try{
			if(qtn.getAnswer() == null || qtn.getAnswer().trim().length() == 0){
				if(validation && operator == Operator.IS_NOT_NULL)
					return false;
				else if(validation || operator == Operator.NOT_EQUAL ||
						operator == Operator.NOT_BETWEEN)
					return true;
				return operator == Operator.IS_NULL;
			}
			else if(operator == Operator.IS_NOT_NULL)
				return true;

			Date answer = getDateTimeSubmitFormat(qtn).parse(qtn.getAnswer());
			Date dateValue = null;
			if(QuestionDef.isDateFunction(value))
				dateValue = QuestionDef.getDateFunctionValue(value);	
			else
				dateValue = getDateTimeSubmitFormat(qtn).parse(value);

			Date secondDateValue = dateValue;
			if(secondValue != null && secondValue.trim().length() > 0){
				if(QuestionDef.isDateFunction(secondValue))
					secondDateValue = QuestionDef.getDateFunctionValue(secondValue);	
				else
					secondDateValue = getDateTimeSubmitFormat(qtn).parse(secondValue);
			}

			if(operator == Operator.EQUAL)
				return dateValue.equals(answer);
			else if(operator == Operator.NOT_EQUAL)
				return !dateValue.equals(answer);
			else if(operator == Operator.LESS)
				return answer.before(dateValue);
			else if(operator == Operator.LESS_EQUAL)
				return answer.before(dateValue) || dateValue.equals(answer);
			else if(operator == Operator.GREATER)
				return answer.after(dateValue);
			else if(operator == Operator.GREATER_EQUAL)
				return answer.after(dateValue) || dateValue.equals(answer);
			else if(operator == Operator.BETWEEN)
				return answer.after(dateValue) && dateValue.before(secondDateValue);
			else if(operator == Operator.NOT_BETWEEN)
				return !(answer.after(dateValue) && dateValue.before(secondDateValue));
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		return false;
	}

	private DateTimeFormat getDateTimeSubmitFormat(QuestionDef qtn){
		if(qtn.getDataType() == QuestionDef.QTN_TYPE_DATE_TIME)
			return FormUtil.getDateTimeSubmitFormat();
		else
			return FormUtil.getDateSubmitFormat();
	}


	/**
	 * Check to see if a condition, attached to a date and time question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isDateTimeTrue(QuestionDef qtn, boolean validation){
		return isDateTrue(qtn,validation);
	}


	/**
	 * Check to see if a condition, attached to a time question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isTimeTrue(QuestionDef qtn, boolean validation){
		return isDateTrue(qtn,validation);
	}


	/**
	 * Check to see if a condition, attached to a multiple select question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isListMultipleTrue(QuestionDef qtn, boolean validation){
		try{
			if(qtn.getAnswer() == null || qtn.getAnswer().trim().length() == 0){
				if(validation && operator == Operator.IS_NOT_NULL)
					return false;
				else if(validation || operator == Operator.NOT_EQUAL || 
						operator == Operator.NOT_IN_LIST)
					return true;
				return operator == Operator.IS_NULL;
			}
			else if(operator == Operator.IS_NOT_NULL)
				return true;

			
            if (operator == Operator.EQUAL) {
                return qtn.getAnswer().contains(value);
            } else if (operator == Operator.NOT_EQUAL) {
                return !qtn.getAnswer().contains(value);
            } else if (operator == Operator.IN_LIST) {
                return value.contains(qtn.getAnswer());
            } else if (operator == Operator.NOT_IN_LIST) {
                return !value.contains(qtn.getAnswer());
            }
            return false;

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}


	/**
	 * Check to see if a condition, attached to a single select question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isListExclusiveTrue(QuestionDef qtn, boolean validation){

		try{
			if(qtn.getAnswer() == null || qtn.getAnswer().trim().length() == 0){
				if(validation && operator == Operator.IS_NOT_NULL)
					return false;
				else if(validation || operator == Operator.NOT_EQUAL || 
						operator == Operator.NOT_IN_LIST)
					return true;
				return operator == Operator.IS_NULL;
			}
			else if(operator == Operator.IS_NOT_NULL)
				return true;

            if (operator == Operator.EQUAL) {
                return qtn.getAnswer().equals(value);
            } else if (operator == Operator.NOT_EQUAL) {
                return !qtn.getAnswer().equals(value);
            } else if (operator == Operator.IN_LIST) {
                return value.contains(qtn.getAnswer());
            } else if (operator == Operator.NOT_IN_LIST) {
                return !value.contains(qtn.getAnswer());
            }
            return false;
            
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		return false;
	}


	/**
	 * Check to see if a condition, attached to a decimal question, is true.
	 * 
	 * @param qtn the question whose answer we are using to test the condition
	 * @param validation has value of true if this is a validation logic condition, else false if skip logic one.
	 * @return true if the condition is true, else false.
	 */
	private boolean isDecimalTrue(QuestionDef qtn, boolean validation){
		try{
			if(qtn.getAnswer() == null || qtn.getAnswer().trim().length() == 0){
				if(validation && operator == Operator.IS_NOT_NULL)
					return false;
				else if(validation || operator == Operator.NOT_EQUAL ||
						operator == Operator.NOT_BETWEEN)
					return true;
				return operator == Operator.IS_NULL;
			}
			else if(operator == Operator.IS_NOT_NULL)
				return true;

			
			String answerString = qtn.getAnswer();
			double answer;
			
			if (answerString.equals("-"))
				return false;
			else
				answer = Double.parseDouble(qtn.getAnswer());
			
			double doubleValue = Double.parseDouble(value);

			double secondDoubleValue = doubleValue;
			if(secondValue != null && secondValue.trim().length() > 0)
				secondDoubleValue = Double.parseDouble(secondValue);

			if(operator == Operator.EQUAL)
				return doubleValue == answer;
			else if(operator == Operator.NOT_EQUAL)
				return doubleValue != answer;
			else if(operator == Operator.LESS)
				return answer < doubleValue;
			else if(operator == Operator.LESS_EQUAL)
				return answer < doubleValue || doubleValue == answer;
			else if(operator == Operator.GREATER)
				return answer > doubleValue;
			else if(operator == Operator.GREATER_EQUAL)
				return answer > doubleValue || doubleValue == answer;
			else if(operator == Operator.BETWEEN)
				return answer > doubleValue && doubleValue < secondDoubleValue;
			else if(operator == Operator.NOT_BETWEEN)
				return !(answer > doubleValue && doubleValue < secondDoubleValue);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		return false;
	}


	/**
	 * Gets the value for this condition. If the value references another question,
	 * it returns the answer of that question.
	 * 
	 * @param formDef the form definition object that this condition belongs to.
	 * @return the text value.
	 */
	public String getValue(FormDef formDef){	
		if(value.startsWith(formDef.getBinding()+"/")){
			QuestionDef qn = formDef.getQuestion(value.substring(value.indexOf('/')+1));
			if(qn != null)
				return qn.getAnswer();
		}
		return value;
	}

	/**
	 * Sets the new value of the condition.
	 * 
	 * @param origValue the original value.
	 * @param newValue the new value.
	 */
	public void updateValue(String origValue, String newValue){
		if(origValue.equals(value))
			value = newValue;
	}

	public void setValueQtnDef(QuestionDef valueQtnDef){
		this.valueQtnDef = valueQtnDef;
	}

	public QuestionDef getValueQtnDef(){
		return valueQtnDef;
	}

	/**
	 * Checks if this condition references an answer of a particular question.
	 * 
	 * @param questionDef the question whose answer is referenced.
	 * @param formDef the form being filled.
	 * @return true if it does, else false.
	 */
	public boolean hasQuestion(QuestionDef questionDef, FormDef formDef){
		if(value.startsWith(formDef.getBinding()+"/")){
			QuestionDef qtn = formDef.getQuestion(value.substring(value.indexOf('/')+1));
			if(qtn != null && qtn == questionDef)
				return true;
		}

		return false;
	}

	/**
	 * Gets the question, if any, referenced by a condition value.
	 * Such values exit for cross field validations where a condition's
	 * value is not static but dynamic in the sense that it comes from 
	 * the answer of some other question (The question we are returning)
	 * An example of such a condition is "Male number of kids should be
	 * less than or equal to the Total number of kids."
	 * 
	 * @param formDef the form that this condition belongs to.
	 * @return the question if any is found.
	 */
	public QuestionDef getQuestion(FormDef formDef){
		if(value.startsWith(formDef.getBinding()+"/"))
			return formDef.getQuestion(value.substring(value.indexOf('/')+1));

		return null;
	}
}
