package org.openxdata.designer.client.controller;

import org.openxdata.sharedlib.client.model.FormDef;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */

public interface ICenterPanel {

	void commitChanges();
	String getJavaScriptSource();
	FormDef getFormDef();
}
