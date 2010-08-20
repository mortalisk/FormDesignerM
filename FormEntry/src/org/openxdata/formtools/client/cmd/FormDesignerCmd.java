package org.openxdata.formtools.client.cmd;

import org.openxdata.formtools.client.FormDesignerWidget;
import org.openxdata.formtools.client.FormEntryContext;
import org.openxdata.formtools.client.controller.IFormSaveListener;

import com.google.gwt.user.client.Window;

/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */

public class FormDesignerCmd implements IFormSaveListener{
	
	public FormDesignerCmd(FormDesignerWidget formDesigner){
		formDesigner.setFormSaveListener(this);
	}
	
	
	public boolean onSaveForm(int formId, String xformsXml, String layoutXml, String javaScriptSrc){
		
		FormEntryContext.getDatabaseManager().saveFormDef(FormEntryContext.getFormDefId(), xformsXml);
		FormEntryContext.getDatabaseManager().saveFormLayout(FormEntryContext.getFormDefId(), layoutXml);
		FormEntryContext.getDatabaseManager().saveFormJavaScript(FormEntryContext.getFormDefId(), javaScriptSrc);
		
		return true;
	}
	
	
	public void onSaveLocaleText(int formId, String xformsLocaleText, String layoutLocaleText){
		FormEntryContext.getDatabaseManager().saveXformLocaleText(FormEntryContext.getFormDefId(), xformsLocaleText);
		FormEntryContext.getDatabaseManager().saveLayoutLocaleText(FormEntryContext.getFormDefId(), layoutLocaleText);
		
		Window.alert("Form Saved Successfully.");
	}
}
