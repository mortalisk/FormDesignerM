package org.openxdata.designer.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Represents a select event on an Xform item
 * 
 */
public class XformItemSelectEvent extends GwtEvent<XformItemSelectHandler> {

	public final static Type<XformItemSelectHandler> TYPE = new Type<XformItemSelectHandler>();
	
	public enum XformItemType {
		FORM, PAGE, QUESTION;
	}
	
	private final XformItemType createType;
	
	public XformItemSelectEvent(XformItemType createType) {
		this.createType = createType;
	}
	
	public XformItemType getXformItemType() {
		return createType;
	}
	
	@Override
	protected void dispatch(XformItemSelectHandler handler) {
		handler.onXformItemSelected(this);
	}

	@Override
	public Type<XformItemSelectHandler> getAssociatedType() {
		return TYPE;
	}
}
