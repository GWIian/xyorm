package com.xjyy.orm;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private String name;
	private List<Field> fields;

	public Table(String name) {
		this.fields = new ArrayList<Field>();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void addField(Field field) {
		this.fields.add(field);
	}

	public List<Field> getFields() {
		return this.fields;
	}
}
