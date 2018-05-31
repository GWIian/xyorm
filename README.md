### xyorm

 **xyorm是一个简单轻便的ORM，无繁琐配置引入jar即可使用,Model无注解，无需大量getter，settter，内建连接池机制**
 
 
 **目前支持的数据库**
* Mysql
* Sqlite
* Oracle

 **Quick Start**
 
 使用基本步骤如下：
 1. 引入xyorm的jar包。
 2. 引入您使用的数据库对应的jdbc connector jar包。
 3. Orm添加数据源。
 4. 启动Orm。
 5. 添加继承自Model的业务Model类。
 6. 添加Model到Table的映射。
 7. 大工告成。

Start.java
```
import com.xjyy.adapter.OracleAdapter;
import com.xjyy.orm.DataSource;
import com.xjyy.orm.Orm;

public class Start {

	public static void main(String[] args) {

		DataSource ds = new DataSource("jdbc:oracle:thin:@localhost:1521:orcl", "user", "password");//定义数据源
		ds.setAdapter(new OracleAdapter());//设置数据库类型
		Orm.getInstance().addDataSource(ds);//添加数据源
		
		Orm.getInstance().init();//启动ORM
		Orm.getInstance().addMappingInfo("TEST", "ID,KEY", Test.class);//添加Model到表的映射，第二个参数是主键，联合主键用逗号隔开

		new Test().findByPrimarys(1);//根据主键获取记录
		new Test().find("");//查找记录
		new Test().findFirst("ID=1");//查找第一条记录
		new Test().get("ID");//获取字段的值
		new Test().set("ID", 1).save();//插入记录
		new Test().set("ID", 1).delete();//删除记录
		new Test().set("ID", 1).update();//更新记录
		new Test().paginate("ID>2",1,20);//根据每页20条获取第一页，条件ID大于2
		
		//执行事务
		Orm.getInstance().getDataSource().runTransaction(new TransactionProcesser() {
			@Override
			public void run() {
				save(new Test());//增
				delete(new Test());//删
				update(new Test());//改
			}
		});
		
		Orm.getInstance().stop();//停止ORM
	}
}
```

Test.java
```
import com.xjyy.orm.Model;

//自定义Model只需要继承Model类即可，如果有额外的逻辑自行写方法即可
public class Test extends Model<Test> {

}
```
