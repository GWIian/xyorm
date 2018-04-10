package com.xjyy.adapter;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xjyy.orm.DSConnection;
import com.xjyy.orm.Table;

public abstract class Adapter {
	/**
	 * 获取表
	 * 
	 * @param connection
	 * @param name
	 * @return
	 */
	public abstract Table getTable(DSConnection connection, String name);

	/**
	 * 根据主键获取记录
	 * 
	 * @param mappingInfo
	 * @param primarys
	 * @return
	 */
	public abstract <T> T getRecordByPrimarys(DSConnection connection, Table table, Class<T> recordType,
			Object... primarys) throws Exception;

	/**
	 * 获取记录
	 * 
	 * @param connection
	 * @param table
	 * @param filter
	 * @return
	 */
	public abstract <T> List<T> getRecords(DSConnection connection, Table table, String filter, Class<T> recordType,
			Object... params);

	/**
	 * 添加记录
	 * 
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 */
	public abstract int addRecord(DSConnection connection, Table table, Map<String, Object> record);

	/**
	 * 更新记录
	 * 
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 */
	public abstract int updateRecord(DSConnection connection, Table table, Map<String, Object> record);

	/**
	 * 删除记录
	 * 
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 */
	public abstract int removeRecord(DSConnection connection, Table table, Map<String, Object> record);

	/**
	 * 获取某表记录行数
	 * 
	 * @param connection
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public abstract int getRecordsCount(DSConnection connection, Table table, String filter, Object... params)
			throws SQLException;

	/**
	 * 根据分页获取记录
	 * 
	 * @param connection
	 * @return
	 */
	public abstract <T> List<T> getRecordsByPage(DSConnection connection, Table table, int pageNumber, int pageSize,
			String filter, Class<T> recordType, Object... params) throws Exception;

	/**
	 * 连接是否可用
	 * 
	 * @param timeout
	 * @return
	 */
	public abstract boolean isValid(DSConnection connection, int timeout);
}
