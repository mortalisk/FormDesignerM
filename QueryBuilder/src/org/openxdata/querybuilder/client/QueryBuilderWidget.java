package org.openxdata.querybuilder.client;

import org.openxdata.querybuilder.client.view.QueryBuilderView;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class QueryBuilderWidget extends Composite{

	private DockPanel dockPanel = new DockPanel();
	//private QueryBuilderController controller;
	private QueryBuilderView view = new QueryBuilderView();
	
	public QueryBuilderWidget(){
		
		dockPanel.setWidth("100%");
		dockPanel.setHeight("100%");
		dockPanel.add(view, DockPanel.CENTER);
		initWidget(dockPanel);
		//controller = new QueryBuilderController();
	}
	
	public void setEmbeddedHeightOffset(int offset){
		//view.setEmbeddedHeightOffset(offset);
	}
	
	public void setXform(String xml){
		view.setXform(xml);
	}
	
	public void setQueryDef(String xml){
		view.setQueryDef(xml);
	}
	
	public void setSql(String sql){
		view.setSql(sql);
	}
	
	public String getQueryDef(){
		return view.getQueryDef();
	}
	
	public String getSql(){
		return view.getSql();
	}
	
	public void load(){
		view.load();
	}
	
	public void hideDebugTabs(){
		view.hideDebugTabs();
	}
}
