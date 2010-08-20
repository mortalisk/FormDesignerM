package org.openxdata.formtools.client.widget;

import com.google.gwt.user.client.ui.Anchor;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class AddConditionHyperlink extends Anchor {
	
	private int depth = 1;
	
	public AddConditionHyperlink(String text, String target, int depth){
		super(text, target);
		this.depth = depth;
	}
	
	public int getDepth(){
		return depth;
	}
}
