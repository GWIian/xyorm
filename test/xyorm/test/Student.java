package xyorm.test;

import com.xjyy.orm.Model;

public class Student extends Model<Student> {
	public static final Student dao = new Student().dao();

	public int getAge() {
		return get("AGE") == null ? 0 : ((Number) get("AGE")).intValue();
	}
}
