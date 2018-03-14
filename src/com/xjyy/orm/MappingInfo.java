package com.xjyy.orm;

import java.util.ArrayList;
import java.util.List;

public class MappingInfo {
	private String className;
	private String dataSourceName;
	private String tableName;
	private List<String> primaryKeysName;
	private Table table;

	public MappingInfo() {
		this.primaryKeysName = new ArrayList<String>();
	}

	public String getClassName() {
		return this.className;
	}

	public String getDataSourceName() {
		return this.dataSourceName;
	}

	public void setDataSourceName(String dataSource) {
		this.dataSourceName = dataSource;
	}

	public void setTableName(String table) {
		this.tableName = table;
	}

	public void setPrimaryKeys(String primaryKeys) {
		for (String x : primaryKeys.split(",")) {
			this.primaryKeysName.add(x);
		}
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setTable() {
		this.table = Orm.getInstance().getDataSource(this.dataSourceName).getTable(this.tableName);
	}

	/**
	 * 更新关系
	 */
	public void updateRelation() {
		this.updatePrimaryKey();
	}

	/**
	 * 更新主键
	 */
	private void updatePrimaryKey() {
		for (Field x : this.table.getFields()) {
			for (String y : this.primaryKeysName) {
				if (y.equals(x.getName())) {
					x.isPrimaryKey(true);
				}
			}
		}
	}

	public Table getTable() {
		return this.table;
	}
}
