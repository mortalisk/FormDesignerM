package org.openxdata.formtools.client.controller;


/**
 * This interface is implemented by those interested in listening to changes
 * in the form widget layout.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface LayoutChangeListener {
	
	/**
	 * Called when the form widget layout changes.
	 * 
	 * @param xml the new widget layout xml.
	 */
	void onLayoutChanged(String xml);
}
