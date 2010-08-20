package org.openxdata.formtools.client.controller;

import java.util.HashMap;
import java.util.List;

import org.openxdata.formtools.client.FormEntryContext;
import org.openxdata.formtools.client.cmd.DataListCollectorCmd;
import org.openxdata.formtools.client.cmd.DataUploadCmd;
import org.openxdata.formtools.client.cmd.FormDataDeleteCmd;
import org.openxdata.formtools.client.listener.DataListCollectorListener;
import org.openxdata.formtools.client.listener.DataUploadListener;
import org.openxdata.formtools.client.listener.FormDataDeleteListener;
import org.openxdata.formtools.client.listener.LoginInfoListener;
import org.openxdata.formtools.client.util.FormUtil;
import org.openxdata.formtools.client.view.impl.LoginInfoDlg;

import com.google.gwt.user.client.Window;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class DataUploadController implements DataListCollectorListener, DataUploadListener, FormDataDeleteListener, LoginInfoListener {

	private List<String> dataList;
	private int pos = 0;
	private int count = 0;
	private int failureCount = 0;
	private String id;
	private HashMap<String, String> dataDefMap;
	
	
	public DataUploadController(){
		
	}
	
	public void uploaData(){
		new LoginInfoDlg(this).center();
	}
	
	private void uploadData(String id){
		this.id = id;
		new DataUploadCmd(id,pos+1, dataList.size(), this); //add 1 for counting
	}
	
	public void onDataListCollected(List<String> dataList, HashMap<String, String> dataDefMap){
		this.dataList = dataList;
		this.dataDefMap = dataDefMap;
		
		count = dataList.size();
		pos = 0;
		failureCount = 0;
		
		if(count == 0){
			Window.alert("No data to upload.");
			return;
		}
		
		uploadData(dataList.get(pos));
	}
	
	public void onDataUploaded(){
		new FormDataDeleteCmd(id, dataDefMap.get(id), this);
	}
	
	public void onDataUploadFailed(){
		failureCount++;
		tryUploadNextData();
	}
	
	private void tryUploadNextData(){
		if(++pos < count)
			uploadData(dataList.get(pos));
		else
			displayUploadCompleteMsg();
	}
	
	private void displayUploadCompleteMsg(){
		
		FormEntryContext.setUserName(null);
		FormEntryContext.setPassword(null);
		
		String message = count + " forms of data uploaded successfully.";
		if(failureCount > 0)
			message = (count - failureCount) + " forms of data uploaded successfully and " + failureCount + " failed";
	
		FormUtil.dlg.hide();
		
		Window.alert(message);
		
		//TODO We need to clear the main view just incase it has a list of data loaded.
	}
	
	public void onFormDataDeleted(String id){
		tryUploadNextData();
	}
	
	public void onLoginInfo(String name, String password){
		FormEntryContext.setUserName(name);
		FormEntryContext.setPassword(password);
		
		new DataListCollectorCmd(this);
	}
}
