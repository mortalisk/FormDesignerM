package org.openxdata.designer.client.view;

import java.util.Vector;

import org.openxdata.designer.client.controller.IConditionController;
import org.openxdata.designer.client.widget.skiprule.ConditionWidget;
import org.openxdata.designer.client.widget.skiprule.GroupOperationWidget;
import org.openxdata.sharedlib.client.OpenXdataConstants;
import org.openxdata.sharedlib.client.locale.LocaleText;
import org.openxdata.sharedlib.client.model.Condition;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.QuestionDef;
import org.openxdata.sharedlib.client.model.ValidationRule;
import org.openxdata.sharedlib.client.util.FormUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import org.openxdata.sharedlib.client.locale.FormsConstants;


/**
 * This widget enables creation of validation rules.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class ValidationRulesView extends AbstractFormDesignerView implements IConditionController {
	
	interface MyUiBinder extends UiBinder<VerticalPanel, ValidationRulesView> {}
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private FormsConstants formsConstants = GWT.create(FormsConstants.class);
	/** The main or root widget. */
	private VerticalPanel verticalPanel;
	
	@UiField VerticalPanel conditionsPanel;
	
	/** Widget for adding new conditions. */
	@UiField Button btnAddCondition;
	
	/** Widget for grouping conditions. Has all,any, none, and not all. */
	@UiField GroupOperationWidget groupHyperlink;
	
	/** Widget for the validation rule error message. */
	@UiField TextBox txtErrorMessage;

	/** Label: "Question: " */
	@UiField Label lblQuestionLabel;
	
	/** Populated with the text of the question whose validation rule is being edited. */
	@UiField Label lblQuestionText;
	
	/** Label: "Error message for invalid response:" */
	@UiField Label lblErrorMsg;
	
	/** Widget for error "when" label. */
	@UiField Label lblMustMeet;

	/** Widget for "of the following apply" label. */
	@UiField Label lblOfTheFollowing;
	
	/** The form definition object that this validation rule belongs to. */
	private FormDef formDef;
	
	/** The question definition object which is the target of the validation rule. */
	private QuestionDef questionDef;
	
	/** The validation rule definition object. */
	private ValidationRule validationRule;
	
	/** Flag determining whether to enable this widget or not. */
	private boolean enabled;
		
	/**
	 * Creates an instance of the validation rules view and sets up its widgets.
	 */
	public ValidationRulesView(){
		verticalPanel = uiBinder.createAndBindUi(this);
		initWidget(verticalPanel);
		
		FormUtil.maximizeWidget(txtErrorMessage);
		lblErrorMsg.setText(formsConstants.errorMessageForInvalidResponse() + ": ");		
		
		lblQuestionLabel.setText(LocaleText.get("question") + ": " /*"Question: "*/);
		
		lblMustMeet.setText(LocaleText.get("validResponseMustMeet"));
		lblOfTheFollowing.setText(LocaleText.get("ofTheFollowingConditions") + ": ");
		
		btnAddCondition.setText(LocaleText.get("addCondition"));
	}
	
	@UiFactory GroupOperationWidget makeGroupHyperlink() {
		return new GroupOperationWidget();
	}
	
	@UiHandler("btnAddCondition")
	void onClick(ClickEvent event){
		addCondition();
	}
	
	/**
	 * @see com.google.gwt.user.client.WindowResizeListener#onWindowResized(int, int)
	 */
	public void onWindowResized(int width, int height){
		if(width - 700 > 0)
			txtErrorMessage.setWidth(width - 700 + OpenXdataConstants.UNITS);
	}
	
	/**
	 * Adds a new condition.
	 */
	public void addCondition(){
		if(formDef != null && enabled){
			ConditionWidget conditionWidget = new ConditionWidget(formDef,this,false,questionDef);
			conditionWidget.setQuestionDef(questionDef);
			conditionsPanel.add(conditionWidget);
			
			txtErrorMessage.setFocus(true);
			
			/*String text = txtErrorMessage.getText();
			if(text != null && text.trim().length() == 0){
				txtErrorMessage.setText(LocaleText.get("errorMessage"));
				//txtErrorMessage.selectAll();
				txtErrorMessage.setFocus(true);
			}*/
		}
	}
		
	/**
	 * Supposed to add a bracket or nested set of related conditions which are 
	 * currently not supported.
	 */
	public void addBracket(){
		
	}
		
	/**
	 * Deletes a condition.
	 * 
	 * @param conditionWidget the widget having the condition to delete.
	 */
	public void deleteCondition(ConditionWidget conditionWidget){
		if(validationRule != null)
			validationRule.removeCondition(conditionWidget.getCondition());
		conditionsPanel.remove(conditionWidget);
	}
		
	/**
	 * Sets or updates the values of the validation rule object from the user's widget selections.
	 */
	public void updateValidationRule(){
		if(questionDef == null){
			validationRule = null;
			return;
		}
		
		if(validationRule == null)
			validationRule = new ValidationRule(questionDef.getId(),formDef);
		
		validationRule.setErrorMessage(txtErrorMessage.getText());
		
		for(Widget w : conditionsPanel){
				Condition condition = ((ConditionWidget)w).getCondition();
				
				if(condition != null && !validationRule.containsCondition(condition) && condition.getValue() != null)
					validationRule.addCondition(condition);
				else if(condition != null && validationRule.containsCondition(condition)){
					if(condition.getValue() != null)
						validationRule.updateCondition(condition);
					else
						validationRule.removeCondition(condition);
				}
		}
		
		if(validationRule.getConditions() == null || validationRule.getConditionCount() == 0){
			formDef.removeValidationRule(validationRule);
			validationRule = null;
		}
		else
			validationRule.setConditionsOperator(groupHyperlink.getConditionsOperator());
		
		if(validationRule != null && !formDef.containsValidationRule(validationRule))
			formDef.addValidationRule(validationRule);
	}
		
	/**
	 * Sets the question definition object which is the target of the validation rule.
	 * 
	 * @param questionDef the question definition object.
	 */
	public void setQuestionDef(QuestionDef questionDef){
		
		clearConditions();		
		if(questionDef != null){
			formDef = questionDef.getParentFormDef();
			lblQuestionText.setText(questionDef.getDisplayText());
			setEnabled(true);
		}
		else {
			lblQuestionText.setText("");
			setEnabled(false);
		}
		
		/*if(questionDef.getParent() instanceof PageDef)
			formDef = ((PageDef)questionDef.getParent()).getParent();
		else
			formDef = ((PageDef)((QuestionDef)questionDef.getParent()).getParent()).getParent();*/		

		this.questionDef = questionDef;
		
		validationRule = formDef.getValidationRule(questionDef);
		if(validationRule != null){
			groupHyperlink.setCondionsOperator(validationRule.getConditionsOperator());
			txtErrorMessage.setText(validationRule.getErrorMessage());
			Vector<Condition> conditions = validationRule.getConditions();
			Vector<Condition> lostConditions = new Vector<Condition>();
			for(int i=0; i<conditions.size(); i++){
				ConditionWidget conditionWidget = new ConditionWidget(formDef,this,false,questionDef);
				if(conditionWidget.setCondition((Condition)conditions.elementAt(i)))
					conditionsPanel.add(conditionWidget);
				else
					lostConditions.add((Condition)conditions.elementAt(i));
			}
			for(int i=0; i<lostConditions.size(); i++)
				validationRule.removeCondition((Condition)lostConditions.elementAt(i));
			if(validationRule.getConditionCount() == 0){
				formDef.removeValidationRule(validationRule);
				validationRule = null;
			}
		}
	}
		
	/**
	 * Sets the form definition object to which this validation rule belongs.
	 * 
	 * @param formDef the form definition object.
	 */
	public void setFormDef(FormDef formDef){
		updateValidationRule();
		this.formDef = formDef;
		this.questionDef = null;
		clearConditions();
	}
		
	/**
	 * Removes all validation rule conditions.
	 */
	private void clearConditions(){
		if(questionDef != null)
			updateValidationRule();
		
		questionDef = null;
		lblQuestionText.setText("");
		
		for (Widget w : conditionsPanel)
			conditionsPanel.remove(w);
		
		txtErrorMessage.setText(null);
	}
	
	/**
	 * Sets whether to enable this widget or not.
	 * 
	 * @param enabled set to true to enable, else false.
	 */
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
		
		setAllEnabled(verticalPanel, enabled);
		
		if(!enabled)
			clearConditions();
	}
}
