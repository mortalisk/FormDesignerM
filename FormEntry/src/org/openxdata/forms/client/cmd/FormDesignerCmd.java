package org.openxdata.forms.client.cmd;

import org.openxdata.forms.client.FormDesignerWidget;
import org.openxdata.forms.client.FormEntryContext;
import org.openxdata.forms.client.controller.IFormSaveListener;

import com.google.gwt.user.client.Window;

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
