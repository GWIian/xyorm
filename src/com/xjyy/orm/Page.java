package com.xjyy.orm;

import java.util.List;

public class Page<T> {
	private int pageNumber;
	private int pageSize;
	private int pageCount;
	private int rowCount;
	private List<T> list;

	Page(List<T> list, int pageNumber, int pageSize, int pageCount, int rowCount) {
		this.list = list;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.pageCount = pageCount;
		this.rowCount = rowCount;
	}

	/**
	 * 获取当前页数据
	 * 
	 * @return
	 */
	public List<T> getList() {
		return this.list;
	}

	/**
	 * 获取当前页码
	 * 
	 * @return
	 */
	public int getPageNumber() {
		return this.pageNumber;
	}

	/**
	 * 是否有上一页
	 * 
	 * @return
	 */
	public boolean hasPre() {
		return this.pageNumber > 1;
	}

	/**
	 * 是否有下一页
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return this.pageNumber < this.pageCount;
	}

	/**
	 * 获取总页数
	 * 
	 * @return
	 */
	public int getPageCount() {
		return this.pageCount;
	}

	/**
	 * 获取总行数
	 * 
	 * @return
	 */
	public int getRowCount() {
		return this.rowCount;
	}

	/**
	 * 获取当前页大小
	 * 
	 * @return
	 */
	public int getPageSize() {
		return this.pageSize;
	}
}
