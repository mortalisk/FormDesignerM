package org.openxdata.formtools.client.controller;

import com.google.gwt.user.client.ui.Widget;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface DisplayColumnActionListener {
	
	public void moveColumnUp(Widget sender);
	public void moveColumnDown(Widget sender);
	public void deleteColumn(Widget sender);
	public void showSimpleColum(Widget sender);
	public void showAggregateColumn(Widget sender);
	public void changeSortOrder(Widget sender, int sortOrder);
	public void changeDisplayText(Widget sender, String text);
}
