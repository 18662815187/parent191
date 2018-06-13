package cn.itcast;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-context.xml"})
public class TestRedis {

	@Resource
	private Jedis jedis;
	@Test
	public void testSpringRedis()throws Exception{
		jedis.set("ooo", "111");
		
	}
	@Test
	public void testRedis()throws Exception{
		Jedis jedis = new Jedis("192.168.200.128", 6379);
		Long pno =jedis.incr("pno");
		System.out.println(pno);
		jedis.close();
	}
}
