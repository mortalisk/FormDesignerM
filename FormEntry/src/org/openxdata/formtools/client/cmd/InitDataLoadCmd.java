package org.openxdata.formtools.client.cmd;

import org.openxdata.formtools.client.FormEntryContext;
import org.openxdata.formtools.client.listener.DataLoadListener;
import org.openxdata.formtools.client.util.Utils;
import org.openxdata.sharedlib.client.util.FormUtil;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class InitDataLoadCmd implements DataLoadListener{

	private int loadCount = 0;
	
	public InitDataLoadCmd(){
		FormEntryContext.getDatabaseManager().loadFormDefList(this);
	}
	
	public void onDataReceived(String data){
		loadCount++;
		
		if(loadCount == 1){
			FormEntryContext.setFormDefList(Utils.getFormDefList(data));
			FormEntryContext.getDatabaseManager().loadFormDownloadUrl(this);
		}
		else if(loadCount == 2){
			String url = data;
			if(url == null)
				url = FormUtil.getFormDefDownloadUrlSuffix();
			FormEntryContext.setFormDownloadUrl(url);
			FormEntryContext.getDatabaseManager().loadDataUploadUrl(this);
		}
		else if(loadCount == 3){
			String url = data;
			if(url == null)
				url = FormUtil.getFormDataUploadUrlSuffix();
			FormEntryContext.setDataUploadUrl(url);
		}
	}
}
