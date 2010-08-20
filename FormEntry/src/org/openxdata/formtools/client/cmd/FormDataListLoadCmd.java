package org.openxdata.formtools.client.cmd;

import org.openxdata.formtools.client.FormEntryContext;
import org.openxdata.formtools.client.listener.DataLoadListener;
import org.openxdata.formtools.client.util.Utils;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormDataListLoadCmd implements DataLoadListener {

	public FormDataListLoadCmd(){
		
	}
	
	public void onDataReceived(String data){
		FormEntryContext.setFormDataList(Utils.getFormDataList(data));
	}
}
