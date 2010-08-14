package org.openxdata.forms.client.listener;

import org.openxdata.forms.client.model.FormDataHeader;

public interface FormSubmitCancelListener {
	
	void onFormCancelled();
	void onNewFormSubmitted(FormDataHeader formDataHeader);
	void onExistingFormSubmitted(FormDataHeader formDataHeader);
}
