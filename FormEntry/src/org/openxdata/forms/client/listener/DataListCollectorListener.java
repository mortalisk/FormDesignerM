package org.openxdata.forms.client.listener;

import java.util.HashMap;
import java.util.List;


/**
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface DataListCollectorListener {
	void onDataListCollected(List<String> dataList, HashMap<String, String> dataDefMap);
}
