package org.openxdata.designer.client.event;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/* This is a simple registry that provides access to the shared event bus 
 * for the form designer. Our hope is that the event bus will ultimately be 
 * injected, and this class will only be a shim to support early refactoring.
 * 
 * Because this class should be temporary, and I can't foresee a need to inherit 
 * from it or mock the event bus, it is not a full-blown singleton.
 */

public class FormDesignerEventBus {
	
	private static EventBus eventBus = new SimpleEventBus();
	
	public static EventBus getBus(){
		return eventBus;
	}
}
