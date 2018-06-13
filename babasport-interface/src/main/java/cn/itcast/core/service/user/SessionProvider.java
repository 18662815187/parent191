package cn.itcast.core.service.user;

public interface SessionProvider {

	//先行提供接口
	//保存用户名到Redis中
	public void setAttribuerForUsername(String name,String value);
	
	//取用户名，从Redis
	public String getAttributeForUsername(String name);
	
	//验证码
	
	
	//退出登录（session失效）
}
