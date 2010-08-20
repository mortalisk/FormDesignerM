package org.openxdata.formtools.client.widget;


import org.openxdata.formtools.client.controller.FilterRowActionListener;
import org.openxdata.formtools.client.locale.LocaleText;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class ConditionActionHyperlink extends AddConditionHyperlink {

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
			menuBar.addItem(LocaleText.get("deleteCondition"),true, new Command(){
				public void execute() {popup.hide(); actionListener.deleteCurrentRow(w);}});
		}

		menuBar.addItem(LocaleText.get("addCondition"),true, new Command(){
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
