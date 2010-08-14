package org.openxdata.forms.client.listener;

import java.util.List;

import org.openxdata.forms.client.model.KeyValue;

public interface FormDefDownloadListener {
	
	void onFormDefDownloaded(String id);
	void onFormDefListDownloaded(List<KeyValue> formList);
}
