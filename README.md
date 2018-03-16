### xyorm

 **xyorm是一个简单轻便的ORM，无繁琐配置引入jar即可使用**

 **Quick Start**

Start.java
```
import com.xjyy.adapter.OracleAdapter;
import com.xjyy.orm.DataSource;
import com.xjyy.orm.Orm;

public class Start {

	public static void main(String[] args) {

		DataSource ds = new DataSource("jdbc:oracle:thin:@localhost:1521:orcl", "user", "password");
		ds.setAdapter(new OracleAdapter());
		Orm.getInstance().addDataSource(ds);
		
		Orm.getInstance().init();
		Orm.getInstance().addMappingInfo("TEST", "ID,KEY", Test.class);

		new Test().find("");
		new Test().findFirst("ID=1");
		new Test().get("ID");
		new Test().set("ID", 1).save();
		new Test().set("ID", 1).delete();
		new Test().set("ID", 1).update();
		
		Orm.getInstance().stop();
	}
}
```

Test.java
```
import com.xjyy.orm.Model;

public class Test extends Model<Test> {

}
```