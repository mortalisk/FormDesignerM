package org.openxdata.querybuilder.client.widget;


import com.google.gwt.core.client.GWT;
import org.openxdata.querybuilder.client.controller.FilterRowActionListener;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import org.openxdata.sharedlib.client.locale.FormsConstants;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class ConditionActionHyperlink extends AddConditionHyperlink {
    final FormsConstants i18n = GWT.create(FormsConstants.class);

	private PopupPanel popup;
	private FilterRowActionListener actionListener;
	private boolean allowDelete = true;
	private AddConditionHyperlink addConditionHyperlink;

	public ConditionActionHyperlink(String text, String targetHistoryToken, boolean allowDelete, int depth, AddConditionHyperlink addConditionHyperlink,
			FilterRowActionListener actionListener){
		super(text, "#", depth);
		this.allowDelete = allowDelete;
		this.actionListener = actionListener;
		this.addConditionHyperlink = addConditionHyperlink;
		DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.ONMOUSEDOWN );
	}

	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			setupPopup();
			popup.setPopupPosition(event.getClientX(), event.getClientY());
			popup.show();
		}
	}

	private void setupPopup(){
		popup = new PopupPanel(true,true);

		MenuBar menuBar = new MenuBar(true);

		final Widget w = this;
		if(allowDelete){
			menuBar.addItem(i18n.deleteCondition(),true, new Command(){
				public void execute() {popup.hide(); actionListener.deleteCurrentRow(w);}});
		}

		menuBar.addItem(i18n.addCondition(),true, new Command(){
			public void execute() {popup.hide(); actionListener.addCondition(w);}});

		menuBar.addItem("Add Bracket",true, new Command(){ //LocaleText.get("???")
			public void execute() {popup.hide(); actionListener.addBracket(w,null,true);}});

		popup.setWidget(menuBar);
	}

	public void setAllowDelete(boolean allowDelete){
		this.allowDelete = allowDelete;
	}
	
	public AddConditionHyperlink getAddConditionHyperlink(){
		return addConditionHyperlink;
	}
}
