package com.xjyy.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.xjyy.orm.Field;
import com.xjyy.orm.Table;

public class OracleAdapter extends Adapter {
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
			return connection.prepareStatement("select * from " + table.getName() + filter).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
