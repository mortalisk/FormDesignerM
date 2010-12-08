package org.openxdata.designer.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class AbstractFormDesignerView extends Composite {
	
	public interface HasEnabled {
		public void setEnabled(boolean enabled);
	}
	
	/** Recursively visits all widgets in a panel and its sub-panels, 
	 *  and enables or disables any that support the "enabled" property (i.e. 
	 *  those that extend FocusWidget).
	 *  
	 * @param p			The panel to enable or disable
	 * @param enabled	If true, the panel will be enabled. If false, the panel will be disabled.
	 */
	protected void setAllEnabled(HasWidgets parent, boolean enabled){
		
		for (Widget w : parent){
			
			if (w instanceof HasWidgets)
				setAllEnabled((HasWidgets) w, enabled);
			else if (w instanceof FocusWidget)
				((FocusWidget) w).setEnabled(enabled);
			else if (w instanceof HasEnabled)
				((HasEnabled) w).setEnabled(enabled);
		}
	}
}
