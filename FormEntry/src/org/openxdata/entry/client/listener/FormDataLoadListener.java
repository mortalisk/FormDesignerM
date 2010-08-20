package org.openxdata.entry.client.listener;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */

public interface FormDataLoadListener {
	void onFormDataLoaded(String xformXml, String layoutXml, String javaScriptSrc, String modelXml);
}
