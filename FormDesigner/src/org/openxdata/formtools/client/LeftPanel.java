package org.openxdata.formtools.client;

import java.util.List;

import org.openxdata.formtools.client.controller.IFormActionListener;
import org.openxdata.formtools.client.controller.IFormChangeListener;
import org.openxdata.formtools.client.controller.IFormDesignerListener;
import org.openxdata.formtools.client.controller.IFormSelectionListener;
import org.openxdata.formtools.client.controller.WidgetPropertyChangeListener;
import org.openxdata.formtools.client.controller.WidgetSelectionListener;
import org.openxdata.formtools.client.locale.LocaleText;
import org.openxdata.formtools.client.model.FormDef;
import org.openxdata.formtools.client.model.Locale;
import org.openxdata.formtools.client.util.FormDesignerUtil;
import org.openxdata.formtools.client.util.FormUtil;
import org.openxdata.formtools.client.view.FormsTreeView;
import org.openxdata.formtools.client.view.PaletteView;
import org.openxdata.formtools.client.view.WidgetPropertiesView;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * This widget is on the left hand side of the form designer and contains
 * the Forms, Palette, and Widget Properties panels.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class LeftPanel extends Composite {

	/**
	 * An image bundle specifying the images for this Widget and aggregating
	 * images needed in child widgets.
	 */
	public interface Images extends FormsTreeView.Images,FormDesignerImages{
		ImageResource tasksgroup();
		ImageResource filtersgroup();
	}

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
	public LeftPanel(Images images, IFormSelectionListener formSelectionListener) {
		formsTreeView = new FormsTreeView(images,formSelectionListener);
		widgetPropertiesView = new WidgetPropertiesView();
		paletteView =  new PaletteView(images);

		add(images,formsTreeView , images.tasksgroup(), LocaleText.get("forms"));
		add(images,paletteView , images.tasksgroup(),LocaleText.get("palette"));
		add(images,widgetPropertiesView , images.filtersgroup(), LocaleText.get("widgetProperties"));

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

	private void add(Images images, Widget widget, ImageResource imageProto,String caption) {
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
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#deleteSelectedItems()
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
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#addNewItem()
	 */
	public void addNewItem(){
		formsTreeView.addNewItem();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#addNewChildItem()
	 */
	public void addNewChildItem(){
		formsTreeView.addNewChildItem();
	}
	
	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#addNewQuestion()
	 */
	public void addNewQuestion(int dataType){
		formsTreeView.addNewQuestion(dataType);
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#moveItemUp()
	 */
	public void moveItemUp() {
		formsTreeView.moveItemUp();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#moveItemDown()
	 */
	public void moveItemDown(){
		formsTreeView.moveItemDown();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#cutItem()
	 */
	public void cutItem(){
		formsTreeView.cutItem();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#copyItem()
	 */
	public void copyItem() {
		formsTreeView.copyItem();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#pasteItem()
	 */
	public void pasteItem(){
		formsTreeView.pasteItem();
	}

	/**
	 * @see org.openxdata.formtools.client.controller.IFormActionListener#refreshItem()
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
