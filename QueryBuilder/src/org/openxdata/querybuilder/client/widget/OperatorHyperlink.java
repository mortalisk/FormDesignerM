package org.openxdata.querybuilder.client.widget;

import com.google.gwt.core.client.GWT;
import org.openxdata.querybuilder.client.controller.ItemSelectionListener;
import org.openxdata.sharedlib.client.OpenXdataConstants;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.openxdata.querybuilder.client.Translations;
import org.openxdata.sharedlib.client.model.Operator;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class OperatorHyperlink extends Hyperlink implements ItemSelectionListener {
    final static Translations i18n = GWT.create(Translations.class);

	public static final String OP_TEXT_EQUAL = i18n.isEqualTo();
	public static final String OP_TEXT_NOT_EQUAL = i18n.isNotEqual();
	public static final String OP_TEXT_LESS_THAN = i18n.isLessThan();
	public static final String OP_TEXT_LESS_THAN_EQUAL = i18n.isLessThanOrEqual();
	public static final String OP_TEXT_GREATER_THAN = i18n.isGreaterThan();
	public static final String OP_TEXT_GREATER_THAN_EQUAL = i18n.isGreaterThanOrEqual();
	public static final String OP_TEXT_NULL = i18n.isNull();
	public static final String OP_TEXT_IN_LIST = i18n.isInList();
	public static final String OP_TEXT_NOT_IN_LIST = i18n.isNotInList();
	public static final String OP_TEXT_STARTS_WITH = i18n.startsWith();
	public static final String OP_TEXT_NOT_START_WITH = i18n.doesNotStartWith();
	public static final String OP_TEXT_CONTAINS = i18n.contains();
	public static final String OP_TEXT_NOT_CONTAIN = i18n.doesNotContain();
	public static final String OP_TEXT_BETWEEN = i18n.isBetween();
	public static final String OP_TEXT_NOT_BETWEEN = i18n.isNotBetween();
	public static final String OP_TEXT_NOT_NULL = i18n.isNotNull();

	private PopupPanel popup;
	private int dataType =  QuestionDef.QTN_TYPE_TEXT;
	private ItemSelectionListener itemSelectionListener;

	public OperatorHyperlink(String text, String targetHistoryToken,ItemSelectionListener itemSelectionListener){
		super(text,targetHistoryToken);
		this.itemSelectionListener = itemSelectionListener;
		DOM.sinkEvents(getElement(), DOM.getEventsSunk(getElement()) | Event.ONMOUSEDOWN );
	}

	public void setDataType(QuestionType dataType){
		this.dataType = dataType.getLegacyConstant();

		//We set the universal operator which is valid for all questions.
		setText(OP_TEXT_EQUAL);
	}

	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			itemSelectionListener.onStartItemSelection(this);
			setupPopup();
			popup.setPopupPosition(event.getClientX(), event.getClientY());
			popup.show();
		}
	}

	/*public void startSelection(){
		  setupPopup();
	      popup.setPopupPosition(this.getAbsoluteLeft(), this.getAbsoluteTop());
	      popup.show();
	}*/

	private void setupPopup(){
		popup = new PopupPanel(true,true);

		int count = 0;

		MenuBar menuBar = new MenuBar(true);

		if(!(dataType == QuestionDef.QTN_TYPE_GPS || dataType == QuestionDef.QTN_TYPE_VIDEO ||
				dataType == QuestionDef.QTN_TYPE_AUDIO || dataType == QuestionDef.QTN_TYPE_IMAGE)){
			menuBar.addItem(OP_TEXT_EQUAL,true, new SelectItemCommand(OP_TEXT_EQUAL,this));
			menuBar.addItem(OP_TEXT_NOT_EQUAL,true, new SelectItemCommand(OP_TEXT_NOT_EQUAL,this));
			count += 2;
		}

		if(dataType == QuestionDef.QTN_TYPE_DATE || dataType == QuestionDef.QTN_TYPE_DATE_TIME ||
				dataType == QuestionDef.QTN_TYPE_DECIMAL || dataType == QuestionDef.QTN_TYPE_NUMERIC ||
				dataType == QuestionDef.QTN_TYPE_TIME || dataType == QuestionDef.QTN_TYPE_REPEAT){

			menuBar.addItem(OP_TEXT_GREATER_THAN,true,new SelectItemCommand(OP_TEXT_GREATER_THAN,this));	  
			menuBar.addItem(OP_TEXT_GREATER_THAN_EQUAL,true, new SelectItemCommand(OP_TEXT_GREATER_THAN_EQUAL,this));	  
			menuBar.addItem(OP_TEXT_LESS_THAN,true,new SelectItemCommand(OP_TEXT_LESS_THAN,this));			  	  
			menuBar.addItem(OP_TEXT_LESS_THAN_EQUAL,true, new SelectItemCommand(OP_TEXT_LESS_THAN_EQUAL,this));		  
			menuBar.addItem(OP_TEXT_BETWEEN,true,new SelectItemCommand(OP_TEXT_BETWEEN,this));	  
			menuBar.addItem(OP_TEXT_NOT_BETWEEN,true, new SelectItemCommand(OP_TEXT_NOT_BETWEEN,this));
			count += 6;
		}

		if(dataType == QuestionDef.QTN_TYPE_LIST_EXCLUSIVE || dataType == QuestionDef.QTN_TYPE_LIST_MULTIPLE){		  
			menuBar.addItem(OP_TEXT_IN_LIST,true,new SelectItemCommand(OP_TEXT_IN_LIST,this));	  
			menuBar.addItem(OP_TEXT_NOT_IN_LIST,true, new SelectItemCommand(OP_TEXT_NOT_IN_LIST,this));
			count += 2;
		}

		menuBar.addItem(OP_TEXT_NULL,true, new SelectItemCommand(OP_TEXT_NULL,this));
		menuBar.addItem(OP_TEXT_NOT_NULL,true, new SelectItemCommand(OP_TEXT_NOT_NULL,this));
		count += 2;

		if(dataType == QuestionDef.QTN_TYPE_TEXT ){	  
			menuBar.addItem(OP_TEXT_STARTS_WITH,true,new SelectItemCommand(OP_TEXT_STARTS_WITH,this));	  
			menuBar.addItem(OP_TEXT_NOT_START_WITH,true, new SelectItemCommand(OP_TEXT_NOT_START_WITH,this));	  
			menuBar.addItem(OP_TEXT_CONTAINS,true,new SelectItemCommand(OP_TEXT_CONTAINS,this));	  
			menuBar.addItem(OP_TEXT_NOT_CONTAIN,true, new SelectItemCommand(OP_TEXT_NOT_CONTAIN,this));
			count += 4;
		}

		int height = count*40;
		if(height > 200)
			height = 200;

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setWidget(menuBar);
		scrollPanel.setWidth("300"+OpenXdataConstants.UNITS);
		scrollPanel.setHeight(height+OpenXdataConstants.UNITS);
		//scrollPanel.setHeight((Window.getClientHeight() - getAbsoluteTop() - 25)+OpenXdataConstants.UNITS);

		popup.setWidget(scrollPanel);
	}

	public void onItemSelected(Object sender, Object item) {
		if(sender instanceof SelectItemCommand){
			popup.hide();
			setText((String)item);
			itemSelectionListener.onItemSelected(this, fromOperatorText2Value((String)item));
		}
	}

	private Operator fromOperatorText2Value(String text){
		if(text.equals(OP_TEXT_EQUAL))
			return Operator.EQUAL;
		else if(text.equals(OP_TEXT_NOT_EQUAL))
			return Operator.NOT_EQUAL;
		else if(text.equals(OP_TEXT_LESS_THAN))
			return Operator.LESS;
		else if(text.equals(OP_TEXT_LESS_THAN_EQUAL))
			return Operator.LESS_EQUAL;
		else if(text.equals(OP_TEXT_GREATER_THAN))
			return Operator.GREATER;
		else if(text.equals(OP_TEXT_GREATER_THAN_EQUAL))
			return Operator.GREATER_EQUAL;
		else if(text.equals(OP_TEXT_NULL))
			return Operator.IS_NULL;
		else if(text.equals(OP_TEXT_IN_LIST))
			return Operator.IN_LIST;
		else if(text.equals(OP_TEXT_NOT_IN_LIST))
			return Operator.NOT_IN_LIST;
		else if(text.equals(OP_TEXT_STARTS_WITH))
			return Operator.STARTS_WITH;
		else if(text.equals(OP_TEXT_NOT_START_WITH))
			return Operator.NOT_START_WITH;
		else if(text.equals(OP_TEXT_CONTAINS))
			return Operator.CONTAINS;
		else if(text.equals(OP_TEXT_NOT_CONTAIN))
			return Operator.NOT_CONTAIN;
		else if(text.equals(OP_TEXT_BETWEEN))
			return Operator.BETWEEN;
		else if(text.equals(OP_TEXT_NOT_BETWEEN))
			return Operator.NOT_BETWEEN;
		else if(text.equals(OP_TEXT_NOT_NULL))
			return Operator.IS_NOT_NULL;

		return Operator.NONE;
	}

	public void onStartItemSelection(Object sender){

	}

	public void setOperator(Operator operator){
		String operatorText = null;

		if(operator == Operator.EQUAL)
			operatorText = OP_TEXT_EQUAL;
		else if(operator == Operator.NOT_EQUAL)
			operatorText = OP_TEXT_NOT_EQUAL;
		else if(operator == Operator.LESS)
			operatorText = OP_TEXT_LESS_THAN;
		else if(operator == Operator.LESS_EQUAL)
			operatorText = OP_TEXT_LESS_THAN_EQUAL;
		else if(operator == Operator.GREATER)
			operatorText = OP_TEXT_GREATER_THAN;
		else if(operator == Operator.GREATER_EQUAL)
			operatorText = OP_TEXT_GREATER_THAN_EQUAL;
		else if(operator == Operator.IS_NULL)
			operatorText = OP_TEXT_NULL;
		else if(operator == Operator.IN_LIST)
			operatorText = OP_TEXT_IN_LIST;
		else if(operator == Operator.NOT_IN_LIST)
			operatorText = OP_TEXT_NOT_IN_LIST;
		else if(operator == Operator.STARTS_WITH)
			operatorText = OP_TEXT_STARTS_WITH;
		else if(operator == Operator.NOT_START_WITH)
			operatorText = OP_TEXT_NOT_START_WITH;
		else if(operator == Operator.CONTAINS)
			operatorText = OP_TEXT_CONTAINS;
		else if(operator == Operator.NOT_CONTAIN)
			operatorText = OP_TEXT_NOT_CONTAIN;
		else if(operator == Operator.BETWEEN)
			operatorText = OP_TEXT_BETWEEN;
		else if(operator == Operator.NOT_BETWEEN)
			operatorText = OP_TEXT_NOT_BETWEEN;
		else if(operator == Operator.IS_NOT_NULL)
			operatorText = OP_TEXT_NOT_NULL;

		setText(operatorText);
	}
}
