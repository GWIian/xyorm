package xyorm.test;

import com.xjyy.adapter.SqliteAdapter;
import com.xjyy.orm.DataSource;
import com.xjyy.orm.Orm;
import com.xjyy.orm.TransactionProcesser;

public class Test1 {

	public static void printFaild(Object retVal) {
		StackTraceElement[] stacks = (new Throwable()).getStackTrace();
		System.err.println("失败：[" + stacks[1].getMethodName() + "][" + retVal + "]");
	}

	public static void printSuccess(Object retVal) {
		StackTraceElement[] stacks = (new Throwable()).getStackTrace();
		System.out.println("成功：[" + stacks[1].getMethodName() + "][" + retVal + "]");
	}

	public static void main(String[] args) {
		testModelSave_Sqlite();
		testModelSave_Tx_Sqlite();
	}

	public static void testModelSave_Sqlite() {
		Orm.getInstance().addDataSource(new DataSource("jdbc:sqlite:resource/testdb.db", "", ""));
		Orm.getInstance().getDataSource().setAdapter(new SqliteAdapter());
		Orm.getInstance().init();
		Orm.getInstance().addMappingInfo("Student", "id", Student.class);

		Student student = new Student();
		student.set("name", "ian").set("age", 23).save();

		Object id = student.get("id");
		if (id == null) {
			printFaild(id);
		} else {
			printSuccess(id);
		}

		Orm.getInstance().stop();
	}

	public static void testModelSave_Tx_Sqlite() {
		Orm.getInstance().addDataSource(new DataSource("jdbc:sqlite:resource/testdb.db", "", ""));
		Orm.getInstance().getDataSource().setAdapter(new SqliteAdapter());
		Orm.getInstance().init();
		Orm.getInstance().addMappingInfo("Student", "id", Student.class);

		Orm.getInstance().getDataSource().runTransaction(new TransactionProcesser() {

			@Override
			public boolean run() {
				Student student;
				Object id;

				for (int i = 0; i < 5; i++) {
					student = new Student();
					student.set("name", "ian").set("age", 23);
					save(student);
					id = student.get("id");
					if (id == null) {
						printFaild(id);
					} else {
						printSuccess(id);
					}
				}

				return false;
			}
		});

		Orm.getInstance().stop();
	}
}
