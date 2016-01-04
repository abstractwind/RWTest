package rwtest.mapper;

import static org.apache.ibatis.jdbc.SqlBuilder.FROM;
import static org.apache.ibatis.jdbc.SqlBuilder.SELECT;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;

public class SomeDataSqlProvider {
	public String findALL() {
		SELECT("*");
		FROM("some_table");
		return SQL();
	}
}
