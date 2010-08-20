package org.openxdata.formtools.client.controller;

import org.openxdata.formtools.client.FormEntryContext;
import org.openxdata.formtools.client.cmd.FormDataDeleteCmd;
import org.openxdata.formtools.client.listener.DataLoadListener;
import org.openxdata.formtools.client.listener.FormDataDeleteListener;
import org.openxdata.formtools.client.listener.FormDataListener;
import org.openxdata.formtools.client.util.FormUtil;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormDataController implements FormDataListener, DataLoadListener {

	private int loadCount = 0;
	private String xformXml;
	private String layoutXml;
	private String javaScriptSrc;
	private String modelXml;


	public void onOpenFormData(String id){
		loadCount = 0;

		FormUtil.dlg.setText("Loading Form");
		FormUtil.dlg.center();

		FormEntryContext.setFormDataId(id);
		FormEntryContext.getDatabaseManager().loadFormData(id, this);
	}

	public void onDeleteFormData(String id, FormDataDeleteListener listener){
		new FormDataDeleteCmd(id, FormEntryContext.getFormDefId(), listener);
	}

	public void onDataReceived(String data){
		loadCount++;

		if(loadCount == 1){
			modelXml = data;
			FormEntryContext.getDatabaseManager().loadFormDef(FormEntryContext.getFormDefId(), this);
		}
		else if(loadCount == 2){
			xformXml = data;
			FormEntryContext.getDatabaseManager().loadFormLayout(FormEntryContext.getFormDefId(), this);
		}
		else if(loadCount == 3){
			layoutXml = data;
			FormEntryContext.getDatabaseManager().loadFormJavaScript(FormEntryContext.getFormDefId(), this);
		}
		else{
			assert(loadCount == 4);

			javaScriptSrc = data;
			FormEntryContext.setCurrentFormData(xformXml, layoutXml, javaScriptSrc, modelXml);
		}
	}
}
