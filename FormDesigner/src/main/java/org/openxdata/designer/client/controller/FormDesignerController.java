package org.openxdata.designer.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openxdata.designer.client.CenterPanel;
import org.openxdata.designer.client.Context;
import org.openxdata.designer.client.DesignerMessages;
import org.openxdata.designer.client.LeftPanel;
import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.designer.client.util.ItextBuilder;
import org.openxdata.designer.client.util.ItextParser;
import org.openxdata.designer.client.util.LanguageUtil;
import org.openxdata.designer.client.view.AdvancedViewDialog;
import org.openxdata.designer.client.view.FormsTreeView;
import org.openxdata.designer.client.view.LocalesDialog;
import org.openxdata.designer.client.xforms.OpenXdataFormBuilder;
import org.openxdata.designer.client.xforms.XhtmlBuilder;
import org.openxdata.sharedlib.client.OpenXdataConstants;
import org.openxdata.sharedlib.client.controller.OpenFileDialogEventListener;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.Locale;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.view.LoginDialog;
import org.openxdata.sharedlib.client.view.OpenFileDialog;
import org.openxdata.sharedlib.client.view.SaveFileDialog;
import org.openxdata.sharedlib.client.xforms.XformBuilder;
import org.openxdata.sharedlib.client.xforms.XformParser;
import org.openxdata.sharedlib.client.xforms.XformUtil;
import org.openxdata.sharedlib.client.xforms.XmlUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import org.openxdata.sharedlib.client.model.QuestionType;

/**
 * Controls the interactions between the menu, tool bar and various views (eg
 * Left and Center panels) for the form designer.
 * 
 * www.openxdata.org - Licensed as written in license.txt and original sources
 * of this file and its authors are found in sources.txt.
 * 
 */
public class FormDesignerController implements IFormDesignerListener,
		OpenFileDialogEventListener {

	/** The panel on the right hand side of the form designer. */
	private CenterPanel centerPanel;

	/** The panel on the left hand side of the form designer. */
	private LeftPanel leftPanel;

	/**
	 * The listener to form save events.
	 */
	private IFormSaveListener formSaveListener;

	/**
	 * The identifier of the loaded or opened form.
	 */
	private Integer formId;

	/** No current action. */
	private static final byte CA_NONE = 0;

	/** Action for loading a form definition. */
	private static final byte CA_LOAD_FORM = 1;

	/** Action for saving form. */
	private static final byte CA_SAVE_FORM = 2;

	/** Action for refreshing a form. */
	private static final byte CA_REFRESH_FORM = 3;

	/** Action for setting file contents. */
	private static final byte CA_SET_FILE_CONTENTS = 4;

	/**
	 * The current action by the time to try to authenticate the user at the
	 * server.
	 */
	private static byte currentAction = CA_NONE;

	/**
	 * The dialog box used to log on the server when the user's session expires
	 * on the server.
	 */
	private static LoginDialog loginDlg = new LoginDialog();

	/**
	 * Static self reference such that the static login call back can have a
	 * reference to proceed with the current action.
	 */
	private static FormDesignerController controller;

	/** The object that is being refreshed. */
	private Object refreshObject;

	private boolean saveAsMode = false;

	final DesignerMessages messages = GWT.create(DesignerMessages.class);

	/**
	 * Constructs a new instance of the form designer controller.
	 * 
	 * @param centerPanel
	 *            the right hand side panel.
	 * @param leftPanel
	 *            the left hand side panel.
	 */
	public FormDesignerController(CenterPanel centerPanel, LeftPanel leftPanel) {
		this.leftPanel = leftPanel;
		this.centerPanel = centerPanel;

		this.centerPanel.setFormDesignerListener(this);

		controller = this;
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#addNewItem()
	 */
	public void addNewItem() {
		leftPanel.addNewItem();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#addNewChildItem()
	 */
	public void addNewChildItem() {
		leftPanel.addNewChildItem();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#addNewQuestion()
	 */
	public void addNewQuestion(QuestionType dataType) {
		leftPanel.addNewQuestion(dataType);
	}

	public void addNewPage() {
		leftPanel.addNewPage();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#printForm()
	 */
	public void printForm() {
		FormDef formDef = centerPanel.getFormDef();
		if (formDef != null)
			openForm(formDef.getName(), centerPanel.getFormInnerHtml());
	}

	/**
	 * Opens a new browser window with a given title and html contents.
	 * 
	 * @param title
	 *            the window title.
	 * @param html
	 *            the window html contents.
	 */
	public static native void openForm(String title, String html) /*-{
		var win =window.open('','formtools','width=350,height=250,menubar=1,toolbar=1,status=1,scrollbars=1,resizable=1');
		win.document.open("text/html","replace");
		win.document.writeln('<html><head><title>' + title + '</title></head><body bgcolor=white onLoad="self.focus()">'+html+'</body></html>');
		win.document.close();
	}-*/;

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#closeForm()
	 */
	public void closeForm() {
		String url = FormUtil.getCloseUrl();
		if (url != null && url.trim().length() > 0)
			Window.Location.replace(url);
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#deleteSelectedItems()
	 */
	public void deleteSelectedItem() {
		if (Context.getCurrentMode() != Context.MODE_DESIGN)
			leftPanel.deleteSelectedItem();
		else
			centerPanel.deleteSelectedItem();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveItemDown()
	 */
	public void moveItemDown() {
		leftPanel.moveItemDown();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveItemUp()
	 */
	public void moveItemUp() {
		leftPanel.moveItemUp();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#newForm()
	 */
	public void newForm() {
		if (isOfflineMode())
			leftPanel.addNewForm();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#showAboutInfo()
	 */
	public void openForm() {
		String xml = centerPanel.getXformsSource();

		// Only load layout if in layout mode and no xforms source is supplied.
		if (centerPanel.isInLayoutMode()
				&& (xml == null || xml.trim().length() == 0)) {
			xml = centerPanel.getLayoutXml();
			if (xml == null || xml.trim().length() == 0) {
				OpenFileDialog dlg = new OpenFileDialog(this,
						FormUtil.getFileOpenUrl());
				dlg.center();
			}
		} else {
			// Whether in layout mode or not, as long as xforms source is
			// supplied, we load it.
			if (xml != null && xml.trim().length() > 0) {
				FormDef formDef = leftPanel.getSelectedForm();
				if (formDef != null)
					refreshFormDeffered();
				else
					openFormDeffered(isOfflineMode() ? ModelConstants.NULL_ID
							: formId, false);
			} else {
				OpenFileDialog dlg = new OpenFileDialog(this,
						FormUtil.getFileOpenUrl());
				dlg.center();
			}
		}
	}

	/**
	 * Loads a form from the Xforms Source tab, in a deferred command.
	 * 
	 * @param id
	 *            the form identifier.
	 * @param readonly
	 *            set to true to load the form in read only mode.
	 */
	public void openFormDeffered(int id, boolean readonly) {
		final int tempFormId = id;
		final boolean tempReadonly = readonly;

		FormUtil.dlg.setText(messages.openingForm());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {

				try {
					prepareFormForLoading(tempFormId, tempReadonly);
					FormUtil.dlg.hide();
				} catch (Exception ex) {
					FormUtil.displayException(ex);
				}
			}
		});
	}

	private void prepareFormForLoading(final int tempFormId,
			final boolean tempReadonly) {
		String xml = centerPanel.getXformsSource().trim();
		if (xml.length() > 0) {
			Document doc = ItextParser.parse(xml);
			FormDef formDef = XformParser.fromXform2FormDef(doc, xml,
					Context.getLanguageText());
			formDef.setReadOnly(tempReadonly);

			if (tempFormId != ModelConstants.NULL_ID)
				formDef.setId(tempFormId);

			if (formDef.getLayoutXml() != null)
				centerPanel.setLayoutXml(formDef.getLayoutXml(), false);
			else {
				// Could be from form runner which puts these
				// contents in center panel
				// because it does not yet have a formdef by the
				// time it has this data.
				formDef.setXformXml(centerPanel.getXformsSource());
				formDef.setLayoutXml(centerPanel.getLayoutXml());
			}

			if (formDef.getJavaScriptSource() != null)
				centerPanel.setJavaScriptSource(formDef.getJavaScriptSource());
			else
				formDef.setJavaScriptSource(centerPanel.getJavaScriptSource());

			// TODO May also need to refresh UI if form was not
			// stored in default lang.
			HashMap<String, String> locales = Context.getLanguageText().get(
					formDef.getId());
			if (locales != null) {
				formDef.setLanguageXml(FormUtil.formatXml(locales.get(Context
						.getLocale())));
				centerPanel.setLanguageXml(formDef.getLanguageXml(), false);
			}

			leftPanel.loadForm(formDef);
			centerPanel.loadForm(formDef, formDef.getLayoutXml());
			centerPanel.format();
		}
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#openFormLayout()
	 */
	public void openFormLayout() {
		openFormLayout(true);
	}

	/**
	 * Loads the form widget layout in the Layout Xml tab.
	 * 
	 * @param selectTabs
	 *            set to true to select the layout xml tab.
	 */
	public void openFormLayout(boolean selectTabs) {
		openFormLayoutDeffered(selectTabs);
	}

	/**
	 * Loads the form widget layout in the Layout Xml tab, in a deferred
	 * command.
	 * 
	 * @param selectTabs
	 *            set to true to select the layout xml tab.
	 */
	public void openFormLayoutDeffered(boolean selectTabs) {
		final boolean selectTbs = selectTabs;

		FormUtil.dlg.setText(messages.openingFormLayout());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				try {
					centerPanel.openFormLayout(selectTbs);
					FormUtil.dlg.hide();
				} catch (Exception ex) {
					FormUtil.displayException(ex);
				}
			}
		});
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#saveForm()
	 */
	public void saveForm() {
		if (isOfflineMode())
			saveTheForm();
		else {
			currentAction = CA_SAVE_FORM;
			FormUtil.isAuthenticated();
		}
	}

	private void saveTheForm() {

		final boolean localSaveAsMode = saveAsMode;
		saveAsMode = false;

		final FormDef obj = leftPanel.getSelectedForm();
		if (obj == null) {
			Window.alert(messages.selectSaveItem());
			return;
		}

		if (obj.isReadOnly()) {
			return;
			// TODO I think we should allow saving of form text and layout
		}

		if (!leftPanel.isValidForm())
			return;

		if (Context.inLocalizationMode()) {
			saveLanguageText(formSaveListener == null && isOfflineMode());
			return;
		}

		FormUtil.dlg.setText(messages.savingForm());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				try {
					prepareFormForSaving(localSaveAsMode, obj);
				} catch (Exception ex) {
					FormUtil.displayException(ex);
					return;
				}

			}
		});
	}

	private void prepareFormForSaving(final boolean localSaveAsMode,
			final FormDef obj) {

		centerPanel.commitChanges();

		// TODO Need to preserve user's model and any others.
		String xml = null;
		FormDef formDef = obj;
		if (formDef.getDoc() == null) {
			if (FormUtil.isJavaRosaSaveFormat())
				xml = XhtmlBuilder.fromFormDef2Xhtml(formDef);
			else
				xml = XformBuilder.fromFormDef2Xform(formDef);
		} else {
			formDef.updateDoc(false);

			if (FormUtil.isJavaRosaSaveFormat())
				ItextBuilder.build(formDef);

			xml = XmlUtil.fromDoc2String(formDef.getDoc());
		}

		xml = XformUtil.normalizeNameSpace(formDef.getDoc(), xml);

		xml = FormDesignerUtil.formatXml(xml);

		formDef.setXformXml(xml);
		centerPanel.setXformsSource(xml, formSaveListener == null
				&& isOfflineMode());
		centerPanel.buildLayoutXml();

		centerPanel.saveLanguageText(false);
		setLocaleText(formDef.getId(), Context.getLocale().getKey(),
				centerPanel.getLanguageXml());

		centerPanel.saveJavaScriptSource();

		if (!isOfflineMode() && formSaveListener == null)
			saveForm(xml, centerPanel.getLayoutXml(),
					OpenXdataFormBuilder.getCombinedLanguageText(Context
							.getLanguageText().get(formDef.getId())),
					centerPanel.getJavaScriptSource());

		boolean saveLocaleText = false;
		if (formSaveListener != null)
			saveLocaleText = formSaveListener.onSaveForm(formDef.getId(), xml,
					centerPanel.getLayoutXml(),
					centerPanel.getJavaScriptSource());

		if (isOfflineMode() || formSaveListener != null)
			FormUtil.dlg.hide();

		// Save text for the current language
		if (saveLocaleText)
			saveTheLanguageText(false, false);

		if (localSaveAsMode)
			saveAs();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#saveFormAs()
	 */
	public void saveFormAs() {
		saveAsMode = true;
		saveForm();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#saveFormLayout()
	 */
	public void saveFormLayout() {
		FormUtil.dlg.setText(messages.savingFormLayout());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				try {
					centerPanel.saveFormLayout();
					FormUtil.dlg.hide();
				} catch (Exception ex) {
					FormUtil.displayException(ex);
				}

			}
		});
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#alignLeft()
	 */
	public void showHelpContents() {
		// TODO do nothing

	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#showLanguages()
	 */
	public void showLanguages() {
		LocalesDialog dlg = new LocalesDialog();
		dlg.center();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#showOptions()
	 */
	public void showOptions() {
		// do nothing

	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#viewToolbar()
	 */
	public void viewToolbar() {
		// do nothing
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#alignLeft()
	 */
	public void alignLeft() {
		centerPanel.alignLeft();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#alignRight()
	 */
	public void alignRight() {
		centerPanel.alignRight();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#alignTop()
	 */
	public void alignTop() {
		centerPanel.alignTop();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#alignBottom()
	 */
	public void alignBottom() {
		centerPanel.alignBottom();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#makeSameHeight()
	 */
	public void makeSameHeight() {
		centerPanel.makeSameHeight();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#makeSameSize()
	 */
	public void makeSameSize() {
		centerPanel.makeSameSize();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#makeSameWidth()
	 */
	public void makeSameWidth() {
		centerPanel.makeSameWidth();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#copyItem()
	 */
	public void copyItem() {
		if (!Context.isStructureReadOnly()) {
			if (Context.getCurrentMode() == Context.MODE_QUESTION_PROPERTIES)
				leftPanel.copyItem();
			else
				centerPanel.copyItem();
		}
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#cutItem()
	 */
	public void cutItem() {
		if (!Context.isStructureReadOnly()) {
			if (Context.getCurrentMode() == Context.MODE_QUESTION_PROPERTIES)
				leftPanel.cutItem();
			else
				centerPanel.cutItem();
		}
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#pasteItem()
	 */
	public void pasteItem() {
		if (!Context.isStructureReadOnly()) {
			if (Context.getCurrentMode() == Context.MODE_QUESTION_PROPERTIES)
				leftPanel.pasteItem();
			else
				centerPanel.pasteItem();
		}
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#refreshItem()
	 */
	public void refreshItem() {
		if (!Context.isStructureReadOnly())
			leftPanel.refreshItem();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormDesignerController#format()
	 */
	public void format() {
		centerPanel.format();
	}

	private void refreshObject() {

		// If the center panel's current mode does not allow refreshes
		// or the forms tree view is the one which has requested a refresh.
		if (!centerPanel.allowsRefresh()
				|| refreshObject instanceof FormsTreeView
				|| Context.getCurrentMode() == Context.MODE_XFORMS_SOURCE) {

			// TODO This controller should not know about LeftPanel
			// implementation details.

			if (formId != null) {
				FormUtil.dlg.setText(messages.refreshingForm());
				FormUtil.dlg.center();

				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						refreshForm();
						FormUtil.dlg.hide();

					}
				});
			} else
				refreshFormDeffered();
		} else {
			centerPanel.refresh();
			leftPanel.refresh();
		}
	}

	public void refresh(Object sender) {
		refreshObject = sender;

		if (isOfflineMode())
			refreshObject();
		else {
			currentAction = CA_REFRESH_FORM;
			FormUtil.isAuthenticated();
		}
	}

	/**
	 * Loads a form from the server.
	 */
	private void loadForm() {
		FormUtil.dlg.setText(messages.openingForm());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				String url = FormUtil.getHostPageBaseURL();
				url += FormUtil.getFormDefDownloadUrlSuffix();
				url += FormUtil.getFormIdName() + "=" + formId;
				
				RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
						URL.encode(url));
				
				try {
					prepareFormForLoading(builder);
				} catch (RequestException ex) {
					FormUtil.displayException(ex);
				}

			}});
	}

	private void prepareFormForLoading(RequestBuilder builder)
			throws RequestException {
		builder.sendRequest(null, new RequestCallback() {
			public void onResponseReceived(Request request, Response response) {

				if (response.getStatusCode() != Response.SC_OK) {
					FormUtil.displayReponseError(response);
					return;
				}

				String xml = response.getText();
				if (xml == null || xml.length() == 0) {
					FormUtil.dlg.hide();
					Window.alert(messages.noDataFound());
					return;
				}

				String xformXml, layoutXml = null, localeXml = null, javaScriptSrc = null;

				int pos = xml
						.indexOf(OpenXdataConstants.OPENXDATA_FORMDEF_LAYOUT_XML_SEPARATOR);
				int pos2 = xml
						.indexOf(OpenXdataConstants.OPENXDATA_FORMDEF_LOCALE_XML_SEPARATOR);
				int pos3 = xml
						.indexOf(OpenXdataConstants.OPENXDATA_FORMDEF_JAVASCRIPT_SRC_SEPARATOR);
				if (pos > 0) {
					xformXml = xml.substring(0, pos);
					layoutXml = FormUtil.formatXml(xml.substring(pos + OpenXdataConstants.OPENXDATA_FORMDEF_LAYOUT_XML_SEPARATOR.length(), (pos2 > 0 ? pos2	: (pos3 > 0 ? pos3 : xml.length()))));

					if (pos2 > 0)
						localeXml = FormUtil.formatXml(xml.substring(pos2 + OpenXdataConstants.OPENXDATA_FORMDEF_LOCALE_XML_SEPARATOR.length(), pos3 > 0 ? pos3 : xml.length()));

					if (pos3 > 0)
						javaScriptSrc = xml.substring(pos3 + OpenXdataConstants.OPENXDATA_FORMDEF_JAVASCRIPT_SRC_SEPARATOR.length(), xml.length());
				} else if (pos2 > 0) {
					xformXml = xml.substring(0, pos2);
					localeXml = FormUtil.formatXml(xml.substring(pos2+ OpenXdataConstants.OPENXDATA_FORMDEF_LOCALE_XML_SEPARATOR.length(), pos3 > 0 ? pos3 : xml.length()));

					if (pos3 > 0)
						javaScriptSrc = xml.substring(pos3 + OpenXdataConstants.OPENXDATA_FORMDEF_JAVASCRIPT_SRC_SEPARATOR.length(), xml.length());
				} else if (pos3 > 0) {
					xformXml = xml.substring(0, pos3);
					javaScriptSrc = xml.substring(pos3 + OpenXdataConstants.OPENXDATA_FORMDEF_JAVASCRIPT_SRC_SEPARATOR.length(), xml.length());
				} else
					xformXml = xml;

				LanguageUtil.loadLanguageText(formId, localeXml,
						Context.getLanguageText());

				centerPanel.setXformsSource(FormUtil.formatXml(xformXml), false);
				centerPanel.setLayoutXml(layoutXml, false);
				centerPanel.setJavaScriptSource(javaScriptSrc);

				openFormDeffered(formId, false);

			}

			@Override
			public void onError(Request request, Throwable exception) {
				FormUtil.displayException(exception);

			}

		});
	}

	/**
	 * Loads or opens a form with a given id.
	 * 
	 * @param frmId
	 *            the form id.
	 */
	public void loadForm(int frmId) {
		this.formId = frmId;

		if (isOfflineMode())
			loadForm();
		else {
			currentAction = CA_LOAD_FORM;
			FormUtil.isAuthenticated();
		}
	}

	public void saveForm(String xformXml, String layoutXml, String languageXml,
			String javaScriptSrc) {
		String url = FormUtil.getHostPageBaseURL();
		url += FormUtil.getFormDefUploadUrlSuffix();
		url += FormUtil.getFormIdName() + "=" + this.formId;

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,
				URL.encode(url));

		try {
			String xml = xformXml;
			if (layoutXml != null && layoutXml.trim().length() > 0)
				xml += OpenXdataConstants.OPENXDATA_FORMDEF_LAYOUT_XML_SEPARATOR
						+ layoutXml;

			if (languageXml != null && languageXml.trim().length() > 0)
				xml += OpenXdataConstants.OPENXDATA_FORMDEF_LOCALE_XML_SEPARATOR
						+ languageXml;

			if (javaScriptSrc != null && javaScriptSrc.trim().length() > 0)
				xml += OpenXdataConstants.OPENXDATA_FORMDEF_JAVASCRIPT_SRC_SEPARATOR
						+ javaScriptSrc;

			builder.sendRequest(xml, new RequestCallback() {
				public void onResponseReceived(Request request,
						Response response) {

					if (response.getStatusCode() != Response.SC_OK) {
						FormUtil.displayReponseError(response);
						return;
					}

					FormUtil.dlg.hide();
					Window.alert(messages.formSaveSuccess());
				}

				public void onError(Request request, Throwable exception) {
					FormUtil.displayException(exception);
				}
			});
		} catch (RequestException ex) {
			FormUtil.displayException(ex);
		}
	}

	public void saveLocaleText(String languageXml) {
		String url = FormUtil.getHostPageBaseURL();
		url += FormUtil.getFormDefUploadUrlSuffix();
		url += FormUtil.getFormIdName() + "=" + this.formId;
		url += "&localeXml=true";

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST,
				URL.encode(url));

		try {
			builder.sendRequest(languageXml, new RequestCallback() {
				public void onResponseReceived(Request request,
						Response response) {

					if (response.getStatusCode() != Response.SC_OK) {
						FormUtil.displayReponseError(response);
						return;
					}

					FormUtil.dlg.hide();
					Window.alert(messages.formSaveSuccess());
				}

				public void onError(Request request, Throwable exception) {
					FormUtil.displayException(exception);
				}
			});
		} catch (RequestException ex) {
			FormUtil.displayException(ex);
		}
	}

	/**
	 * Checks if the form designer is in offline mode.
	 * 
	 * @return true if in offline mode, else false.
	 */
	public boolean isOfflineMode() {
		return formId == null;
	}

	private void refreshForm() {
		String url = FormUtil.getHostPageBaseURL();
		url += FormUtil.getFormDefRefreshUrlSuffix();
		url += FormUtil.getFormIdName() + "=" + this.formId;

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				URL.encode(url));

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onResponseReceived(Request request,
						Response response) {

					if (response.getStatusCode() != Response.SC_OK) {
						FormUtil.displayReponseError(response);
						return;
					}

					String xml = response.getText();
					if (xml == null || xml.length() == 0) {
						Window.alert(messages.noDataFound());
						return;
					}

					centerPanel.setXformsSource(xml, false);
					refreshFormDeffered();
				}

				public void onError(Request request, Throwable exception) {
					FormUtil.displayException(exception);
				}
			});
		} catch (RequestException ex) {
			FormUtil.displayException(ex);
		}
	}

	/**
	 * Refreshes the selected from in a deferred command.
	 */
	private void refreshFormDeffered() {
		FormUtil.dlg.setText(messages.refreshingForm());
		FormUtil.dlg.center();
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				try {
					String xml = centerPanel.getXformsSource();
					if (xml != null && xml.trim().length() > 0) {
						FormDef formDef = XformParser.fromXform2FormDef(xml);

						FormDef oldFormDef = centerPanel.getFormDef();

						formDef.refresh(oldFormDef);

						formDef.updateDoc(false);
						xml = formDef.getDoc().toString();

						formDef.setXformXml(FormUtil.formatXml(xml));

						formDef.setLayoutXml(oldFormDef.getLayoutXml());
						formDef.setLanguageXml(oldFormDef.getLanguageXml());

						leftPanel.refresh(formDef);
						centerPanel.refresh();
					}
					FormUtil.dlg.hide();
				} catch (Exception ex) {
					FormUtil.displayException(ex);
				}
			}
		});
	}

	/**
	 * Sets the listener to form save events.
	 * 
	 * @param formSaveListener
	 *            the listener.
	 */
	public void setFormSaveListener(IFormSaveListener formSaveListener) {
		this.formSaveListener = formSaveListener;
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveUp()
	 */
	public void moveUp() {
		leftPanel.getFormActionListener().moveUp();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveDown()
	 */
	public void moveDown() {
		leftPanel.getFormActionListener().moveUp();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveToParent()
	 */
	public void moveToParent() {
		leftPanel.getFormActionListener().moveToParent();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveToChild()
	 */
	public void moveToChild() {
		leftPanel.getFormActionListener().moveToChild();
	}

	/**
	 * @see org.openxdata.sharedlib.client.controller.OpenFileDialogEventListener#onSetFileContents()
	 */
	public void onSetFileContents(String contents) {
		if (isOfflineMode())
			setFileContents();
		else {
			currentAction = CA_SET_FILE_CONTENTS;
			FormUtil.isAuthenticated();
		}
	}

	private void setFileContents() {

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
				FormUtil.getFileOpenUrl());

		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onResponseReceived(Request request,
						Response response) {

					if (response.getStatusCode() != Response.SC_OK) {
						FormUtil.displayReponseError(response);
						return;
					}

					String contents = response.getText();
					if (contents != null && contents.trim().length() > 0) {
						if (centerPanel.isInLayoutMode())
							centerPanel.setLayoutXml(contents, false);
						else {
							centerPanel.setXformsSource(contents, true);
							openForm();
						}
					}
				}

				public void onError(Request request, Throwable exception) {
					FormUtil.displayException(exception);
				}
			});
		} catch (RequestException ex) {
			FormUtil.displayException(ex);
		}
	}

	public void saveAs() {
		try {
			String data = (centerPanel.isInLayoutMode() ? centerPanel
					.getLayoutXml() : centerPanel.getXformsSource());
			if (data == null || data.trim().length() == 0)
				return;

			FormDef formDef = leftPanel.getSelectedForm();
			String fileName = "filename";
			if (formDef != null)
				fileName = formDef.getName();

			if (centerPanel.isInLayoutMode())
				fileName += "-" + messages.layout();

			SaveFileDialog dlg = new SaveFileDialog(FormUtil.getFileSaveUrl(),
					data, fileName);
			dlg.center();
		} catch (Exception ex) {
			FormUtil.displayException(ex);
		}
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormDesignerListener#openLanguageText()
	 */
	public void openLanguageText() {

		FormUtil.dlg.setText(messages.translatingFormLanguage());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				try {
					int selFormId = -1;
					String xml = null;
					FormDef formDef = leftPanel.getSelectedForm();
					if (formDef != null)
						selFormId = formDef.getId();

					List<FormDef> forms = leftPanel.getForms();
					if (forms != null && forms.size() > 0) {
						List<FormDef> newForms = new ArrayList<FormDef>();
						for (FormDef form : forms) {
							xml = getFormLocaleText(form.getId(), Context
									.getLocale().getKey());
							if (xml != null) {
								String xform = FormUtil.formatXml(LanguageUtil
										.translate(form.getDoc(), xml, true));
								FormDef newFormDef = XformParser
										.fromXform2FormDef(xform);
								newFormDef.setXformXml(xform);
								newFormDef.setLayoutXml(FormUtil.formatXml(LanguageUtil
										.translate(form.getLayoutXml(), xml,
												false)));
								newFormDef.setLanguageXml(xml);
								newForms.add(newFormDef);

								if (newFormDef.getId() == selFormId)
									formDef = newFormDef;
							} else {
								newForms.add(form);
								if (form.getId() == selFormId)
									formDef = form;
							}
						}

						leftPanel.loadForms(newForms, formDef.getId());
					}

					FormUtil.dlg.hide();

					String layoutXml = centerPanel.getLayoutXml();
					if (layoutXml != null && layoutXml.trim().length() > 0) {
						openFormLayout(false);

						if (Context.getCurrentMode() == Context.MODE_PREVIEW) {
							Scheduler.get().scheduleDeferred(new ScheduledCommand() {
								public void execute() {
									refreshObject();
								}
							});
						}
					} else if (Context.getCurrentMode() == Context.MODE_DESIGN) {
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {
							public void execute() {
								refreshObject();
							}
						});
					}
				} catch (Exception ex) {
					FormUtil.displayException(ex);
				}
			}
		});
	}

	/**
	 * Saves locale text for the selected form.
	 * 
	 * @param selectTab
	 *            set to true to select the language tab.
	 */
	public void saveLanguageText(boolean selectTab) {
		saveLanguageTextDeffered(selectTab);
	}

	/**
	 * Saves locale text for the selected form.
	 */
	public void saveLanguageText() {
		saveLanguageTextDeffered(true);
	}

	/**
	 * Saves locale text for the selected form, in a deferred command.
	 * 
	 * @param selectTab
	 *            set to true to select the language tab.
	 */
	public void saveLanguageTextDeffered(boolean selectTab) {
		final boolean selTab = selectTab;

		FormUtil.dlg.setText(messages.savingLanguageText());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				saveTheLanguageText(selTab, true);
			}
		});
	}

	/**
	 * Saves locale text for the selected form, in a non deferred command.
	 * 
	 * @param selectTab
	 *            set to true to select the language tab.
	 * @param rebuild
	 *            set to true to rebuild the language xml.
	 */
	public void saveTheLanguageText(boolean selTab, boolean rebuild) {
		try {
			FormDef formDef = centerPanel.getFormDef();

			if (rebuild) {
				centerPanel.saveLanguageText(selTab);
				setLocaleText(formDef.getId(), Context.getLocale().getKey(),
						centerPanel.getLanguageXml());
			}

			String langXml = formDef.getLanguageXml();

			if (isOfflineMode()) {
				if (formSaveListener != null) {
					if (langXml != null && langXml.trim().length() > 0) {
						Document doc = XMLParser.parse(langXml);
						formSaveListener.onSaveLocaleText(formDef.getId(),
								LanguageUtil.getXformsLocaleText(doc),
								LanguageUtil.getLayoutLocaleText(doc));
					}
				}

				FormUtil.dlg.hide();
			} else
				saveLocaleText(OpenXdataFormBuilder
						.getCombinedLanguageText(Context.getLanguageText().get(
								formDef.getId())));
		} catch (Exception ex) {
			FormUtil.displayException(ex);
		}
	}

	/**
	 * Reloads forms in a given locale.
	 * 
	 * @param locale
	 *            the locale.
	 */
	public boolean changeLocale(final Locale locale) {

		final FormDef formDef = centerPanel.getFormDef();
		if (formDef == null)
			return false;

		// We need to have saved a form in order to translate it.
		if (formDef.getDoc() == null)
			saveForm();
		else if (!Window.confirm(messages.localeChangePrompt()))
			return false;

		// We need to do the translation in a differed command such that it
		// happens after form saving,
		// just in case form hadn't yet been saved.
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {

				// Store the new locale.
				Context.setLocale(locale);

				HashMap<String, String> map = Context.getLanguageText().get(
						formDef.getId());

				String xml = null;
				// Get text for this locale, if we have it.
				if (map != null)
					xml = map.get(locale.getKey());

				// If we don't, then get text for the default locale.
				if (xml == null && map != null)
					xml = map.get(Context.getDefaultLocale().getKey());

				// Now reload the forms in this selected locale.
				centerPanel.setLanguageXml(xml, false);
				openLanguageText();
			}
		});

		return true;
	}

	/**
	 * Sets locale text for a given form.
	 * 
	 * @param formId
	 *            the form identifier.
	 * @param locale
	 *            the locale key.
	 * @param text
	 *            the form locale text.
	 */
	private void setLocaleText(Integer formId, String locale, String text) {
		HashMap<String, String> map = Context.getLanguageText().get(formId);
		if (map == null) {
			map = new HashMap<String, String>();
			Context.getLanguageText().put(formId, map);
		}

		map.put(locale, text);
	}

	/**
	 * Gets locale text for a given form.
	 * 
	 * @param formId
	 *            the form identifier.
	 * @param locale
	 *            the locale key.
	 * @return the form locale text.
	 */
	private String getFormLocaleText(int formId, String locale) {
		HashMap<String, String> map = Context.getLanguageText().get(formId);
		if (map != null)
			return map.get(locale);
		return null;
	}

	/**
	 * Sets xforms and layout locale text for a given form.
	 * 
	 * @param formId
	 *            the form identifier.
	 * @param locale
	 *            the locale key.
	 * @param xform
	 *            the xforms locale text.
	 * @param layout
	 *            the layout locale text.
	 */
	public void setLocaleText(Integer formId, String locale, String xform,
			String layout) {
		setLocaleText(formId, locale, LanguageUtil.getLocaleText(xform, layout));
	}

	/**
	 * Sets the default locale used by the form designer.
	 * 
	 * @param locale
	 *            the locale.
	 */
	public void setDefaultLocale(Locale locale) {
		Context.setDefaultLocale(locale);
	}

	/**
	 * Embeds the selected xform into xhtml.
	 */
	public void saveAsXhtml() {
		if (!isOfflineMode())
			return;

		final Object obj = leftPanel.getSelectedForm();
		if (obj == null) {
			Window.alert(messages.selectSaveItem());
			return;
		}

		FormUtil.dlg.setText(messages.savingForm());
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				try {
					FormDef formDef = new FormDef((FormDef) obj);
					formDef.setDoc(((FormDef) obj).getDoc()); 
					String xml = XhtmlBuilder.fromFormDef2Xhtml(formDef);
					xml = FormDesignerUtil.formatXml(xml);
					centerPanel.setXformsSource(xml, formSaveListener == null);
					FormUtil.dlg.hide();
				} catch (Exception ex) {
					FormUtil.displayException(ex);
				}
			}
		});
	}

	public void saveAsOpenXdataForm() {

		if (isOfflineMode())
			saveTheForm();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {

				FormUtil.dlg.setText(messages.savingForm());
				FormUtil.dlg.center();

				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					public void execute() {
						try {
							FormDef formDef = leftPanel.getSelectedForm();
							String xml = OpenXdataFormBuilder.build(
									formDef,
									Context.getLanguageText().get(
											formDef.getId()));

							FormUtil.dlg.hide();

							if (isOfflineMode())
								centerPanel.setXformsSource(xml,
										formSaveListener == null
												&& isOfflineMode());
							else {
								SaveFileDialog dlg = new SaveFileDialog(
										FormUtil.getFileSaveUrl(), xml, formDef
												.getName());
								dlg.center();
							}
						} catch (Exception ex) {
							FormUtil.displayException(ex);
						}
					}
				});

			}
		});
	}

	/**
	 * This is called from the server after an attempt to authenticate the
	 * current user before they can submit form data.
	 * 
	 * @param authenticated
	 *            has a value of true if the server has successfully
	 *            authenticated the user, else false.
	 */
	private static void authenticationCallback(boolean authenticated) {

		// If user has passed authentication, just go on with whatever they
		// wanted to do
		// else just redisplay the login dialog and let them enter correct
		// user name and password.
		if (authenticated) {
			loginDlg.hide();

			if (currentAction == CA_REFRESH_FORM)
				controller.refreshObject();
			else if (currentAction == CA_LOAD_FORM)
				controller.loadForm();
			else if (currentAction == CA_SAVE_FORM)
				controller.saveTheForm();
			else if (currentAction == CA_SET_FILE_CONTENTS)
				controller.setFileContents();

			currentAction = CA_NONE;
		} else
			loginDlg.center();
	}

	/**
	 * Called to handle form designer global keyboard short cuts.
	 */
	public boolean handleKeyBoardEvent(Event event) {
		if (event.getCtrlKey()) {

			if (event.getKeyCode() == 'S') {
				saveForm();
				// Returning false such that firefox does not try to save the
				// page.
				return false;
			} else if (event.getKeyCode() == 'O') {
				openForm();
				return false;
			}
		}

		return true;
	}

	@Override
	public void launchAdvancedViews() {
		AdvancedViewDialog dialog = new AdvancedViewDialog(null);
		dialog.center();

	}
}
