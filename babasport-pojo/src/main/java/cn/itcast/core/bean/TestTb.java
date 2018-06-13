package cn.itcast.core.bean;

import java.io.Serializable;
import java.util.Date;

public class TestTb implements Serializable {
	/**
	 * 消费方及提供方传递的参数必须实现序列化接口
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Date birthday;

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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	//初始化
	@Override
	public String toString() {
		return "TestTb [id=" + id + ", name=" + name + ", birthday=" + birthday + "]";
	}

}
