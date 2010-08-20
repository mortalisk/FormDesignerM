package org.openxdata.formtools.client.controller;

import com.google.gwt.user.client.ui.Widget;


/**
 * This interface is implemented by those classes which want to listen to 
 * widget context menu commands.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface IWidgetPopupMenuListener {
	
	/**
	 * Called when the user wants to cut the selected widget.
	 * 
	 * @param sender the selected widget.
	 */
	void onCut(Widget sender);
	
	/**
	 * Called when the user wants to copy the selected widget.
	 * 
	 * @param sender the selected widget.
	 */
	void onCopy(Widget sender);
	
	/**
	 * Called when the user wants to delete the selected widget.
	 * 
	 * @param sender the selected widget.
	 */
	void onDelete(Widget sender);
}
