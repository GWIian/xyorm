package com.xjyy.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Map;

import com.xjyy.orm.Table;

public abstract class Adapter {
	/**
	 * 获取表
	 * 
	 * @param connection
	 * @param name
	 * @return
	 */
	public abstract Table getTable(Connection connection, String name);

	/**
	 * 获取记录
	 * 
	 * @param connection
	 * @param table
	 * @param filter
	 * @return
	 */
	public abstract ResultSet getRecords(Connection connection, Table table, String filter);

	/**
	 * 添加记录
	 * 
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 */
	public abstract int addRecord(Connection connection, Table table, Map<String, Object> record);

	/**
	 * 更新记录
	 * 
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 */
	public abstract int updateRecord(Connection connection, Table table, Map<String, Object> record);

	/**
	 * 删除记录
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 */
	public abstract int removeRecord(Connection connection, Table table, Map<String, Object> record);
}
