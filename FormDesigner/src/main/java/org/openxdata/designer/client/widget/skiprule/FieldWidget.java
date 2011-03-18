package org.openxdata.designer.client.widget.skiprule;

import java.util.Vector;

import org.openxdata.designer.client.controller.ItemSelectionListener;
import org.openxdata.designer.client.util.FormDesignerUtil;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.QuestionDef;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * This widget is used to let one select a field or question for a skip or validation
 * rule condition. For validation rules, this can be used for the condition value. eg
 * Weight less than Height. For skip rules, this can be used for both the condition
 * question and value.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FieldWidget extends Composite{

	/** The text to display when no value is specified for a condition. */
	private static final String EMPTY_VALUE = "_____";

	/** The form to which the question, represented by this widget, belongs. */
	private FormDef formDef;

	/** The main widget. */
	private HorizontalPanel horizontalPanel;

	/** The widget to do auto suggest for form questions as the user types. */
	private SuggestBox sgstField = new SuggestBox();

	/** The text field where to type the question name. */
	private TextBox txtField = new TextBox();

	/** The widget to display the selected question text when not in selection mode. */
	private Anchor fieldHyperlink;

	/** The listener for item selection events. */
	private ItemSelectionListener itemSelectionListener;

	/** A flag determining if the current field selection is for a single select dynamic.
	 * type of question.
	 */
	private boolean forDynamicOptions = false;

	/** The single select dynamic question. */
	private QuestionDef dynamicQuestionDef;

	//TODO I think we need only one of questionDef or dynamicQuestionDef to serve the same purpose
	/** The question that this field widget is handling. eg the skip logic question. */
	private QuestionDef questionDef;

	boolean enabled = true;


	public FieldWidget(ItemSelectionListener itemSelectionListener){
		this.itemSelectionListener = itemSelectionListener;
		setupWidgets();
	}

	/**
	 * Sets the form to which the referenced question belongs.
	 * 
	 * @param formDef the form definition object.
	 */
	public void setFormDef(FormDef formDef){
		this.formDef = formDef;
		setupPopup();
	}

	private void setupWidgets(){
		fieldHyperlink = new Anchor("", "#"); //Field 1

		horizontalPanel = new HorizontalPanel();
		horizontalPanel.add(fieldHyperlink);

		fieldHyperlink.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				if(enabled){
					itemSelectionListener.onStartItemSelection(this);
					horizontalPanel.remove(fieldHyperlink);
					horizontalPanel.add(sgstField);
					sgstField.setText(fieldHyperlink.getText());
					sgstField.setFocus(true);
					txtField.selectAll();
				}
			}
		});

		/*txtField.addFocusListener(new FocusListenerAdapter(){
			public void onLostFocus(Widget sender){
				stopSelection();
			}
		});

		/*sgstField.addFocusListener(new FocusListenerAdapter(){
			public void onLostFocus(Widget sender){
				stopSelection();
			}
		});*/

		sgstField.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>(){
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event){
				stopSelection();
			}
		});

		initWidget(horizontalPanel);
	}

	public void stopSelection(){
		if(horizontalPanel.getWidgetIndex(fieldHyperlink) != -1)
			return;

		String val = sgstField.getText();
		if(val.trim().length() == 0)
			val = EMPTY_VALUE;
		fieldHyperlink.setText(val);
		horizontalPanel.remove(sgstField);
		horizontalPanel.add(fieldHyperlink);
		QuestionDef qtn = formDef.getQuestionWithText(txtField.getText());
		if(qtn != null)
			itemSelectionListener.onItemSelected(this,qtn);
	}

	private void setupPopup(){
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

		for(int i=0; i<formDef.getPageCount(); i++)
			FormDesignerUtil.loadQuestions(false, formDef.getPageAt(i).getQuestions(), (forDynamicOptions ? dynamicQuestionDef : questionDef),oracle,forDynamicOptions);

		txtField = new TextBox();
		sgstField = new SuggestBox(oracle,txtField);
		selectFirstQuestion();

		sgstField.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>(){
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event){
				stopSelection();
			}
		});
	}

	public void selectQuestion(QuestionDef questionDef){
		fieldHyperlink.setText(questionDef.getText());
		itemSelectionListener.onItemSelected(this, questionDef);
	}

	private void selectFirstQuestion(){
		for(int i=0; i<formDef.getPageCount(); i++){
			if(selectFirstQuestion(formDef.getPageAt(i).getQuestions()))
				return;
		}
	}

	private boolean selectFirstQuestion(Vector<QuestionDef> questions){
		for(int i=0; i<questions.size(); i++){
			QuestionDef questionDef = (QuestionDef)questions.elementAt(i);

			if(questionDef.getDataType() == QuestionType.REPEAT)
				selectFirstQuestion(questionDef.getRepeatQtnsDef().getQuestions());
			else{
				if(forDynamicOptions){
					if(questionDef == dynamicQuestionDef)
						continue;

					if(!(questionDef.getDataType() == QuestionType.LIST_EXCLUSIVE ||
							questionDef.getDataType() == QuestionType.LIST_EXCLUSIVE_DYNAMIC))
						continue;
				}

				if(this.questionDef == questionDef)
					continue;

				selectQuestion(questionDef);
				return true;
			}
		}
		return false;
	}


	/**
	 * Sets the question for this widget.
	 * 
	 * @param questionDef the question definition object.
	 */
	public void setQuestion(QuestionDef questionDef){
		this.questionDef = questionDef;

		if(questionDef != null)
			fieldHyperlink.setText(questionDef.getText());
		else{
			horizontalPanel.remove(fieldHyperlink);
			horizontalPanel.remove(sgstField);

			//Removing and adding of fieldHyperlink is to prevent a wiered bug from
			//happening where focus is taken off, brought back and the hyperlink
			//displays no more text.
			horizontalPanel.add(fieldHyperlink);
			fieldHyperlink.setText("");
		}
	}

	public void setForDynamicOptions(boolean forDynamicOptions){
		this.forDynamicOptions = forDynamicOptions;
	}

	public void setDynamicQuestionDef(QuestionDef dynamicQuestionDef){
		this.dynamicQuestionDef = dynamicQuestionDef;
	}

	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
}
