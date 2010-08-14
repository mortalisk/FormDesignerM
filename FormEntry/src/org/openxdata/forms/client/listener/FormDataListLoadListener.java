package org.openxdata.forms.client.listener;

import java.util.List;

import org.openxdata.forms.client.model.FormDataHeader;

public interface FormDataListLoadListener {
	void onFormDataListLoaded(List<FormDataHeader> dataList);
}
