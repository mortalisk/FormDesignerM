package org.openxdata.designer.client.widget.skiprule;

import com.google.gwt.core.client.GWT;
import org.openxdata.sharedlib.client.model.ModelConstants;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.ListBox;
import org.openxdata.sharedlib.client.locale.FormsConstants;

/**
 * Widget used to display the conditions grouping operators (all,any,none)
 * for skip and validation conditions.
 *  
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class GroupOperationWidget extends Composite implements HasEnabled {
    
        final static FormsConstants i18n = GWT.create(FormsConstants.class);

	/** The conditions grouping operator text: all */
	public static final String CONDITIONS_OPERATOR_TEXT_ALL = i18n.all();
	
	/** The conditions grouping operator text: any */
	public static final String CONDITIONS_OPERATOR_TEXT_ANY = i18n.any();
	
	/** The conditions grouping operator text: none */
	public static final String CONDITIONS_OPERATOR_TEXT_NONE = i18n.none();
	
	/** ListBox that holds the grouping operators */
	private final ListBox groupingOptions = new ListBox();
	
	/**
	 * Creates a new instance of the conditions grouping operator.
	 */
	public GroupOperationWidget(){
		addGroupingOptions();
		setEnabled(false);
		initWidget(groupingOptions);
	}

	private void addGroupingOptions(){
		groupingOptions.addItem(CONDITIONS_OPERATOR_TEXT_ALL);
		groupingOptions.addItem(CONDITIONS_OPERATOR_TEXT_ANY);
		groupingOptions.addItem(CONDITIONS_OPERATOR_TEXT_NONE);
	}

	/**
	 * Gets the currently selected conditions grouping operator.
	 * 
	 * @return the operator value. Can only be ModelConstants.CONDITIONS_OPERATOR_AND,
	 *        ModelConstants.CONDITIONS_OPERATOR_OR or ModelConstants.CONDITIONS_OPERATOR_NULL
	 */
	public byte getConditionsOperator(){
		String selectedValue = groupingOptions.getValue(groupingOptions.getSelectedIndex());
		
		if(selectedValue.equals(CONDITIONS_OPERATOR_TEXT_ALL)) {
			return ModelConstants.CONDITIONS_OPERATOR_AND;
		} else if(selectedValue.equals(CONDITIONS_OPERATOR_TEXT_ANY)) {
			return ModelConstants.CONDITIONS_OPERATOR_OR;
		}

		return ModelConstants.CONDITIONS_OPERATOR_NULL;
	}

	
	/**
	 * Sets the conditions grouping operator.
	 * 
	 * @param operator the operator value. Should be ModelConstants.CONDITIONS_OPERATOR_AND,
	 *        ModelConstants.CONDITIONS_OPERATOR_OR or ModelConstants.CONDITIONS_OPERATOR_NULL
	 */
	public void setCondionsOperator(int operator){
		if(operator == ModelConstants.CONDITIONS_OPERATOR_AND) {
			groupingOptions.setSelectedIndex(0);
		} else if(operator == ModelConstants.CONDITIONS_OPERATOR_OR) {
			groupingOptions.setSelectedIndex(1);
		}
	}

	public boolean isEnabled(){
		return groupingOptions.isEnabled();
	}
	
	/** 
	 * Sets whether we enable this widget or not.
	 * 
	 * @param enable set to true to enable, else false.
	 */
	public void setEnabled(boolean enable){
		groupingOptions.setEnabled(enable);
	}

	/**
	 * Reset the group operator widget to its default state
	 */
	public void reset() {
		groupingOptions.setSelectedIndex(0);
	}
}
