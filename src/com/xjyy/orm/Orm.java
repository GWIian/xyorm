package com.xjyy.orm;

import java.util.ArrayList;
import java.util.List;

public class Orm {

	private List<MappingInfo> mappingInfos;
	private List<DataSource> dataSources;

	private static final Orm instance = new Orm();

	private Orm() {
		dataSources = new ArrayList<>();
		mappingInfos = new ArrayList<>();
	}

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static Orm getInstance() {
		return instance;
	}

	/**
	 * 添加数据源
	 * 
	 * @param ds
	 */
	public void addDataSource(DataSource ds) {
		this.dataSources.add(ds);
	}

	List<MappingInfo> getMappingInfos() {
		return this.mappingInfos;
	}

	/**
	 * orm初始化
	 * 
	 * @param dbType
	 */
	public void init() {
		for (DataSource x : this.dataSources) {
			x.init();
		}
	}

	/**
	 * orm停止
	 */
	public void stop() {
		for (DataSource x : this.dataSources) {
			x.stop();
		}
	}

	/**
	 * 添加映射
	 * 
	 * @param dataSource
	 * @param table
	 * @param primary
	 * @param cls
	 */
	public void addMappingInfo(String dataSource, String table, String primary, Class<? extends Model<?>> cls) {
		MappingInfo mappingInfo = null;
		for (MappingInfo x : this.mappingInfos) {
			if (cls.getName().equals(x.getClassName())) {
				mappingInfo = x;
				break;
			}
		}
		if (mappingInfo == null) {
			mappingInfo = new MappingInfo();
			this.mappingInfos.add(mappingInfo);
		}
		mappingInfo.setDataSourceName(dataSource);
		mappingInfo.setTableName(table);
		mappingInfo.setPrimaryKeys(primary);
		mappingInfo.setClassName(cls.getName());
		mappingInfo.setTable();
	}

	/**
	 * 添加映射
	 * 
	 * @param table
	 * @param primary
	 * @param cls
	 */
	public void addMappingInfo(String table, String primary, Class<? extends Model<?>> cls) {
		this.addMappingInfo("main", table, primary, cls);
	}

	/**
	 * 获取数据源
	 * 
	 * @param dataSourceName
	 * @return
	 */
	public DataSource getDataSource(String dataSourceName) {
		for (DataSource x : this.dataSources) {
			if (x.getName().equals(dataSourceName)) {
				return x;
			}
		}
		return null;
	}
}
