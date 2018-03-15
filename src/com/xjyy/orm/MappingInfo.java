package com.xjyy.orm;

public class MappingInfo {
	private String className;
	private String dataSourceName;
	private String tableName;

	private Table table;

	public MappingInfo() {

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
			for (String y : this.table.getPrimaryKeysName()) {
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
