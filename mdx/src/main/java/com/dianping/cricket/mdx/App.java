package com.dianping.cricket.mdx;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.olap4j.OlapException;
import org.olap4j.mdx.AxisNode;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.impl.DefaultMdxParserImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dianping.cricket.dal.SessionStore;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws OlapException
    {
    	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");
    	SessionStore sessionStore = (SessionStore)context.getBean("sessionStore");
    	//SqlSession sqlSession = sessionStore.openSesion("mdx");
//    	MdxCatalog catalog = new MdxCatalog("xteng");
//    	MdxSchema schema = new MdxSchema("tenglinxiao", catalog);
//    	MdxCube cube = new MdxCube("cube", schema);
//    	schema.addCube(cube);
//    	catalog.getSchemas().add(schema);
//    	catalog.save();
//    	System.out.println(catalog.getId());
    	MdxCatalog catalog = new MdxCatalog(11);
    	catalog.load();
    	catalog.delete();
//   	SelectNode selectNode = new DefaultMdxParserImpl().parseSelect( 
//    			"WITH " +
//			"MEMBER [Measures].[NegDiscountAmount] AS -[Measures].[Discount Amount] " +
//			"SELECT " +
//			"{[Measures].[Discount Amount],[Measures].[NegDiscountAmount]} on COLUMNS, NON EMPTY [Product].[Product].MEMBERS ON Rows " +
//			"FROM [Adventure Works] " +
//			"WHERE [Product].[Category].[Bikes]"
//    		"SELECT [Measures].MEMBERS ON COLUMNS, [Date].[Calendar Year].MEMBERS ON ROWS" + 
//" FROM (SELECT [Measures].[Internet Sales Amount] ON COLUMNS, [Date].[Calendar Year].&[2004] ON ROWS" +
//" FROM [Adventure Works])"
//        );
//    	
//    	List<AxisNode> nodes = selectNode.getAxisList();
//    	for (AxisNode node : nodes) {
//    		System.out.println(node.getExpression());
//    	}
    	context.close();
    }
}
