package org.openxdata.entry.client.listener;

/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */

public interface FormDataListener {

	void onOpenFormData(String id);
	void onDeleteFormData(String id, FormDataDeleteListener listener);
}
