package org.openxdata.entry.client.cmd;

import org.openxdata.entry.client.FormEntryContext;
import org.openxdata.entry.client.listener.DataLoadListener;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormDefLoadCmd implements DataLoadListener {
	
	private boolean designForm;
	
	private int loadCount = 0;
	private String xformXml;
	private String layoutXml;
	private String javaScriptSrc;
	
	public FormDefLoadCmd(boolean designForm){
		this.designForm = designForm;
	}
	
	public void onDataReceived(String data){
		loadCount++;
		
		if(loadCount == 1){
			xformXml = data;
			FormEntryContext.getDatabaseManager().loadFormLayout(FormEntryContext.getFormDefId(), this);
		}
		else if(loadCount == 2){
			layoutXml = data;
			FormEntryContext.getDatabaseManager().loadFormJavaScript(FormEntryContext.getFormDefId(), this);
		}
		else{
			assert(loadCount == 3);
			
			javaScriptSrc = data;
			FormEntryContext.setCurrentFormDef(designForm, xformXml, layoutXml, javaScriptSrc);
		}
	}
}
