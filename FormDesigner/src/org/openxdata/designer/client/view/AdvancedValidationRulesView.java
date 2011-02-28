package org.openxdata.designer.client.view;

import java.util.List;

import org.openxdata.designer.client.controller.IConditionController;
import org.openxdata.designer.client.controller.ItemSelectionListener;
import org.openxdata.sharedlib.client.locale.FormsConstants;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.PageDef;
import org.openxdata.sharedlib.client.model.QuestionDef;
import org.openxdata.sharedlib.client.util.FormUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdvancedValidationRulesView extends Composite implements SelectionHandler<TreeItem>,ClickHandler{

	private FormsConstants constants = GWT.create(FormsConstants.class);
	
	/** The main or root widget. */
	private VerticalPanel verticalPanel = new VerticalPanel();

	/** The widget horizontal spacing in horizontal panels. */
	private static final int HORIZONTAL_SPACING = 5;
	
	/*text box for add an expression*/
	private TextArea expressionEditor = new TextArea();
	
	/* ok button*/
	private Button okBtn = new Button(constants.ok());
	/* cancel button*/
	private Button cancelBtn = new Button(constants.cancel());
	/* help button*/
	private Button helpBtn = new Button(constants.help());
	/* List box to hold expression elements*/
	protected ListBox expressionElements = new ListBox(true);
	/* label to post description*/
	private Label descriptionLabel = new Label("Description");
	/** The form definition object that this validation rule belongs to. */
	private FormDef formDef;
	
	/* Tree to keep left expression group elements*/
	private Tree tree;
	/*left tree items*/
	private TreeItem questions;
	private TreeItem operators;
	private TreeItem commonFunctions;
	private List<QuestionDef> questionElements;
	public AdvancedValidationRulesView(){
		setupWidgets();
		
	}
	
	/**
	 * Sets up the widgets.
	 */
	private void setupWidgets(){
		expressionElements.setHeight("150px");
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(HORIZONTAL_SPACING);
		FormUtil.maximizeWidget(expressionEditor);
		horizontalPanel.add(expressionEditor);
		horizontalPanel.setWidth("100%");
		verticalPanel.add(horizontalPanel);
		
		FlexTable buttonTable = new FlexTable();
		ScrollPanel spanel = new ScrollPanel(addTree(tree));
		spanel.setSize("100%", "150px");
		buttonTable.setWidget(0,0,spanel);
		buttonTable.setWidget(0,1,expressionElements);
		descriptionLabel.setStyleName("validation-description-label");
		buttonTable.setWidget(0,2,descriptionLabel);
		
		okBtn.addClickHandler(this);
		cancelBtn.addClickHandler(this);
		helpBtn.addClickHandler(this);
		buttonTable.setWidget(1,1,okBtn);
		buttonTable.setWidget(1, 2, helpBtn);
		expressionElements.setWidth("100%");
		
		FormUtil.maximizeWidget(buttonTable);
		verticalPanel.add(buttonTable);
		
		FlexCellFormatter cellFormatter = buttonTable.getFlexCellFormatter();
		cellFormatter.setStyleName(0, 2,"validation-description-label");
		cellFormatter.setHorizontalAlignment(1,1,HasHorizontalAlignment.ALIGN_RIGHT);
		cellFormatter.setWidth(0,0,"40%");
		cellFormatter.setWidth(0,1,"30%");
		
		expressionElements.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				appendToExprEditor();
			}
		});
		expressionElements.addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER){	
					appendToExprEditor();
				}
				
			}
		});
		initWidget(verticalPanel);
		
	}

	public void okAction(){
		descriptionLabel.setText(expressionEditor.getText());
	}
	 
	public void addQuestions(PageDef pageDef){
		questionElements = pageDef.getQuestions();	
		/*check whether question node is selected on the tree 
		and update accordingly */
		if(questions.isSelected()){
			exprElements("Questions");
		}
	}
	
	public void appendToExprEditor(){
		if(expressionElements.getItemCount()> 0){
		String selected = expressionElements.getItemText(expressionElements.getSelectedIndex());
		descriptionLabel.setText(selected);
		expressionEditor.setText(expressionEditor.getText()+" "+selected);	
		}			
	}
	
	public void updateValidationRule(){
		if(formDef == null){
			return;
		}
		System.out.println(expressionEditor.getText());
	}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		if(sender == okBtn){
			okAction();
		}
	}

	public Tree addTree(Tree tree){
		tree = new Tree();
		tree.addSelectionHandler(this);
		tree.ensureSelectedItemVisible();
		questions = new TreeItem("Questions");
		
		operators = new TreeItem("Operators");
		operators.addItem("Arithmetic");
		operators.addItem("Comparison");
		operators.addItem("Logical/bitwise");
		operators.addItem("Bit Shift");
		
		commonFunctions = new TreeItem("Common Functions");
		commonFunctions.addItem("Text");
		commonFunctions.addItem("Date & Time");
		commonFunctions.addItem("Math");
		
		tree.addItem(questions);
		tree.addItem(operators);
		tree.addItem(commonFunctions);
		
		return tree;
	}
	
	public void exprElements(String selected){
		if(selected.equals("Questions")){			
			expressionElements.clear();
			if (questionElements == null)
				return;
			for(int i=0;i<questionElements.size();++i){	
				expressionElements.addItem(questionElements.get(i).getText());
			}
			
		}else if(selected.equals("Arithmetic")){
			expressionElements.clear();
			expressionElements.addItem("+");
			expressionElements.addItem("-");
			expressionElements.addItem("/");
			expressionElements.addItem("*");
		}else if(selected.equals("Comparison")){
			expressionElements.clear();
			expressionElements.addItem("=");
			expressionElements.addItem("==");
			expressionElements.addItem("!=");
			expressionElements.addItem(">");
			expressionElements.addItem("<");
		}else if(selected.equals("Logical/bitwise")){
			expressionElements.clear();
			expressionElements.addItem("And");
			expressionElements.addItem("Not");
			expressionElements.addItem("Or");
			expressionElements.addItem("Xor");		
			expressionElements.addItem("AndAlso");	
			expressionElements.addItem("OrElse");
		}else if(selected.equals("Bit Shift")){
			expressionElements.clear();
			expressionElements.addItem(">>");
			expressionElements.addItem("<<");
		}else if(selected.equals("Text")){
			expressionElements.clear();
			expressionElements.addItem("{");
			expressionElements.addItem("}");
			expressionElements.addItem(",");
			expressionElements.addItem("(");
			expressionElements.addItem(")");
			expressionElements.addItem("$");
		}else if(selected.equals("Date & Time")){
			expressionElements.clear();
			expressionElements.addItem("Today()");
			expressionElements.addItem("now()");
		}else if(selected.equals("Math")){
			expressionElements.clear();
			expressionElements.addItem("sum");
			expressionElements.addItem("diff");
			expressionElements.addItem("product");
			expressionElements.addItem("tan");
			expressionElements.addItem("cos");
			expressionElements.addItem("sin");
		}
	}
// construct an expression
	public void buildRule(String expression){
		StringBuilder sb = new StringBuilder();
		sb.append("");
	}
	// receive events on the tree item
	@Override
	public void onSelection(SelectionEvent<TreeItem> event) {
		TreeItem itm = event.getSelectedItem();
		exprElements(itm.getText().trim());
		
	}

}
