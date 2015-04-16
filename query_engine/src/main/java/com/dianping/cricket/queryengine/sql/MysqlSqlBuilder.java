package com.dianping.cricket.queryengine.sql;

import java.util.List;

import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.exception.ODataApplicationException;
import org.apache.olingo.odata2.api.exception.ODataMessageException;
import org.apache.olingo.odata2.api.uri.UriParser;
import org.apache.olingo.odata2.api.uri.expression.CommonExpression;
import org.apache.olingo.odata2.api.uri.expression.ExceptionVisitExpression;
import org.apache.olingo.odata2.api.uri.expression.ExpressionParserException;
import org.apache.olingo.odata2.api.uri.expression.OrderExpression;
import org.apache.olingo.odata2.api.uri.expression.SortOrder;
import org.apache.olingo.odata2.api.uri.info.GetEntitySetUriInfo;
import org.apache.olingo.odata2.api.uri.info.GetEntityUriInfo;
import org.springframework.util.StringUtils;

import com.dianping.dataplatform.util.FilterExpressionVisitor;

/**
 * Sql builder for mysql database.
 * @author tenglinxiao
 * @since 1.0
 */
public class MysqlSqlBuilder extends SqlBuilder {
	
	public MysqlSqlBuilder (GetEntitySetUriInfo uriInfo) {
		super(uriInfo);
	}
	
	public MysqlSqlBuilder (GetEntityUriInfo uriInfo) {
		super(uriInfo);
	}

	@Override
	protected void buildSelect() throws EdmException {
		String[] fields = null;
		
		// If no select fields are specified, then select all the fields, otherwise use the fields offered.
		if (select.size() == 0) {
			fields = new String[table.getColumns().size()];
			for (int index = 0; index< table.getColumns().size(); index++) {
				fields[index] = table.getColumns().get(index).getName();
			}
		} else {
			fields = new String[select.size()];
			for (int index = 0; index < select.size(); index++) {
				fields[index] = select.get(index).getProperty().getName();
			}
		}
		
		// Build the select clause.
		sqlStatement.append("select " + StringUtils.arrayToCommaDelimitedString(fields) + " from " + table.getName());
	}

	@Override
	protected void buildFilter() throws ODataApplicationException, ExpressionParserException, ODataMessageException {
		if (filter == null) {
			return;
		}
		
		// Build the where clause for filters.
		String where = (String)filter.accept(new FilterExpressionVisitor());
		sqlStatement.append(" " + where);		
	}

	@Override
	protected void buildOrderBy() {
		if (orderBy == null) {
			return;
		}
		
		List<OrderExpression> orderExps = orderBy.getOrders();
		String[] orders = new String[orderExps.size()];
		for (int index = 0; index < orderExps.size(); index++) {
			String column = orderExps.get(index).getExpression().getUriLiteral();
			SortOrder order = orderExps.get(index).getSortOrder();
			orders[index] = column + " " + order.name();
		}
		
		// Build the orderby clause.
		sqlStatement.append(" order by " +  StringUtils.arrayToCommaDelimitedString(orders));
	}

	@Override
	protected void buildSkip() {
		if (skip == 0) {
			return;
		}
		sqlStatement.append(" offset " + skip);
	}

	@Override
	protected void buildTop() {
		if (top == 0) {
			return;
		}
		sqlStatement.append(" limit " + top);
	}
}
