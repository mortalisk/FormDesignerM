package org.openxdata.designer.client.widget;

import org.openxdata.designer.client.controller.IFormActionListener;
import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.sharedlib.client.util.FormUtil;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Widget for tree items which gives them a context menu.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class TreeItemWidget extends Composite{

	/** Popup panel for the context menu. */
	private PopupPanel popup;

	/** Listener for form action events. */
	private IFormActionListener formActionListener;


	/**
	 * Creates a new tree item.
	 * 
	 * @param imageProto the item image.
	 * @param caption the time caption or text.
	 * @param popup the pop up panel for context menu.
	 * @param formActionListener listener to form action events.
	 */
	public TreeItemWidget(ImageResource imageProto, String caption, PopupPanel popup,IFormActionListener formActionListener){

		this.popup = popup;
		this.formActionListener = formActionListener;

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(0);

		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hPanel.add(FormUtil.createImage(imageProto));
		//HTML headerText = new HTML(caption); //Replaced with Label due to bug which surfaces when we have text including things like "<sdsff>"
		Widget headerText = new Label(caption);
		hPanel.add(headerText);
		hPanel.setStyleName("gwt-noWrap");
		initWidget(hPanel);

		DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.ONMOUSEDOWN | Event.ONKEYDOWN );
	}

	@Override
	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			if( (event.getButton() & Event.BUTTON_RIGHT) != 0 /*&& !Context.isStructureReadOnly()*/){	  
				
				int ypos = event.getClientY();
				if(Window.getClientHeight() - ypos < 350)
					ypos = event.getClientY() - 350;
					
				FormDesignerUtil.disableContextMenu(popup.getElement());
				popup.setPopupPosition(event.getClientX(), ypos);
				popup.show();
			}
		}
		else if(DOM.eventGetType(event) == Event.ONKEYDOWN){
			if(event.getKeyCode() == KeyCodes.KEY_DELETE)
				formActionListener.deleteSelectedItem();
		}
	}
}
