package org.openxdata.forms.client.controller;


/**
 * Interface for notification whenever the list of locales changes.
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface ILocaleListChangeListener {
	
	/**
	 * Called whenever the locale list changes.
	 */
	void onLocaleListChanged();
}
