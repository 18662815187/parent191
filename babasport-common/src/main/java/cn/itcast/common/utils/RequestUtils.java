package cn.itcast.common.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 获取CSESSIONID
 * 
 * @author john
 *
 */
public class RequestUtils {
	// 获取
	public static String getCSESSIONID(HttpServletRequest request, HttpServletResponse response) {
		// 1.去除Cookie
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			// 2.判断Cookie中是否有CSESSIONID
			for (Cookie cookie : cookies) {
				if ("CSESSIONID".equals(cookie.getName())) {
					// 3.有 直接使用
					return cookie.getValue();
				}
			}
		}
		// 4.没有 创建一个CSESSIONID 并保存到Cookie中 同时把cookie写回浏览器
		// 使用此生成的CSESSIONID
		String csessionid = UUID.randomUUID().toString().replace("-", "");
		Cookie cookie = new Cookie("CSESSIONID", csessionid);
		// 设置Cookie存活时间，-1, 0 , >0 -1是关闭浏览器销毁，0是立即销毁
		cookie.setMaxAge(-1);
		// 设置路径,不设置成/时会不携带cookie
		cookie.setPath("/");
		// 设置跨域
		// cookie.setDomain(".mryisheng.com");
		// 写回浏览器
		response.addCookie(cookie);
		return csessionid;
	}
}
