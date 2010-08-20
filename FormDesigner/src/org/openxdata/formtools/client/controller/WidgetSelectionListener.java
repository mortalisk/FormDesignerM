package org.openxdata.formtools.client.controller;

import com.google.gwt.user.client.ui.Widget;


/**
 * This interface is implemented by those who want to listening to changes in
 * widget selection.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface WidgetSelectionListener {
	
	/**
	 * Called when a widgets is selected.
	 * 
	 * @param widget the selected widget.
	 * @param multipleSel has a value of true if we are doing multiple selection, else false.
	 */
	public void onWidgetSelected(Widget widget, boolean multipleSel);
}
