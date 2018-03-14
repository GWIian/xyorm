package com.xjyy.adapter;

import java.sql.Connection;
import java.sql.ResultSet;

import com.xjyy.orm.Table;

public abstract class Adapter {
	/**
	 * 获取表
	 * @param connection
	 * @param name
	 * @return
	 */
	public abstract Table getTable(Connection connection, String name);

	/**
	 * 获取记录
	 * @param connection
	 * @param table
	 * @param filter
	 * @return
	 */
	public abstract ResultSet getRecords(Connection connection, Table table, String filter);
}
