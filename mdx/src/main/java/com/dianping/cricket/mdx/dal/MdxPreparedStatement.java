package com.dianping.cricket.mdx.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.olap4j.mdx.SelectNode;

import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.PreparedStatement;


public class MdxPreparedStatement extends PreparedStatement {
	private SelectNode select;

	public MdxPreparedStatement(MySQLConnection connection, String catalog, SelectNode select)
			throws SQLException {
		super(connection, catalog);
		this.select = select;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return super.executeQuery();
	}
}
