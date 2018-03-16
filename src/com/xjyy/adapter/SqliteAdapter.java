package com.xjyy.adapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
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
			StringBuilder sbSql = new StringBuilder("select * from ").append(table.getName());
			if (filter != null && !filter.trim().equals("")) {
				sbSql.append(" where ").append(filter);
			}
			PreparedStatement stmt = connection.prepareStatement(sbSql.toString());
			return stmt.executeQuery();
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
		StringBuilder sbSql = new StringBuilder("update ").append(table.getName()).append(" set");
		StringBuilder sbFilter = new StringBuilder(" where 1=1");
		ArrayList<Object> values = new ArrayList<Object>();
		ArrayList<Object> filters = new ArrayList<Object>();

		int i = 0;
		for (Field field : table.getFields()) {
			if (field.isPrimaryKey()) {
				sbFilter.append(" and ").append(field.getName()).append("=?");
				filters.add(record.get(field.getName()));
			} else {
				sbSql.append(" ").append(field.getName()).append("=?,");
				values.add(record.get(field.getName()));
				i++;
			}

		}
		if (i > 0) {
			sbSql.deleteCharAt(sbSql.length() - 1);
		}
		sbSql.append(sbFilter);
		values.addAll(filters);

		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sbSql.toString());
			for (i = 0; i < values.size(); i++) {
				stmt.setObject(i + 1, values.get(i));
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
	public int removeRecord(Connection connection, Table table, Map<String, Object> record) {
		StringBuilder sbSql = new StringBuilder("delete from ").append(table.getName()).append(" where");
		StringBuilder sbFilter = new StringBuilder();
		int primaryCount = 0;
		ArrayList<Object> primaryValues = new ArrayList<Object>();
		for (String x : table.getPrimaryKeysName()) {
			sbFilter.append(" and ").append(x).append("=?");
			primaryValues.add(record.get(x));
			primaryCount++;
		}
		if (primaryCount > 0) {
			sbSql.append(" 1=1");
		}
		sbSql.append(sbFilter);
		System.out.println(sbSql);
		PreparedStatement stmt;
		try {
			stmt = connection.prepareStatement(sbSql.toString());
			for (int i = 0; i < primaryValues.size(); i++) {
				stmt.setObject(i + 1, primaryValues.get(i));
			}
			int ret = stmt.executeUpdate();
			stmt.close();
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

}
