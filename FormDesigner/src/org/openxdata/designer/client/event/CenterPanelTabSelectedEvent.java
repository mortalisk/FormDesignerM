package org.openxdata.designer.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class CenterPanelTabSelectedEvent extends GwtEvent<CenterPanelTabSelectedHandler> {
	
	private static Type<CenterPanelTabSelectedHandler> TYPE;
	
	private int tabIndex;
	
	public CenterPanelTabSelectedEvent(int tabIndex){
		this.tabIndex = tabIndex;
	}
	
	@Override
	protected void dispatch(CenterPanelTabSelectedHandler handler) {
		handler.doCenterPanelTabSelected(this);
	}

	@Override
	public Type<CenterPanelTabSelectedHandler> getAssociatedType() {
		if (TYPE == null)
			TYPE = new Type<CenterPanelTabSelectedHandler>();
		
		return TYPE;
	}
	
	public int getSelectedIndex(){
		return tabIndex;
	}
	
	public static Type<CenterPanelTabSelectedHandler> getType(){
		if (TYPE == null)
			TYPE = new Type<CenterPanelTabSelectedHandler>();
		
		return TYPE;
	}

}
