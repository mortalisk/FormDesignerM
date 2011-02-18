package org.openxdata.designer.client;

import org.openxdata.designer.client.controller.IFormDesignerListener;
import org.openxdata.designer.client.controller.ILocaleListChangeListener;
import org.openxdata.designer.client.event.CenterPanelTabSelectedEvent;
import org.openxdata.designer.client.event.CenterPanelTabSelectedHandler;
import org.openxdata.designer.client.event.XformItemSelectEvent;
import org.openxdata.designer.client.event.XformItemSelectHandler;
import org.openxdata.designer.client.event.FormDesignerEventBus;
import org.openxdata.designer.client.event.XformListEmptyEvent;
import org.openxdata.designer.client.event.XformListEmptyHandler;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import org.openxdata.sharedlib.client.locale.FormsConstants;

/**
 * This widget is the main tool bar for the form designer.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class Toolbar extends Composite implements ILocaleListChangeListener, XformItemSelectHandler, XformListEmptyHandler, 
													CenterPanelTabSelectedHandler {
    final FormsConstants formsConstants = GWT.create(FormsConstants.class);
    final DesignerMessages designerMessages = GWT.create(DesignerMessages.class);

	private static ToolbarUiBinder uiBinder = GWT.create(ToolbarUiBinder.class);
	
	private final EventBus eventBus = FormDesignerEventBus.getBus();

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
		addHandlersToEventBus();
		setButtonLocaleText();
		setDesignSurfaceButtonsEnabled(false);
	}
	
	private void addHandlersToEventBus() {
		eventBus.addHandler(XformItemSelectEvent.TYPE, this);
		eventBus.addHandler(XformListEmptyEvent.TYPE, this);
		eventBus.addHandler(CenterPanelTabSelectedEvent.getType(), this);
	}
	
	public void onXformItemSelected(XformItemSelectEvent event) {
		switch(event.getXformItemType()) {
			case FORM:
				btnAddPage.setEnabled(true);
				btnAddQuestion.setEnabled(false);
				btnDelete.setEnabled(true);
				break;
			case PAGE:
			case QUESTION:
				btnAddQuestion.setEnabled(true);
		}
	}
	
	public void onListEmpty(XformListEmptyEvent event) {
		btnAddPage.setEnabled(false);
		btnAddQuestion.setEnabled(false);
		btnDelete.setEnabled(false);
	}

	/**
	 * Sets up the tool bar.
	 */
	private void setButtonLocaleText(){
		btnOpenForm.setTitle(formsConstants.open());
		btnSaveForm.setTitle(formsConstants.save());
		
		btnAddForm.setTitle(formsConstants.newForm());
		btnAddPage.setTitle(designerMessages.addNewPage());
		btnAddQuestion.setTitle(designerMessages.addNewQuestion());
		btnDelete.setTitle(designerMessages.deleteSelected());
		
		btnCut.setTitle(designerMessages.cut());
		btnCopy.setTitle(designerMessages.copy());
		btnPaste.setTitle(designerMessages.paste());
		
		btnJustifyLeft.setTitle(designerMessages.alignLeft());
		btnJustifyRight.setTitle(designerMessages.alignRight());
		btnAlignTop.setTitle(designerMessages.alignTop());
		btnAlignBottom.setTitle(designerMessages.alignBottom());
		btnSameWidth.setTitle(designerMessages.makeSameWidth());
		btnSameHeight.setTitle(designerMessages.makeSameHeight());
		btnSameSize.setTitle(designerMessages.makeSameSize());
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
		controller.addNewPage();
	}
	
	@UiHandler("btnAddQuestion")
	public void handleAddQuestion(ClickEvent event) {
		controller.addNewQuestion(QuestionDef.QTN_TYPE_TEXT);
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
	}

	@Override
	public void doCenterPanelTabSelected(CenterPanelTabSelectedEvent event) {
		
		if (event.getSelectedIndex() == CenterPanel.SELECTED_INDEX_DESIGN_SURFACE)
			setDesignSurfaceButtonsEnabled(true);
		else
			setDesignSurfaceButtonsEnabled(false);
	}
	
	private void setDesignSurfaceButtonsEnabled(boolean enabled){
		
		PushButton[] buttons = {btnJustifyLeft, btnJustifyRight, btnAlignTop, btnAlignBottom, btnSameWidth, btnSameHeight, btnSameSize};
			
		for (PushButton b : buttons)
			b.setEnabled(enabled);
	}
}
