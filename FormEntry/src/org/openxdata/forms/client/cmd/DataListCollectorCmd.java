package org.openxdata.forms.client.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openxdata.forms.client.FormEntryContext;
import org.openxdata.forms.client.listener.DataListCollectorListener;
import org.openxdata.forms.client.listener.DataLoadListener;
import org.openxdata.forms.client.model.KeyValue;
import org.openxdata.forms.client.util.Utils;

//85,000
//15,000

/**
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class DataListCollectorCmd implements DataLoadListener{

	private DataListCollectorListener listener;
	private List<String> dataList;
	private int loadCount = 0;
	private List<KeyValue> formDefList;
	private String id;
	private HashMap<String, String> dataDefMap = new HashMap<String, String>();
	
	
	public DataListCollectorCmd(DataListCollectorListener listener){
		this.listener = listener;
		fillDataList();
	}
	
	private void fillDataList(){
		dataList = new ArrayList<String>();
		
		formDefList = FormEntryContext.getFormDefList();
		if(formDefList == null || formDefList.size() == 0)
			return;
		
		loadNextFormDataList();
	}
	
	public void onDataReceived(String data){
		if(data != null)
			Utils.fillFormDataIdList(data, dataList, id, dataDefMap);
		
		if(++loadCount == FormEntryContext.getFormDefList().size())
			listener.onDataListCollected(dataList, dataDefMap);
		else
			loadNextFormDataList();
	}
	
	private void loadNextFormDataList(){
		KeyValue keyValue = formDefList.get(loadCount);
		id = keyValue.getKey();
		FormEntryContext.getDatabaseManager().loadFormDataList(id, this);
	}
}
