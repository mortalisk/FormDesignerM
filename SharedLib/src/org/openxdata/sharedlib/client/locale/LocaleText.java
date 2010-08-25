package org.openxdata.sharedlib.client.locale;

import com.google.gwt.i18n.client.Dictionary;


/**
 * Used for getting localized text messages.
 * <p>
 * This option of setting localized messages as a javascript object in the html host file
 * has been chosen because it does not require the form designer to be compiled into
 * various languages, which would be required if we had used the other method of
 * localization in GWT. 
 * </p>
 * The html host file holding the widget will always have text for
 * one locale. When the user switched to another locale, the page has to be reloaded such
 * that the server replaces this text with that of the new locale.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class LocaleText {

	/**
	 * The dictionary having all localized text.
	 */
	private static Dictionary formtoolsText = Dictionary.getDictionary("OpenXdataText");
	
	/**
	 * Gets the localized text for a given key.
	 * 
	 * @param key the key
	 * @return the localized text.
	 */
	public static String get(String key){
		return formtoolsText.get(key);
	}
}
