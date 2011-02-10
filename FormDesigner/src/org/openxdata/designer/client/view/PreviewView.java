package org.openxdata.designer.client.view;

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.List;

import org.openxdata.designer.client.controller.ICenterPanel;
import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.designer.client.vew.widget.images.FormDesignerImages;
import org.openxdata.sharedlib.client.controller.SubmitListener;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.view.FormRunnerView;
import org.openxdata.sharedlib.client.widget.RuntimeWidgetWrapper;
import org.openxdata.sharedlib.client.xforms.XformBuilder;
import org.openxdata.sharedlib.client.xforms.XformUtil;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import org.openxdata.sharedlib.client.locale.FormsConstants;


/**
 * This widget is used to preview a form in the form designer.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class PreviewView extends FormRunnerView {
    final FormsConstants formsConstants = GWT.create(FormsConstants.class);

	/** Popup for displaying the context menu for the preview. */
	private PopupPanel popup;
	
	/** Reference to the design surface for getting layout xml during refresh. */
	private DesignSurfaceView designSurfaceView;
	
	/** Reference to the center panel for committing edit changes and getting the current form. */
	private ICenterPanel centerPanel;

	
	/**
	 * Creates a new instance of the preview widget.
	 * 
	 * @param images the images for the preview context menu.
	 */
	public PreviewView(FormDesignerImages images){
		popup = new PopupPanel(true,true);
		MenuBar menuBar = new MenuBar(true);
		menuBar.addItem(FormDesignerUtil.createHeaderHTML(images.refresh(),formsConstants.refresh()),true,new Command(){
			public void execute() {popup.hide(); refresh();}});

		menuBar.addSeparator();
		menuBar.addItem(FormDesignerUtil.createHeaderHTML(images.save(),formsConstants.submit()),true,new Command(){
			public void execute() {popup.hide(); submit();}});

		popup.setWidget(menuBar);

		addNewTab(formsConstants.page()+"1");

		DOM.sinkEvents(getElement(),DOM.getEventsSunk(getElement()) | Event.ONMOUSEDOWN);

		//This is needed for IE
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				setHeight(getHeight());
			}
		});
	}

	//TODO These two should bind to interfaces.
	public void setDesignSurface(DesignSurfaceView designSurfaceView){
		this.designSurfaceView = designSurfaceView;
	}

	public void setCenterPanel(ICenterPanel centerPanel){
		this.centerPanel = centerPanel;
	}

	/**
	 * Sets up the preview widget.
	 */
	protected void initPanel(){
		AbsolutePanel panel = new AbsolutePanel();
		selectedPanel = panel;

		//This is needed for IE
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				setHeight(getHeight());
			}
		});
	}

	@Override
	protected void submit(){		
		if(formDef != null){
			if(formDef.getDoc() == null)
				XformBuilder.fromFormDef2Xform(formDef);

			saveValues();

			if(!isValid(false))
				return;

			String xml = XformUtil.getInstanceDataDoc(formDef.getDoc()).toString();
			xml = FormDesignerUtil.formatXml(xml);
			submitListener.onSubmit(xml);
		}
	}
	
	/**
	 * Sets the listener for form submission events.
	 * 
	 * @param submitListener the listener.
	 */
	public void setSubmitListener(SubmitListener submitListener){
		this.submitListener = submitListener;
	}

	/**
	 * Checks if the preview surface has any widgets.
	 * 
	 * @return true if yes, else false.
	 */
	public boolean isPreviewing(){
		return tabs.getWidgetCount() > 0 && selectedPanel != null && selectedPanel.getWidgetCount() > 0;
	}

	@Override
	public void onBrowserEvent(Event event) {
		int type = DOM.eventGetType(event);

		switch (type) {
		case Event.ONMOUSEDOWN:
			
			FormDesignerUtil.enableContextMenu(getElement());
			
			if( (event.getButton() & Event.BUTTON_RIGHT) != 0){
				if(event.getTarget().getClassName().length() == 0){
					
					int ypos = event.getClientY();
					if(Window.getClientHeight() - ypos < 100)
						ypos = event.getClientY() - 100;
					
					int xpos = event.getClientX();
					if(Window.getClientWidth() - xpos < 110)
						xpos = event.getClientX() - 110;
					
					FormDesignerUtil.disableContextMenu(popup.getElement());
					FormDesignerUtil.disableContextMenu(getElement());
					popup.setPopupPosition(xpos, ypos);
					popup.show();
				}
			}
			break;
		}	
	}

	/**
	 * Reloads widgets on the preview surface.
	 */
	public void refresh(){
		FormUtil.dlg.setText(formsConstants.refreshingPreview());
		FormUtil.dlg.center();

		DeferredCommand.addCommand(new Command(){
			public void execute() {
				try{
					centerPanel.commitChanges();
					List<RuntimeWidgetWrapper> externalSourceWidgets = new ArrayList<RuntimeWidgetWrapper>();
					loadForm(centerPanel.getFormDef(), designSurfaceView.getLayoutXml(),centerPanel.getJavaScriptSource(),externalSourceWidgets,true);
					FormUtil.dlg.hide();
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}
			}
		});
	}

	/**
	 * Removes all widgets from the preview surface.
	 */
	public void clearPreview(){
		tabs.clear();
	}
}
