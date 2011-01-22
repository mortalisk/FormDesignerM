package org.openxdata.designer.client.event;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

/* This is a simple registry that provides access to the shared event bus 
 * for the form designer. Our hope is that the event bus will ultimately be 
 * injected, and this class will only be a shim to support early refactoring.
 */

public class FormDesignerEventBus {
	
	private static FormDesignerEventBus instance = null;
	private EventBus eventBus;
	
	private FormDesignerEventBus(){
		this.eventBus = new SimpleEventBus();
	}
	
	public EventBus getEventBus(){
		return eventBus;
	}
	
	public static FormDesignerEventBus getInstance(){
		if (instance == null)
			instance = new FormDesignerEventBus();
		
		return instance;
	}
	
	public static EventBus getBus(){
		return getInstance().getEventBus();
	}
}
