package org.openxdata.designer.client;

import com.google.gwt.core.client.GWT;
import java.util.List;

import org.openxdata.designer.client.controller.IFormActionListener;
import org.openxdata.designer.client.controller.IFormChangeListener;
import org.openxdata.designer.client.controller.IFormDesignerListener;
import org.openxdata.designer.client.controller.IFormSelectionListener;
import org.openxdata.designer.client.controller.WidgetPropertyChangeListener;
import org.openxdata.designer.client.controller.WidgetSelectionListener;
import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.designer.client.vew.widget.images.FormDesignerImages;
import org.openxdata.designer.client.view.FormsTreeView;
import org.openxdata.designer.client.view.PaletteView;
import org.openxdata.designer.client.view.WidgetPropertiesView;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.Locale;
import org.openxdata.sharedlib.client.util.FormUtil;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Widget;
import org.openxdata.sharedlib.client.locale.FormsConstants;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * This widget is on the left hand side of the form designer and contains
 * the Forms, Palette, and Widget Properties panels.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class LeftPanel extends Composite {
    final FormsConstants formsConstants = GWT.create(FormsConstants.class);

	/** The GWT stack panel which serves as the main or root widget. */
	private DecoratedStackPanel stackPanel = new DecoratedStackPanel();
	
	/** Widgets which displays the list of forms in a tree view. */
	private FormsTreeView formsTreeView;
	
	/** Widget which displays properties for the selected widget on the design surface. */
	private WidgetPropertiesView widgetPropertiesView;
	
	/** The palette widget from which one can drag and drop widgets onto the design surface. */
	private PaletteView paletteView;
	

	/**
	 * Constructs a new left panel object.
	 * 
	 * @param images a bundle that provides the images for this widget
	 */
	public LeftPanel(FormDesignerImages images, IFormSelectionListener formSelectionListener) {
		formsTreeView = new FormsTreeView(images, formSelectionListener);
		widgetPropertiesView = new WidgetPropertiesView();
		paletteView =  new PaletteView(images);

		add(formsTreeView , images.tasksgroup(), formsConstants.forms());
		add(paletteView , images.tasksgroup(), formsConstants.palette());
		add(widgetPropertiesView , images.filtersgroup(), formsConstants.widgetProperties());

		formsTreeView.addFormSelectionListener(widgetPropertiesView);
		FormUtil.maximizeWidget(stackPanel);

		initWidget(stackPanel);
	}

	/**
	 * Sets the listener to form designer global events.
	 * 
	 * @param formDesignerListener the listener.
	 */
	public void setFormDesignerListener(IFormDesignerListener formDesignerListener){
		formsTreeView.setFormDesignerListener(formDesignerListener);
	}

	public void showFormAsRoot(boolean showFormAsRoot){
		formsTreeView.showFormAsRoot(showFormAsRoot);
	}

	/**
	 * Gets the listener for form item property changes.
	 * 
	 * @return the listener.
	 */
	public IFormChangeListener getFormChangeListener(){
		return formsTreeView;
	}

	private void add(Widget widget, ImageResource imageProto,String caption) {
		stackPanel.add(widget, FormDesignerUtil.createHeaderHTML(imageProto, caption), true);
	}

	/**
	 * Loads a given form.
	 * 
	 * @param formDef the form definition object.
	 */
	public void loadForm(FormDef formDef){
		formsTreeView.loadForm(formDef,true,false);
	}

	public void refresh(FormDef formDef){
		formsTreeView.refreshForm(formDef);
	}
	
	/**
	 * Gets the list of forms which are loaded.
	 * 
	 * @return the forms list.
	 */
	public List<FormDef> getForms(){
		return formsTreeView.getForms();
	}
	
	/**
	 * Loads a list of forms and selects one of them.
	 * 
	 * @param forms the form list to load.
	 * @param selFormId the id of the form to select.
	 */
	public void loadForms(List<FormDef> forms, int selFormId){
		formsTreeView.loadForms(forms,selFormId);
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#deleteSelectedItems()
	 */
	public void deleteSelectedItem(){
		formsTreeView.deleteSelectedItem();
	}

	/**
	 * Adds a new form.
	 */
	public void addNewForm(){
		formsTreeView.addNewForm();
	}

	/**
	 * Adds a new form with a given name, binding, and id
	 * 
	 * @param name the form name.
	 * @param varName the form binding.
	 * @param formId the form id.
	 */
	public void addNewForm(String name, String varName, int formId){
		formsTreeView.addNewForm(name, varName, formId);
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#addNewItem()
	 */
	public void addNewItem(){
		formsTreeView.addNewItem();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#addNewChildItem()
	 */
	public void addNewChildItem(){
		formsTreeView.addNewChildItem();
	}
	
	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#addNewQuestion()
	 */
	public void addNewQuestion(QuestionType dataType){
		formsTreeView.addNewQuestion(dataType);
	}
	
	public void addNewPage() {
		formsTreeView.addNewPage();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveItemUp()
	 */
	public void moveItemUp() {
		formsTreeView.moveItemUp();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#moveItemDown()
	 */
	public void moveItemDown(){
		formsTreeView.moveItemDown();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#cutItem()
	 */
	public void cutItem(){
		formsTreeView.cutItem();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#copyItem()
	 */
	public void copyItem() {
		formsTreeView.copyItem();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#pasteItem()
	 */
	public void pasteItem(){
		formsTreeView.pasteItem();
	}

	/**
	 * @see org.openxdata.designer.client.controller.IFormActionListener#refreshItem()
	 */
	public void refreshItem(){
		formsTreeView.refreshItem();
	}

	/**
	 * Gets the selected form.
	 * 
	 * @return the form definition object.
	 */
	public FormDef getSelectedForm(){
		return formsTreeView.getSelectedForm();
	}

	/**
	 * Gets the listener to widget selection events.
	 * 
	 * @return the listener.
	 */
	public WidgetSelectionListener getWidgetSelectionListener(){
		return widgetPropertiesView;
	}

	/**
	 * Refreshes the panel to match the current form definition object.
	 * An example of a result to such a refresh is reloading of question
	 * bindings in the widget properties.
	 */
	public void refresh(){
		widgetPropertiesView.refresh();
	}

	/**
	 * Removes all forms.
	 */
	public void clear(){
		formsTreeView.clear();
	}

	/**
	 * Check if a with a given id is loaded.
	 * 
	 * @param formId the form id.
	 * @return true if it exists, else false.
	 */
	public boolean formExists(int formId){
		return formsTreeView.formExists(formId);
	}

	/**
	 * Checks if the selected form is valid for saving.
	 * 
	 * @return true if valid, else false.
	 */
	public boolean isValidForm(){
		return formsTreeView.isValidForm();
	}
	
	/**
	 * Gets the listener to form action events.
	 * 
	 * @return the listener.
	 */
	public IFormActionListener getFormActionListener(){
		return formsTreeView;
	}
	
	/**
	 * Sets the default locale.
	 * 
	 * @param locale the localey.
	 */
	public void setDefaultLocale(Locale locale){
		Context.setDefaultLocale(locale);
	}
	
	
	public void setWidgetPropertyChangeListener(WidgetPropertyChangeListener widgetPropertyChangeListener){
		widgetPropertiesView.setWidgetPropertyChangeListener(widgetPropertyChangeListener);
	}
}
