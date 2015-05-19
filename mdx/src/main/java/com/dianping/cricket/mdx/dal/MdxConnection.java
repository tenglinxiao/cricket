package com.dianping.cricket.mdx.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface MdxConnection {
	// statement
	public Statement createStetement(String mdx) throws SQLException;
	
	// prepared statement.
	public PreparedStatement preparedStatement(String mdx) throws SQLException;

}
