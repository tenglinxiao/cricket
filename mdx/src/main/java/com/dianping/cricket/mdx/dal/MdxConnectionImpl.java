package com.dianping.cricket.mdx.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.impl.DefaultMdxParserImpl;

import com.dianping.cricket.mdx.MdxDatabase;
import com.dianping.cricket.mdx.exception.MdxSQLException;
import com.mysql.jdbc.MySQLConnection;

public class MdxConnectionImpl implements MdxConnection {
	private static Logger logger = Logger.getLogger(MdxConnection.class);
	private static String JDBC_URL = "jdbc:mysql://%s:%d/%s";
	private Connection connection; 
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.info("No jdbc driver class found on classpath!");
			System.exit(1);
		}
	}
	
	private MdxConnectionImpl(Connection connection)
	{
		this.connection = connection;
	}
	
	public static MdxConnection getConnection(MdxDatabase db)
	{
		try {
			Connection c = DriverManager.getConnection(String.format(JDBC_URL, db.getHost(), db.getPort(), db.getName()), db.getUsername(), db.getPassword());
			return new MdxConnectionImpl(c);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Statement createStetement(String mdx) throws SQLException {
		if (!connection.isClosed()) {
			return new MdxStatement((MySQLConnection)connection, connection.getCatalog(), parseMdx(mdx));
		}
		
		throw new SQLException("Connection is already closed!");
	}

	@Override
	public PreparedStatement preparedStatement(String mdx) throws SQLException {
		if (!connection.isClosed()) {
			return new MdxPreparedStatement((MySQLConnection)connection, connection.getCatalog(), parseMdx(mdx));
		}
		
		throw new SQLException("Connection is already closed!");
	}
	
	private SelectNode parseMdx(String mdx) throws MdxSQLException
	{
		try {
			return new DefaultMdxParserImpl().parseSelect(mdx);
		} catch(RuntimeException e) {
			throw new MdxSQLException(e);
		}
	}
	
	public static void main(String args[]) throws SQLException {
		MdxDatabase database = new MdxDatabase("test", "localhost", 0, "root", "angelfish");
		MdxConnection c = database.getMdxConnection();
		Statement s = c.createStetement("select * from user");
		ResultSet r = s.executeQuery("select * from company");
		while (r.next()) {
			System.out.println(r.getObject(1));
		}
		
	}
}
