package com.xjyy.orm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model<T> {
	private MappingInfo mappingInfo;
	private Map<String, Object> attributes;
	// private boolean isDao;

	protected Model() {
		this.setMappingInfo();
		this.setAttributes();
		// this.isDao = false;
	}

	private void setMappingInfo() {
		for (MappingInfo x : Orm.getInstance().getMappingInfos()) {
			if (x.getClassName().equals(this.getClass().getName())) {
				this.mappingInfo = x;
			}
		}
	}

	private void setAttributes() {
		this.attributes = new HashMap<String, Object>();
		for (Field x : this.mappingInfo.getTable().getFields()) {
			this.attributes.put(x.getName(), null);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		sb.append("{");
		for (Map.Entry<String, Object> x : this.attributes.entrySet()) {
			sb.append(x.getKey());
			sb.append(":");
			sb.append(x.getValue());
			sb.append(",");
		}
		if (count > 0) {
			sb.deleteCharAt(-1);
		}
		sb.append("}");
		return sb.toString();
	}

	public void set(String key, Object value) {
		this.attributes.put(key, value);
	}

	public Object get(String key) {
		return this.attributes.get(key);
	}

	public List<T> findAll() {
		ResultSet rs = Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName())
				.getRecords(this.mappingInfo.getTable(), "");
		ArrayList<T> datas = new ArrayList<T>();
		try {
			Method set = this.getClass().getMethod("set", String.class, Object.class);
			while (rs.next()) {
				@SuppressWarnings("unchecked")
				T data = (T) this.getClass().newInstance();
				for (String x : this.attributes.keySet()) {
					set.invoke(data, x, rs.getObject(x));
				}
				datas.add(data);
			}
			rs.close();
		} catch (SQLException | IllegalArgumentException | NoSuchMethodException | SecurityException
				| IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}

		return datas;
	}

}
