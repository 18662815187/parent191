package cn.itcast.core.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Hex;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.common.utils.RequestUtils;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.service.user.BuyerService;
import cn.itcast.core.service.user.SessionProvider;

/**
 * 单点登录 去登录页面GET 提交登录表单POST 加密MD5、十六进制 加盐
 * 
 * @author john
 *
 */

@Controller
public class LoginController {
	@Resource
	private BuyerService buyerService;
	// session供应类,此处已经保存到redis中由redis提供
	@Resource
	private SessionProvider sessionProvider;

	// 去登录页面GET
	@RequestMapping(value = "/login.aspx", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	// 判断用户是否登录
	@ResponseBody
	@RequestMapping("isLogin.aspx")
	public MappingJacksonValue isLogin(String callback, HttpServletRequest request, HttpServletResponse response) {
		Integer result = 0;
		// 判断用户是否以登录
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		if (null != username) {
			result = 1;
		}
		//支持jsonp,回调
		MappingJacksonValue mjv = new MappingJacksonValue(result);
		mjv.setJsonpFunction(callback);
		return mjv;
	}

	// 提交登录POST,使用springmvc的Converter转换器可以大大简化判断空值的操作,转换器工厂在mvc配置文件，工具类在common项目中
	@RequestMapping(value = "/login.aspx", method = RequestMethod.POST)
	public String login(String username, String password, String returnUrl, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 如有验证码优先级在最前面
		// 1.用户名不能为空
		if (null != username) {
			// 2.密码不能为空
			if (null != password) {
				// 3.用户名必须正确
				Buyer buyer = buyerService.selectBuyerByUsername(username);
				if (null != buyer) {
					// 4.密码必须正确
					if (buyer.getPassword().equals(encodePassword(password))) {
						// 5.保存用户名到session，原名JSESSIONID，自定义为CSESSIONID，
						// 避免被request使用造成在集群中其他机器重新生成而取不到数据，生成
						// 规则为UUID（36位带“-”连接），把4个“-”去掉剩余32位
						// 首次使用session需要创建后保存到cookie，非首次直接在cookie取
						sessionProvider.setAttribuerForUsername(RequestUtils.getCSESSIONID(request, response),
								buyer.getUsername());
						// 6.跳转到之前访问页面
						return "redirect:" + returnUrl;
					} else {
						model.addAttribute("error", "密码不正确");
					}
				} else {
					model.addAttribute("error", "用户名不能为空！");
				}
			} else {
				model.addAttribute("error", "密码不能为空！");
			}

		} else {
			model.addAttribute("error", "用户名不能为空！");
		}
		return "login";
	}

	// 加密
	public String encodePassword(String password) {
		// 加盐,此次不加
		// password = "qwerty" + password + "xyzc";
		// 1.MD5 算法
		String algorithm = "MD5";
		char[] encodeHex = null;
		try {
			// MD5加密
			MessageDigest instance = MessageDigest.getInstance(algorithm);
			// 加密后的密文
			byte[] digest = instance.digest(password.getBytes());
			//
			// 2.十六进制
			encodeHex = Hex.encodeHex(digest);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new String(encodeHex);
	}

	public static void main(String[] args) {
		LoginController loginController = new LoginController();
		String w = loginController.encodePassword("123456");
		System.out.println(w);
	}
}
