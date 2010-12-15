package org.openxdata.designer.client;

import org.openxdata.designer.client.controller.IFormDesignerListener;
import org.openxdata.designer.client.controller.ILocaleListChangeListener;
import org.openxdata.sharedlib.client.locale.LocaleText;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget is the main tool bar for the form designer.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class Toolbar extends Composite implements ILocaleListChangeListener{

	private static ToolbarUiBinder uiBinder = GWT.create(ToolbarUiBinder.class);

	interface ToolbarUiBinder extends UiBinder<Widget, Toolbar> {
	}

	/** The tool bar buttons. */
	@UiField PushButton btnOpenForm;
	@UiField PushButton btnSaveForm;

	@UiField PushButton btnAddForm;
	@UiField PushButton btnAddPage;
	@UiField PushButton btnAddQuestion;
	@UiField PushButton btnDelete;

	@UiField PushButton btnCut;
	@UiField PushButton btnCopy;
	@UiField PushButton btnPaste;	
	
	@UiField PushButton btnJustifyLeft;
	@UiField PushButton btnJustifyRight;
	@UiField PushButton btnAlignTop;
	@UiField PushButton btnAlignBottom;
	@UiField PushButton btnSameWidth;
	@UiField PushButton btnSameHeight;
	@UiField PushButton btnSameSize;
	
	/** Listener to the tool bar button click events. */
	private final IFormDesignerListener controller;
	
	/**
	 * Creates a new instance of the tool bar.
	 * 
	 * @param images the images for tool bar icons.
	 * @param controller listener to the tool bar button click events.
	 */
	public Toolbar(IFormDesignerListener controller){
		this.controller = controller;
		initWidget(uiBinder.createAndBindUi(this));
		setButtonLocaleText();
	}
	
	/**
	 * Sets up the tool bar.
	 */
	private void setButtonLocaleText(){
		btnOpenForm.setTitle(LocaleText.get("open"));
		btnSaveForm.setTitle(LocaleText.get("save"));
		
		btnAddForm.setTitle(LocaleText.get("newForm"));
		btnAddPage.setTitle(LocaleText.get("addNewPage"));
		btnAddQuestion.setTitle(LocaleText.get("addNewQuestion"));
		btnDelete.setTitle(LocaleText.get("deleteSelected"));
		
		btnCut.setTitle(LocaleText.get("cut"));
		btnCopy.setTitle(LocaleText.get("copy"));
		btnPaste.setTitle(LocaleText.get("paste"));
		
		btnJustifyLeft.setTitle(LocaleText.get("alignLeft"));
		btnJustifyRight.setTitle(LocaleText.get("alignRight"));
		btnAlignTop.setTitle(LocaleText.get("alignTop"));
		btnAlignBottom.setTitle(LocaleText.get("alignBottom"));
		btnSameWidth.setTitle(LocaleText.get("makeSameWidth"));
		btnSameHeight.setTitle(LocaleText.get("makeSameHeight"));
		btnSameSize.setTitle(LocaleText.get("makeSameSize"));
	}
	
	@UiHandler("btnOpenForm")
	public void handleOpenForm(ClickEvent event) {
		controller.openForm();
	}
	
	@UiHandler("btnSaveForm")
	public void handleSaveForm(ClickEvent event) {
		controller.saveForm();
	}
	
	@UiHandler("btnAddForm")
	public void handleAddForm(ClickEvent event) {
		controller.newForm();
	}
	
	@UiHandler("btnAddPage")
	public void handleAddPage(ClickEvent event) {
		// TODO
	}
	
	@UiHandler("btnAddQuestion")
	public void handleAddQuestion(ClickEvent event) {
		// TODO
	}
	
	@UiHandler("btnDelete")
	public void handleDelete(ClickEvent event) {
		controller.deleteSelectedItem();
	}
	
	@UiHandler("btnCut")
	public void handleCut(ClickEvent event) {
		controller.cutItem();
	}
	
	@UiHandler("btnCopy")
	public void handleCopy(ClickEvent event) {
		controller.copyItem();
	}
	
	@UiHandler("btnPaste")
	public void handlePaste(ClickEvent event) {
		controller.pasteItem();
	}
	
	@UiHandler("btnJustifyLeft")
	public void handleJustifyLeft(ClickEvent event) {
		controller.alignLeft();
	}
	
	@UiHandler("btnJustifyRight")
	public void handleJustifyRight(ClickEvent event) {
		controller.alignRight();
	}
	
	@UiHandler("btnAlignTop")
	public void handleAlignTop(ClickEvent event) {
		controller.alignTop();
	}
	
	@UiHandler("btnAlignBottom")
	public void handleAlignBottom(ClickEvent event) {
		controller.alignBottom();
	}
	
	@UiHandler("btnSameWidth")
	public void handleSameWidth(ClickEvent event) {
		controller.makeSameWidth();
	}
	
	@UiHandler("btnSameHeight")
	public void handleSameHeight(ClickEvent event) {
		controller.makeSameHeight();
	}
	
	@UiHandler("btnSameSize")
	public void handleSameSize(ClickEvent event) {
		controller.makeSameSize();
	}
	
	
	public void onLocaleListChanged(){
		// TODO: remove or fix this
	}
}
