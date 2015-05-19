package com.dianping.cricket.mdx.dal;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.dianping.cricket.mdx.dal.PooledConnection.ConnectionStatus;

public class PooledConnectionStore {
	private static Logger logger = Logger.getLogger(PooledConnectionStore.class);
	// Data source.
	private DataSource ds;
	// Pool base size.
	private int coreSize;
	// Pool maximum size.
	private int maximumSize;
	// Allowed max idleTime
	private int maxIdleTime;
	// Connections pool.
	private List<PooledConnection> pool;
	
	PooledConnectionStore(DataSource ds, int coreSize, int maximumSize, int maxIdleTime) {
		this.ds = ds;
		this.coreSize = coreSize;
		this.maximumSize = maximumSize;
		this.pool = new LinkedList<PooledConnection>();
		this.maxIdleTime = maxIdleTime;
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				cleanUpIdle();
			}
		});
		t.setDaemon(true);
		t.start();
	}
	
	public int getMaxIdleTime()
	{
		return maxIdleTime;
	}
	
	public PooledConnection getConnection() throws SQLException, InterruptedException {
		synchronized(this) {
			// If there is any free connection is the pool, reuse it to answer the request.
			for (PooledConnection c : pool) {
				if (c.getStatus().isReturned()) {
					return c;
				}
			}
			
			// If there is no free connection, and not be upward to maximum size, then create one for request 
			// and add it to the pool as well.
			if (pool.size() < maximumSize) {
				PooledConnection c = new PooledConnection(ds, this);
				c.getStatus().setConnectionStatus(ConnectionStatus.USING);
				pool.add(c);
				return c;
			}
			
			// If already be upwards to the limit size, then wait for free connections.
			this.wait();
			
			// When thread is notified, request the connection resource again.
			return getConnection();
		}
	}
	
	public void cleanUpIdle() {
		// If current pool size less than core size, then DO NOT do the cleanup job.
		if (pool.size() <= coreSize) {
			return;
		}
		
		// If the current pool size larger than core size, start the clean up check.
		for (int index = 0; index < pool.size(); index++) {
			PooledConnection c = pool.get(index);
			
			// If the connection status is 'returned' & is expired determined by max idle time, then destroy the connection.
			if (c.getStatus().getConnectionStatus() == ConnectionStatus.RETURNED && c.getStatus().isExpired(maxIdleTime)) {
				// Step into the destroy process.
				synchronized (this) {
					c.destroy();
					pool.remove(index);
				}
				
				// Clean up the pool to let it decrease to the core size if possible.
				// and keep that size even has more free connections can be released.
				if (pool.size() <= coreSize) {
					return;
				}
				
				// Reset the index.
				index--;
			}
		}
	}
}
