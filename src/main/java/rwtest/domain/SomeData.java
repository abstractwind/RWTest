package rwtest.domain;

import org.apache.ibatis.type.Alias;

@Alias("some_table")
public class SomeData {
	private Integer id;
	private String name;
	private String val;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return "SomeData [id=" + id + ", name=" + name + ", val=" + val + "]";
	}
	
	
}
