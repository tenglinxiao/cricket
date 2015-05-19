package com.dianping.cricket.mdx.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.olap4j.mdx.SelectNode;

import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.StatementImpl;


public class MdxStatement extends StatementImpl {
	private SelectNode select;

	public MdxStatement(MySQLConnection connection, String catalog, SelectNode select) throws SQLException {
		super(connection, catalog);
		this.select = select;
	}

	@Override
	public ResultSet executeQuery(String mdx) throws SQLException {
		this.getConnection();
		return super.executeQuery(mdx);
	}
}
