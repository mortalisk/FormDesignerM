package org.openxdata.designer.client.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.openxdata.designer.client.controller.IConditionController;
import org.openxdata.designer.client.controller.QuestionSelectionListener;
import org.openxdata.designer.client.widget.skiprule.ConditionWidget;
import org.openxdata.designer.client.widget.skiprule.GroupOperationWidget;
import org.openxdata.sharedlib.client.locale.LocaleText;
import org.openxdata.sharedlib.client.model.Condition;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.QuestionDef;
import org.openxdata.sharedlib.client.model.SkipRule;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * This widget enables creation of skip rules.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class SkipRulesView extends Composite implements IConditionController, QuestionSelectionListener{

	private static SkipRulesViewUiBinder uiBinder = GWT.create(SkipRulesViewUiBinder.class);

	interface SkipRulesViewUiBinder extends UiBinder<VerticalPanel, SkipRulesView> {
	}	
	
	/** The main or root widget. */
	private VerticalPanel verticalPanel;

	/** Widget for adding new conditions. */
	@UiField Button addConditionButton;

	/** Widget for grouping conditions. Has all,any, none, and not all. */
	@UiField GroupOperationWidget groupOperatorWidget;

	/** The form definition object that this skip rule belongs to. */
	private FormDef formDef;

	/** The question definition object which is the target of the skip rule. 
	 *  As for now, the form designer supports only one skip rule target. But the
	 *  skip rule object supports an un limited number.
	 */
	private QuestionDef questionDef;

	/** The skip rule definition object. */
	private SkipRule skipRule;

	/** Flag determining whether to enable this widget or not. */
	private boolean enabled;

	/** Widget for the skip rule action to enable a question. */
	@UiField RadioButton rdEnable;

	/** Widget for the skip rule action to disable a question. */
	@UiField RadioButton rdDisable;

	/** Widget for the skip rule action to show a question. */
	@UiField RadioButton rdShow;

	/** Widget for the skip rule action to hide a question. */
	@UiField RadioButton rdHide;

	/** Widget for the skip rule action to make a question required. */
	@UiField CheckBox chkMakeRequired;

	/** Widget for Label "for question". */
	@UiField SpanElement lblAction;

	/** Widget for Label "and". */
	@UiField SpanElement lblAnd;
	
	/** Button to show other questions */
	@UiField Button otherQts;

	/** Panel containing text and link for when to apply condition */
	@UiField HTMLPanel conditionPanel;
	
	/** A list of condition widgets applied to the current selected question */
	private final List<ConditionWidget> conditions = new ArrayList<ConditionWidget>();

	/**
	 * Creates a new instance of the skip logic widget.
	 */
	public SkipRulesView(){
		verticalPanel = uiBinder.createAndBindUi(this);
		
		initWidget(verticalPanel);
		
		// set localized text on some of the widgets
		// TODO(droberge): Determine if there is a way to set localized text in XML
		lblAction.setInnerText(LocaleText.get("forQuestion"));
		lblAnd.setInnerText(LocaleText.get("and"));
		otherQts.setText(LocaleText.get("clickForOtherQuestions"));
		addConditionButton.setText(LocaleText.get("newCondition"));
		
		SpanElement conditionSpan = SpanElement.as(conditionPanel.getElementById("whenSpan"));
		conditionSpan.setInnerText(LocaleText.get("when"));
		
		conditionSpan = SpanElement.as(conditionPanel.getElementById("followingSpan"));
		conditionSpan.setInnerText(LocaleText.get("ofTheFollowingApply"));
	}
	
	@UiFactory GroupOperationWidget makeGroupHyperlink() {
		return new GroupOperationWidget();
	}
	
	@UiHandler("addConditionButton")
	protected void handleConditionLinkClick(ClickEvent event) {
		addCondition();
	}
	
	@UiHandler("otherQts")
	protected void handleOtherQtsClick(ClickEvent event) {
		showOtherQuestions();
	}

	@UiHandler({"rdEnable", "rdDisable", "rdShow", "rdHide"})
	protected void handleRadioClick(ClickEvent event) {
		updateMakeRequired();
	}
	
	/**
	 * Enables the make required widget if the enable or show widget is ticked, 
	 * else disables and unticks it.
	 */
	private void updateMakeRequired(){
		chkMakeRequired.setEnabled(rdEnable.getValue() == true || rdShow.getValue() == true);
		if(!chkMakeRequired.isEnabled())
			chkMakeRequired.setValue(false);
	}

	/**
	 * Adds a new condition.
	 */
	public void addCondition(){
		if(formDef != null && enabled){
			ConditionWidget conditionWidget = new ConditionWidget(formDef,this,true,questionDef);
			conditions.add(conditionWidget);
			verticalPanel.add(conditionWidget);

			if(!(rdEnable.getValue() == true||rdDisable.getValue() == true||rdShow.getValue() == true||rdHide.getValue() == true)){
				rdEnable.setValue(true);
				updateMakeRequired();
			}
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
		if(skipRule != null){
			Condition condition = conditionWidget.getCondition();
			if(condition != null){
				if(skipRule.getConditionCount() == 1 && skipRule.getActionTargetCount() > 1)
					skipRule.removeActionTarget(questionDef);
				else
					skipRule.removeCondition(condition);
			}
		}
		conditions.remove(conditionWidget);
		verticalPanel.remove(conditionWidget);
	}

	/**
	 * Sets or updates the values of the skip rule object from the user's widget selections.
	 */
	public void updateSkipRule(){
		if(questionDef == null){
			skipRule = null;
			return;
		}

		if(skipRule == null)
			skipRule = new SkipRule();

		for(int i=0; i < conditions.size(); i++){
			Condition condition = conditions.get(i).getCondition();

			if(condition != null && !skipRule.containsCondition(condition)) {
				skipRule.addCondition(condition);
			} else if(condition != null && skipRule.containsCondition(condition)) {
				skipRule.updateCondition(condition);
			}
		}

		if(skipRule.getConditions() == null || conditions.size() == 0)
			skipRule = null;
		else{
			skipRule.setConditionsOperator(groupOperatorWidget.getConditionsOperator());
			skipRule.setAction(getAction());
			
			if(!skipRule.containsActionTarget(questionDef.getId()))
				skipRule.addActionTarget(questionDef.getId());
		}

		if(skipRule != null && !formDef.containsSkipRule(skipRule))
			formDef.addSkipRule(skipRule);
	}

	/**
	 * Gets the skip rule action based on the user's widget selections.
	 * 
	 * @return the skip rule action.
	 */
	private int getAction(){
		int action = 0;
		if(rdEnable.getValue() == true)
			action |= ModelConstants.ACTION_ENABLE;
		else if(rdShow.getValue() == true)
			action |= ModelConstants.ACTION_SHOW;
		else if(rdHide.getValue() == true)
			action |= ModelConstants.ACTION_HIDE;
		else
			action |= ModelConstants.ACTION_DISABLE;

		if(chkMakeRequired.getValue() == true)
			action |= ModelConstants.ACTION_MAKE_MANDATORY;
		else
			action |= ModelConstants.ACTION_MAKE_OPTIONAL;

		return action;
	}

	/**
	 * Updates the widgets basing on a given skip rule action.
	 * 
	 * @param action the skip rule action.
	 */
	private void setAction(int action){
		rdEnable.setValue((action & ModelConstants.ACTION_ENABLE) != 0);
		rdDisable.setValue((action & ModelConstants.ACTION_DISABLE) != 0);
		rdShow.setValue((action & ModelConstants.ACTION_SHOW) != 0);
		rdHide.setValue((action & ModelConstants.ACTION_HIDE) != 0);
		chkMakeRequired.setValue((action & ModelConstants.ACTION_MAKE_MANDATORY) != 0);
		updateMakeRequired();
	}

	/**
	 * Sets the question definition object which is the target of the skip rule.
	 * For now we support only one target for the skip rule.
	 * 
	 * @param questionDef the question definition object.
	 */
	public void setQuestionDef(QuestionDef questionDef){
		clearConditions();

		if(questionDef != null){
			formDef = questionDef.getParentFormDef();
			lblAction.setInnerText(LocaleText.get("forQuestion") + questionDef.getDisplayText());
			
			// only enable grouping operator and add condition widgets
			// if the form definition contains more than 1 question
			// it doesn't really make sense to allow these to be enabled
			// when the form definition has only 1 question
			if (questionDef.getParentFormDef().getQuestionCount() > 1) {
				addConditionButton.setEnabled(true);
				groupOperatorWidget.setEnabled(true);
			} else {
				addConditionButton.setEnabled(false);
				groupOperatorWidget.setEnabled(false);
			}
		}
		else {
			lblAction.setInnerText(LocaleText.get("forQuestion"));
			addConditionButton.setEnabled(false);
			groupOperatorWidget.setEnabled(false);
		}

		this.questionDef = questionDef;

		skipRule = formDef.getSkipRule(questionDef);
		if(skipRule != null){
			groupOperatorWidget.setCondionsOperator(skipRule.getConditionsOperator());
			setAction(skipRule.getAction());
			Vector<Condition> conditions = skipRule.getConditions();
			Vector<Condition> lostConditions = new Vector<Condition>();
			for(int i=0; i<conditions.size(); i++){
				ConditionWidget conditionWidget = new ConditionWidget(formDef,this,true,questionDef);

				if(conditionWidget.setCondition((Condition)conditions.elementAt(i))) {
					verticalPanel.add(conditionWidget);
					this.conditions.add(conditionWidget);
				} else
					lostConditions.add((Condition)conditions.elementAt(i));
			}
			for(int i=0; i<lostConditions.size(); i++)
				skipRule.removeCondition((Condition)lostConditions.elementAt(i));
			if(skipRule.getConditionCount() == 0){
				formDef.removeSkipRule(skipRule);
				skipRule = null;
			}

		}
	}

	/**
	 * Sets the form definition object to which this skip rule belongs.
	 * 
	 * @param formDef the form definition object.
	 */
	public void setFormDef(FormDef formDef){
		updateSkipRule();
		this.formDef = formDef;
		this.questionDef = null;
		clearConditions();
	}

	/**
	 * Removes all skip rule conditions.
	 */
	private void clearConditions(){
		if(questionDef != null)
			updateSkipRule();

		questionDef = null;
		lblAction.setInnerText(LocaleText.get("forQuestion"));

		// clear condition widgets from ui
		for(int i = 0; i < conditions.size(); i++) {
			verticalPanel.remove(conditions.get(i));
		}
		
		conditions.clear();
		
		groupOperatorWidget.reset();

		rdEnable.setValue(false);
		rdDisable.setValue(false);
		rdShow.setValue(false);
		rdHide.setValue(false);
		chkMakeRequired.setValue(false);
		updateMakeRequired();
	}

	/**
	 * Sets whether to enable this widget or not.
	 * 
	 * @param enabled set to true to enable, else false.
	 */
	public void setEnabled(boolean enabled){
		this.enabled = enabled;

		rdEnable.setEnabled(enabled);
		rdDisable.setEnabled(enabled);
		rdShow.setEnabled(enabled);
		rdHide.setEnabled(enabled);
		chkMakeRequired.setEnabled(enabled);

		if(!enabled)
			clearConditions();
	}


	/**
	 * Shows a list of other questions that are targets of the current skip rule.
	 */
	private void showOtherQuestions(){
		if(enabled){
			SkipQtnsDialog dialog = new SkipQtnsDialog(this);
			dialog.setData(formDef,questionDef,skipRule);
			dialog.center();
		}
	}


	/**
	 * @see org.openxdata.designer.client.controller.QuestionSelectionListener#onQuestionsSelected(List)
	 */
	public void onQuestionsSelected(List<String> questions){
		if(skipRule == null)
			skipRule = new SkipRule();

		//Check if we have any action targets. If we do not, just add all as new.
		List<Integer> actnTargets = skipRule.getActionTargets();
		if(actnTargets == null){
			for(String varName : questions)
				skipRule.addActionTarget(formDef.getQuestion(varName).getId());
			
			return;
		}

		//Remove any de selected action targets from the skip rule.
		for(int index = 0; index < actnTargets.size(); index++){
			Integer qtnId = actnTargets.get(index);

			QuestionDef qtnDef = formDef.getQuestion(qtnId);
			if(qtnDef == questionDef)
				continue; //Ignore the question for which we are editing the skip rule.

			if(qtnDef == null || !questions.contains(qtnDef.getBinding())){
				actnTargets.remove(index);
				index = index - 1;
			}
		}
		
		//Add any newly added questions as action targets.
		for(String varName : questions){
			QuestionDef qtnDef = formDef.getQuestion(varName);
			if(!skipRule.containsActionTarget(qtnDef.getId()))
				skipRule.addActionTarget(qtnDef.getId());
		}
	}
}
