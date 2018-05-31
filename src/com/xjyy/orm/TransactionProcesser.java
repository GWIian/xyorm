package com.xjyy.orm;

import com.xjyy.adapter.Adapter;

public abstract class TransactionProcesser {
	private Adapter adapter;
	private boolean hasError;

	void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	private DSConnection connection;

	void setConnection(DSConnection connection) {
		this.connection = connection;
	}

	boolean hasError() {
		return hasError;
	}

	/**
	 * 执行事务 返回true 提交事务 返回false 回滚事务
	 * 
	 * @return
	 */
	public abstract void run();

	/**
	 * 增
	 * 
	 * @param model
	 */
	public <T> void save(Model<T> model) {
		if (!hasError) {
			hasError = this.adapter.addRecord(this.connection, model.getMappingInfo().getTable(),
					model.getAttributes()) > 0 ? false : true;
		}
	}

	/**
	 * 改
	 * 
	 * @param model
	 */
	public <T> void update(Model<T> model) {
		if (!hasError) {
			hasError = this.adapter.updateRecord(this.connection, model.getMappingInfo().getTable(),
					model.getAttributes()) > 0 ? false : true;
		}
	}

	/**
	 * 删
	 * 
	 * @param model
	 */
	public <T> void delete(Model<T> model) {
		if (!hasError) {
			hasError = this.adapter.removeRecord(this.connection, model.getMappingInfo().getTable(),
					model.getAttributes()) > 0 ? false : true;
		}
	}
}
