package org.openxdata.querybuilder.client.sql;

import java.util.List;

import org.openxdata.querybuilder.client.model.DisplayField;
import org.openxdata.querybuilder.client.model.FilterCondition;
import org.openxdata.querybuilder.client.model.FilterConditionGroup;
import org.openxdata.querybuilder.client.model.FilterConditionRow;
import org.openxdata.querybuilder.client.model.SortField;
import org.openxdata.querybuilder.client.widget.GroupHyperlink;
import org.openxdata.sharedlib.client.model.FormDef;
import org.openxdata.sharedlib.client.model.ModelConstants;
import org.openxdata.sharedlib.client.model.Operator;
import org.openxdata.sharedlib.client.model.QuestionDef;
import org.openxdata.sharedlib.client.model.QuestionType;


/**
 * 
 *  www.openxdata.org - Licensed as written in license.txt and original sources of this file and its authors are found in sources.txt.
 *
 */
public class SqlBuilder {

	private static String DATE_SEPARATOR = "'";
	private static  String LIKE_SEPARATOR = "%";

	public static String buildSql(FormDef formDef, List<DisplayField> displayFields, FilterConditionGroup filterConditionGroup, List<SortField> sortFields){
		if(formDef == null || filterConditionGroup == null)
			return null;

		String sql = "SELECT " + getSelectList(displayFields) + " \r\nFROM " + formDef.getBinding();

		String filter = "";
		if(filterConditionGroup.getConditionCount() > 0)
			filter = getFilter(filterConditionGroup);

		if(filter.length() > 0)
			sql = sql + " \r\nWHERE " + filter;
		
		String groupByClause = getGroupByClause(displayFields);
		if(groupByClause != null)
			sql = sql + " \r\nGROUP BY " + groupByClause;

		
		String orderByClause = getOrderByClause(sortFields);
		if(orderByClause != null)
			sql = sql + " \r\nORDER BY " + orderByClause;

		return sql;
	}
	
	private static String getSelectList(List<DisplayField> displayFields){
		if(displayFields == null || displayFields.size() == 0)
			return "*";
		
		String selectList = null;
		
		for(DisplayField field : displayFields){
			if(selectList == null)
				selectList = "";
			else
				selectList += ",";
			
			String aggFunc = field.getAggFunc();
			if(aggFunc != null)
				selectList += aggFunc + "(";
			
			selectList += field.getName();
			
			if(aggFunc != null)
				selectList +=")";
			
			selectList += " AS '" + field.getText()+"'";
		}
		
		return selectList;
	}
	
	private static String getGroupByClause(List<DisplayField> displayFields){
		if(displayFields == null || displayFields.size() == 0)
			return null;
		
		int aggFuncCount = 0;
		String groupByClause = null;
		for(DisplayField field : displayFields){
			if(groupByClause == null && field.getAggFunc() == null)
				groupByClause = "";
			else if(field.getAggFunc() == null)
				groupByClause += ",";
			
			if(field.getAggFunc() == null)
				groupByClause += field.getName();
			else
				aggFuncCount++;
		}
		
		if(aggFuncCount > 0 && aggFuncCount < displayFields.size())
			return groupByClause;
		return null;
	}
	
	private static String getOrderByClause(List<SortField> sortFields){
		if(sortFields == null || sortFields.size() == 0)
			return null;
		
		String orderByClause = null;
		
		for(SortField field : sortFields){
			if(orderByClause == null)
				orderByClause = "";
			else
				orderByClause += ",";
			
			orderByClause += field.getName() + " " + (field.getSortOrder() == SortField.SORT_ASCENDING ? "ASC" : "DESC");
		}
		
		return orderByClause;
	}

	private static String getFilter(FilterConditionGroup filterGroup){

		String filter = "";

		List<FilterConditionRow> rows = filterGroup.getConditions();
		for(FilterConditionRow row : rows){
			
			if(filter.length() > 0)
				filter += getSQLInnerCombiner(filterGroup.getConditionsOperator());
			
			if(row instanceof FilterConditionGroup)
				filter += getFilter((FilterConditionGroup)row);
			else
				filter += getFilter((FilterCondition)row);
		}
		
		if(filter.length() > 0)
			filter = getSQLOuterCombiner(filterGroup.getConditionsOperator()) + "(" + filter + ")";

		return filter;
	}

	private static String getFilter(FilterCondition condition){		
		String filter = condition.getFieldName();
		filter += getDBOperator(condition.getOperator());
		filter += getQuotedValue(condition.getFirstValue(), condition.getDataType().getLegacyConstant(),condition.getOperator());
		return filter;
	}

	private static String getDBOperator(Operator operator)
	{
		if(operator == Operator.EQUAL){
			return " = ";}
        else if(operator == Operator.NOT_EQUAL){
			return " <> ";}
		else if(operator ==  Operator.LESS){
			return " < ";}
		else if(operator ==  Operator.LESS_EQUAL){
			return " <= ";}
		else if(operator ==  Operator.GREATER){
			return " > ";}
		else if(operator ==  Operator.GREATER_EQUAL){
			return " >= ";}
		else if(operator ==  Operator.IS_NULL){
			return " IS NULL";}
		else if(operator ==  Operator.IS_NOT_NULL){
			return " IS NOT NULL";}
		else if(operator ==  Operator.IN_LIST){
			return " IN (";}
		else if(operator ==  Operator.NOT_IN_LIST){
			return " NOT IN (";}
		else if(operator ==  Operator.STARTS_WITH){
			return " LIKE ";}
		else if(operator ==  Operator.NOT_START_WITH){
			return " NOT LIKE ";}
		else if(operator ==  Operator.CONTAINS){
			return " LIKE ";}
		else if(operator ==  Operator.NOT_CONTAIN){
			return " NOT LIKE ";}
		else if(operator ==  Operator.BETWEEN){
			return " BETWEEN ";}
		else if(operator ==  Operator.NOT_BETWEEN){
			return " NOT BETWEEN ";}
		else if(operator ==  Operator.ENDS_WITH){
			return " LIKE ";}
		else if(operator ==  Operator.NOT_END_WITH){
			return " NOT LIKE ";
		}

		return null;
	}

	private static String getQuotedValue(String fieldVal,int dataType, Operator operator)
	{
		if(operator == Operator.IS_NULL || operator == Operator.IS_NOT_NULL)
			return "";
		
		switch(dataType)
		{
		case QuestionDef.QTN_TYPE_TEXT:
		case QuestionDef.QTN_TYPE_LIST_EXCLUSIVE:
		case QuestionDef.QTN_TYPE_LIST_MULTIPLE:
		case QuestionDef.QTN_TYPE_LIST_EXCLUSIVE_DYNAMIC:
		{
			if(operator == Operator.STARTS_WITH || operator == Operator.NOT_START_WITH)
				return "'" + fieldVal + LIKE_SEPARATOR + "'";
			if(operator == Operator.NOT_END_WITH || operator == Operator.NOT_END_WITH)
				return "'" + LIKE_SEPARATOR + fieldVal + "'";
			if(operator == Operator.CONTAINS || operator == Operator.NOT_CONTAIN)
				return "'" + LIKE_SEPARATOR + fieldVal + LIKE_SEPARATOR  + "'";
			else
				return "'" + fieldVal + "'";
		}
		case QuestionDef.QTN_TYPE_DATE:
			return DATE_SEPARATOR + fieldVal + DATE_SEPARATOR;
		default:
			return " " + fieldVal + " ";
		}
	}

	private static String getSQLInnerCombiner(String val)
	{
		if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_ALL))
			return " AND ";
		else if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_ANY))
			return " OR ";
		else if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_NONE))
			return " OR ";
		else if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_NOT_ALL))
			return " AND ";

		return null;
	}

	private static String getSQLOuterCombiner(String val)
	{
		if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_ALL))
			return "";
		else if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_ANY))
			return "";
		else if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_NONE))
			return " NOT ";
		else if(val.equalsIgnoreCase(GroupHyperlink.CONDITIONS_OPERATOR_TEXT_NOT_ALL))
			return " NOT ";

		return null;
	}
}
