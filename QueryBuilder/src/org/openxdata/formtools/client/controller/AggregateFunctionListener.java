package org.openxdata.formtools.client.controller;

import com.google.gwt.user.client.ui.Widget;

/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface AggregateFunctionListener {

	public void onSum(Widget sender);
	public void onAverage(Widget sender);
	public void onMinimum(Widget sender);
	public void onMaximum(Widget sender);
	public void onCount(Widget sender);
}
