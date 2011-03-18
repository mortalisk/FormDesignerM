package org.openxdata.designer.client.widget;

import com.google.gwt.core.client.GWT;
import java.util.List;

import org.openxdata.designer.client.Context;
import org.openxdata.designer.client.controller.FormDesignerDragController;
import org.openxdata.designer.client.controller.WidgetSelectionListener;
import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.designer.client.vew.widget.images.FormDesignerImages;
import org.openxdata.sharedlib.client.controller.QuestionChangeListener;
import org.openxdata.sharedlib.client.model.OptionDef;
import org.openxdata.sharedlib.client.model.QuestionDef;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.widget.DatePickerEx;
import org.openxdata.sharedlib.client.widget.DatePickerWidget;
import org.openxdata.sharedlib.client.widget.DateTimeWidget;
import org.openxdata.sharedlib.client.widget.TimeWidget;
import org.openxdata.sharedlib.client.widget.WidgetEx;
import org.openxdata.sharedlib.client.xforms.XformConstants;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import org.openxdata.designer.client.DesignerMessages;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * 
 * Wraps a widget and gives it capability to be designed.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class DesignWidgetWrapper extends WidgetEx implements QuestionChangeListener, HasAllMouseHandlers{
    final DesignerMessages designerMessages = GWT.create(DesignerMessages.class);

	private WidgetSelectionListener widgetSelectionListener;
	private PopupPanel popup;
	private Element layoutNode;


	public DesignWidgetWrapper(DesignWidgetWrapper designWidgetWrapper, FormDesignerImages images){
		super(designWidgetWrapper);

		setText(designWidgetWrapper.getText());

		this.widgetSelectionListener = designWidgetWrapper.widgetSelectionListener;
		this.popup = designWidgetWrapper.popup;

		this.questionDef = designWidgetWrapper.questionDef;

		initWidget();
	}

	protected void copyWidget(WidgetEx widget){
		if(widget.getWrappedWidget() instanceof DesignGroupWidget)
			this.widget = new DesignGroupWidget((DesignGroupWidget)widget.getWrappedWidget(),
					((DesignGroupWidget)widget.getWrappedWidget()).getImages(),((DesignGroupWidget)widget.getWrappedWidget()).getWidgetPopupMenuListener());
		super.copyWidget(widget);
	}

	private void initWidget(){
		if(!(widget instanceof TabBar)){
			panel.add(widget);
			initWidget(panel);
			DOM.sinkEvents(getElement(),DOM.getEventsSunk(getElement()) | Event.MOUSEEVENTS | Event.ONCONTEXTMENU | Event.KEYEVENTS | Event.ONCHANGE);
		}
	}

	public DesignWidgetWrapper(Widget widget,PopupPanel popup,WidgetSelectionListener widgetSelectionListener) {
		super();

		this.widget = widget;
		this.popup = popup;
		this.widgetSelectionListener = widgetSelectionListener;
		if(widgetSelectionListener == null)
			widget.getElement();

		initWidget();
	}

	public void onBrowserEvent(Event event) {
		int type = DOM.eventGetType(event);
		if((widget instanceof Label && panel.getWidget(0) instanceof TextBox))
			return; //Must be in label edit mode.

		switch (type) {
		case Event.ONCONTEXTMENU:
			if(popup != null){
				if(!event.getCtrlKey())
					widgetSelectionListener.onWidgetSelected(null,true); //clear current selection. Multiple sel is given a value of true because right click does not turn off other selections

				int ypos = event.getClientY();
				if(Window.getClientHeight() - ypos < 150)
					ypos = event.getClientY() - 150;

				int xpos = event.getClientX();
				if(Window.getClientWidth() - xpos < 170)
					xpos = event.getClientX() - 170;

				widgetSelectionListener.onWidgetSelected(this,true);
				popup.setPopupPosition(xpos, ypos);
				popup.show();

				if(widget instanceof TextBox)
					((TextBox)widget).setFocus(false);
				else if(widget instanceof DateTimeWidget)
					((DateTimeWidget)widget).setFocus(false);
			}
			break;
		case Event.ONMOUSEDOWN:
			if(!(widget instanceof DesignGroupWidget) && !event.getCtrlKey())
				widgetSelectionListener.onWidgetSelected(this,event.getCtrlKey());
			//The above is turned on for now because of selectedDragController.setBehaviorDragStartSensitivity(1);

			//When the header label of a groupbox is selected, all widgets within should be deselected.
			if(widget instanceof Label && "100%".equals(width) && getParent().getParent() instanceof DesignGroupWidget){
				DesignGroupWidget designGroupWidget = (DesignGroupWidget)getParent().getParent();
				if(designGroupWidget.getHeaderLabel() == this)
					designGroupWidget.clearGroupBoxSelection();
			}
			else if(widget instanceof DesignGroupWidget){
				//When a group box is clicked anywhere outside a widget, deselect it to turn off the selection graying out
				//but reselect it such that its properties can be shown in the properties pane.
				widgetSelectionListener.onWidgetSelected(null,false);
				widgetSelectionListener.onWidgetSelected(this,false);
			}

		case Event.ONMOUSEUP:
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEMOVE:
		case Event.ONMOUSEOUT:

			if(widget instanceof DesignGroupWidget){
				if(isRepeated() || !"default".equals(DOM.getStyleAttribute(widget.getElement(), "cursor")))
					super.onBrowserEvent(event);
			}
			else
				super.onBrowserEvent(event);

			if(widget instanceof RadioButton)
				((RadioButton)widget).setValue(false);
			if(widget instanceof CheckBox)
				((CheckBox)widget).setValue(false);

			if(!(widget instanceof CheckBox || widget instanceof RadioButton)){

				String cursorval = getDesignCursor(event.getClientX(),event.getClientY(),3);
				if(Context.getLockWidgets())
					cursorval = "pointer";

				DOM.setStyleAttribute(widget.getElement(), "cursor", cursorval);

				if(widget instanceof DateTimeWidget)
					((DateTimeWidget)widget).setStyle("cursor", cursorval);
			}

			break;
		}

		FormDesignerUtil.disableContextMenu(widget.getElement());

		//TODO Check to ensure this does not bring bugs. It has been put to allow
		//Rubber band mouse drags to move out of design surface through group widgets.
		if(!(widget instanceof DesignGroupWidget && type == Event.ONMOUSEMOVE)){
			if(type != Event.ONMOUSEMOVE) //This lets firefox display tooltips on widget design surface
				DOM.eventCancelBubble(event, true); //Without this, rubber band will draw
		}
	}

	public void startEditMode(TextBox txtEdit){
		if(hasLabelEdidting()){
			if(!(widget instanceof TabBar)){
				storePosition();
				panel.remove(0);
				panel.add(txtEdit);

				setLabelEditStyle(txtEdit);
			}

			String text = null;
			if(widget instanceof Label)
				text = ((Label)widget).getText();
			else if(widget instanceof Hyperlink)
				text = ((Hyperlink)widget).getText();
			else if(widget instanceof RadioButton)
				text = ((RadioButton)widget).getText();
			else if(widget instanceof CheckBox)
				text = ((CheckBox)widget).getText();
			else if(widget instanceof Button)
				text = ((Button)widget).getText();
			else
				text = getText();
			

			txtEdit.setText(text);
			txtEdit.selectAll();
			txtEdit.setFocus(true);
		}
	}

	private void setLabelEditStyle(TextBox txtEdit){
		String value = "";
		if(fontFamily != null && fontFamily.trim().length() > 0)
			value = fontFamily;
		DOM.setStyleAttribute(txtEdit.getElement(), "fontFamily", value);

		value = "";
		if(fontSize != null && fontSize.trim().length() > 0)
			value = fontSize;
		DOM.setStyleAttribute(txtEdit.getElement(), "fontSize", value);

		value = "";
		if(textDecoration != null && textDecoration.trim().length() > 0)
			value = textDecoration;
		DOM.setStyleAttribute(txtEdit.getElement(), "textDecoration", value);

		value = "";
		if(textAlign != null && textAlign.trim().length() > 0)
			value = textAlign;
		DOM.setStyleAttribute(txtEdit.getElement(), "textAlign", value);

		value = "";
		if(color != null && color.trim().length() > 0)
			value = color;
		DOM.setStyleAttribute(txtEdit.getElement(), "color", value);

		value = "";
		if(fontWeight != null && fontWeight.trim().length() > 0)
			value = fontWeight;
		DOM.setStyleAttribute(txtEdit.getElement(), "fontWeight", value);

		value = "";
		if(fontStyle != null && fontStyle.trim().length() > 0)
			value = fontStyle;
		DOM.setStyleAttribute(txtEdit.getElement(), "fontStyle", value);

		value = "";
		if(backgroundColor != null && backgroundColor.trim().length() > 0)
			value = backgroundColor;
		DOM.setStyleAttribute(txtEdit.getElement(), "backgroundColor", value);

		if("100%".equals(width))
			DOM.setStyleAttribute(txtEdit.getElement(), "width", width);
	}

	public boolean hasLabelEdidting(){
		return (widget instanceof Label || widget instanceof Hyperlink || widget instanceof Button ||
				widget instanceof CheckBox || widget instanceof RadioButton);
	}

	public boolean isResizable(){
		return !(widget instanceof Label || widget instanceof Hyperlink || widget instanceof CheckBox);
	}

	public boolean stopEditMode(){
		if(hasLabelEdidting()){
			panel.remove(0);
			panel.add(widget);
			if(!(widget instanceof TabBar))
				restorePosition();
			return true;
		}
		return false;
	}

	public String getDesignCursor(int x, int y, int incr){
		x = x - getParent().getAbsoluteLeft();
		y = y - getParent().getAbsoluteTop();

		int left = getLeftInt();
		int top = getTopInt();
		int right = left + getWidthInt();
		int bottom = top + getHeightInt();

		if(y >= top-incr && y <= top+incr && (x >= right-incr && x <= right+incr))
			return "ne-resize";
		else if(y >= bottom-incr && y <= bottom+incr && (x >= right-incr && x <= right+incr))
			return "se-resize";
		else if(y >= top-incr && y <= top+incr && (x >= left-incr && x <= left+incr))
			return "nw-resize";
		else if(y >= bottom-incr && y <= bottom+incr && (x >= left-incr && x <= left+incr))
			return "sw-resize";
		else if(x >= right-incr && x <= right+incr)
			return "e-resize";
		else if(x >= left-incr && x <= left+incr)
			return "w-resize";
		else if(y >= top-incr && y <= top+incr)
			return "n-resize";
		else if(y >= bottom-incr && y <= bottom+incr)
			return "s-resize";

		if(widget instanceof DesignGroupWidget && !isRepeated())
			return "default";

		return "move";
	}

	public void setText(String text){
		if(widget instanceof TabBar && text != null && text.trim().length() > 0)
			setTabText(text);
		else
			super.setText(text);
	}

	public String getTitle(){
		if(widget instanceof DesignGroupWidget)
			return ((DesignGroupWidget)widget).getTitle();
		return super.getTitle();
	}

	public void setTitle(String title){
		if(widget instanceof DesignGroupWidget)
			((DesignGroupWidget)widget).setTitle(title);
		else
			super.setTitle(title);
	}

	public String getWidgetName(){
		if(widget instanceof RadioButton)
			return WidgetEx.WIDGET_TYPE_RADIOBUTTON;
		else if(widget instanceof CheckBox)
			return WidgetEx.WIDGET_TYPE_CHECKBOX;
		else if(widget instanceof Button)
			return WidgetEx.WIDGET_TYPE_BUTTON;
		else if(widget instanceof ListBox)
			return WidgetEx.WIDGET_TYPE_LISTBOX;
		else if(widget instanceof TextArea)
			return WidgetEx.WIDGET_TYPE_TEXTAREA;
		else if(widget instanceof DatePickerEx)
			return WidgetEx.WIDGET_TYPE_DATEPICKER;
		else if(widget instanceof DateTimeWidget)
			return WidgetEx.WIDGET_TYPE_DATETIME;
		else if(widget instanceof TimeWidget)
			return WidgetEx.WIDGET_TYPE_TIME;
		else if(widget instanceof TextBox)
			return WidgetEx.WIDGET_TYPE_TEXTBOX;
		else if(widget instanceof Label)
			return WidgetEx.WIDGET_TYPE_LABEL;
		else if(widget instanceof Image)
			return WidgetEx.WIDGET_TYPE_IMAGE;
		else if(widget instanceof Hyperlink)
			return WidgetEx.WIDGET_TYPE_VIDEO_AUDIO;
		else if(widget instanceof DesignGroupWidget)
			return WidgetEx.WIDGET_TYPE_GROUPBOX;
		return null;
	}

	public void storePosition(){
		super.storePosition();

		if(widget instanceof DesignGroupWidget)
			((DesignGroupWidget)widget).storePosition();
	}

	public boolean isWidgetInRect(int left, int top, int right, int bottom){
		int x = FormUtil.convertDimensionToInt(getLeft());
		int y = FormUtil.convertDimensionToInt(getTop());
		int temp = left;
		if(left > right){
			left = right;
			right = temp;
		}
		temp = top;
		if(top > bottom){
			top = bottom;
			bottom = temp;
		}

		return (x > left && x < right && y > top && y < bottom) ||
		(x > right && x < left && y > bottom && y < top);
	}

	public int getTabIndex(){
		if(widget instanceof RadioButton)
			return ((RadioButton)widget).getTabIndex();
		else if(widget instanceof CheckBox)
			return ((CheckBox)widget).getTabIndex();
		else if(widget instanceof Button)
			return ((Button)widget).getTabIndex();
		else if(widget instanceof ListBox)
			return ((ListBox)widget).getTabIndex();
		else if(widget instanceof TextArea)
			return ((TextArea)widget).getTabIndex();
		else if(widget instanceof DatePickerEx)
			return ((DatePickerEx)widget).getTabIndex();
		else if(widget instanceof DateTimeWidget)
			return ((DateTimeWidget)widget).getTabIndex();
		else if(widget instanceof TextBox)
			return ((TextBox)widget).getTabIndex();
		else if(widget instanceof DesignGroupWidget)
			return ((DesignGroupWidget)widget).getTabIndex();
		return 0;
	}

	public void setTabIndex(int index){
		this.tabIndex = index;

		if(widget instanceof RadioButton)
			((RadioButton)widget).setTabIndex(index);
		else if(widget instanceof CheckBox)
			((CheckBox)widget).setTabIndex(index);
		else if(widget instanceof Button)
			((Button)widget).setTabIndex(index);
		else if(widget instanceof ListBox)
			((ListBox)widget).setTabIndex(index);
		else if(widget instanceof TextArea)
			((TextArea)widget).setTabIndex(index);
		else if(widget instanceof DatePickerEx)
			((DatePickerEx)widget).setTabIndex(index);
		else if(widget instanceof DateTimeWidget)
			((DateTimeWidget)widget).setTabIndex(index);
		else if(widget instanceof TextBox)
			((TextBox)widget).setTabIndex(index);
		else if(widget instanceof DesignGroupWidget)
			((DesignGroupWidget)widget).setTabIndex(index);
	}

	public Element buildLayoutXml(Element parent, com.google.gwt.xml.client.Document doc){
		Element node = doc.createElement("Item");
		parent.appendChild(node);			 
		node.setAttribute(WidgetEx.WIDGET_PROPERTY_WIDGETTYPE, getWidgetName());

		layoutNode = node;

		String value = getText();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_TEXT, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_TEXT);

		value = getTitle();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_HELPTEXT, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_HELPTEXT);

		value = getBinding();
		if(value == null || value.trim().length() == 0)	{
			//Widgets should have unique bindings to get unique xpath expressions for locale translation
			setBinding("LEFT"+getLeft()+"TOP"+getTop());
			value = getBinding();
		}
		node.setAttribute(WidgetEx.WIDGET_PROPERTY_BINDING, value);

		value = getParentBinding();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_PARENTBINDING, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_PARENTBINDING);

		node.setAttribute(WidgetEx.WIDGET_PROPERTY_LEFT, getLeft());
		node.setAttribute(WidgetEx.WIDGET_PROPERTY_TOP, getTop());

		value = getWidth();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_WIDTH, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_WIDTH);

		value = getHeight();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_HEIGHT, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_HEIGHT);

		node.setAttribute(WidgetEx.WIDGET_PROPERTY_TABINDEX, String.valueOf(getTabIndex()));

		buildLabelProperties(node);

		if(widget instanceof DesignGroupWidget){
			((DesignGroupWidget)widget).buildLayoutXml(node, doc);

			if(!isRepeated()){
				setBinding("LEFT"+getLeft()+"TOP"+getTop());
				node.setAttribute(WidgetEx.WIDGET_PROPERTY_BINDING, binding);
				DesignWidgetWrapper headerLabel = ((DesignGroupWidget)widget).getHeaderLabel();
				if(headerLabel != null)
					headerLabel.setBinding(binding);
			}
		}

		if(isRepeated())
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_REPEATED, WidgetEx.REPEATED_TRUE_VALUE);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_REPEATED);

		value = getExternalSource();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_EXTERNALSOURCE, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_EXTERNALSOURCE);

		value = getDisplayField();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_DISPLAYFIELD, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_DISPLAYFIELD);

		value = getValueField();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_VALUEFIELD, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_VALUEFIELD);
		
		value = getFilterField();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_FILTERFIELD, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_FILTERFIELD);

		value = getId();
		if(value != null && value.trim().length() > 0)
			node.setAttribute(WidgetEx.WIDGET_PROPERTY_ID, value);
		else
			node.removeAttribute(WidgetEx.WIDGET_PROPERTY_ID);

		return node;
	}

	public void buildLanguageXml(com.google.gwt.xml.client.Document doc, Element parentNode, String xpath){
		if(binding == null || binding.trim().length() == 0)
			return; //DesignGroupWidget may not have binding

		String xpathRoot = xpath;
		if(binding != null && binding.trim().length() > 0)
			xpathRoot +=  "Binding='" + binding + "' and @";

		if(parentBinding != null && parentBinding.trim().length() > 0)
			xpathRoot +=  "ParentBinding='" + parentBinding + "' and @";

		xpathRoot += WidgetEx.WIDGET_PROPERTY_WIDGETTYPE + "='" + getWidgetName()+ "'][@";

		buildLanguageText(doc,getText(),WidgetEx.WIDGET_PROPERTY_TEXT,parentNode,xpathRoot);
		buildLanguageText(doc,getTitle(),WidgetEx.WIDGET_PROPERTY_HELPTEXT,parentNode,xpathRoot);
		buildLanguageText(doc,getTop(),WidgetEx.WIDGET_PROPERTY_TOP,parentNode,xpathRoot);
		buildLanguageText(doc,getLeft(),WidgetEx.WIDGET_PROPERTY_LEFT,parentNode,xpathRoot);
		buildLanguageText(doc,getWidth(),WidgetEx.WIDGET_PROPERTY_WIDTH,parentNode,xpathRoot);

		if(getWrappedWidget() instanceof DesignGroupWidget){
			if(binding != null && binding.trim().length() > 0)
				xpath += "Binding='" + binding + "' and @"+ WidgetEx.WIDGET_PROPERTY_WIDGETTYPE + "='" + getWidgetName() + "']/Item[@";
			else
				xpath += WidgetEx.WIDGET_PROPERTY_WIDGETTYPE + "='" + getWidgetName() + "']/Item[@";

			((DesignGroupWidget)getWrappedWidget()).buildLanguageXml(doc,parentNode, xpath);
		}
	}

	private void buildLanguageText(com.google.gwt.xml.client.Document doc, String text, String name, Element parentNode, String xpathRoot){
		if(text != null && text.trim().length() > 0){
			Element node = doc.createElement(XformConstants.NODE_NAME_TEXT);
			node.setAttribute(XformConstants.ATTRIBUTE_NAME_XPATH, xpathRoot + name + "]");
			node.setAttribute(XformConstants.ATTRIBUTE_NAME_VALUE, text);
			parentNode.appendChild(node);
		}
	}

	public void buildLabelProperties(Element node){
		String value = getForeColor();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("color", value);
		else
			node.removeAttribute("color");

		value = getFontWeight();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("fontWeight", value);
		else
			node.removeAttribute("fontWeight");

		value = getFontStyle();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("fontStyle", value);
		else
			node.removeAttribute("fontStyle");

		value = getFontSize();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("fontSize", value);
		else
			node.removeAttribute("fontSize");

		value = getFontFamily();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("fontFamily", value);
		else
			node.removeAttribute("fontFamily");

		value = getTextDecoration();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("textDecoration", value);
		else
			node.removeAttribute("textDecoration");

		value = getBackgroundColor();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("backgroundColor", value);
		else
			node.removeAttribute("backgroundColor");

		value = getBorderStyle();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("borderStyle", value);
		else
			node.removeAttribute("borderStyle");

		value = getBorderWidth();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("borderWidth", value);
		else
			node.removeAttribute("borderWidth");

		value = getBorderColor();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("borderColor", value);
		else
			node.removeAttribute("borderColor");

		value = getTextAlign();
		if(value != null && value.trim().length() > 0)
			node.setAttribute("textAlign", value);
		else
			node.removeAttribute("textAlign");	
	}

	public FormDesignerDragController getDragController(){
		if(widget instanceof DesignGroupWidget)
			return ((DesignGroupWidget)widget).getDragController();
		return null;
	}

	public AbsolutePanel getPanel(){
		if(widget instanceof DesignGroupWidget)
			return ((DesignGroupWidget)widget).getPanel();
		return null;
	}

	public PopupPanel getWidgetPopup(){
		if(widget instanceof DesignGroupWidget)
			return ((DesignGroupWidget)widget).getWidgetPopup();
		return null;
	}

	public void setQuestionDef(QuestionDef questionDef){
		if(questionDef != null && (this.questionDef != questionDef)){
			String title = questionDef.getHelpText();
			if(title == null || title.trim().length() == 0)
				title = questionDef.getText();
			setTitle(title);
		}

		this.questionDef = questionDef;
		this.questionDef.addChangeListener(this);
	}

	public QuestionDef getQuestionDef(){
		return questionDef;
	}

	public void onBindingChanged(QuestionDef sender,String newValue) {
		setBinding(newValue);

	}

	public void onEnabledChanged(QuestionDef sender,boolean enabled) {
		// TODO Auto-generated method stub

	}

	public void onLockedChanged(QuestionDef sender,boolean locked) {
		// TODO Auto-generated method stub

	}

	public void onRequiredChanged(QuestionDef sender,boolean required) {
		// TODO Auto-generated method stub

	}

	public void onVisibleChanged(QuestionDef sender,boolean visible) {
		// TODO Auto-generated method stub

	}	

	public void onDataTypeChanged(QuestionDef sender,QuestionType dataType){
		if(widget instanceof Label)
			return; //We do not change labels into data input widgets.
		if(dataType == QuestionType.DATE){
			if(!(widget instanceof DatePickerEx)){
				storePosition();
				panel.remove(widget);
				widget = new DatePickerWidget();
				panel.add(widget);
				refreshSize();
			}
		}
		else if(dataType == QuestionType.DATE_TIME){
			if(!(widget instanceof DateTimeWidget)){
				storePosition();
				panel.remove(widget);
				widget = new DateTimeWidget();
				panel.add(widget);
				refreshSize();
			}
		}
		else if(dataType == QuestionType.TIME){
			if(!(widget instanceof TimeWidget)){
				storePosition();
				panel.remove(widget);
				widget = new TimeWidget();
				panel.add(widget);
				refreshSize();
			}
		}
		else if(dataType == QuestionType.TEXT || dataType == QuestionType.NUMERIC || dataType == QuestionType.DECIMAL){
			if(( !(widget instanceof TextBox || widget instanceof TextArea) || (widget instanceof DatePickerEx)) ){
				storePosition();
				panel.remove(widget);
				widget = new TextBox();
				panel.add(widget);
				refreshSize();
			}
		}
		else if(dataType == QuestionType.BOOLEAN){
			if(!(widget instanceof ListBox || widget instanceof CheckBox)){
				storePosition();
				panel.remove(widget);
				widget = new ListBox(false);
				panel.add(widget);
				refreshSize();
			}
		}
		else if(dataType == QuestionType.LIST_EXCLUSIVE){
			if(!(widget instanceof ListBox || widget instanceof RadioButton)){
				if((externalSource != null && externalSource.trim().length() > 0) ||
						(displayField != null && displayField.trim().length() > 0) ||
						(valueField != null && valueField.trim().length() > 0)){

					onDataTypeChanged(sender,QuestionType.TEXT);
				}
				else{
					storePosition();
					panel.remove(widget);
					widget = new ListBox(false);
					panel.add(widget);
					refreshSize();
				}
			}
		}
		else if(dataType == QuestionType.IMAGE){
			if(!(widget instanceof Image)){
				storePosition();
				panel.remove(widget);
				widget = new Image();
				panel.add(widget);
				refreshSize();
			}
		}
		else if(dataType == QuestionType.VIDEO || dataType == QuestionType.VIDEO){
			if(!(widget instanceof Hyperlink)){
				storePosition();
				panel.remove(widget);
				widget = new Hyperlink();
				((Hyperlink)widget).setText(designerMessages.clickToPlay());
				panel.add(widget);
				refreshSize();
			}
		}
	}

	public void onOptionsChanged(QuestionDef sender,List<OptionDef> optionList){

	}

	public Element getLayoutNode(){
		return layoutNode;
	}

	public void setLayoutNode(Element node){
		layoutNode = node;
	}

	public void setPopupPanel(PopupPanel popup){
		this.popup = popup;
	}

	public PopupPanel getPopupPanel(){
		return popup;
	}

	public void setWidgetSelectionListener(WidgetSelectionListener widgetSelectionListener){
		this.widgetSelectionListener = widgetSelectionListener;
	}

	public WidgetSelectionListener getWidgetSelectionListener(){
		return widgetSelectionListener;
	}

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler, MouseWheelEvent.getType());
	}
}
