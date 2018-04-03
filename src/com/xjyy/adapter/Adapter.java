package com.xjyy.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
	 * 根据主键获取记录
	 * 
	 * @param mappingInfo
	 * @param primarys
	 * @return
	 */
	public abstract <T> T getRecordByPrimarys(Connection connection, Table table, Class<T> recordType,
			Object... primarys) throws Exception;

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
	 * 
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 */
	public abstract int removeRecord(Connection connection, Table table, Map<String, Object> record);

	/**
	 * 获取某表记录行数
	 * 
	 * @param connection
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public abstract int getRecordsCount(Connection connection, Table table, String filter, Object... params)
			throws SQLException;

	/**
	 * 根据分页获取记录
	 * 
	 * @param connection
	 * @return
	 */
	public abstract <T> List<T> getRecordsByPage(Connection connection, Table table, int pageNumber, int pageSize,
			String filter, Class<T> recordType, Object... params) throws Exception;
}
