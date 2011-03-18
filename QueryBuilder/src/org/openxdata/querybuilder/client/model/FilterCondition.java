package org.openxdata.querybuilder.client.model;

import org.openxdata.sharedlib.client.model.Operator;
import org.openxdata.sharedlib.client.model.QuestionType;



/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FilterCondition extends FilterConditionRow {

	/**
	 * Generated serialization ID
	 */
	private static final long serialVersionUID = -2688667012773543641L;
	
	private String fieldName;
	private String firstValue;
	private String secondValue;
	private Operator operator;
	private int dataType;
	
	
	public FilterCondition(){
		
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(String firstValue) {
		this.firstValue = firstValue;
	}

	public String getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(String secondValue) {
		this.secondValue = secondValue;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(QuestionType dataType) {
		this.dataType = dataType.getLegacyConstant();
	}
}
