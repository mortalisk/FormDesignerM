package org.openxdata.designer.client.widget.skiprule;

import org.openxdata.designer.client.controller.IConditionController;
import org.openxdata.designer.client.controller.ItemSelectionListener;
import org.openxdata.sharedlib.client.model.Condition;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;


/**
 * This widget is used to display a skip or validation rule widget.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class ConditionWidget extends Composite implements ItemSelectionListener{

	/** Value for horizontal panel spacing. */
	private static final int HORIZONTAL_SPACING = 5;

	/** The form that this condition belongs to. */
	private FormDef formDef;
	
	/** The field selection widget. */
	private FieldWidget fieldWidget;
	
	/** The operator selection widget. */
	private OperatorWidget operatorWidget;
	
	/** The value selection or entry widget. */
	private ValueWidget valueWidget = new ValueWidget();
	
	private HorizontalPanel horizontalPanel;
	
	/** The remove button */
	private Button btnRemove;

	/** The question that this widget condition references. */
	private QuestionDef questionDef;
	
	/** The selected operator for the condition. */
	private int operator;
	
	/** Listener to condition events. */
	private IConditionController view;
	
	/** The condition that this widget references. */
	private Condition condition;
	
	//private Label lbLabel = new Label(LocaleText.get("value"));
	
	/** The function selection widget for condition. */
	FunctionHyperlink funcHyperlink;
	
	/** The selected validation function. Could be Length or just Value. */
	private int function = ModelConstants.FUNCTION_VALUE;

	/** Flag determining whether we should allow field selection for the condition.
	 *  Skip rule conditions have filed selection while validation rules normally
	 *  just have values. eg skip rule (have allowFieldSelection = true) may be Current Pregnancy question is skipped 
	 *  when Sex field = Male, while validation (have allowFieldSelection = false) for the current say Weight question
	 *  is valid when Value > 0
	 */
	private boolean allowFieldSelection = false;

	
	/**
	 * Creates a new instance of the condition widget.
	 * 
	 * @param formDef the form whose condition this widget represents.
	 * @param view listener to condition events.
	 * @param allowFieldSelection a flag to determine if we should allow selection for the condition.
	 * @param questionDef the question that this condition references.
	 */
	public ConditionWidget(FormDef formDef, IConditionController view, boolean allowFieldSelection, QuestionDef questionDef){
		this.formDef = formDef;
		this.view = view;
		this.allowFieldSelection = allowFieldSelection;
		this.questionDef = questionDef;
		setupWidgets();
	}

	/**
	 * Creates the condition widgets.
	 */
	private void setupWidgets(){
		btnRemove = new Button("x");
		
		btnRemove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				remove();
			}
		});

		if(allowFieldSelection){
			fieldWidget = new FieldWidget(this);
			fieldWidget.setQuestion(questionDef);
		}

		operatorWidget = new OperatorWidget(ModelConstants.OPERATOR_EQUAL, this);
		funcHyperlink = new FunctionHyperlink(FunctionHyperlink.FUNCTION_TEXT_VALUE,"",this);
		
		horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(HORIZONTAL_SPACING);
		horizontalPanel.add(btnRemove);

		if(allowFieldSelection)
			horizontalPanel.add(fieldWidget);
		else
			horizontalPanel.add(funcHyperlink);

		horizontalPanel.add(operatorWidget);
		horizontalPanel.add(valueWidget);

		initWidget(horizontalPanel);

		//This should be before the next line as fieldWidget.setFormDef() will set questionDef to a new value of the condition instead of parent.
		valueWidget.setParentQuestionDef(questionDef);
		
		if(allowFieldSelection)
			fieldWidget.setFormDef(formDef);
		
		valueWidget.setFormDef(formDef);

		operator = ModelConstants.OPERATOR_EQUAL;
		valueWidget.setOperator(operator);
	}

	/**
	 * @see org.openxdata.designer.client.controller.ItemSelectionListener#onItemSelected(java.lang.Object, java.lang.Object)
	 */
	public void onItemSelected(Object sender, Object item) {
		if(sender == fieldWidget){
			questionDef = (QuestionDef)item;
			setOperatorDataType(questionDef);
			valueWidget.setQuestionDef(questionDef);
		}
		else if(sender == operatorWidget){
			operator = ((Integer)item).intValue();
			valueWidget.setOperator(operator);

			if(allowFieldSelection)
				fieldWidget.stopSelection();
		}
		else if(sender == funcHyperlink){
			function = ((Integer)item).intValue();
			valueWidget.setFunction(function);
			setOperatorDataType(questionDef);
		}
	}

	/**
	 * @see org.openxdata.designer.client.controller.ItemSelectionListener#onStartItemSelection(Object)
	 */
	public void onStartItemSelection(Object sender){
		if(sender != valueWidget)
			valueWidget.stopEdit(false); //Temporary hack to turn off edits when focus goes off the edit widget

		if(allowFieldSelection && sender != fieldWidget)
			fieldWidget.stopSelection();
	}

	/**
	 * Deletes this condition.
	 */
	public void remove(){
		view.deleteCondition(this);
	}

	/**
	 * Gets the condition for this widget.
	 * 
	 * @return the condition object.
	 */
	public Condition getCondition(){
		if(condition == null)
			condition = new Condition();

		condition.setQuestionId(questionDef.getId());
		condition.setOperator(operator);
		
		if (ModelConstants.operatorTakesSecondValue(operator)){
			String[] valuePair = valueWidget.getValue().split(ValueWidget.BETWEEN_VALUE_SEPARATOR);
			condition.setValue(valuePair[0].trim());
			condition.setSecondValue(valuePair[1].trim());
		}
		else
			condition.setValue(valueWidget.getValue());
		
		condition.setValueQtnDef(valueWidget.getValueQtnDef());
		condition.setFunction(function);

		if(condition.getValue() == null)
			return null;

		return condition;
	}

	/**
	 * Sets the condition for this widget.
	 * 
	 * @param condition the condition object.
	 * @return true if the question referenced by the condition exists, else false.
	 */
	public boolean setCondition(Condition condition){
		this.condition = condition;
		questionDef = formDef.getQuestion(condition.getQuestionId());
		if(questionDef == null)
			return false;

		setQuestionDef(questionDef);

		return true;
	}

	/**
	 * Sets the question for this widget.
	 * 
	 * @param questionDef the question id definition object.
	 */
	public void setQuestionDef(QuestionDef questionDef){
		this.questionDef = questionDef;
		valueWidget.setQuestionDef(questionDef);
		
		setOperatorDataType(questionDef);
		
		if(condition != null){
			operator = condition.getOperator();
			function = condition.getFunction();
			setOperatorDataType(questionDef);

			if(allowFieldSelection)
				fieldWidget.setQuestion(questionDef);

			funcHyperlink.setFunction(function);
			operatorWidget.setOperator(operator);
			valueWidget.setOperator(operator);
			valueWidget.setValueQtnDef(condition.getValueQtnDef()); //Should be set before value such that value processing finds it.
			valueWidget.setValue(condition.getValue());
		}
	}
	
	private void setOperatorDataType(QuestionDef questionDef){
		operatorWidget.setDataType(function == ModelConstants.FUNCTION_LENGTH ? QuestionDef.QTN_TYPE_NUMERIC : questionDef.getDataType());
	}
}
