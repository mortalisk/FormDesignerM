package org.openxdata.designer.server.util;

import java.util.Vector;

import org.openxdata.designer.server.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class LanguageUtil {

	public static final String ATTRIBUTE_NAME_XPATH = "xpath";
	public static final String ATTRIBUTE_NAME_VALUE = "value";


	/*public static String translate(String srcXml, String languageXml){
		return translate(XMLParser.parse(srcXml),XMLParser.parse(languageXml).getDocumentElement());
	}*/

	public static String translate(Document doc, Node parentLangNode){
		NodeList nodes = parentLangNode.getChildNodes();
		for(int index = 0; index < nodes.getLength(); index++){
			Node node = nodes.item(index);
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue;

			String xpath = ((Element)node).getAttribute(ATTRIBUTE_NAME_XPATH);
			String value = ((Element)node).getAttribute(ATTRIBUTE_NAME_VALUE);
			if(xpath == null || value == null)
				continue;

			Vector<?> result = new XPathExpression(doc, xpath).getResult();
			if(result != null){
				
				//TODO We need to uniquely identify nodes and so each xpath should
				//point to no more than one node.
				if(result.size() > 1)
					continue;
				
				for(int item = 0; item < result.size(); item++){
					Element targetNode = (Element)result.get(item);
					int pos = xpath.lastIndexOf('@');
					if(pos > 0 && xpath.indexOf('=',pos) < 0){
						String attributeName = xpath.substring(pos + 1, xpath.indexOf(']',pos));
						targetNode.setAttribute(attributeName, value);
					}
					else
						targetNode.setTextContent(value);
				}
			}
		}
		return doc.toString();
	}
}
