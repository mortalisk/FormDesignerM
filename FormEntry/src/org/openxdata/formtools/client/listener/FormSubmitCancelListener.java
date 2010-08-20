package org.openxdata.formtools.client.listener;

import org.openxdata.formtools.client.model.FormDataHeader;

/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */

public interface FormSubmitCancelListener {
	
	void onFormCancelled();
	void onNewFormSubmitted(FormDataHeader formDataHeader);
	void onExistingFormSubmitted(FormDataHeader formDataHeader);
}
