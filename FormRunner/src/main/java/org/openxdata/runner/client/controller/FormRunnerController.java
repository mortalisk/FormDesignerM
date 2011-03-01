package org.openxdata.runner.client.controller;

import com.google.gwt.core.client.GWT;
import java.util.ArrayList;
import java.util.List;

import org.openxdata.runner.client.widget.FormRunnerWidget;
import org.openxdata.sharedlib.client.OpenXdataConstants;
import org.openxdata.sharedlib.client.controller.SubmitListener;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.widget.RuntimeWidgetWrapper;
import org.openxdata.sharedlib.client.xforms.XformParser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import org.openxdata.sharedlib.client.locale.FormsConstants;


/**
 * Controls the interactions between the views for the form runner
 * and also handles server side communication on behalf of this view.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormRunnerController implements SubmitListener{

    private final FormsConstants i18n = GWT.create(FormsConstants.class);

	private FormRunnerWidget formRunner;
	private String xformXml;
	private String layoutXml;
	private String javaScriptSrc;
	private int formId;
	private int entityId;

	public FormRunnerController(FormRunnerWidget formRunner){
		this.formRunner = formRunner;
	}

	public void loadForm(int frmId, int entyId){
		this.formId = frmId;
		this.entityId = entyId;

		FormUtil.dlg.setText(i18n.openingForm());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {

				String url = FormUtil.getHostPageBaseURL();
				url += FormUtil.getEntityFormDefDownloadUrlSuffix();
				url += FormUtil.getFormIdName()+"="+formId;
				url += "&" + FormUtil.getEntityIdName() + "="+entityId;

				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,URL.encode(url));

				try{
					builder.sendRequest(null, new RequestCallback(){
						public void onResponseReceived(Request request, Response response){
							
							if(response.getStatusCode() != Response.SC_OK){
								FormUtil.displayReponseError(response);
								return;
							}
								
							String xml = response.getText();
							if(xml == null || xml.length() == 0){
								FormUtil.dlg.hide();
								Window.alert(i18n.noDataFound());
								return;
							}

							xformXml = null; layoutXml = null; javaScriptSrc = null;

							int pos = xml.indexOf(OpenXdataConstants.OPENXDATA_FORMDEF_LAYOUT_XML_SEPARATOR);
							int pos2 = xml.indexOf(OpenXdataConstants.OPENXDATA_FORMDEF_JAVASCRIPT_SRC_SEPARATOR);
							if(pos > 0){
								xformXml = xml.substring(0,pos);
								layoutXml = xml.substring(pos+OpenXdataConstants.OPENXDATA_FORMDEF_LAYOUT_XML_SEPARATOR.length(), pos2 > 0 ? pos2 : xml.length());

								if(pos2 > 0)
									javaScriptSrc = xml.substring(pos2+OpenXdataConstants.OPENXDATA_FORMDEF_JAVASCRIPT_SRC_SEPARATOR.length(), xml.length());

								openForm();
							}
							else{
								FormUtil.dlg.hide();
								Window.alert(i18n.noFormLayout());
							}
						}

						public void onError(Request request, Throwable exception){
							FormUtil.displayException(exception);
						}
					});
				}
				catch(RequestException ex){
					FormUtil.displayException(ex);
				}
			}
		});
	}

	public void openForm() {
		FormUtil.dlg.setText(i18n.openingForm());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				try{
					List<RuntimeWidgetWrapper> externalSourceWidgets = new ArrayList<RuntimeWidgetWrapper>();
					FormDef formDef = XformParser.fromXform2FormDef(xformXml);
					formRunner.loadForm(formDef, layoutXml,javaScriptSrc,externalSourceWidgets);

					FormUtil.dlg.hide();	
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}
			}
		});
	}

	public void onCancel(){
		String url = FormUtil.getHostPageBaseURL();
		url += FormUtil.getAfterSubmitUrlSuffix();

		if(FormUtil.appendEntityIdAfterSubmit()){
			url += FormUtil.getEntityIdName();
			url += "=" + entityId;
		}

		Window.Location.replace(url);
	}

	public void onSubmit(String xml){

		FormUtil.dlg.setText(i18n.submitting());
		FormUtil.dlg.center();

		final String submitXml = xml;

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				String url = FormUtil.getHostPageBaseURL();
				url += FormUtil.getFormDataUploadUrlSuffix();

				RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,URL.encode(url));

				try{
					builder.sendRequest(submitXml, new RequestCallback(){
						public void onResponseReceived(Request request, Response response){
							FormUtil.dlg.hide();
							
							if(response.getStatusCode() != Response.SC_OK){
								FormUtil.displayReponseError(response);
								return;
							}

							if(response.getStatusCode() == Response.SC_OK){
								if(FormUtil.showSubmitSuccessMsg())
									Window.alert(i18n.formSubmitSuccess());

								String url = FormUtil.getHostPageBaseURL();
								url += FormUtil.getAfterSubmitUrlSuffix();

								if(FormUtil.appendEntityIdAfterSubmit()){
									url += FormUtil.getEntityIdName();
									if(entityId > 0)
										url += "=" + entityId;
									else if(entityId == 0 && response.getText().trim().length() > 0)
										url += "=" + response.getText();
								}

								Window.Location.replace(url);
							}
							else
								FormUtil.displayReponseError(response);
						}

						public void onError(Request request, Throwable exception){
							FormUtil.displayException(exception);
						}
					});
				}
				catch(RequestException ex){
					FormUtil.displayException(ex);
				}
			}
		});
	}
}
