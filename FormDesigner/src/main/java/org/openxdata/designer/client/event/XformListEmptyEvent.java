package org.openxdata.designer.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents an event that is fired when the Form Designer
 * no longer has any Xform definitions in its queue to display
 */
public class XformListEmptyEvent extends GwtEvent<XformListEmptyHandler> {

	public static final Type<XformListEmptyHandler> TYPE = new Type<XformListEmptyHandler>();
	
	@Override
	protected void dispatch(XformListEmptyHandler handler) {
		handler.onListEmpty(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<XformListEmptyHandler> getAssociatedType() {
		return TYPE;
	}
}
