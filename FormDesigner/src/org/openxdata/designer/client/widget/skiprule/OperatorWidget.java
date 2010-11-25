package org.openxdata.designer.client.widget.skiprule;

import org.openxdata.designer.client.controller.ItemSelectionListener;
import org.openxdata.sharedlib.client.locale.LocaleText;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;


/**
 * Widget used to display the condition operators (eg equal to, less than, greater than, etc)
 * for skip and validation rules.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class OperatorWidget extends Composite implements ChangeHandler {
	
	/** The operator text: is equal to */
	public static final String OP_TEXT_EQUAL = LocaleText.get("isEqualTo");
	
	/** The operator text: is not equal to */
	public static final String OP_TEXT_NOT_EQUAL = LocaleText.get("isNotEqual");
	
	/** The operator text: is less than */
	public static final String OP_TEXT_LESS_THAN = LocaleText.get("isLessThan");
	
	/** The operator text: is less than or equal to */
	public static final String OP_TEXT_LESS_THAN_EQUAL = LocaleText.get("isLessThanOrEqual");
	
	/** The operator text: is greater than */
	public static final String OP_TEXT_GREATER_THAN = LocaleText.get("isGreaterThan");
	
	/** The operator text: is greater than or equal to */
	public static final String OP_TEXT_GREATER_THAN_EQUAL = LocaleText.get("isGreaterThanOrEqual");
	
	/** The operator text: is null */
	public static final String OP_TEXT_NULL = LocaleText.get("isNull");
	
	/** The operator text: is null */
	public static final String OP_TEXT_NOT_NULL = LocaleText.get("isNotNull");
	
	/** The operator text: is in list */
	public static final String OP_TEXT_IN_LIST = LocaleText.get("isInList");
	
	/** The operator text: is not in list */
	public static final String OP_TEXT_NOT_IN_LIST = LocaleText.get("isNotInList");
	
	/** The operator text: is between */
	public static final String OP_TEXT_BETWEEN = LocaleText.get("isBetween");
	
	/** The operator text: is not between */
	public static final String OP_TEXT_NOT_BETWEEN = LocaleText.get("isNotBetween");

	/** The current question data type. */
	private int dataType =  QuestionDef.QTN_TYPE_TEXT;
	
	/** The selection change listener. */
	private ItemSelectionListener itemSelectionListener;

	/** List box that contains the operators the user can select from */
	private final ListBox listbox;
	
	/**
	 * Creates a new instance of the operator widget
	 * 
	 * @param text the default display text.
	 * @param itemSelectionListener the listener to selection change events.
	 */
	public OperatorWidget(String displayText, ItemSelectionListener itemSelectionListener){
		listbox = new ListBox();
		listbox.addChangeHandler(this);
		initWidget(listbox);
		this.itemSelectionListener = itemSelectionListener;
		
		buildOptionList();
		setSelectedOperator(displayText);
	}
	
	/**
	 * Sets the data type of the question to which the operator is being applied.
	 * 
	 * @param dataType the data type.
	 */
	public void setDataType(int dataType){
		this.dataType = dataType;
		buildOptionList();
	}
	  
	/**
	 * Builds the options to put in the list box. The options are based
	 * on the data type of the question
	 */
	private void buildOptionList(){
		listbox.clear();
		
		if(!(dataType == QuestionDef.QTN_TYPE_GPS || dataType == QuestionDef.QTN_TYPE_VIDEO ||
				dataType == QuestionDef.QTN_TYPE_AUDIO || dataType == QuestionDef.QTN_TYPE_IMAGE ||
				dataType == QuestionDef.QTN_TYPE_BARCODE)){
			listbox.addItem(OP_TEXT_EQUAL);
			listbox.addItem(OP_TEXT_NOT_EQUAL);
		}
		  
		if(dataType == QuestionDef.QTN_TYPE_DATE || dataType == QuestionDef.QTN_TYPE_DATE_TIME ||
			dataType == QuestionDef.QTN_TYPE_DECIMAL || dataType == QuestionDef.QTN_TYPE_NUMERIC ||
			dataType == QuestionDef.QTN_TYPE_TIME || dataType == QuestionDef.QTN_TYPE_REPEAT){
			
			listbox.addItem(OP_TEXT_GREATER_THAN);	  
			listbox.addItem(OP_TEXT_GREATER_THAN_EQUAL);	  
			listbox.addItem(OP_TEXT_LESS_THAN);			  	  
			listbox.addItem(OP_TEXT_LESS_THAN_EQUAL);		  
			listbox.addItem(OP_TEXT_BETWEEN);	  
			listbox.addItem(OP_TEXT_NOT_BETWEEN);
		}
		
		if(dataType == QuestionDef.QTN_TYPE_LIST_EXCLUSIVE || dataType == QuestionDef.QTN_TYPE_LIST_MULTIPLE){		  
			listbox.addItem(OP_TEXT_IN_LIST);	  
			listbox.addItem(OP_TEXT_NOT_IN_LIST);
		}
			  
		listbox.addItem(OP_TEXT_NULL);
		listbox.addItem(OP_TEXT_NOT_NULL);
	}
	
	@Override
	public void onChange(ChangeEvent event) {
		String itemSelected = listbox.getItemText(listbox.getSelectedIndex());
		itemSelectionListener.onItemSelected(this, fromOperatorText2Value(itemSelected));
	}
	
	/**
	 * Converts operator text to its int representation.
	 * 
	 * @param text the operator text.
	 * @return the operator int value.
	 */
	private int fromOperatorText2Value(String text){
		if(text.equals(OP_TEXT_EQUAL))
			return ModelConstants.OPERATOR_EQUAL;
		else if(text.equals(OP_TEXT_NOT_EQUAL))
			return ModelConstants.OPERATOR_NOT_EQUAL;
		else if(text.equals(OP_TEXT_LESS_THAN))
			return ModelConstants.OPERATOR_LESS;
		else if(text.equals(OP_TEXT_LESS_THAN_EQUAL))
			return ModelConstants.OPERATOR_LESS_EQUAL;
		else if(text.equals(OP_TEXT_GREATER_THAN))
			return ModelConstants.OPERATOR_GREATER;
		else if(text.equals(OP_TEXT_GREATER_THAN_EQUAL))
			return ModelConstants.OPERATOR_GREATER_EQUAL;
		else if(text.equals(OP_TEXT_NULL))
			return ModelConstants.OPERATOR_IS_NULL;
		else if(text.equals(OP_TEXT_NOT_NULL))
			return ModelConstants.OPERATOR_IS_NOT_NULL;
		else if(text.equals(OP_TEXT_IN_LIST))
			return ModelConstants.OPERATOR_IN_LIST;
		else if(text.equals(OP_TEXT_NOT_IN_LIST))
			return ModelConstants.OPERATOR_NOT_IN_LIST;
		else if(text.equals(OP_TEXT_BETWEEN))
			return ModelConstants.OPERATOR_BETWEEN;
		else if(text.equals(OP_TEXT_NOT_BETWEEN))
			return ModelConstants.OPERATOR_NOT_BETWEEN;
		return ModelConstants.OPERATOR_NULL;
	}
	
	/**
	 * Converts the operator int value to its text representation.
	 * 
	 * @param operator the operator int value.
	 */
	public void setOperator(int operator){
		String operatorText = null;
		
		if(operator == ModelConstants.OPERATOR_EQUAL)
			operatorText = OP_TEXT_EQUAL;
		else if(operator == ModelConstants.OPERATOR_NOT_EQUAL)
			operatorText = OP_TEXT_NOT_EQUAL;
		else if(operator == ModelConstants.OPERATOR_LESS)
			operatorText = OP_TEXT_LESS_THAN;
		else if(operator == ModelConstants.OPERATOR_LESS_EQUAL)
			operatorText = OP_TEXT_LESS_THAN_EQUAL;
		else if(operator == ModelConstants.OPERATOR_GREATER)
			operatorText = OP_TEXT_GREATER_THAN;
		else if(operator == ModelConstants.OPERATOR_GREATER_EQUAL)
			operatorText = OP_TEXT_GREATER_THAN_EQUAL;
		else if(operator == ModelConstants.OPERATOR_IS_NULL)
			operatorText = OP_TEXT_NULL;
		else if(operator == ModelConstants.OPERATOR_IS_NOT_NULL)
			operatorText = OP_TEXT_NOT_NULL;
		else if(operator == ModelConstants.OPERATOR_IN_LIST)
			operatorText = OP_TEXT_IN_LIST;
		else if(operator == ModelConstants.OPERATOR_NOT_IN_LIST)
			operatorText = OP_TEXT_NOT_IN_LIST;
		else if(operator == ModelConstants.OPERATOR_BETWEEN)
			operatorText = OP_TEXT_BETWEEN;
		else if(operator == ModelConstants.OPERATOR_NOT_BETWEEN)
			operatorText = OP_TEXT_NOT_BETWEEN;
		
		// now set the operator as the selected option in the listbox
		setSelectedOperator(operatorText);
	}

	/**
	 * Given an operator string, this method will attempt to set the corresponding
	 * option in the list box as the currently selected item
	 * 
	 * @param operatorText
	 */
	private void setSelectedOperator(String operatorText) {
		int optionCnt = listbox.getItemCount();
		for(int i = 0; i < optionCnt; i++) {
			if (listbox.getItemText(i).equals(operatorText)) {
				listbox.setSelectedIndex(i);
				break;
			}
		}
	}
}
