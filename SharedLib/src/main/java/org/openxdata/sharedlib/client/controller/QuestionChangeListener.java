package org.openxdata.sharedlib.client.controller;

import java.util.List;

import org.openxdata.sharedlib.client.model.OptionDef;
import org.openxdata.sharedlib.client.model.QuestionDef;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * This interface is implemented by those interested in listening to changes
 * on a question definition.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public interface QuestionChangeListener {
	
	/**
	 * Called when the enabled property changes.
	 * 
	 * @param sender the question whose property value has changed.
	 * @param enabled the new value of the property.
	 */
	public void onEnabledChanged(QuestionDef sender,boolean enabled);
	
	/**
	 * Called when the visible property changes.
	 * 
	 * @param sender the question whose property value has changed.
	 * @param visible the new value of the property.
	 */
	public void onVisibleChanged(QuestionDef sender,boolean visible);
	
	/**
	 * Called when the required property changes.
	 * 
	 * @param sender the question whose property value has changed.
	 * @param required the new value of the property.
	 */
	public void onRequiredChanged(QuestionDef sender,boolean required);
	
	/**
	 * Called when the locked property changes.
	 * 
	 * @param sender the question whose property value has changed.
	 * @param locked
	 */
	public void onLockedChanged(QuestionDef sender,boolean locked);
	
	/**
	 * Called when the binding property changes.
	 * 
	 * @param sender the question whose property value has changed.
	 * @param binding  the new value of the property.
	 */
	public void onBindingChanged(QuestionDef sender,String binding);
	
	/**
	 * Called when the data type property changes.
	 * 
	 * @param sender the question whose property value has changed.
	 * @param dataType the new value of the property.
	 */
	public void onDataTypeChanged(QuestionDef sender,QuestionType dataType);
	
	/**
	 * Called when the list of options changes for a single or multiple select question.
	 * 
	 * @param sender the question whose list of options has changed.
	 * @param optionList the new list of options.
	 */
	public void onOptionsChanged(QuestionDef sender,List<OptionDef> optionList);
}
