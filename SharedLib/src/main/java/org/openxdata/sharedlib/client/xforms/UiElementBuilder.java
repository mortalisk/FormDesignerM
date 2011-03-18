package org.openxdata.sharedlib.client.xforms;

import java.util.List;

import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.OptionDef;
import org.openxdata.sharedlib.client.model.QuestionDef;
import org.openxdata.sharedlib.client.model.RepeatQtnsDef;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * Builds xforms UI elements like input, select, select1, etc. 
 * from question definition objects of the form model.
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class UiElementBuilder {

	/**
	 * All methods in this class are static and hence we expect no external
	 * Instantiation of this class.
	 */
	private UiElementBuilder(){

	}
	
	
	/**
	 * Converts a question definition object to xforms.
	 * 
	 * @param qtn the question definition object.
	 * @param doc the xforms document.
	 * @param xformsNode the root node of the xforms document.
	 * @param formDef the form definition object to which the question belongs.
	 * @param formNode the xforms instance data node.
	 * @param modelNode the xforms model node.
	 * @param groupNode the xforms group node to which the question belongs.
	 */
	public static void fromQuestionDef2Xform(QuestionDef qtn, Document doc, Element xformsNode, FormDef formDef, Element formNode, Element modelNode, Element groupNode){
		Element dataNode =  XformBuilderUtil.fromVariableName2Node(doc,qtn.getBinding(),formDef,formNode);
		if(qtn.getDefaultValue() != null && qtn.getDefaultValue().trim().length() > 0)
			dataNode.appendChild(doc.createTextNode(qtn.getDefaultValue()));
		qtn.setDataNode(dataNode);

		Element bindNode =  doc.createElement(XformConstants.NODE_NAME_BIND);
		String id = XformBuilderUtil.getBindIdFromVariableName(qtn.getBinding(),false);
		bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, id);

		String nodeset = qtn.getBinding();
		if(!nodeset.startsWith("/"))
			nodeset = "/" + nodeset;
		if(!nodeset.startsWith("/" + formDef.getBinding() + "/"))
			nodeset = "/" + formDef.getBinding() + "/" + qtn.getBinding();
		bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_NODESET, nodeset);

		if(qtn.getDataType() != QuestionType.REPEAT) {
            QuestionType type = qtn.getDataType();
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_TYPE, XformBuilderUtil.getXmlType(type,bindNode));
        }
		if(qtn.isRequired())
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_REQUIRED, XformConstants.XPATH_VALUE_TRUE);
		if(!qtn.isEnabled())
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_READONLY, XformConstants.XPATH_VALUE_TRUE);
		if(qtn.isLocked())
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_LOCKED, XformConstants.XPATH_VALUE_TRUE);
		if(!qtn.isVisible())
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_VISIBLE, XformConstants.XPATH_VALUE_FALSE);

		String bindAttributeName = XformConstants.ATTRIBUTE_NAME_REF;
		if(!groupNode.getNodeName().equals(XformConstants.NODE_NAME_REPEAT)){
			modelNode.appendChild(bindNode);
			qtn.setBindNode(bindNode);
			bindAttributeName = XformConstants.ATTRIBUTE_NAME_BIND;
		}	

		Element uiNode =  getXformUIElement(doc,qtn,bindAttributeName,false);
		groupNode.appendChild(uiNode);

		qtn.setControlNode(uiNode);

		Element labelNode =  doc.createElement(XformConstants.NODE_NAME_LABEL);
		labelNode.appendChild(doc.createTextNode(qtn.getText()));
		uiNode.appendChild(labelNode);
		qtn.setLabelNode(labelNode);

		addHelpTextNode(qtn,doc,uiNode,null);

		if(qtn.getDataType() != QuestionType.REPEAT){
			if(qtn.getDataType() == QuestionType.LIST_EXCLUSIVE_DYNAMIC)
				qtn.setFirstOptionNode(ItemsetBuilder.createDynamicOptionDefNode(doc,uiNode));
			else{
				List<OptionDef> options = qtn.getOptions();
				if(options != null && options.size() > 0){
					for(int j=0; j<options.size(); j++){
						OptionDef optionDef = (OptionDef)options.get(j);
						Element itemNode = fromOptionDef2Xform(optionDef,doc,uiNode);	
						if(j == 0)
							qtn.setFirstOptionNode(itemNode);
					}
				}
			}
		}
		else{
			Element repeatNode =  doc.createElement(XformConstants.NODE_NAME_REPEAT);
			repeatNode.setAttribute(XformConstants.ATTRIBUTE_NAME_BIND, id);
			uiNode.appendChild(repeatNode);
			qtn.setControlNode(repeatNode);

			RepeatQtnsDef rptQtns = qtn.getRepeatQtnsDef();
			for(int j=0; j<rptQtns.size(); j++)
				createQuestion(rptQtns.getQuestionAt(j),repeatNode,dataNode,doc);
		}
	}


	/**
	 * Creates an xforms ui node for a child question of a parent repeat question type.
	 * 
	 * @param qtnDef the child question definition object.
	 * @param parentControlNode the ui node of the parent repeat question.
	 * @param parentDataNode the data node of the parent repeat question.
	 * @param doc the xforms document.
	 */
	private static void createQuestion(QuestionDef qtnDef, Element parentControlNode, Element parentDataNode, Document doc){
		String name = qtnDef.getBinding();

		//TODO Should do this for all invalid characters in node names.
		name = name.replace("/", "");
		name = name.replace("\\", "");
		name = name.replace(" ", "");

		Element dataNode =  doc.createElement(name);
		if(qtnDef.getDefaultValue() != null && qtnDef.getDefaultValue().trim().length() > 0)
			dataNode.appendChild(doc.createTextNode(qtnDef.getDefaultValue()));
		parentDataNode.appendChild(dataNode);
		qtnDef.setDataNode(dataNode);

		Element inputNode =  getXformUIElement(doc,qtnDef,XformConstants.ATTRIBUTE_NAME_REF,true);
        QuestionType type = qtnDef.getDataType();
		inputNode.setAttribute(XformConstants.ATTRIBUTE_NAME_TYPE, XformBuilderUtil.getXmlType(type,inputNode));
		if(qtnDef.isRequired())
			inputNode.setAttribute(XformConstants.ATTRIBUTE_NAME_REQUIRED, XformConstants.XPATH_VALUE_TRUE);
		if(!qtnDef.isEnabled())
			inputNode.setAttribute(XformConstants.ATTRIBUTE_NAME_READONLY, XformConstants.XPATH_VALUE_TRUE);
		if(qtnDef.isLocked())
			inputNode.setAttribute(XformConstants.ATTRIBUTE_NAME_LOCKED, XformConstants.XPATH_VALUE_TRUE);
		if(!qtnDef.isVisible())
			inputNode.setAttribute(XformConstants.ATTRIBUTE_NAME_VISIBLE, XformConstants.XPATH_VALUE_FALSE);

		parentControlNode.appendChild(inputNode);
		qtnDef.setControlNode(inputNode);
		qtnDef.setBindNode(inputNode);

		Element labelNode =  doc.createElement(XformConstants.NODE_NAME_LABEL);
		labelNode.appendChild(doc.createTextNode(qtnDef.getText()));
		inputNode.appendChild(labelNode);
		qtnDef.setLabelNode(labelNode);

		addHelpTextNode(qtnDef,doc,inputNode,null);

		if(qtnDef.getDataType() != QuestionType.REPEAT){
			List<OptionDef> options = qtnDef.getOptions();
			if(options != null && options.size() > 0){
				for(int index=0; index<options.size(); index++){
					OptionDef optionDef = (OptionDef)options.get(index);
					Element itemNode = fromOptionDef2Xform(optionDef,doc,inputNode);	
					if(index == 0)
						qtnDef.setFirstOptionNode(itemNode);
				}
			}
		}
	}


	/**
	 * Gets the xforms ui node for a given question definition object.
	 * 
	 * @param doc the xforms document.
	 * @param qtnDef the question definition object.
	 * @param bindAttributeName the attribute name for binding. Could be "bind" or "ref".
	 * @param isRepeatKid set to true if this question is a child of another repeat question type.
	 * @return the xforms ui node.
	 */
	private static Element getXformUIElement(Document doc, QuestionDef qtnDef, String bindAttributeName, boolean isRepeatKid){

		String name = XformConstants.NODE_NAME_INPUT;

		int type = qtnDef.getDataType().getLegacyConstant();
		if(type == QuestionDef.QTN_TYPE_LIST_EXCLUSIVE || type == QuestionDef.QTN_TYPE_LIST_EXCLUSIVE_DYNAMIC)
			name = XformConstants.NODE_NAME_SELECT1;
		else if(type == QuestionDef.QTN_TYPE_LIST_MULTIPLE)
			name = XformConstants.NODE_NAME_SELECT;
		else if(type == QuestionDef.QTN_TYPE_REPEAT)
			name = XformConstants.NODE_NAME_GROUP;
		else if(type == QuestionDef.QTN_TYPE_IMAGE || type == QuestionDef.QTN_TYPE_AUDIO || type == QuestionDef.QTN_TYPE_VIDEO)
			name = XformConstants.NODE_NAME_UPLOAD;

		String id = XformBuilderUtil.getBindIdFromVariableName(qtnDef.getBinding(), isRepeatKid);
		Element node = doc.createElement(name);
		if(type != QuestionDef.QTN_TYPE_REPEAT)
			node.setAttribute(bindAttributeName, id);
		else
			node.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, qtnDef.getBinding());

		//Make ODK happy.
		setMediaType(node, type);

		return node;
	}
	
	public static void setMediaType(Element node, int type){
		String mediatype = null;
		if(type == QuestionDef.QTN_TYPE_IMAGE)
			mediatype = XformConstants.ATTRIBUTE_VALUE_IMAGE;
		else if(type == QuestionDef.QTN_TYPE_AUDIO)
			mediatype = XformConstants.ATTRIBUTE_VALUE_AUDIO;
		else if(type == QuestionDef.QTN_TYPE_VIDEO)
			mediatype = XformConstants.ATTRIBUTE_VALUE_VIDEO;
		
		if(mediatype != null)
			node.setAttribute(XformConstants.ATTRIBUTE_NAME_MEDIATYPE, mediatype + "/*");
	}


	/**
	 * Converts an option definition object to xforms.
	 * 
	 * @param optionDef the option definition object.
	 * @param doc the xforms document.
	 * @param uiNode the xforms ui node of the question to which this option belongs.
	 * @return the item node of the option definition object.
	 */
	public static Element fromOptionDef2Xform(OptionDef optionDef, Document doc, Element uiNode){
		Element itemNode =  doc.createElement(XformConstants.NODE_NAME_ITEM);
		itemNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, optionDef.getVariableName());

		Element node =  doc.createElement(XformConstants.NODE_NAME_LABEL);
		node.appendChild(doc.createTextNode(optionDef.getText()));
		itemNode.appendChild(node);
		optionDef.setLabelNode(node);

		node =  doc.createElement(XformConstants.NODE_NAME_VALUE);
		node.appendChild(doc.createTextNode(optionDef.getVariableName()));
		itemNode.appendChild(node);
		optionDef.setValueNode(node);

		uiNode.appendChild(itemNode);
		optionDef.setControlNode(itemNode);
		return itemNode;
	}


	/**
	 * Sets the xforms help text or hint node for a question.
	 * 
	 * @param qtn the question definition object.
	 * @param doc the xforms document.
	 * @param inputNode the xforms ui node.
	 * @param firstOptionNode the first option node if a single or multiple select question type.
	 */
	public static void addHelpTextNode(QuestionDef qtn, Document doc, Element inputNode, Element firstOptionNode){
		String helpText = qtn.getHelpText();
		if(helpText != null && helpText.length() > 0){
			Element hintNode =  doc.createElement(XformConstants.NODE_NAME_HINT);
			hintNode.appendChild(doc.createTextNode(helpText));
			if(firstOptionNode == null)
				inputNode.appendChild(hintNode);
			else
				inputNode.insertBefore(hintNode, firstOptionNode);
			qtn.setHintNode(hintNode);
		}
	}
}
