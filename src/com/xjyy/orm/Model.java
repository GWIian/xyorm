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

	public Model() {
		this.setMappingInfo();
		this.setAttributes();
	}

	/**
	 * 初始化映射信息
	 */
	private void setMappingInfo() {
		for (MappingInfo x : Orm.getInstance().getMappingInfos()) {
			if (x.getClassName().equals(this.getClass().getName())) {
				this.mappingInfo = x;
			}
		}
	}

	/**
	 * 初始化Attributes
	 */
	private void setAttributes() {
		this.attributes = new HashMap<String, Object>();
		for (Field x : this.mappingInfo.getTable().getFields()) {
			this.attributes.put(x.getName(), null);
		}
	}

	/**
	 * Override toString()
	 */
	@Override
	public String toString() {
		return this.attributes.toString();
	}

	/**
	 * 设置字段值
	 * 
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public T set(String key, Object value) {
		this.attributes.put(key, value);
		return (T) this;
	}

	/**
	 * 获取字段值
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return this.attributes.get(key);
	}

	/**
	 * 根据主键获取记录
	 * 
	 * @param primarys
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public T findByPrimarys(Object... primarys) {
		try {
			return (T) Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName())
					.getRecordByPrimarys(this.mappingInfo, this.getClass(), primarys);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查找记录
	 * 
	 * @param filter
	 * @return
	 */
	public List<T> find(String filter) {
		ResultSet rs = Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName())
				.getRecords(this.mappingInfo, filter);
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
			rs.getStatement().close();
			rs.close();
		} catch (SQLException | IllegalArgumentException | NoSuchMethodException | SecurityException
				| IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}

		return datas;
	}

	/**
	 * 查找第一条记录
	 * 
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findFirst(String filter) {
		ResultSet rs = Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName())
				.getRecords(this.mappingInfo, filter);

		T data = null;
		try {
			Method set = this.getClass().getMethod("set", String.class, Object.class);
			if (rs.next()) {
				data = (T) this.getClass().newInstance();
				for (String x : this.attributes.keySet()) {
					set.invoke(data, x, rs.getObject(x));
				}
			}
			rs.getStatement().close();
			rs.close();
			return data;
		} catch (SQLException | IllegalArgumentException | NoSuchMethodException | SecurityException
				| IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 写入一条新记录
	 */
	public void save() {
		Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName()).addRecord(this.mappingInfo,
				this.attributes);
	}

	/**
	 * 更新记录
	 */
	public void update() {
		Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName()).updateRecord(this.mappingInfo,
				this.attributes);
	}

	/**
	 * 删除记录
	 */
	public void delete() {
		Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName()).removeRecord(this.mappingInfo,
				this.attributes);
	}
}
