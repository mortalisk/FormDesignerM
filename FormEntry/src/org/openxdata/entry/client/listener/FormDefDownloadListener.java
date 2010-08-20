package org.openxdata.entry.client.listener;

import java.util.List;

import org.openxdata.entry.client.model.KeyValue;

/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */

public interface FormDefDownloadListener {
	
	void onFormDefDownloaded(String id);
	void onFormDefListDownloaded(List<KeyValue> formList);
}
