package org.openxdata.designer.client.view;

import org.openxdata.designer.client.controller.FormDesignerDragController;
import org.openxdata.designer.client.vew.widget.images.FormDesignerImages;
import org.openxdata.designer.client.widget.PaletteWidget;
import org.openxdata.sharedlib.client.util.FormUtil;

import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.openxdata.designer.client.DesignerMessages;
import org.openxdata.sharedlib.client.locale.FormsConstants;


/**
 * Contains the palette where the user can pick widgets and drag drop
 *  them onto the design surface.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class PaletteView extends Composite {
    
        private final FormsConstants i18n = GWT.create(FormsConstants.class);
        private final DesignerMessages messages = GWT.create(DesignerMessages.class);
	

	/** The panel to contain the palette widgets. */
	private VerticalPanel verticalPanel = new VerticalPanel();
	
	/** The palette images. */
	private final FormDesignerImages images;
	
	/** The DND drag controller. */
	private static FormDesignerDragController dragController;

	
	/**
	 * Creates a new instance of the palette.
	 * 
	 * @param images the palette images.
	 */
	public PaletteView(FormDesignerImages images) {

		this.images = images;

		if(dragController == null)
			initDnd();
		
		verticalPanel.setSpacing(10);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		verticalPanel.add(createPaletteWidget(new HTML(i18n.label())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.textBox())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.checkBox())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.radioButton())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.listBox())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.textArea())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.button())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.datePicker())));
		verticalPanel.add(createPaletteWidget(new HTML(messages.dateTimeWidget())));
		verticalPanel.add(createPaletteWidget(new HTML(messages.timeWidget())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.groupBox())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.repeatSection())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.picture())));
		verticalPanel.add(createPaletteWidget(new HTML(i18n.videoAudio())));
		
		//verticalPanel.add(createPaletteWidget(new HTML(i18n.searchServer())));

		initWidget(verticalPanel);
		FormUtil.maximizeWidget(verticalPanel);
	}
	
	/**
	 * Sets up the DND drag controller.
	 */
	private static void initDnd(){
		dragController = new FormDesignerDragController(RootPanel.get(), false,null);
		dragController.setBehaviorMultipleSelection(false);
	}

	/**
	 * Creates a new palette widget.
	 * 
	 * @param html the name of the widget.
	 * @return the new palette widget.
	 */
	private PaletteWidget createPaletteWidget(HTML html){
		PaletteWidget widget = new PaletteWidget(images, html);
		dragController.makeDraggable(widget);
		return widget;
	}
	
	/**
	 * Registers a drop controller for widgets from this palette.
	 * 
	 * @param dropController the drop controller to register.
	 */
	public static void registerDropController(DropController dropController) {
		if(dragController == null)
			initDnd();
		dragController.registerDropController(dropController);
	}
	
	/**
	 * Removes a previously registered drop controller.
	 * 
	 * @param dropController the drop controller to un register.
	 */
	public static void unRegisterDropController(DropController dropController){
		dragController.unregisterDropController(dropController);
	}
	
	/**
	 * Removes all registered drop controllers.
	 */
	public static void unRegisterAllDropControllers(){
		dragController.unregisterAllDropControllers();
	}
}
