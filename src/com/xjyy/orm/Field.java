package com.xjyy.orm;

public class Field {
	private String name;
//	private String dataTypeName;
	private boolean isPrimaryKey;

	public Field() {
		this.isPrimaryKey = false;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	// public void setDataTypeName(String name) {
	// this.dataTypeName = name;
	// }

	public boolean isPrimaryKey() {
		return this.isPrimaryKey;
	}

	public void isPrimaryKey(boolean bool) {
		this.isPrimaryKey = bool;
	}
}
