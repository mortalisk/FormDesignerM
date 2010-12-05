package org.openxdata.designer.client.view;

import java.util.Vector;

import org.openxdata.designer.client.controller.IConditionController;
import org.openxdata.designer.client.widget.skiprule.ConditionWidget;
import org.openxdata.designer.client.widget.skiprule.GroupHyperlink;
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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * This widget enables creation of validation rules.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class ValidationRulesView extends Composite implements IConditionController {
	
	interface MyUiBinder extends UiBinder<VerticalPanel, ValidationRulesView> {}
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	/** The main or root widget. */
	private VerticalPanel verticalPanel;
	
	/** Widget for adding new conditions. */
	@UiField Anchor addConditionLink;
	
	/** Widget for grouping conditions. Has all,any, none, and not all. */
	@UiField GroupHyperlink groupHyperlink;
	
	/** Widget for the validation rule error message. */
	@UiField TextBox txtErrorMessage;

	/** Widget for Label "Question: ". */
	@UiField Label lblAction;
	
	/** Widget for error message label. */
	@UiField Label lblErrorMsg;
	
	/** Widget for error "when" label. */
	@UiField Label lblWhen;

	/** Widget for "of the following apply" label. */
	@UiField Label lblOfTheFollowingApply;
	
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
		lblErrorMsg.setText(LocaleText.get("errorMessage"));		
		
		lblAction.setText(LocaleText.get("question")+": " /*"Question: "*/);
		
		lblWhen.setText(LocaleText.get("when"));
		lblOfTheFollowingApply.setText(LocaleText.get("ofTheFollowingApply"));
		
		addConditionLink.setText(LocaleText.get("clickToAddNewCondition"));
	}
	
	@UiFactory GroupHyperlink makeGroupHyperlink() {
		return new GroupHyperlink(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_ALL, "#");
	}
	
	@UiHandler("addConditionLink")
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
			verticalPanel.remove(addConditionLink);
			ConditionWidget conditionWidget = new ConditionWidget(formDef,this,false,questionDef);
			conditionWidget.setQuestionDef(questionDef);
			verticalPanel.add(conditionWidget);
			verticalPanel.add(addConditionLink);
			
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
		verticalPanel.remove(conditionWidget);
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
		
		int count = verticalPanel.getWidgetCount();
		for(int i=0; i<count; i++){
			Widget widget = verticalPanel.getWidget(i);
			if(widget instanceof ConditionWidget){
				Condition condition = ((ConditionWidget)widget).getCondition();
				
				if(condition != null && !validationRule.containsCondition(condition) && condition.getValue() != null)
					validationRule.addCondition(condition);
				else if(condition != null && validationRule.containsCondition(condition)){
					if(condition.getValue() != null)
						validationRule.updateCondition(condition);
					else
						validationRule.removeCondition(condition);
				}
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
			lblAction.setText(LocaleText.get("question")+":  " + questionDef.getDisplayText() + "  "+LocaleText.get("isValidWhen"));
		}
		else
			lblAction.setText(LocaleText.get("question")+": ");
		
		/*if(questionDef.getParent() instanceof PageDef)
			formDef = ((PageDef)questionDef.getParent()).getParent();
		else
			formDef = ((PageDef)((QuestionDef)questionDef.getParent()).getParent()).getParent();*/		

		this.questionDef = questionDef;
		
		validationRule = formDef.getValidationRule(questionDef);
		if(validationRule != null){
			groupHyperlink.setCondionsOperator(validationRule.getConditionsOperator());
			txtErrorMessage.setText(validationRule.getErrorMessage());
			verticalPanel.remove(addConditionLink);
			Vector<Condition> conditions = validationRule.getConditions();
			Vector<Condition> lostConditions = new Vector<Condition>();
			for(int i=0; i<conditions.size(); i++){
				ConditionWidget conditionWidget = new ConditionWidget(formDef,this,false,questionDef);
				if(conditionWidget.setCondition((Condition)conditions.elementAt(i)))
					verticalPanel.add(conditionWidget);
				else
					lostConditions.add((Condition)conditions.elementAt(i));
			}
			for(int i=0; i<lostConditions.size(); i++)
				validationRule.removeCondition((Condition)lostConditions.elementAt(i));
			if(validationRule.getConditionCount() == 0){
				formDef.removeValidationRule(validationRule);
				validationRule = null;
			}
			
			verticalPanel.add(addConditionLink);
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
		lblAction.setText(LocaleText.get("question")+": ");
		
		while(verticalPanel.getWidgetCount() > 4)
			verticalPanel.remove(verticalPanel.getWidget(3));
		
		txtErrorMessage.setText(null);
	}
	
	/**
	 * Sets whether to enable this widget or not.
	 * 
	 * @param enabled set to true to enable, else false.
	 */
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
		this.groupHyperlink.setEnabled(enabled);
		
		txtErrorMessage.setEnabled(enabled);
		
		if(!enabled)
			clearConditions();
	}
}
