package org.openxdata.runner.client.widget;

import java.util.List;

import org.openxdata.runner.client.controller.FormRunnerController;
import org.openxdata.sharedlib.client.controller.SubmitListener;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.view.FormRunnerView;
import org.openxdata.sharedlib.client.view.FormRunnerView.Images;
import org.openxdata.sharedlib.client.widget.RuntimeWidgetWrapper;
import org.openxdata.sharedlib.client.xforms.XformParser;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;



/**
 * This is the widget for the form runtime engine.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormRunnerWidget extends Composite{

	private DockPanel dockPanel = new DockPanel();
	
	/** The form runner controller. */
	private FormRunnerController controller;
	
	/** The form runtime view. */
	private FormRunnerView view;
	
	
	/**
	 * Creates a new instance of the form runner widget.
	 * 
	 * @param images the images to load icons.
	 */
	public FormRunnerWidget(Images images){
		
		view = new FormRunnerView(/*images*/);
		
		dockPanel.add(view, DockPanel.CENTER);
		
		FormUtil.maximizeWidget(dockPanel);

		initWidget(dockPanel);
		
		controller = new FormRunnerController(this);
		view.setSubmitListener(controller);
		
		//process key board events and pick Ctrl + S for saving.
		previewEvents();
	}
	
	
	/**
	 * Loads a form definition object and uses a given layout xml for its widgets.
	 * 
	 * @param formDef the form definition object.
	 * @param layoutXml the widget layout xml.
	 * @param externalSourceWidgets list of widgets whose data sources come from outside the xform.
	 */
	public void loadForm(FormDef formDef,String layoutXml, String javaScriptSrc, List<RuntimeWidgetWrapper> externalSourceWidgets){
		view.loadForm(formDef, layoutXml,javaScriptSrc,externalSourceWidgets,false);
	}
	
	
	/**
	 * Loads a form with a given id and for a certain entity id.
	 * 
	 * @param formId the form id.
	 * @param entityId the entity id.
	 */
	public void loadForm(int formId, int entityId){
		controller.loadForm(formId,entityId);
	}
	
	/**
	 * Loads an xforms document with its layout.
	 * 
	 * @param xformXml the xforms xml.
	 * @param layoutXml the layout xml.
	 */
	public void loadForm(String xformXml, String layoutXml, String javaScriptSource){
		view.loadForm(XformParser.fromXform2FormDef(xformXml), layoutXml,javaScriptSource, null,false);
	}
	
	
	/**
	 * Sets the offset height of the form runner. This is useful for GWT
	 * applications using the form runner as an embedded widget and do not
	 * want it to take the whole browser space.
	 * 
	 * @param offset the offset height in pixels.
	 */
	public void setEmbeddedHeightOffset(int offset){
		view.setEmbeddedHeightOffset(offset);
	}
	
	
	/**
	 * Sets the listener to form submission events.
	 * 
	 * @param submitListener the listener.
	 */
	public void setSubmitListener(SubmitListener submitListener){
		view.setSubmitListener(submitListener);
	}
	
	
	/**
	 * @see com.google.gwt.user.client.DOM#addEventPreview(EventPreview)
	 */
	private void previewEvents(){
		DOM.addEventPreview(new EventPreview() { 
			public boolean onEventPreview(Event event) 
			{ 				
				if (DOM.eventGetType(event) == Event.ONKEYDOWN)
					return view.handleKeyBoardEvent(event);
				
				return true;
			}
		});
	}
	
	
	public void loadForm(int formId, String xformXml, String modelXml,String layoutXml, String javaScriptSource){
		FormDef formDef = XformParser.fromXform2FormDef(xformXml,modelXml);
		formDef.setId(formId);
		view.loadForm(formDef, layoutXml,javaScriptSource, null,false);
	}
	
	public void clear(){
		view.clear();
	}
}
