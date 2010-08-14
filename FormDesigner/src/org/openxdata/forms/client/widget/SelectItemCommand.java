package org.openxdata.forms.client.widget;

import org.openxdata.forms.client.controller.ItemSelectionListener;

import com.google.gwt.user.client.Command;


/**
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class SelectItemCommand implements Command{

	private Object item;
	private ItemSelectionListener itemSelectionListener;
	
	public SelectItemCommand(Object item,ItemSelectionListener itemSelectionListener){
		this.item = item;
		this.itemSelectionListener = itemSelectionListener;
	}
	
	/**
	 * @see com.google.gwt.user.client.Command#execute()
	 */
	public void execute() {
		itemSelectionListener.onItemSelected(this, item);
	}

	
}
