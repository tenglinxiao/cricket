package com.dianping.cricket.mdx;

import org.olap4j.OlapConnection;
import org.olap4j.OlapException;
import org.olap4j.mdx.SelectNode;
import org.olap4j.mdx.parser.MdxParser;
import org.olap4j.mdx.parser.MdxParserFactory;
import org.olap4j.mdx.parser.MdxValidator;
import org.olap4j.mdx.parser.impl.DefaultMdxParserImpl;

public class MdxParserFactoryImpl implements MdxParserFactory {

	@Override
	public MdxParser createMdxParser(OlapConnection connection) {
		// TODO Auto-generated method stub
		return new DefaultMdxParserImpl();
	}

	@Override
	public MdxValidator createMdxValidator(OlapConnection connection) {
		return new MdxValidator() {

			@Override
			public SelectNode validateSelect(SelectNode selectNode)
					throws OlapException {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}

}
