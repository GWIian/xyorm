package com.xjyy.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DSConnection {
	/**
	 * 可用连接
	 */
	public static final int DSC_READY = 1;
	/**
	 * 正在使用
	 */
	public static final int DSC_USING = 2;
	/**
	 * 无效连接
	 */
	public static final int DSC_INVALID = 3;

	private Connection connection;
	private int status;

	public DSConnection(String url, String user, String password) {
		try {
			this.connection = DriverManager.getConnection(url, user, password);
			this.status = DSC_READY;
		} catch (Exception e) {
			this.status = DSC_INVALID;
			e.printStackTrace();
		}
	}

	/**
	 * 获取状态
	 * 
	 * @return
	 */
	public int getStatus() {
		try {
			if (this.connection.isValid(3000)) {
				this.status = DSC_INVALID;
			}
		} catch (SQLException e) {
			this.status = DSC_INVALID;
			e.printStackTrace();
		}
		return this.status;
	}

	/**
	 * 使用连接
	 * 
	 * @return
	 */
	public Connection open() {
		this.status = DSC_USING;
		return this.connection;
	}

	/**
	 * 回收连接
	 */
	public void close() {
		this.status = DSC_READY;
	}

	/**
	 * 释放和关闭jdbc连接
	 */
	void dispose() {
		this.status = DSC_INVALID;
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
