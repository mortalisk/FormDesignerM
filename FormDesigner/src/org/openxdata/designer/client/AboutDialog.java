
package org.openxdata.designer.client;

import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.sharedlib.client.locale.FormsConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This is formtools about dialog box.
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 * 
 */
public class AboutDialog extends DialogBox {
	
	private FormsConstants constants = GWT.create(FormsConstants.class);

	public AboutDialog() {
		// Use this opportunity to set the dialog's caption.
		setText(constants.about()+" " + FormDesignerUtil.getTitle());

		// Create a VerticalPanel to contain the 'about' label and the 'OK' button.
		VerticalPanel outer = new VerticalPanel();

		// Create the 'about' text and set a style name so we can style it with CSS.

		HTML text = new HTML(constants.aboutMessage());
		text.setStyleName("formDesigner-AboutText");
		outer.add(text);

		// Create the 'OK' button, along with a listener that hides the dialog
		// when the button is clicked.
		Button btn = new Button(constants.close(), new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		outer.add(btn);
		outer.setCellHorizontalAlignment(btn, HasAlignment.ALIGN_CENTER);

		setWidget(outer);
	}

	@Override
	public boolean onKeyDownPreview(char key, int modifiers) {
		// Use the popup's key preview hooks to close the dialog when either
		// enter or escape is pressed.
		switch (key) {
		case KeyCodes.KEY_ENTER:
		case KeyCodes.KEY_ESCAPE:
			hide();
			break;
		}

		return true;
	}
}
