package com.xjyy.orm;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String name;
	private List<Field> fields;
	private List<String> primaryKeysName;

	public Table(String name) {
		this.fields = new ArrayList<Field>();
		this.name = name;
		this.primaryKeysName = new ArrayList<String>();
	}

	public String getName() {
		return this.name;
	}

	public void setPrimaryKeysName(String primaryKeys) {
		for (String x : primaryKeys.split(",")) {
			this.primaryKeysName.add(x);
			Field field = this.getField(x);
			if (field != null) {
				field.isPrimaryKey(true);
			}
		}
	}

	public List<String> getPrimaryKeysName() {
		return this.primaryKeysName;
	}

	public void addField(Field field) {
		this.fields.add(field);
	}

	public List<Field> getFields() {
		return this.fields;
	}

	public Field getField(String fieldName) {
		for (Field x : this.fields) {
			if (x.getName().equals(fieldName)) {
				return x;
			}
		}
		return null;
	}
}
