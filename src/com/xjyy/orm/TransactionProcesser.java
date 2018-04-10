package com.xjyy.orm;

import com.xjyy.adapter.Adapter;

public abstract class TransactionProcesser {
	private Adapter adapter;

	void setAdapter(Adapter adapter) {
		this.adapter = adapter;
	}

	private DSConnection connection;

	void setConnection(DSConnection connection) {
		this.connection = connection;
	}

	/**
	 * 执行事务
	 * 返回true 提交事务
	 * 返回false 回滚事务
	 * @return
	 */
	public abstract boolean run();

	/**
	 * 增
	 * @param model
	 */
	public <T> void save(Model<T> model) {
		this.adapter.addRecord(this.connection, model.getMappingInfo().getTable(), model.getAttributes());
	}

	/**
	 * 改
	 * @param model
	 */
	public <T> void update(Model<T> model) {
		this.adapter.updateRecord(this.connection, model.getMappingInfo().getTable(), model.getAttributes());
	}

	/**
	 * 删
	 * @param model
	 */
	public <T> void delete(Model<T> model) {
		this.adapter.removeRecord(this.connection, model.getMappingInfo().getTable(), model.getAttributes());
	}
}
