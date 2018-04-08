package com.xjyy.orm;

import java.util.ArrayList;
import java.util.List;

class FloodProcesser extends Thread {
	static final int FP_PAGINATE = 1;
	private int type;
	private List<Object> params;
	private Object retValue;

	FloodProcesser(int type, Object... params) {
		this.type = type;
		this.params = new ArrayList<>();
		for (Object param : params) {
			this.params.add(param);
		}
	}

	Object getRetValue() {
		return this.retValue;
	}

	@Override
	public void run() {
		try {
			if (FP_PAGINATE == this.type) {
				runPaginate();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 0、recordType 1、filter 2、pageNumber 3、pageSize 4、params
	 */
	@SuppressWarnings("unchecked")
	private void runPaginate() throws Exception {
		Class<? extends Model<?>> recordType = (Class<? extends Model<?>>) this.params.get(0);
		String filter = (String) this.params.get(1);
		int pageNumber = (int) this.params.get(2);
		int pageSize = (int) this.params.get(3);
		Object[] params = (Object[]) this.params.get(4);
		this.retValue = recordType.getMethod("paginate", String.class, int.class, int.class, Object[].class)
				.invoke(recordType.newInstance(), filter, pageNumber, pageSize, params);
	}
}
