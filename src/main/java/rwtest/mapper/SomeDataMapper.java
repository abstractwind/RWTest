package rwtest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import rwtest.domain.SomeData;

public interface SomeDataMapper {

	@Insert("insert into some_table values (#{id},#{name},#{val})")
	int addSomeData(SomeData someData);
	
	@Select("select * from some_table where id = #{id}")
	SomeData findOneSomeData(@Param("id") long id);
	
	@SelectProvider(type = SomeDataSqlProvider.class, method = "findALL")
	List<SomeData> findALL();

}
