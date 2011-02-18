package org.openxdata.designer.client;

import java.util.ArrayList;
import java.util.List;

import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.sharedlib.client.OpenXdataConstants;
import org.openxdata.sharedlib.client.locale.FormsConstants;
import org.openxdata.sharedlib.client.model.Locale;
import org.openxdata.sharedlib.client.util.FormUtil;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 */
public class FormDesignerEntryPoint implements EntryPoint ,ResizeHandler{
    final FormsConstants formsConstants = GWT.create(FormsConstants.class);

	/**
	 * Reference to the form designer widget.
	 */
	private FormDesignerWidget designer;

	/**
	 * This is the GWT entry point method.
	 */
	public void onModuleLoad() {
		
		FormUtil.dlg.setText(formsConstants.loading());
		FormUtil.dlg.center();
		
		publishJS();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				onModuleLoadDeffered();
			}
		});		
	}
	
	/**
	 * Sets up the form designer.
	 */
	public void onModuleLoadDeffered() {

		try{
			RootPanel rootPanel = RootPanel.get("formtoolsdesigner");
			if(rootPanel == null){
				FormUtil.dlg.hide();
				return;
			}

			FormUtil.setupUncaughtExceptionHandler();

			FormDesignerUtil.setDesignerTitle();

			String s = FormUtil.getDivValue("allowBindEdit");
			if(s != null && (s.equals("0") || s.equals("false")))
				Context.setAllowBindEdit(false);

			FormUtil.retrieveUserDivParameters();
			
			Context.setOfflineModeStatus();

			// Get rid of scrollbars, and clear out the window's built-in margin,
			// because we want to take advantage of the entire client area.
			Window.enableScrolling(false);
			Window.setMargin("0"+OpenXdataConstants.UNITS);

			// Different themes use different background colors for the body
			// element, but IE only changes the background of the visible content
			// on the page instead of changing the background color of the entire
			// page. By changing the display style on the body element, we force
			// IE to redraw the background correctly.
			RootPanel.getBodyElement().getStyle().setProperty("display", "none");
			RootPanel.getBodyElement().getStyle().setProperty("display", "");

			loadLocales();
			
			designer = new FormDesignerWidget(true,true,true);
			
			// Finally, add the designer widget to the RootPanel, so that it will be displayed.
			rootPanel.add(designer);
			
			updateTabs();
			
			//If a form id has been specified in the html host page, load the form
			//with that id in the designer.
			s = FormUtil.getFormId();
			if(s != null)
				designer.loadForm(Integer.parseInt(s));
			

			// Call the window resized handler to get the initial sizes setup. Doing
			// this in a deferred command causes it to occur after all widgets' sizes
			// have been computed by the browser.
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				public void execute() {
					designer.onWindowResized(Window.getClientWidth(), Window.getClientHeight());
					
					String id = FormUtil.getFormId();
					if(id == null || id.equals("-1"))
						FormUtil.dlg.hide();
				}
			});
			
			// Hook the window resize event, so that we can adjust the UI.
			Window.addResizeHandler(this);
		}
		catch(Exception ex){
			FormUtil.displayException(ex);
		}
	}
	
	private void updateTabs(){
		String s = FormUtil.getDivValue("showXformsSourceTab");
		if(!("1".equals(s) || "true".equals(s)))
			designer.removeXformSourceTab();
		
		s = FormUtil.getDivValue("showLayoutXmlTab");
		if(!("1".equals(s) || "true".equals(s)))
			designer.removeLayoutXmlTab();
		
		s = FormUtil.getDivValue("showLanguageTab");
		if(!("1".equals(s) || "true".equals(s)))
			designer.removeLanguageTab();
		
		s = FormUtil.getDivValue("showModelXmlTab");
		if(!("1".equals(s) || "true".equals(s)))
			designer.removeModelXmlTab();
		
		s = FormUtil.getDivValue("showJavaScriptTab");
		if(!("1".equals(s) || "true".equals(s)))
			designer.removeJavaScriptTab();
	}
	
	public void onResize(ResizeEvent event){
		designer.onWindowResized(event.getWidth(), event.getHeight());
	}
	
	// Set up the JS-callable signature as a global JS function.
	private native void publishJS() /*-{
   		$wnd.authenticationCallback = @org.openxdata.designer.client.controller.FormDesignerController::authenticationCallback(Z);
   		$wnd.submitForm = @org.openxdata.sharedlib.client.view.FormRunnerView::submitForm();
	}-*/;
	
	/**
	 * Loads a list of locales supported by the form designer.
	 */
	private void loadLocales(){
		String localesList = FormUtil.getDivValue("localeList");
		
		if(localesList == null || localesList.trim().length() == 0)
			return;
		
		String[] tokens = localesList.split(",");
		if(tokens == null || tokens.length == 0)
			return;
		
		List<Locale> locales = new ArrayList<Locale>();
		
		for(String token: tokens){
			int index = token.indexOf(':');
			
			//Should at least have one character for key or name
			if(index < 1 || index == token.length() - 1)
				continue;
			
			locales.add(new Locale(token.substring(0,index).trim(),token.substring(index+1).trim()));
		}
		
		Context.setLocales(locales);
	}
}
