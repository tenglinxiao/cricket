package com.dianping.cricket.mdx;

import org.olap4j.metadata.Datatype;
import org.olap4j.metadata.Measure;


public class MdxMeasure extends MdxMember implements Measure {
	private Datatype type;
	private Aggregator aggregator;
	
	public MdxMeasure(int id) {
		super(id);
	}
	
	public MdxMeasure(String name, MdxLevel level) {
		super(name, level);
	}

	@Override
	public Aggregator getAggregator() {
		return aggregator;
	}

	@Override
	public Datatype getDatatype() {
		return type;
	}
}
