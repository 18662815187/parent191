package cn.itcast.core.service;

import javax.annotation.Resource;

import cn.itcast.common.web.Constants;
import cn.itcast.core.service.user.SessionProvider;
import redis.clients.jedis.Jedis;

/**
 * session供应类 保存用户名或者密码、验证码到redis中 Session共享
 * 
 * @author john
 *
 */
public class SessionProviderImpl implements SessionProvider {
	// 注入redis
	@Resource
	private Jedis Jedis;
	private Integer exp = 30;

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	@Override
	public void setAttribuerForUsername(String name, String value) {
		// 保存用户名到redis中
		// K: CSESSIONID:Constants.USER_NAME == NAME
		Jedis.set(name + ":" + Constants.USER_NAME, value);
		// 时间,session生命周期，这里用30分钟
		Jedis.expire(name + ":" + Constants.USER_NAME, 60 * exp);
	}

	@Override
	public String getAttributeForUsername(String name) {
		// 取session
		String value = Jedis.get(name + ":" + Constants.USER_NAME);
		//session过期时间为最后一次访问开始计时，一直在使用时是不会失效的
		if(null!=value){
			//如果值不为空每次获取都重新刷新生命周期为原来的周期
			Jedis.expire(name + ":" + Constants.USER_NAME, 60*exp);
		}
		return value;
	}

}
