package org.openxdata.formtools.client.listener;

import java.util.List;

import org.openxdata.formtools.client.model.KeyValue;

/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */

public interface FormDefListChangeListener {
	void onFormDefListChanged(List<KeyValue> formList);
}
