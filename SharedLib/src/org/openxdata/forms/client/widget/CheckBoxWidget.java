package org.openxdata.forms.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;



/**
 * This class is only to enable us have check boxes that can be locked
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class CheckBoxWidget extends CheckBox{
	
	/**
	 * Creates a new instance of the check box widget.
	 * 
	 * @param label the text or label for the check box.
	 */
	public CheckBoxWidget(String label){
		super(label);
		sinkEvents(Event.getTypeInt(ClickEvent.getType().getName()));
	}

	
	@Override
	public void onBrowserEvent(Event event){
		if(DOM.eventGetType(event) == Event.ONCLICK){
			if((getParent().getParent() instanceof RuntimeWidgetWrapper &&
					((RuntimeWidgetWrapper)getParent().getParent()).isLocked()) ||
					!(getParent().getParent() instanceof RuntimeWidgetWrapper)){
				
				event.preventDefault();
				event.stopPropagation();
				return;
			}
		}
		
		if(DOM.eventGetType(event) == Event.ONMOUSEUP || DOM.eventGetType(event) == Event.ONMOUSEDOWN){
			if(!(getParent().getParent() instanceof RuntimeWidgetWrapper)){
				event.preventDefault();
				event.stopPropagation();
				return;
			}
		}
		
		super.onBrowserEvent(event);
	}
}
