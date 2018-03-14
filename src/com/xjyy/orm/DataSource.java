package com.xjyy.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.xjyy.adapter.Adapter;

public class DataSource {
	private Connection connection;
	private String name;
	private String url;
	private String user;
	private String password;
	private Adapter adapter;

	/**
	 * 新数据源
	 * 
	 * @param url
	 * @param user
	 * @param password
	 */
	public DataSource(String url, String user, String password) {
		this.name = "main";
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * 新数据源
	 * 
	 * @param dataSourceName
	 * @param url
	 * @param user
	 * @param password
	 */
	public DataSource(String dataSourceName, String url, String user, String password) {
		this(url, user, password);
		this.name = dataSourceName;
	}

	/**
	 * 获取数据源名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置适配器
	 * 
	 * @param adapter
	 */
	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * 初始化
	 */
	public void init() {
		try {
			connection = DriverManager.getConnection(this.url, this.user, this.password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止
	 */
	public void stop() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取table
	 * 
	 * @param name
	 * @return
	 */
	public Table getTable(String name) {
		return this.adapter.getTable(this.connection, name);
	}

	public ResultSet getRecords(Table table, String filter) {
		return this.adapter.getRecords(connection, table, filter);
	}
}
