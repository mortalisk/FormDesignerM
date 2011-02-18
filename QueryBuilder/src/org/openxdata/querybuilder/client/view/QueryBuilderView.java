package org.openxdata.querybuilder.client.view;

import org.openxdata.querybuilder.client.sql.SqlBuilder;
import org.openxdata.querybuilder.client.sql.XmlBuilder;
import org.openxdata.sharedlib.client.OpenXdataConstants;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.util.FormUtil;
import org.openxdata.sharedlib.client.xforms.XformParser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.TextArea;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class QueryBuilderView  extends Composite implements SelectionHandler<Integer>,ResizeHandler{

	private int selectedTabIndex;
	private DecoratedTabPanel tabs = new DecoratedTabPanel();
	private TextArea txtXform = new TextArea();
	private TextArea txtDefXml= new TextArea();
	private TextArea txtSql = new TextArea();
	
	private FilterConditionsView filterConditionsView = new FilterConditionsView();
	private DisplayFieldsView displayFieldsView = new DisplayFieldsView();
	
	public QueryBuilderView(){
		
		txtXform.setWidth("100%");
		txtXform.setHeight("100%");
		tabs.setWidth("100%");
		tabs.setHeight("100%");
		
		tabs.add(txtXform,"XForms Source");
		tabs.add(filterConditionsView,"Filter Conditions");
		tabs.add(displayFieldsView,"Display Fields");
		tabs.add(txtDefXml,"Definition XML");
		tabs.add(txtSql,"SQL");
		
		tabs.addSelectionHandler(this);
		initWidget(tabs);
		
		tabs.selectTab(1);
		
		Window.addResizeHandler(this);

		//		This is needed for IE
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				onWindowResized(Window.getClientWidth(), Window.getClientHeight());
			}
		});
		
		txtXform.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				parseXform();
			}
		});
		
		txtDefXml.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event){
				parseQueryDef();
			}
		});
		
		//txtXform.setText(FormUtil.formatXml(getTestXform()));
		//parseXform();
		
		//txtDefXml.setText(getTestQueryDef());
		//parseQueryDef();
	}

	/**
	 * @see com.google.gwt.event.logical.shared.SelectionHandler#onSelection(SelectionEvent)
	 */
	public void onSelection(SelectionEvent<Integer> event){
		selectedTabIndex = event.getSelectedItem();
		
		FormUtil.dlg.setText("Building " + (selectedTabIndex == 3 ? "Query Definition" : "SQL")); //LocaleText.get("???????")
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				try{
					if(selectedTabIndex == 3)
						buildQueryDef();
					else if(selectedTabIndex == 4)
						buildSql();

					FormUtil.dlg.hide();
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}	
			}
		});
	}
	
	public void onWindowResized(int width, int height) {
		txtXform.setHeight(height-50+OpenXdataConstants.UNITS);
		txtDefXml.setHeight(height-50+OpenXdataConstants.UNITS);
		txtSql.setHeight(height-50+OpenXdataConstants.UNITS);
	} 
	
	private void parseXform(){
		FormUtil.dlg.setText("Parsing Xform");
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				try{
					FormDef formDef = null;
					String xml = txtXform.getText().trim();
					if(xml.length() > 0)
						formDef = XformParser.fromXform2FormDef(xml);

					filterConditionsView.setFormDef(formDef);
					displayFieldsView.setFormDef(formDef);
					
					FormUtil.dlg.hide();
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}	
			}
		});
	}
	
	private void parseQueryDef(){
		FormUtil.dlg.setText("Parsing Query Definition"); 
		FormUtil.dlg.center();

		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			public void execute() {
				try{
					String xml = txtDefXml.getText().trim();
					if(xml.length() > 0){
						filterConditionsView.loadQueryDef(xml);
						displayFieldsView.loadQueryDef(xml);
					}
					
					FormUtil.dlg.hide();
				}
				catch(Exception ex){
					FormUtil.displayException(ex);
				}	
			}
		});
	}
	
	private void buildSql(){
		txtSql.setText(SqlBuilder.buildSql(filterConditionsView.getFormDef(),displayFieldsView.getDisplayFields(),filterConditionsView.getFilterConditionRows(),displayFieldsView.getSortFields()));
	}
	
	private void buildQueryDef(){
		txtDefXml.setText(FormUtil.formatXml(FormUtil.formatXml(XmlBuilder.buildXml(filterConditionsView.getFormDef(),filterConditionsView.getFilterConditionRows(),displayFieldsView.getDisplayFields(),displayFieldsView.getSortFields()))));
	}
	
	public String getQueryDef(){
		buildQueryDef();
		return txtDefXml.getText();
	}
	
	public String getSql(){
		buildSql();
		return txtSql.getText();
	}
	
	public void setXform(String xml){
		txtXform.setText(xml);
		//parseXform();
	}
	
	public void setQueryDef(String xml){
		txtDefXml.setText(xml);
		//parseQueryDef();
	}
	
	public void setSql(String sql){
		txtSql.setText(sql);
	}
	
	public void load(){
		parseXform();
		parseQueryDef();
	}
	
	public void onResize(ResizeEvent event){
		onWindowResized(Window.getClientWidth(), Window.getClientHeight());
	}
	
	public void hideDebugTabs(){
		tabs.remove(0);
		tabs.remove(2);
		tabs.remove(2);
	}
}
