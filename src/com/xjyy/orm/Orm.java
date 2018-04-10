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
		for (DataSource dataSource : this.dataSources) {
			if (dataSource.getName().equals(ds.getName())) {
				System.err.println("数据源已存在，已忽略该数据源");
				return;
			}
		}
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
			System.out.println("[数据源：" + x.getName() + " 已启动]");
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
	 * @throws Exception
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

		mappingInfo.setClassName(cls.getName());
		try {
			mappingInfo.setTable();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		mappingInfo.getTable().setPrimaryKeysName(primary);
	}

	/**
	 * 添加映射
	 * 
	 * @param table
	 * @param primary
	 * @param cls
	 * @throws Exception
	 */
	public void addMappingInfo(String table, String primary, Class<? extends Model<?>> cls) {
		try {
			this.addMappingInfo("main", table, primary, cls);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
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

	/**
	 * 获取默认数据源
	 */
	public DataSource getDataSource() {
		for (DataSource x : this.dataSources) {
			if (x.getName().equals("main")) {
				return x;
			}
		}
		return null;
	}
}
