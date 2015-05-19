package com.dianping.cricket.dal;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.dianping.cricket.dal.conf.DALConfiguration;
import com.dianping.cricket.dal.conf.DBConfig;
import com.dianping.cricket.dal.conf.DBConfigs;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


/**
 * Session store for storing all the session factories for all the dbs offered in the config, 
 * which can accelerate the speed on requesting an sql session. 
 * @author tenglinxiao
 * @since 0.0.1
 */
public class SessionStore {
	private final static String MYBATIS_CONFIG = "/mybatis.xml";
	private final static String DRIVER = "com.mysql.jdbc.Driver";
	private final static String CONNECTION_URL = "jdbc:mysql://%s:%d/%s";
	private final static String CONFIG_PATH = "classpath*:/mappers/*.xml";
	private static Logger logger = Logger.getLogger(SessionStore.class);
	private static SessionStore store;
	private HashMap<String, SqlSessionFactory> sessionFatories = new HashMap<String, SqlSessionFactory>();
	private DBConfigs dbs = DALConfiguration.getConf().getDBs();
	
	public void init() {
		try {
			logger.info("Init session store ...");
			
			// Dump config file content to ByteArrayInputStream obj.
			ByteArrayInputStream input = new ByteArrayInputStream(getMyBatisConfig().getBytes());
			
			for (DBConfig db : dbs.getDBs().values()) {
				Environment env = new Environment(
						db.getId(),
						new ManagedTransactionFactory(),
						new PooledDataSource(DRIVER, 
								String.format(CONNECTION_URL, db.getHost(), db.getPort(), db.getName()), 
								db.getUsername(), 
								db.getPassword()
						)
					);
				
				// Parse to get session factory object.
				SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(input);
				
				// Set the env as what we wanted.
				sqlSessionFactory.getConfiguration().setEnvironment(env);
				
				// Put the session factory into map.
				sessionFatories.put(db.getId(), sqlSessionFactory);
				
				logger.info("Created session factory for db: " + db.getId());
				
				// Reset the input stream for next time reuse.
				input.reset();
			}
			
			store = this;
			
			logger.info("Done initializing session store!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getMyBatisConfig() throws IOException, SAXException, ParserConfigurationException {
		logger.info("It will take a while to search classpath for all mybatis mapper config files ... DO NOT regard this as system death. :)");
		
		// Read the mybatis config file.
		BufferedReader reader = new BufferedReader(new InputStreamReader(SessionStore.class.getResourceAsStream(MYBATIS_CONFIG)));
		
		// Parse to get the doc.
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputSource source = new InputSource();
		source.setCharacterStream(reader);
		Document doc = documentBuilder.parse(source);
		
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		
		// Find the first mapper node.
		Node mapperNode = doc.getElementsByTagName("mapper").item(0);
		
		// Find the parent node.
		Node parent = mapperNode.getParentNode();
		parent.removeChild(mapperNode);
		
		Resource[] resources = resolver.getResources(CONFIG_PATH);
		
		// Check for duplicate named files, mybatis api CAN NOT load correct mapper with same name under classpath.
		checkDuplicates(resources);
		
		for (Resource resource : resources) {
			Node node = mapperNode.cloneNode(true);
			Attr attr = doc.createAttribute("resource");
			attr.setNodeValue("mappers/" + resource.getFilename());
			node.getAttributes().setNamedItem(attr);
			parent.appendChild(node);
			logger.info("Add found resource mapper file: [" + resource + "]");
		}
		
		// Set output format.
		OutputFormat format = new OutputFormat(doc);
		format.setOmitXMLDeclaration(true);
		format.setIndenting(true);
		
		// Use serializer to serialize the xml to string.
		StringWriter writer = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(writer, format);
		serializer.serialize(doc);
		return writer.getBuffer().toString();
	}
	
	private void checkDuplicates(Resource[] resources) {
		for (int index = 0; index < resources.length; index++) {
			for (int i = index + 1; i < resources.length; i++) {
				if (resources[index].getFilename().equals(resources[i].getFilename())) {
					logger.fatal("Found duplicate named files: [" + resources[index] + "," + resources[i] + "]");
					logger.fatal("Duplicate named mapper files CAN NOT be well loaded into mybatis, due to this serious error, system will halt.");
					System.exit(1);
				}
			}
		}
	}
	
	public SqlSession openSesion(String id) {
		return sessionFatories.get(id).openSession();
	}
	
	public SqlSessionFactory getSessionFactory(String id)
	{
		return sessionFatories.get(id);
	}
	
	public static SessionStore getSessionStore() {
		return store;
	}

}
