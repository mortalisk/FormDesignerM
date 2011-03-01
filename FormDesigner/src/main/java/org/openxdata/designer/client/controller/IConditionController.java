package org.openxdata.designer.client.controller;

import org.openxdata.designer.client.widget.skiprule.ConditionWidget;


/**
 * This interface is implemented by those classes that want to listen to events which
 * happen when the user manipulates conditions for validation and skip rules.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface IConditionController {

	/**
	 * Called to add a new condition.
	 */
	public void addCondition();
	
	/**
	 * Called to add a bracket for grouping of related conditions.
	 */
	public void addBracket();
	
	/**
	 * Called to delete a condition.
	 * 
	 * @param conditionWidget the widget for the condition to delete.
	 */
	public void deleteCondition(ConditionWidget conditionWidget);
}
