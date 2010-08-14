package org.openxdata.forms.client.controller;


/**
 * Interface for listening to item selection events.
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface ItemSelectionListener {

	/**
	 * Called when an item has been selected.
	 * 
	 * @param sender the widget sending this event.
	 * @param item the item being selected.
	 */
	public void onItemSelected(Object sender, Object item);
	
	/**
	 * Called when an item is about to be selected.
	 * 
	 * @param sender the widget sending this event.
	 */
	public void onStartItemSelection(Object sender);
}
