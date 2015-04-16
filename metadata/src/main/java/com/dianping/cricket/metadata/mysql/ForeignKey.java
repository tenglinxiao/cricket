package com.dianping.cricket.metadata.mysql;

import com.dianping.cricket.api.exception.NotSupportedException;

/**
 * Class to describe foreign key. 
 * @author tenglinxiao
 * @since 1.0
 */
public class ForeignKey extends Key {
	// Reference table.
	private Table referencedTable;
	// Reference column.
	private Column referencedColumn;
	
	public ForeignKey() {}
	
	public ForeignKey(String name) {
		super(name);
	}

	public Table getReferencedTable() {
		return referencedTable;
	}

	public void setReferencedTable(Table referencedTable) {
		this.referencedTable = referencedTable;
	}

	public Column getReferencedColumn() {
		return referencedColumn;
	}

	public void setReferencedColumn(Column referencedColumn) {
		referencedColumn.setForeignkey(this);
		this.referencedColumn = referencedColumn;
	}

	@Override
	public boolean isUnique() throws NotSupportedException {
		throw new NotSupportedException("No unique concept for foreign key");
	}
}
