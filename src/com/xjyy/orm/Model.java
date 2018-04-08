package com.xjyy.orm;

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
	@SuppressWarnings("unchecked")
	public List<T> find(String filter, Object... params) {
		List<T> list = new ArrayList<>();
		try {
			list = (List<T>) Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName())
					.getRecords(this.mappingInfo, filter, this.getClass(), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查找第一条记录
	 * 
	 * @param filter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T findFirst(String filter, Object... params) {
		T data = null;
		try {
			data = (T) Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName())
					.getRecords(this.mappingInfo, filter, this.getClass(), params).get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * 写入一条新记录
	 */
	public void save() {
		try {
			Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName()).addRecord(this.mappingInfo,
					this.attributes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新记录
	 */
	public void update() {
		try {
			Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName()).updateRecord(this.mappingInfo,
					this.attributes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除记录
	 */
	public void delete() {
		try {
			Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName()).removeRecord(this.mappingInfo,
					this.attributes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分页记录
	 * 
	 * @param filter
	 * @param pageNumber
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page<T> paginate(String filter, int pageNumber, int pageSize, Object... params) {
		Page<T> page = null;
		DataSource ds = Orm.getInstance().getDataSource(this.mappingInfo.getDataSourceName());
		try {
			int rowCount = ds.getRecordsCount(this.mappingInfo, filter, params);
			@SuppressWarnings({ "unchecked" })
			List<T> list = (List<T>) ds.getRecordsByPage(this.mappingInfo, pageNumber, pageSize, filter,
					this.getClass(), params);
			int pageCount = rowCount % pageSize == 0 ? rowCount / pageSize : rowCount / pageSize + 1;
			page = new Page<T>(list, pageNumber, list.size(), pageCount, rowCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
}
