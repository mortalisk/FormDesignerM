package org.openxdata.forms.client.listener;

import java.util.List;

import org.openxdata.forms.client.model.KeyValue;

public interface FormDefListChangeListener {
	void onFormDefListChanged(List<KeyValue> formList);
}
