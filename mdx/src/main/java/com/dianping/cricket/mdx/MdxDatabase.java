package com.dianping.cricket.mdx;

import java.util.ArrayList;
import java.util.List;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.impl.NamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.Catalog;
import org.olap4j.metadata.Database;
import org.olap4j.metadata.NamedList;

import com.dianping.cricket.mdx.dal.MdxConnection;
import com.dianping.cricket.mdx.dal.MdxConnectionImpl;

public class MdxDatabase implements Database {
	private String name;
	private String description;
	private String host;
	private int port = 3306;
	private String username;
	private String password;
	private NamedList<MdxCatalog> catalogs = new NamedListImpl<MdxCatalog>();

	public MdxDatabase(String name, String host, int port, String username, String password) {
		this.name = name;
		this.host = host;
		if (port > 0) {
			this.port = port;
		}
		this.username = username;
		this.password = password;
	}
	
	public MdxConnection getMdxConnection() {
		return MdxConnectionImpl.getConnection(this);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NamedList<Catalog> getCatalogs() {
		return Olap4jUtil.cast(catalogs);
	}

	@Override
	public OlapConnection getOlapConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getURL() throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataSourceInfo() throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProviderName() throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProviderType> getProviderTypes() throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AuthenticationMode> getAuthenticationModes()
			throws OlapException {
		// TODO Auto-generated method stub
		return null;
	}

}
