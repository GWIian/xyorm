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
	private String user;
	private String password;
	private DataSource dataSource;

	public DSConnection(DataSource dataSource, String user, String password) {
		try {
			this.dataSource = dataSource;
			this.user = user;
			this.password = password;
			this.connection = DriverManager.getConnection(this.dataSource.getUrl(), this.user, this.password);
			this.status = DSC_READY;
		} catch (Exception e) {
			this.status = DSC_INVALID;
			e.printStackTrace();
		}
	}

	/**
	 * 重新激活连接
	 */
	public void reActive() {
		try {
			this.connection = DriverManager.getConnection(this.dataSource.getUrl(), this.user, this.password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.status = DSC_READY;
	}

	/**
	 * 获取状态
	 * 
	 * @return
	 */
	public int getStatus() {
		try {
			if (!this.dataSource.getAdapter().isValid(this, 3000)) {
				this.status = DSC_INVALID;
			}
		} catch (Exception e) {
			this.status = DSC_READY;
		}
		return this.status;
	}

	/**
	 * 使用连接
	 * 
	 * @return
	 */
	public Connection use() {
		this.status = DSC_USING;
		return this.connection;
	}

	/**
	 * 回收连接 @throws
	 */
	void reback() {
		try {
			this.connection.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
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

	@Override
	public String toString() {
		return this.status + "";
	}
}
