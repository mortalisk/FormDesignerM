package org.openxdata.querybuilder.client.controller;

import org.openxdata.querybuilder.client.widget.ConditionActionHyperlink;
import org.openxdata.querybuilder.client.widget.ConditionWidget;

import com.google.gwt.user.client.ui.Widget;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface FilterRowActionListener {
	
	public ConditionWidget addCondition(Widget sender);
	public ConditionActionHyperlink addBracket(Widget sender, String operator, boolean addCondition);
	public void deleteCurrentRow(Widget sender);
}
