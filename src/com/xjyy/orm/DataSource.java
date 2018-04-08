package com.xjyy.orm;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;

import com.xjyy.adapter.Adapter;

public class DataSource {
	// 状态枚举
	public static final int DS_STOP = 1;
	public static final int DS_INITING = 2;
	public static final int DS_READY = 3;

	private String name;
	private String url;
	private String user;
	private String password;
	private Adapter adapter;
	private int minPoolSize;// 连接池最小连接数
	private LinkedList<DSConnection> connections;
	private int status;// 连接池状态

	/**
	 * 新数据源
	 * 
	 * @param url
	 * @param user
	 * @param password
	 */
	public DataSource(String url, String user, String password) {
		this.name = "main";
		this.url = url;
		this.user = user;
		this.password = password;
		this.connections = new LinkedList<DSConnection>();
		this.status = DS_STOP;
		this.minPoolSize = 10;
	}

	/**
	 * 新数据源
	 * 
	 * @param dataSourceName
	 * @param url
	 * @param user
	 * @param password
	 */
	public DataSource(String dataSourceName, String url, String user, String password) {
		this(url, user, password);
		this.name = dataSourceName;
		this.connections = new LinkedList<DSConnection>();
		this.status = DS_STOP;
		this.minPoolSize = 10;
	}

	/**
	 * 获取数据源名称
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * 设置适配器
	 * 
	 * @param adapter
	 */
	public void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * 设置连接池最小连接数
	 * 
	 * @param minSize
	 */
	public void setMinPoolSize(int minSize) {
		this.minPoolSize = minSize;
	}

	/**
	 * 初始化
	 */
	public synchronized void init() {
		try {
			if (DS_STOP == this.status) {
				this.status = DS_INITING;
				for (int i = 0; i < this.minPoolSize; i++) {
					DSConnection connection = new DSConnection(this.url, this.user, this.password);
					this.connections.add(connection);
				}
				this.status = DS_READY;
			}
		} catch (Exception e) {
			this.status = DS_STOP;
			e.printStackTrace();
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public synchronized DSConnection getConnection() throws Exception {
		// for (DSConnection con : this.connections) {
		// System.out.print(con.getStatus() + "_");
		//
		// }
		// System.out.println();
		if (DS_READY == this.status) {
			for (DSConnection connection : this.connections) {
				if (DSConnection.DSC_READY == connection.getStatus()) {
					connection.open();
					return connection;
				} else if (DSConnection.DSC_INVALID == connection.getStatus()) {
					connection.reActive();
				}
			}
			DSConnection connection = new DSConnection(this.url, this.user, this.password);
			this.connections.add(connection);
			connection.open();
			return connection;
		} else {
			throw new Exception("数据源没有初始化");
		}
	}

	/**
	 * 停止
	 */
	public synchronized void stop() {
		if (DS_READY == this.status) {
			this.status = DS_STOP;
			for (DSConnection connection : this.connections) {
				connection.dispose();
			}
			this.connections.clear();
		}
	}

	/**
	 * 获取table
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Table getTable(String name) throws Exception {
		return this.adapter.getTable(getConnection(), name);
	}

	/**
	 * 根据主键获取记录
	 * 
	 * @param mappingInfo
	 * @param primarys
	 * @return
	 * @throws Exception
	 */
	public <T> T getRecordByPrimarys(MappingInfo mappingInfo, Class<T> recordType, Object... primarys)
			throws Exception {
		return this.adapter.getRecordByPrimarys(getConnection(), mappingInfo.getTable(), recordType, primarys);
	}

	/**
	 * 获取记录
	 * 
	 * @param <T>
	 * 
	 * @param table
	 * @param filter
	 * @return
	 */
	public <T> List<T> getRecords(MappingInfo mappingInfo, String filter, Class<T> recordType, Object... params)
			throws Exception {
		return this.adapter.getRecords(getConnection(), mappingInfo.getTable(), filter, recordType);
	}

	/**
	 * 添加记录
	 * 
	 * @param connection
	 * @param table
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public int addRecord(MappingInfo mappingInfo, Map<String, Object> record) throws Exception {
		return this.adapter.addRecord(getConnection(), mappingInfo.getTable(), record);
	}

	/**
	 * 更新记录
	 * 
	 * @param mappingInfo
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public int updateRecord(MappingInfo mappingInfo, Map<String, Object> record) throws Exception {
		return this.adapter.updateRecord(getConnection(), mappingInfo.getTable(), record);
	}

	/**
	 * 删除记录
	 * 
	 * @param mappingInfo
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public int removeRecord(MappingInfo mappingInfo, Map<String, Object> record) throws Exception {
		return this.adapter.removeRecord(getConnection(), mappingInfo.getTable(), record);
	}

	/**
	 * 获取某表记录行数
	 * 
	 * @param mappingInfo
	 * @param filter
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public int getRecordsCount(MappingInfo mappingInfo, String filter, Object... params) throws Exception {
		return this.adapter.getRecordsCount(getConnection(), mappingInfo.getTable(), filter, params);
	}

	/**
	 * 根据分页获取记录
	 * 
	 * @param mappingInfo
	 * @param filter
	 * @param pageNumber
	 * @param pageSize
	 * @param recordType
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> getRecordsByPage(MappingInfo mappingInfo, int pageNumber, int pageSize, String filter,
			Class<T> recordType, Object... params) throws Exception {
		return this.adapter.getRecordsByPage(getConnection(), mappingInfo.getTable(), pageNumber, pageSize, filter,
				recordType, params);
	}
}
