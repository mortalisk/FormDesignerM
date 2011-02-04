package org.openxdata.designer.client;

import com.google.gwt.core.client.GWT;
import org.openxdata.designer.client.controller.IFormDesignerListener;
import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.designer.client.vew.widget.images.FormDesignerImages;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import org.openxdata.sharedlib.client.locale.FormsConstants;

/**
 * Creates the main menu widget for the form designer.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class Menu extends Composite {
    final DesignerMessages designerMessages = GWT.create(DesignerMessages.class);
    final FormsConstants formConstants = GWT.create(FormsConstants.class);
    
	/** The images for menu icons. */
	private final FormDesignerImages images;

	/** The underlying GWT menu bar. */
	private MenuBar menuBar;

	/** Listener to menu item click events. */
	private IFormDesignerListener controller;

	/**
	 * Creates a new instance of the menu bar.
	 * 
	 * @param images the images for menu icons.
	 * @param controller the listener to menu item click events.
	 */
	public Menu(FormDesignerImages images,IFormDesignerListener controller){
		this.images = images;
		this.controller = controller;

		setupMenu();

		initWidget(menuBar);
	}

	/**
	 * Sets up the menu bar.
	 */
	private void setupMenu()
	{
		//Set up the file menu.
		MenuBar fileMenu = new MenuBar(true);

		if(Context.isOfflineMode()){
			fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.newform(),designerMessages.newForm()),true, new Command(){
				public void execute() {controller.newForm();}});
		}

		fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.open(),formConstants.open()),true, new Command(){
			public void execute() {controller.openForm();}});

		fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.open(),designerMessages.print()),true, new Command(){
			public void execute() {controller.printForm();}});

		fileMenu.addSeparator();


		fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.save(),formConstants.save()),true, new Command(){
			public void execute() {controller.saveForm();}});

		if(Context.isOfflineMode()){
			fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.save(),formConstants.saveAs()),true, new Command(){
				public void execute() {controller.saveFormAs();}});

			fileMenu.addSeparator();
			fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.open(),formConstants.openLayout()),true, new Command(){
				public void execute() {controller.openFormLayout();}});

			fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.save(),formConstants.saveLayout()),true, new Command(){
				public void execute() {controller.saveFormLayout();}});

			fileMenu.addSeparator();
			fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.open(),formConstants.openLanguageText()),true, new Command(){
				public void execute() {controller.openLanguageText();}});

			fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.save(),formConstants.saveLanguageText()),true, new Command(){
				public void execute() {controller.saveLanguageText();}});

			fileMenu.addSeparator();
			fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.save(),formConstants.saveAsXhtml()),true, new Command(){
				public void execute() {controller.saveAsXhtml();}});
		}

		fileMenu.addItem(FormDesignerUtil.createHeaderHTML(images.save(),designerMessages.saveAsOpenXdataForm()),true, new Command(){
			public void execute() {controller.saveAsOpenXdataForm();}});


		fileMenu.addSeparator();
		fileMenu.addItem(formConstants.close(), new Command(){
			public void execute() {controller.closeForm();}});



		//Set up the view menu.
		MenuBar viewMenu = new MenuBar(true);
		viewMenu.addItem(FormDesignerUtil.createHeaderHTML(images.refresh(),formConstants.refresh()),true, new Command(){
			public void execute() {controller.refresh(this);}});


		//Set up the item menu.
		MenuBar itemMenu = new MenuBar(true);
		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.add(),formConstants.addNew()),true, new Command(){
			public void execute() {controller.addNewItem();}});

		itemMenu.addSeparator();
		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.addchild(),formConstants.addNewChild()),true, new Command(){
			public void execute() {controller.addNewChildItem();}});

		itemMenu.addSeparator();
		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.delete(),formConstants.deleteSelected()),true, new Command(){
			public void execute() {controller.deleteSelectedItem();}});

		itemMenu.addSeparator();
		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.moveup(),formConstants.moveUp()),true, new Command(){
			public void execute() {controller.moveItemUp();}});

		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.movedown(),formConstants.moveDown()),true, new Command(){
			public void execute() {controller.moveItemDown();}});

		itemMenu.addSeparator();
		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.cut(),formConstants.cut()),true, new Command(){
			public void execute() {controller.cutItem();}});

		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.copy(),formConstants.copy()),true, new Command(){
			public void execute() {controller.copyItem();}});

		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.paste(),formConstants.paste()),true, new Command(){
			public void execute() {controller.pasteItem();}});

		itemMenu.addSeparator();
		itemMenu.addItem(FormDesignerUtil.createHeaderHTML(images.refresh(),formConstants.refresh()),true, new Command(){
			public void execute() {controller.refreshItem();}});


		// Set up the tools menu.
		MenuBar toolsMenu = new MenuBar(true);
		toolsMenu.addItem(formConstants.format(), new Command(){
			public void execute() {controller.format();}});

		toolsMenu.addSeparator();
		toolsMenu.addItem(formConstants.languages(), new Command(){
			public void execute() {controller.showLanguages();}});

		toolsMenu.addSeparator();
		toolsMenu.addItem(formConstants.options(), new Command(){
			public void execute() {controller.showOptions();}});


		//Set up the help menu.
		MenuBar helpMenu = new MenuBar(true);
		helpMenu.addItem(FormDesignerUtil.createHeaderHTML(images.info(),formConstants.helpContents()),true, new Command(){
			public void execute() {controller.showHelpContents();}});

		helpMenu.addSeparator();
		helpMenu.addItem(formConstants.about() + " " + FormDesignerUtil.getTitle(), new Command(){
			public void execute() {controller.showAboutInfo();}});


		//Add all the top level menus to the GWT menu bar.
		menuBar = new MenuBar();
		menuBar.addItem(formConstants.file(), fileMenu);
		menuBar.addItem(formConstants.view(), viewMenu);
		menuBar.addItem(formConstants.item(),itemMenu);
		menuBar.addItem(formConstants.tools(), toolsMenu);
		menuBar.addItem(formConstants.help(), helpMenu);

		menuBar.setAnimationEnabled(true);
	}
}
