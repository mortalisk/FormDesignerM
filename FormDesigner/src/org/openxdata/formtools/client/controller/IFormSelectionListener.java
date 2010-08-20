package org.openxdata.formtools.client.controller;


/**
 * Interface for listening to form item (form,page,question or question option) 
 * selection events during form design.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface IFormSelectionListener {

	/**
	 * Called when a form item (form,page,question or question option) is selected.
	 * 
	 * @param formItem the item which has been selected.
	 */
	public void onFormItemSelected(Object formItem);
}
