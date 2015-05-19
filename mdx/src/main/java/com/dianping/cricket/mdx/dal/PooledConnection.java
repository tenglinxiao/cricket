package com.dianping.cricket.mdx.dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class PooledConnection {
	private static Logger logger = Logger.getLogger(MdxConnection.class);
	private PooledConnectionStore store;
	private Connection connection; 
	private Status status;
	private DataSource ds; 
	
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.info("No jdbc driver class found on classpath!");
			System.exit(1);
		}
	}
	
	public PooledConnection(DataSource ds, PooledConnectionStore store) throws SQLException {
		this.store = store;
		connection = ds.getConnection();
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void destroy() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Connection close error when try to destroy connection: [" + e.getMessage() + "]");
		}
	}
	
	
	public static enum ConnectionStatus {
		USING, RETURNED
	}
	
	public static class Status {
		private ConnectionStatus status;
		private Date lastReleasedTime;
		private boolean isReturned;

		public ConnectionStatus getConnectionStatus() {
			return status;
		}
		
		public boolean isReturned() {
			return isReturned;
		}
		
		public Date getLastReleasedTime() {
			return lastReleasedTime;
		}		
		
		public void setConnectionStatus(ConnectionStatus status) {
			this.status = status;
		}
		
		public boolean isExpired(int idleTime) {
			if ((System.currentTimeMillis() - lastReleasedTime.getTime()) / 1000 > idleTime) {
				return true;
			}
			return false;
		}
	}

}
