package com.xjyy.orm;

import java.util.List;
import java.util.Map;
import java.sql.SQLException;
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
	private int maxPoolSize;// 连接池最大连接数
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
		this.maxPoolSize = 15;
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
		this.maxPoolSize = 15;
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
	 * 获取Url
	 * 
	 * @return
	 */
	public String getUrl() {
		return this.url;
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
	 * 获取适配器
	 * 
	 * @return
	 * 
	 */
	public Adapter getAdapter() {
		return this.adapter;
	}

	/**
	 * 设置连接池最小连接数
	 * 
	 * @param minSize
	 */
	public void setMinPoolSize(int minSize) {
		this.minPoolSize = minSize > this.maxPoolSize ? this.maxPoolSize : minSize;
	}

	/**
	 * 设置连接池最大连接数
	 * 
	 * @param maxSize
	 */
	public void setMaxPoolSize(int maxSize) {
		this.maxPoolSize = maxSize < this.minPoolSize ? this.minPoolSize : maxSize;
	}

	/**
	 * 获取最小连接数
	 * 
	 * @return
	 */
	public int getMinPoolSize() {
		return this.minPoolSize;
	}

	/**
	 * 获取最大连接数
	 * 
	 * @return
	 */
	public int getMaxPoolSize() {
		return this.maxPoolSize;
	}

	/**
	 * 初始化
	 */
	public synchronized void init() {
		try {
			if (DS_STOP == this.status) {
				this.status = DS_INITING;
				for (int i = 0; i < this.minPoolSize; i++) {
					DSConnection connection = new DSConnection(this, this.user, this.password);
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
		for (DSConnection con : this.connections) {
			System.out.print(con.getStatus() + "_");
		}
		System.out.println(":" + this.connections.size());
		if (DS_READY == this.status) {
			for (DSConnection connection : this.connections) {
				if (DSConnection.DSC_READY == connection.getStatus()) {
					connection.use();
					return connection;
				} else if (DSConnection.DSC_INVALID == connection.getStatus()) {
					connection.reActive();
				}
			}
			if (this.connections.size() < this.maxPoolSize) {
				DSConnection connection = new DSConnection(this, this.user, this.password);
				this.connections.add(connection);
				connection.use();
				return connection;
			} else {
				while (true) {
					for (DSConnection connection : this.connections) {
						if (DSConnection.DSC_READY == connection.getStatus()) {
							connection.use();
							return connection;
						}
					}
					Thread.sleep(100);
				}
			}
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
		DSConnection connection = getConnection();
		Table table = this.adapter.getTable(connection, name);
		connection.reback();
		return table;
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
		DSConnection connection = getConnection();
		T record = this.adapter.getRecordByPrimarys(connection, mappingInfo.getTable(), recordType, primarys);
		connection.reback();
		return record;
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
		DSConnection connection = getConnection();
		List<T> records = this.adapter.getRecords(connection, mappingInfo.getTable(), filter, recordType, params);
		connection.reback();
		return records;
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
		DSConnection connection = getConnection();
		int count = this.adapter.addRecord(connection, mappingInfo.getTable(), record);
		connection.reback();
		return count;
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
		DSConnection connection = getConnection();
		int count = this.adapter.updateRecord(connection, mappingInfo.getTable(), record);
		connection.reback();
		return count;
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
		DSConnection connection = getConnection();
		int count = this.adapter.removeRecord(connection, mappingInfo.getTable(), record);
		connection.reback();
		return count;
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
		DSConnection connection = getConnection();
		int count = this.adapter.getRecordsCount(connection, mappingInfo.getTable(), filter, params);
		connection.reback();
		return count;
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
		DSConnection connection = getConnection();
		List<T> list = this.adapter.getRecordsByPage(connection, mappingInfo.getTable(), pageNumber, pageSize, filter,
				recordType, params);
		connection.reback();
		return list;
	}

	/**
	 * 执行事务 @throws
	 */
	public void runTransaction(TransactionProcesser tp) {
		DSConnection connection = null;
		try {
			connection = getConnection();
			connection.use().setAutoCommit(false);
			tp.setAdapter(this.adapter);
			tp.setConnection(connection);
			if (tp.run()) {
				connection.use().commit();
			} else {
				connection.use().rollback();
			}

		} catch (Exception e) {
			try {
				connection.use().rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.reback();
			}
		}
	}
}
