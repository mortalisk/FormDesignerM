package org.openxdata.formtools.client.controller;

import org.openxdata.formtools.client.FormEntryContext;
import org.openxdata.formtools.client.cmd.FormDataListLoadCmd;
import org.openxdata.formtools.client.cmd.FormDefDeleteCmd;
import org.openxdata.formtools.client.cmd.FormDefLoadCmd;
import org.openxdata.formtools.client.cmd.FormDownloadCmd;
import org.openxdata.formtools.client.listener.FormDefDownloadListener;
import org.openxdata.formtools.client.util.FormUtil;
import org.openxdata.formtools.client.view.impl.SettingsViewImpl;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormEntryController {

	private FormDownloadController formDownloadController = new FormDownloadController();
	private DataUploadController dataUploadController = new DataUploadController();
	

	public FormEntryController(){

	}

	public void downloadForms(){
		formDownloadController.downloadForms(new FormDownloadCmd());
	}
	
	public void uploadData(){
		dataUploadController.uploaData();
	}

	public void downloadForm(final String id, String name, final FormDefDownloadListener formDownloadListener){
		formDownloadController.downloadForm(id, name, formDownloadListener);
	}

	public void designForm(final String id){

		FormUtil.dlg.setText("Loading Form");
		FormUtil.dlg.center();

		DeferredCommand.addCommand(new Command(){
			public void execute() {
				try{
					FormEntryContext.getDatabaseManager().loadFormDef(id, new FormDefLoadCmd(true));
					FormUtil.dlg.hide();
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}
			}
		});
	}
	
	public void enterNewForm(final String id){

		FormEntryContext.setFormDataId(null);
		
		FormUtil.dlg.setText("Loading Form");
		FormUtil.dlg.center();

		DeferredCommand.addCommand(new Command(){
			public void execute() {
				try{
					FormEntryContext.getDatabaseManager().loadFormDef(id, new FormDefLoadCmd(false));
					//FormUtil.dlg.hide();
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}
			}
		});
	}
	
	
	public void listData(final String id){

		FormUtil.dlg.setText("Loading Data");
		FormUtil.dlg.center();

		DeferredCommand.addCommand(new Command(){
			public void execute() {
				try{
					FormEntryContext.getDatabaseManager().loadFormDataList(id, new FormDataListLoadCmd());
					FormUtil.dlg.hide();
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}
			}
		});
	}
	
	
	public void showSettings(){
		new SettingsViewImpl().center();
	}
	
	public void setFormDownloadUrl(String url){
		FormEntryContext.setFormDownloadUrl(url);
		FormEntryContext.getDatabaseManager().saveFormDownloadUrl(url);
	}
	
	public void setDataUploadUrl(String url){
		FormEntryContext.setDataUploadUrl(url);
		FormEntryContext.getDatabaseManager().saveDataUploadUrl(url);
	}
	
	public void deleteFormDef(){
		if(!Window.confirm("Do you really want to delete the form: " + FormEntryContext.getFormName()))
			return;
		
		new FormDefDeleteCmd(FormEntryContext.getFormDefId());
	}
}
