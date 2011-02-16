package org.openxdata.querybuilder.client.widget;

import com.google.gwt.core.client.GWT;
import org.openxdata.sharedlib.client.model.ModelConstants;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import org.openxdata.sharedlib.client.locale.FormsConstants;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class GroupHyperlink extends Anchor {
    static final FormsConstants i18n = GWT.create(FormsConstants.class);

	public static final String CONDITIONS_OPERATOR_TEXT_ALL = i18n.all();
	public static final String CONDITIONS_OPERATOR_TEXT_ANY = i18n.any();
	public static final String CONDITIONS_OPERATOR_TEXT_NONE = i18n.none();
	public static final String CONDITIONS_OPERATOR_TEXT_NOT_ALL = i18n.notAll();

	private PopupPanel popup;
	private boolean enabled = true;
	private int depth = 1;

	public GroupHyperlink(String text, int depth){
		super(text, "#");
		this.depth = depth;

		DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.ONMOUSEDOWN );
	}

	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN && enabled) {
			setupPopup();
			popup.setPopupPosition(event.getClientX(), event.getClientY());
			popup.show();
		}
	}

	private void setupPopup(){
		popup = new PopupPanel(true,true);

		MenuBar menuBar = new MenuBar(true);
		menuBar.addItem(CONDITIONS_OPERATOR_TEXT_ALL,true, new Command(){
			public void execute() {popup.hide(); setText(CONDITIONS_OPERATOR_TEXT_ALL);}});

		menuBar.addSeparator();		  
		menuBar.addItem(CONDITIONS_OPERATOR_TEXT_ANY,true, new Command(){
			public void execute() {popup.hide(); setText(CONDITIONS_OPERATOR_TEXT_ANY);}});

		menuBar.addSeparator();		  
		menuBar.addItem(CONDITIONS_OPERATOR_TEXT_NONE,true,new Command(){
			public void execute() {popup.hide(); setText(CONDITIONS_OPERATOR_TEXT_NONE);}});

		menuBar.addSeparator();		  
		menuBar.addItem(CONDITIONS_OPERATOR_TEXT_NOT_ALL,true, new Command(){
			public void execute() {popup.hide(); setText(CONDITIONS_OPERATOR_TEXT_NOT_ALL);}});

		popup.setWidget(menuBar);
	}

	public String getConditionsOperator(){
		return getText();
	}

	public void setCondionsOperator(int operator){
		if(operator == ModelConstants.CONDITIONS_OPERATOR_AND)
			setText(CONDITIONS_OPERATOR_TEXT_ALL);
		else if(operator == ModelConstants.CONDITIONS_OPERATOR_OR)
			setText(CONDITIONS_OPERATOR_TEXT_ANY);	
	}

	public void setEnabled(boolean enable){
		this.enabled = enable;
	}
	
	public int getDepth(){
		return depth;
	}
}
