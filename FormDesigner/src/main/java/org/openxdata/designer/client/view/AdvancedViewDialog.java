package org.openxdata.designer.client.view;

import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.xforms.XformUtil;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;

public class AdvancedViewDialog extends DialogBox {

	/**
	 * Tab widget housing the contents.
	 */
	private TabLayoutPanel tabs = new TabLayoutPanel(1.5, Unit.EM);

	/**
	 * TextArea displaying the XForms xml.
	 */
	private TextArea txtXformsSource = new TextArea();

	/** The text area which contains javascript source. */
	private TextArea txtJavaScriptSource = new TextArea();

	/** The text area which contains layout xml. */
	private TextArea txtLayoutXml = new TextArea();

	/** The text area which contains model xml. */
	private TextArea txtModelXml = new TextArea();

	/** The text area which contains locale or language xml. */
	private TextArea txtLanguageXml = new TextArea();

	private FormDef formDef;

	private PushButton btnClose;	

	public AdvancedViewDialog(FormDef formDef) {

		this.formDef = formDef;

		
		tabs.add(txtXformsSource, "Form XML");
		tabs.add(txtLayoutXml, "Layout XML");
		tabs.add(txtLanguageXml, "Language XML");
		tabs.add(txtModelXml, "Model XML");
		tabs.add(txtJavaScriptSource, "Javascript");

		setViewSource();

		btnClose = new PushButton("Close");
		btnClose.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				close();

			}
		});

		tabs.add(btnClose);
		setWidget(tabs);
		FormUtil.maximizeWidget(tabs);

	}

	protected void close() {
		this.hide();

	}

	private void setViewSource() {
		if (formDef != null) {

			String modelXML = XformUtil.getInstanceDataDoc(formDef.getDoc())
					.toString();

			txtXformsSource.setText(formDef.getXformXml());
			txtLayoutXml.setText(formDef.getLayoutXml());
			txtLanguageXml.setText(formDef.getLanguageXml());
			txtModelXml.setText(modelXML);
			txtJavaScriptSource.setText(formDef.getJavaScriptSource());

		}

	}

}
