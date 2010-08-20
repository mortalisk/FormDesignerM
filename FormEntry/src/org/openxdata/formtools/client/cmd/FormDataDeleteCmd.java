package org.openxdata.formtools.client.cmd;

import org.openxdata.formtools.client.FormEntryConstants;
import org.openxdata.formtools.client.FormEntryContext;
import org.openxdata.formtools.client.listener.DataLoadListener;
import org.openxdata.formtools.client.listener.FormDataDeleteListener;
import org.openxdata.sharedlib.client.xforms.XmlUtil;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class FormDataDeleteCmd implements DataLoadListener {

	private String dataId;
	private String defId;
	private FormDataDeleteListener listener;
	
	public FormDataDeleteCmd(String dataId, String defId, FormDataDeleteListener listener){
		this.dataId = dataId;
		this.defId = defId;
		this.listener = listener;
		
		FormEntryContext.getDatabaseManager().loadFormDataList(defId, this);
	}
	
	
	public void onDataReceived(final String data){
		
		Document doc = XmlUtil.getDocument(data);
		NodeList nodes = doc.getElementsByTagName(FormEntryConstants.NODE_NAME_DATA);
		for(int index = 0; index < nodes.getLength(); index++){
			Node node = nodes.item(index);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			if(dataId.equals(((Element)node).getAttribute(FormEntryConstants.ATTRIBUTE_NAME_ID))){
				node.getParentNode().removeChild(node);
				break;
			}
		}
		
		FormEntryContext.getDatabaseManager().deleteFormData(dataId);
		FormEntryContext.getDatabaseManager().saveFormDataList(defId, doc.toString());
		
		//Check if this is the last form data for its def and then delete the data list.
		if(nodes.getLength() == 0)
			FormEntryContext.getDatabaseManager().deleteFormDataList(defId);
		
		listener.onFormDataDeleted(dataId);
	}
}
