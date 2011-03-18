package org.openxdata.sharedlib.client.xforms;

import java.util.HashMap;
import java.util.Iterator;

import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.xml.client.Element;
import org.openxdata.sharedlib.client.model.Operator;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * Utility methods used during the parsing of xforms documents.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class XformParserUtil {

	/**
	 * All methods in this class are static and hence we expect no external
	 * Instantiation of this class.
	 */
	private XformParserUtil(){

	}
	
	
	/**
	 * Converts an xpath operator in a relevant or constraint expression to its 
	 * corresponding constant in our object model.
	 * 
	 * @param expression the xpath expression.
	 * @param action the skip or validation rule action to the target questions.
	 * @return the operator constant.
	 */
	//TODO Add the other xpath operators
	public static Operator getOperator(String expression, int action){
		//We return the operator which is the opposite of the expression
		if(expression.indexOf(">=") > 0 || expression.indexOf("&gt;=") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.GREATER_EQUAL;
			return Operator.LESS;
		}
		else if(expression.indexOf('>') > 0 || expression.indexOf("&gt;") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.GREATER;
			return Operator.LESS_EQUAL;
		}
		else if(expression.indexOf("<=") > 0 || expression.indexOf("&lt;=") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.LESS_EQUAL;
			return Operator.GREATER;
		}
		else if(expression.indexOf('<') > 0 || expression.indexOf("&lt;") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.LESS;
			return Operator.GREATER_EQUAL;
		}
		else if(expression.indexOf("!=") > 0 || expression.indexOf("!=") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.NOT_EQUAL;
			return Operator.EQUAL;
		}
		else if(expression.indexOf('=') > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.EQUAL;
			return Operator.NOT_EQUAL;
		}
		else if(expression.indexOf("not(starts-with") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.NOT_START_WITH;
			return Operator.STARTS_WITH;
		}
		else if(expression.indexOf("starts-with") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.STARTS_WITH;
			return Operator.NOT_START_WITH;
		}
		else if(expression.indexOf("not(contains") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.NOT_CONTAIN;
			return Operator.CONTAINS;
		}
		else if(expression.indexOf("contains") > 0){
			if(XformBuilderUtil.isPositiveAction(action))
				return Operator.CONTAINS;
			return Operator.NOT_CONTAIN;
		}

		return Operator.NONE;
	}

	
	/**
	 * Gets the xpath operator size of an operator constant.
	 * 
	 * @param operator the operator constant.
	 * @param action the skip or validation rule target action.
	 * @return the xpath operator size.
	 */
	public static int getOperatorSize(Operator operator, int action){
		if(operator == Operator.GREATER_EQUAL || 
				operator == Operator.LESS_EQUAL ||
				operator == Operator.NOT_EQUAL)
			return XformBuilderUtil.isPositiveAction(action) ? 2 : 1;
		else if(operator == Operator.LESS ||
				operator == Operator.GREATER || 
				operator == Operator.EQUAL)
			return XformBuilderUtil.isPositiveAction(action) ? 1 : 2;

		return 0;
	}

	
	/**
	 * Gets the position of an operator in an xpath expression.
	 * 
	 * @param expression the xpath expression.
	 * @return the operator start position or index.
	 */
	public static int getOperatorPos(String expression){
		//Using lastindexof because of expressions like:
		//relevant="/ClinicalData/SubjectData/StudyEventData/FormData/ItemGroupData/ItemData[@ItemOID='I_REVI_IMPROVEMENT']/@Value = '1'"
		int pos = expression.lastIndexOf("!=");
		if(pos > 0)
			return pos;

		pos = expression.lastIndexOf(">=");
		if(pos > 0)
			return pos;

		pos = expression.lastIndexOf("<=");
		if(pos > 0)
			return pos;

		pos = expression.lastIndexOf('>');
		if(pos > 0)
			return pos;

		pos = expression.lastIndexOf('<');
		if(pos > 0)
			return pos;

		pos = expression.lastIndexOf('=');
		if(pos > 0)
			return pos;
		
		//the order of the code below should not be changed as for example 'starts with' can be taken
		//even when condition is 'not(starts-with'
		
		pos = expression.lastIndexOf("not(starts-with");
		if(pos > 0)
			return pos;
		
		pos = expression.lastIndexOf("starts-with");
		if(pos > 0)
			return pos;
		
		pos = expression.lastIndexOf("not(contains");
		if(pos > 0)
			return pos;
		
		pos = expression.lastIndexOf("contains");
		if(pos > 0)
			return pos;

		return pos;
	}

	
	/**
	 * Gets the question variable name without the form prefix (/newform1/)
	 * 
	 * @param bindNode the xforms bind node.
	 * @param formDef the form to which the question belongs.
	 * @return the question variable name.
	 */
	public static String getQuestionVariableName(Element bindNode, FormDef formDef){
		String name = bindNode.getAttribute(XformConstants.ATTRIBUTE_NAME_NODESET);

		if(name.startsWith("/"+formDef.getBinding()+"/"))
			name = name.replace("/"+formDef.getBinding()+"/", "");
		
		return name;
	}
	
	
	/**
	 * Sets the question definition object data type based on an xml xsd type.
	 * 
	 * @param def the question definition object.
	 * @param type the xml xsd type.
	 * @param node the xforms node having the type attribute.
	 */
	public static void setQuestionType(QuestionDef def, String type, Element node){
		if(type != null){
			if(type.equals(XformConstants.DATA_TYPE_TEXT) || type.indexOf("string") != -1 ){
				String format = node.getAttribute(XformConstants.ATTRIBUTE_NAME_FORMAT);
				if(XformConstants.ATTRIBUTE_VALUE_GPS.equals(format))
					def.setDataType(QuestionType.GPS);
				else
					def.setDataType(QuestionType.TEXT);
			}
			else if((type.equals("xsd:integer") || type.equals(XformConstants.DATA_TYPE_INT)) || (type.indexOf("integer") != -1 || (type.indexOf("int") != -1) && !type.equals("geopoint") ))
				def.setDataType(QuestionType.NUMERIC);
			else if(type.equals("xsd:decimal") || type.indexOf("decimal") != -1 )
				def.setDataType(QuestionType.DECIMAL);
			else if(type.equals("xsd:dateTime") || type.indexOf("dateTime") != -1 )
				def.setDataType(QuestionType.DATE_TIME);
			else if(type.equals("xsd:time") || type.indexOf("time") != -1 )
				def.setDataType(QuestionType.TIME);
			else if(type.equals(XformConstants.DATA_TYPE_DATE) || type.indexOf("date") != -1 )
				def.setDataType(QuestionType.DATE);
			else if(type.equals(XformConstants.DATA_TYPE_BOOLEAN) || type.indexOf("boolean") != -1 )
				def.setDataType(QuestionType.BOOLEAN);
			else if(type.equals(XformConstants.DATA_TYPE_BINARY) || type.indexOf("base64Binary") != -1 ){
				String format = node.getAttribute(XformConstants.ATTRIBUTE_NAME_FORMAT);
				if(XformConstants.ATTRIBUTE_VALUE_VIDEO.equals(format))
					def.setDataType(QuestionType.VIDEO);
				else if(XformConstants.ATTRIBUTE_VALUE_AUDIO.equals(format))
					def.setDataType(QuestionType.AUDIO);
				else
					def.setDataType(QuestionType.IMAGE);
			}
			
			else if(type.equalsIgnoreCase("binary")){
				def.setDataType(QuestionType.IMAGE);
			}
			else if(type.equalsIgnoreCase("geopoint"))
				def.setDataType(QuestionType.GPS);
			else if(type.equalsIgnoreCase("barcode"))
				def.setDataType(QuestionType.BARCODE);
		}
		else
			def.setDataType(QuestionType.TEXT); //QTN_TYPE_REPEAT
	}
	
	
	/**
	 * Goes through a given map of constraints attribute vaues and replaces the question 
	 * whose variable name matches with a given question.
	 * 
	 * @param constraints a map of contraints attribute values keyed by their question 
	 * 					  definition objects.
	 * @param questionDef the question definition object to replace that in the constraint map.
	 */
	public static void replaceConstraintQtn(HashMap<QuestionDef, String> constraints, QuestionDef questionDef){
		Iterator<QuestionDef> keys = constraints.keySet().iterator();
		while(keys.hasNext()){
			QuestionDef qtn = (QuestionDef)keys.next();
			if(qtn.getBinding().equals(questionDef.getBinding())){
				String constraint = (String)constraints.get(qtn);
				if(constraint != null){
					constraints.remove(qtn);
					constraints.put(questionDef, constraint);
				}
				return;
			}
		}
	}
	
	
	/**
	 * Gets the skip rule action for a question.
	 * 
	 * @param qtn the question definition object.
	 * @return the skip rule action which can be (ModelConstants.ACTION_DISABLE,
	 *         ModelConstants.ACTION_HIDE,ModelConstants.ACTION_SHOW or ModelConstants.ACTION_ENABLE)
	 */
	public static int getAction(QuestionDef qtn){
		Element node = qtn.getBindNode();
		if(node == null)
			return ModelConstants.ACTION_DISABLE;

		String value = node.getAttribute(XformConstants.ATTRIBUTE_NAME_ACTION);
		if(value == null)
			return ModelConstants.ACTION_DISABLE;

		int action = 0;
		if(value.equalsIgnoreCase(XformConstants.ATTRIBUTE_VALUE_ENABLE))
			action |= ModelConstants.ACTION_ENABLE;
		else if(value.equalsIgnoreCase(XformConstants.ATTRIBUTE_VALUE_DISABLE))
			action |= ModelConstants.ACTION_DISABLE;
		else if(value.equalsIgnoreCase(XformConstants.ATTRIBUTE_VALUE_SHOW))
			action |= ModelConstants.ACTION_SHOW;
		else if(value.equalsIgnoreCase(XformConstants.ATTRIBUTE_VALUE_HIDE))
			action |= ModelConstants.ACTION_HIDE;

		value = node.getAttribute(XformConstants.ATTRIBUTE_NAME_REQUIRED);
		if(XformConstants.XPATH_VALUE_TRUE.equalsIgnoreCase(value))
			action |= ModelConstants.ACTION_MAKE_MANDATORY;
		else 
			action |= ModelConstants.ACTION_MAKE_OPTIONAL;

		return action;
	}
	
	
	/**
	 * Gets the operator constant used to combine conditions in an expath expression.
	 * This will either be and AND or OR
	 * For now, we do not allow a mixture of these operators in the same expression.
	 * But we allow more than one as long as it is of the same type, either AND or OR.
	 * 
	 * @param expression the xpath expression.
	 * @return the operator constant.
	 */
	public static int getConditionsOperator(String expression){
		if(expression.toLowerCase().indexOf(XformConstants.CONDITIONS_OPERATOR_TEXT_AND) > 0)
			return ModelConstants.CONDITIONS_OPERATOR_AND;
		else if(expression.toLowerCase().indexOf(XformConstants.CONDITIONS_OPERATOR_TEXT_OR) > 0)
			return ModelConstants.CONDITIONS_OPERATOR_OR;
		return ModelConstants.CONDITIONS_OPERATOR_NULL;
	}
}
