package org.openxdata.forms.client.listener;

import java.util.List;

import org.openxdata.forms.client.model.KeyValue;


/**
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface FormListSelectionListener {
	void onFormListSelected(List<KeyValue> formList);
}
