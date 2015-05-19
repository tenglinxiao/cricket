package com.dianping.cricket.metadata;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.dianping.cricket.api.exception.InvalidCaseException;
import com.dianping.cricket.api.exception.InvalidFormatException;
import com.dianping.cricket.dal.conf.DBConfig;
import com.dianping.cricket.metadata.mysql.Table;
import com.dianping.cricket.metadata.util.MetaDataQueryAdapter;
import com.dianping.cricket.metadata.util.MetadataParser;

public class MetaDataStore extends MetaDataLoader {
	private final static String SCHEMA_CHECK = "show create table %s";
	private static Logger logger = Logger.getLogger(MetaDataStore.class);
	private HashMap<String, String> checkSums = new HashMap<String, String>();
	private HashMap<String, Table> tables = new HashMap<String, Table>();
	private List<String> lastUpdatedTables = new ArrayList<String>();
	
	public MetaDataStore(DBConfig conf, SqlSessionFactory sessionFactory)
	{
		super(conf, sessionFactory);
	}
	
	public void init() {
		try {
			tables = load(getFullUpdateQueryAdapter(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected HashMap<String, Table> load(MetaDataQueryAdapter params, boolean fullUpdate) throws InvalidCaseException {
		List<HashMap<String, Object>> columnsMetaData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> constraintsMetaData = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> foreignKeyMetaData = new ArrayList<HashMap<String, Object>>();
		
		logger.info("Start fetching the table schemas...");
		SqlSession sqlSession = openSession();
		List<HashMap<String, Object>> columns= sqlSession.selectList("metadata.findColumns", params);
		List<HashMap<String, Object>> constraints = sqlSession.selectList("metadata.findConstraints", params);
		List<HashMap<String, Object>> foreignKeys = sqlSession.selectList("metadata.findForeignKeys", params);
		columnsMetaData.addAll(columns);
		constraintsMetaData.addAll(constraints);
		foreignKeyMetaData.addAll(foreignKeys);
		
		logger.info("Start parsing the table schemas...");
		
		MetadataParser parser = null;
		if (fullUpdate) {
			parser = MetadataParser.getParser(conf.getHost());
		} else {
			parser = MetadataParser.getParser(conf.getHost(), tables);
		}
		
		// Parse the schemas with parser.
		return parser.parse(columnsMetaData, constraintsMetaData, foreignKeyMetaData);
	}
	
	// Run checksum to see whether table schema has changed.
	public boolean checkSum() 
	{
		// Clear last updated tables list.
		lastUpdatedTables.clear();
		SqlSession sqlSession = openSession();
		List<HashMap<String, String>> tablesList = sqlSession.selectList("findTablesList", conf.getName());
		
		// Iterate all the tables and check sum for each table.
		for(HashMap<String, String> result : tablesList) {
			String tableName = result.get("table_name");
			HashMap<String, String> schemaCheck= sqlSession.selectOne("select", String.format(SCHEMA_CHECK, tableName));
			String checkSum = schemaCheck.get("Create Table");
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(checkSum.getBytes());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			
			if (md != null){
				checkSum =  new String(md.digest());
			}
			
			String oldCheckSum = checkSums.put(tableName, checkSum);
			if (!checkSum.equals(oldCheckSum)) {
				lastUpdatedTables.add(tableName);
			}
		}
		
		if (lastUpdatedTables.size() > 0) {
			return false;
		}
		return true;
	}
	
	@Override
	public void create(MetaDataEvent event) {
		
	}

	@Override
	public void delete(MetaDataEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(MetaDataEvent event) {
		
	}
	
	// Define the query adapter for updated tables.
	private MetaDataQueryAdapter getQueryAdaperForUpdatedTable(String tableName) {
		MetaDataQueryAdapter params = new MetaDataQueryAdapter();
		params.setDbName(conf.getName());
		params.setUseIncludes(true);
		List<String> tableList = new ArrayList<String>();
		tableList.add(tableName);
		params.setTableList(tableList);
		return params;
	}
	
	// Define the query adapter for loading all tables.
	private MetaDataQueryAdapter getFullUpdateQueryAdapter() throws InvalidFormatException {
		MetaDataQueryAdapter params = new MetaDataQueryAdapter();
		params.setDbName(conf.getName());
		return params;
	}
	
	public HashMap<String, Table> getTables()
	{
		return tables;
	}


}
