package com.dianping.cricket.metadata.mysql;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * Class for unique key.
 * 
 * @author tenglinxiao
 * @since 1.0
 */
@XmlRootElement(name = "uniqueKey")
public class UniqueKey extends Key {

	public UniqueKey() {
	}

	public UniqueKey(String name) {
		super(name);
	}

	public boolean isUnique() {
		return true;
	}
}
