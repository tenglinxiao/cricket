package com.dianping.cricket.queryengine.sql;

import java.util.List;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataMessageException;
import org.apache.olingo.odata2.api.uri.SelectItem;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;
import org.apache.olingo.odata2.api.uri.expression.ExpressionParserException;
import org.apache.olingo.odata2.api.uri.expression.FilterExpression;
import org.apache.olingo.odata2.api.uri.expression.OrderByExpression;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;

import com.dianping.platform.metadata.mysql.Table;

/**
 * Base class for sql builder.
 * @author tenglinxiao
 * @since 1.0
 */
public abstract class SqlBuilder {
	// Statement builder
	protected StringBuilder sqlStatement = new StringBuilder();
	// Table name.
	protected Table table;
	// Select
	protected List<SelectItem> select;
	// Filter.
	protected FilterExpression filter;
	// OrderBy
	protected OrderByExpression orderBy;
	// Skip
	protected int skip = 0;
	// Top (eg. mysql => limit)
	protected int top = 0;
	
	public SqlBuilder (GetEntitySetUriInfo uriInfo) {
		this.select = uriInfo.getSelect();
		this.filter = uriInfo.getFilter();
		this.orderBy = uriInfo.getOrderBy();
		if (uriInfo.getSkip() != null) {
			this.skip = uriInfo.getSkip();
		}
		if (uriInfo.getTop() != null) {
			this.top = uriInfo.getTop();
		}
	}
	
	public SqlBuilder (GetEntityUriInfo uriInfo) {
		this.select = uriInfo.getSelect();
		this.filter = uriInfo.getFilter();
	}
	
	/**
	 * Set the table for the builder.
	 * @param table
	 */
	public void table(Table table) {
		this.table = table;
	}
	
	public void select(List<SelectItem> select) {
		this.select = select;
	}
	
	public void filter(FilterExpression filter) {
		this.filter = filter;
	}
	
	public void orderBy(OrderByExpression orderBy) {
		this.orderBy = orderBy;
	}
	
	public void skip(int skip) {
		this.skip = skip;
	}
	
	public void top(int top) {
		this.top = top;
	}
	
	/**
	 * Method for build select clause.
	 * @throws EdmException
	 */
	protected abstract void buildSelect() throws EdmException;
	
	/**
	 * Method for build filter clause.
	 * @throws ExceptionVisitExpression
	 * @throws ODataApplicationException
	 * @throws ExpressionParserException
	 * @throws ODataMessageException
	 */
	protected abstract void buildFilter() throws ExceptionVisitExpression, ODataApplicationException, ExpressionParserException, ODataMessageException;
	
	/**
	 * Method for build order by clause.
	 */
	protected abstract void buildOrderBy();
	
	/**
	 * Method for build skip clause.
	 */
	protected abstract void buildSkip();
	
	/**
	 * Method for build top clause.
	 */
	protected abstract void buildTop();
	
	/**
	 * Method that really take the ops to build sql.
	 * @return sql statement.
	 */
	public String build()
	{
		// Call the build method one by one to complete the building.
		try {
			buildSelect();
			buildFilter();
			buildOrderBy();
			buildTop();
			buildSkip();
			return sqlStatement.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
