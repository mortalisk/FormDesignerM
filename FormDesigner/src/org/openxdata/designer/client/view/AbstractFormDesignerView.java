package org.openxdata.designer.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class AbstractFormDesignerView extends Composite {
	
	/** 
	 * Given some UI component that contains other components, recursively disables or 
	 * enables that component and its children, subject to whether each component
	 * supports the "enabled" property (i.e. implements HasEnabled).
	 * 
	 * If some components do not support the enabled property, that will not stop those that 
	 * do from being processed.
	 *  
	 * @param parent	The parent object to enable or disable.
	 * @param enabled	If true, parent and children will be enabled. If false, they will be disabled.
	 */
	protected void setAllEnabled(HasWidgets parent, boolean enabled){
		
		if (parent instanceof HasEnabled)
			((HasEnabled) parent).setEnabled(enabled);
		
		for (Widget w : parent){
			
			if (w instanceof HasWidgets)
				setAllEnabled((HasWidgets) w, enabled);
			else if (w instanceof HasEnabled)
				((HasEnabled) w).setEnabled(enabled);
		}
	}
}
