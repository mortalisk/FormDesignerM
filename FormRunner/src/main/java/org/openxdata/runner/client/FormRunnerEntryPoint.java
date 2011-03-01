package org.openxdata.runner.client;

import org.openxdata.runner.client.widget.FormRunnerWidget;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.view.FormRunnerView.Images;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import org.openxdata.sharedlib.client.locale.FormsConstants;


/**
 * This is the GWT entry point for the form runtime engine.
 */
public class FormRunnerEntryPoint implements EntryPoint{

    private final FormsConstants i18n = GWT.create(FormsConstants.class);

	/** The form runtime widget. */
	private FormRunnerWidget formRunner;

	/**
	 * Instantiate an application-level image bundle. This object will provide
	 * programatic access to all the images needed by widgets.
	 */
	public static final Images images = (Images) GWT.create(Images.class);


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		FormUtil.dlg.setText(i18n.loading());
		FormUtil.dlg.center();
		
		publishJS();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				onModuleLoadDeffered();
			}
		});		
	}

	public void onModuleLoadDeffered() {
		try{
			RootPanel rootPanel = RootPanel.get("formtoolsrunner");
			if(rootPanel == null){
				FormUtil.dlg.hide();
				return;
			}
			
			FormUtil.setupUncaughtExceptionHandler();	

			FormUtil.retrieveUserDivParameters();

			formRunner = new FormRunnerWidget(images);
			
			rootPanel.add(formRunner);

			FormUtil.maximizeWidget(formRunner);

			String formId = FormUtil.getFormId();
			String entityId = FormUtil.getEntityId();
			if(formId != null && entityId != null)
				formRunner.loadForm(Integer.parseInt(formId),Integer.parseInt(entityId));
			else{
				FormUtil.dlg.hide();
				Window.alert(i18n.noFormId() + FormUtil.getEntityIdName() + i18n.divFound());
			}

			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				public void execute() {
					String formId = FormUtil.getFormId();
					String entityId = FormUtil.getEntityId();
					if(formId == null || entityId == null)
						FormUtil.dlg.hide();
				}
			});
		}
		catch(Exception ex){
			FormUtil.displayException(ex);
		}
	}
	
	/**
	 * This is just a temporary hack for those who use both the form designer and runner as two
	 * separate GWT widgets and then the form designer registers the authentication callback
	 * instead of the form runner. So this method is just a away for them to override the
	 * form designer's call back with that of the form runner.
	 */
	public static void registerAuthenticationCallback(){
		publishJS();
	}

	// Set up the JS-callable signature as a global JS function.
	private static native void publishJS() /*-{
   		$wnd.authenticationCallback = @org.openxdata.sharedlib.client.view.FormRunnerView::authenticationCallback(Z);
   		$wnd.submitForm = @org.openxdata.sharedlib.client.view.FormRunnerView::submitForm();
	}-*/;
}
