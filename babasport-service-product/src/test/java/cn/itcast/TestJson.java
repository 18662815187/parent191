package cn.itcast;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.core.bean.user.Buyer;


public class TestJson {
	

	@Test
	public void testJson() throws Exception {
		//@RequestBody、@ResponseBody  JSON与对象互转
		Buyer buyer = new Buyer();
		buyer.setUsername("测试对象");
		ObjectMapper om = new ObjectMapper();
		//排除null的字段
		om.setSerializationInclusion(Include.NON_NULL);
		//writeValueString是一次性拿，现在用流处理一点点拿
		Writer w = new StringWriter();
		om.writeValue(w, buyer);
		System.out.println(w.toString());
		//转回对象
		Buyer r = om.readValue(w.toString(), Buyer.class);
		System.out.println(r);
	}

}
