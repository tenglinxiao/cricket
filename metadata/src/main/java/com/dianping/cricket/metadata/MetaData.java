package com.dianping.cricket.metadata;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * Base class to describe metadata.
 * @author tenglinxiao
 * @since 1.0
 */
public abstract class MetaData {
	// Metadata name.
	private String name;
	// Metadata type.
	private String type;
	// Metadata desc.
	private String comment;
	
	public MetaData(){}
	
	public MetaData(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String toString() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JAXBContext context = JAXBContext.newInstance(this.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new MetaDataPrefixMapper());
			marshaller.marshal(this, out);
			return new String(out.toByteArray());
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
}
