package com.xjyy.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.xjyy.orm.Field;
import com.xjyy.orm.Table;

public class SqliteAdapter extends Adapter {

	public SqliteAdapter() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Table getTable(Connection connection, String name) {
		Table table = new Table(name);
		try {
			ResultSet rs = connection.createStatement().executeQuery("select * from " + name);
			ResultSetMetaData metaData = rs.getMetaData();
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				Field field = new Field();
				field.setName(metaData.getColumnName(i));
				// field.setDataTypeName(metaData.getColumnTypeName(i));
				table.addField(field);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return table;
	}

	@Override
	public ResultSet getRecords(Connection connection, Table table, String filter) {
		try {
			return connection.prepareStatement("select * from " + table.getName() + " " + filter).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int addRecord(Connection connection, Table table, Map<String, Object> record) {
		StringBuilder sbFields = new StringBuilder().append("insert into ").append(table.getName()).append(" (");
		StringBuilder sbValues = new StringBuilder().append(" values (");
		for (Field x : table.getFields()) {
			sbFields.append(x.getName()).append(",");
			sbValues.append("?,");
		}
		sbFields.deleteCharAt(sbFields.length() - 1).append(")");
		sbValues.deleteCharAt(sbValues.length() - 1).append(")");
		sbFields.append(sbValues);
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sbFields.toString());
			int i = 1;
			for (Field x : table.getFields()) {
				stmt.setObject(i, record.get(x.getName()));
				i++;
			}
			int ret = stmt.executeUpdate();
			stmt.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int updateRecord(Connection connection, Table table, Map<String, Object> record) {
		// TODO 自动生成的方法存根
		return 0;
	}

}
