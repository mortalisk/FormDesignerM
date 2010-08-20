package org.openxdata.formtools.client.xforms;

import java.util.HashMap;
import java.util.Set;

import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.xforms.XmlUtil;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class OpenXdataFormBuilder {

	public static String build(FormDef formDef, HashMap<String,String> localeText){

		Document doc = XMLParser.createDocument();
		doc.appendChild(doc.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\""));

		Element openXdataFormNode = doc.createElement("PurcForm");
		doc.appendChild(openXdataFormNode);

		Element xformNode = doc.createElement("Xform");
		Element node = (Element)formDef.getDoc().getDocumentElement().cloneNode(true);
		doc.importNode(node, true);
		xformNode.appendChild(node);
		openXdataFormNode.appendChild(xformNode);


		String xml = formDef.getLayoutXml();
		if(xml != null && xml.trim().length() > 0){
			node = (Element)XmlUtil.getDocument(xml).getDocumentElement().cloneNode(true);
			doc.importNode(node, true);
			Element layoutNode = doc.createElement("Layout");
			layoutNode.appendChild(node);
			openXdataFormNode.appendChild(layoutNode);
		}
		
		String src = formDef.getJavaScriptSource();
		if(src != null && src.trim().length() > 0){
			Element javaScriptNode = doc.createElement("JavaScript");
			javaScriptNode.appendChild(doc.createCDATASection(src));
			openXdataFormNode.appendChild(javaScriptNode);
		}
		
		openXdataFormNode.appendChild(getLanguageNode(doc,localeText));

		return XmlUtil.fromDoc2String(doc);
	}
	
	
	private static Element getLanguageNode(Document doc, HashMap<String,String> localeText){
		Element languageNode = doc.createElement("Language");
		
		Set<String> locales = localeText.keySet();
		for(String locale : locales){
			Element node = (Element)XmlUtil.getDocument(localeText.get(locale)).getDocumentElement().cloneNode(true);
			doc.importNode(node, true);
			languageNode.appendChild(node);
		}
		
		return languageNode;
	}
	
	
	public static String getCombinedLanguageText(HashMap<String,String> localeText){
		Document doc = XMLParser.createDocument();
		doc.appendChild(doc.createProcessingInstruction("xml", "version=\"1.0\" encoding=\"UTF-8\""));
		doc.appendChild(getLanguageNode(doc,localeText));
		
		return XmlUtil.fromDoc2String(doc);
	}
}
