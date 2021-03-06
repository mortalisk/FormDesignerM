package org.openxdata.designer.client.widget.skiprule;

import com.google.gwt.core.client.GWT;
import org.openxdata.designer.client.controller.ItemSelectionListener;
import org.openxdata.designer.client.widget.SelectItemCommand;
import org.openxdata.sharedlib.client.OpenXdataConstants;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.openxdata.sharedlib.client.locale.FormsConstants;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * This widget is used to display a list of function which can be used in a
 * validation rule conditions question value. eg Length
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FunctionHyperlink extends Hyperlink implements ItemSelectionListener {
    
        final static FormsConstants i18n = GWT.create(FormsConstants.class);
	
	/** The length function text: Length */
	public static final String FUNCTION_TEXT_LENGTH = i18n.length();
	
	/** The value function text: Length */
	public static final String FUNCTION_TEXT_VALUE = i18n.value();
	
	/** The popup to display functions. */
	private PopupPanel popup;
	
	/** The data type of the currently selected question. */
	private QuestionType dataType =  QuestionType.TEXT;
	
	/** The listener to item selection events. */
	private ItemSelectionListener itemSelectionListener;
	
	
	/**
	 * Creates a new instance of the question value widget.
	 * 
	 * @param text the display text.
	 * @param targetHistoryToken the history token to which it will link.
	 * @param itemSelectionListener the listener to item selection events.
	 */
	public FunctionHyperlink(String text, String targetHistoryToken,ItemSelectionListener itemSelectionListener){
		super(text,targetHistoryToken);
		this.itemSelectionListener = itemSelectionListener;
		DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.ONMOUSEDOWN );
	}
	
	/** 
	 * Sets the data type for the currently selected question.
	 * 
	 * @param dataType the data type.
	 */
	public void setDataType(int dataType){
		this.dataType = QuestionType.fromLegacyConstant(dataType);
		
		setText((this.dataType == QuestionType.REPEAT) ? FUNCTION_TEXT_LENGTH : FUNCTION_TEXT_VALUE);
	}
	  
	@Override
	public void onBrowserEvent(Event event) {
		  if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			  itemSelectionListener.onStartItemSelection(this);
			  setupPopup();
		      popup.setPopupPosition(event.getClientX(), event.getClientY() - 65);
		      popup.show();
		  }
	}
	
	/**
	 * Creates the popup of the question value functions.
	 */
	private void setupPopup(){
		popup = new PopupPanel(true,true);
		
		int count = 0;
		
		MenuBar menuBar = new MenuBar(true);
		
		if(dataType != QuestionType.REPEAT){
			menuBar.addItem(FUNCTION_TEXT_VALUE,true, new SelectItemCommand(FUNCTION_TEXT_VALUE,this));
			count += 1;
		}
		
		if(dataType == QuestionType.TEXT || dataType == QuestionType.REPEAT){
			menuBar.addItem(FUNCTION_TEXT_LENGTH,true, new SelectItemCommand(FUNCTION_TEXT_LENGTH,this));
			count += 1;
		}
		 
		int height = count*30;
		if(height > 200)
			height = 200;
		
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidget(menuBar);
		scrollPanel.setWidth("70"+OpenXdataConstants.UNITS);
		scrollPanel.setHeight(height+OpenXdataConstants.UNITS);
		//scrollPanel.setHeight((Window.getClientHeight() - getAbsoluteTop() - 25)+OpenXdataConstants.UNITS);
		
		popup.setWidget(scrollPanel);
	}
	
	/**
	 * Converts the function text to its int value.
	 * 
	 * @param text the function text.
	 * @return the function int value.
	 */
	private int fromFunctionText2Value(String text){
		if(text.equals(FUNCTION_TEXT_LENGTH))
			return ModelConstants.FUNCTION_LENGTH;
		return ModelConstants.FUNCTION_VALUE;
	}
	
	/**
	 * @see org.openxdata.designer.client.controller.ItemSelectionListener#onItemSelected(Object, Object)
	 */
	public void onItemSelected(Object sender, Object item) {
		if(sender instanceof SelectItemCommand){
			popup.hide();
			setText((String)item);
			itemSelectionListener.onItemSelected(this, fromFunctionText2Value((String)item));
		}
	}
	
	/**
	 * @see org.openxdata.designer.client.controller.ItemSelectionListener#onStartItemSelection(Object)
	 */
	public void onStartItemSelection(Object sender){
		
	}
	
	/**
	 * Sets the selected function.
	 * 
	 * @param function the selected function. Can only be ModelConstants.FUNCTION_LENGTH
	 * 		  or ModelConstants.FUNCTION_VALUE
	 */
	public void setFunction(int function){
		String functionText = FUNCTION_TEXT_VALUE;
		
		if(function == ModelConstants.FUNCTION_LENGTH)
			functionText = FUNCTION_TEXT_LENGTH;
		
		setText(functionText);
	}

}
