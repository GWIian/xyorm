package com.xjyy.adapter;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xjyy.orm.DSConnection;
import com.xjyy.orm.Table;

public class MySQLAdapter extends Adapter {

	public MySQLAdapter() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Table getTable(DSConnection connection, String name) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public <T> T getRecordByPrimarys(DSConnection connection, Table table, Class<T> recordType, Object... primarys)
			throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public <T> List<T> getRecords(DSConnection connection, Table table, String filter, Class<T> recordType,
			Object... params) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int addRecord(DSConnection connection, Table table, Map<String, Object> record) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int updateRecord(DSConnection connection, Table table, Map<String, Object> record) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int removeRecord(DSConnection connection, Table table, Map<String, Object> record) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int getRecordsCount(DSConnection connection, Table table, String filter, Object... params)
			throws SQLException {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public <T> List<T> getRecordsByPage(DSConnection connection, Table table, int pageNumber, int pageSize,
			String filter, Class<T> recordType, Object... params) throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

}
