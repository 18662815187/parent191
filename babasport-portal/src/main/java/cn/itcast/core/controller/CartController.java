package cn.itcast.core.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.common.utils.RequestUtils;
import cn.itcast.common.web.Constants;
import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.user.BuyerService;
import cn.itcast.core.service.user.SessionProvider;

/**
 * 购物车控制器 去购物车页面 添加商品到购物车 删除 + -
 * 
 * @author john
 *
 */

@Controller
public class CartController {
	@Resource
	private SkuService skuService;
	@Resource
	private SessionProvider sessionProvider;
	@Resource
	private BuyerService buyerService;

	// 加入购物车
	@RequestMapping("/addCart")
	public String addCart(Long skuId, Integer amount, Model model, HttpServletRequest request,
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
		// @RequestBody、@ResponseBody JSON与对象互转,声明ObjectMapper对象
		ObjectMapper om = new ObjectMapper();
		// 排除null的字段
		om.setSerializationInclusion(Include.NON_NULL);
		// 声明购物车
		BuyerCart buyerCart = null;
		// 1.从Request中取Cookies,遍历Cookie 取出之前的购物车
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			// 遍历Cookie 取出之前的购物车
			for (Cookie cookie : cookies) {
				// 2.判断cookie中有没有购物车,如果购物车的name和cookie中name一样则表示有购物车
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					// 转回对象
					buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
					// 节省性能找到直接结束
					break;
				}
			}
		}
		// 判断购物车是否为null
		if (null == buyerCart) {
			buyerCart = new BuyerCart();
		}
		// 追加当前款商品
		Sku sku = new Sku();
		sku.setId(skuId);
		BuyerItem buyerItem = new BuyerItem();
		buyerItem.setSku(sku);
		// Amount数量
		buyerItem.setAmount(amount);
		// 追加商品到购物车
		buyerCart.addItem(buyerItem);
		// 用户是否登录
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		// 判断是否登录
		if (null != username) {
			// 有 把购物车内商品添加到redis的购物车中，
			skuService.insertBuyerCartToRedis(buyerCart, username);
			// 清理cookie
			Cookie cookie = new Cookie(Constants.BUYER_CART, null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
		} else {
			// 如果没有创建购物车
			// 创建cookie
			Writer w = new StringWriter();
			om.writeValue(w, buyerCart);
			Cookie cookie = new Cookie(Constants.BUYER_CART, w.toString());
			// 设置时间,开发期间设置为1天,以秒计算
			cookie.setMaxAge(60 * 60 * 24);
			// 设置路径
			cookie.setPath("/");
			// 跨域
			// cookie.setDomain("www.mryisheng.com");
			// 保存cookie写回浏览器
			response.addCookie(cookie);
		}
		// 跳转到购物车页面
		return "redirect:/toCart";
	}

	// 去购物车页面
	@RequestMapping("/toCart")
	public String toCart(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ObjectMapper om = new ObjectMapper();
		// 排除null的字段
		om.setSerializationInclusion(Include.NON_NULL);
		// 声明购物车
		BuyerCart buyerCart = null;
		// 1.从Request中取Cookies,遍历Cookie 取出之前的购物车
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			// 遍历Cookie 取出之前的购物车
			for (Cookie cookie : cookies) {
				// 2.判断cookie中有没有购物车,如果购物车的name和cookie中name一样则表示有购物车
				if (Constants.BUYER_CART.equals(cookie.getName())) {
					// 转回对象
					buyerCart = om.readValue(cookie.getValue(), BuyerCart.class);
					// 节省性能找到直接结束
					break;
				}
			}
		}
		// 用户是否登录
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		// 判断是否登录
		if (null != username) {
			// 有 把购物车内商品添加到redis的购物车中，
			if (null != buyerCart) {
				skuService.insertBuyerCartToRedis(buyerCart, username);
				// 清理cookie
				Cookie cookie = new Cookie(Constants.BUYER_CART, null);
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}

			// 从redis中取出购物车service层，这时候的数据是cookie和redis的数据总和
			buyerCart = skuService.selectBuyerCartFromRedis(username);
		}
		// 购物车内有,把购物车内需要的数据装满用于回显
		if (null != buyerCart) {
			List<BuyerItem> items = buyerCart.getItems();
			for (BuyerItem buyerItem : items) {
				// buyerItem.getSku().getId();
				buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
			}
		}
		// 回显购物车内容
		model.addAttribute("buyerCart", buyerCart);
		// 跳转到购物车页面
		return "cart";
	}

	// 结算
	@RequestMapping("/buyer/trueBuy")
	public String trueBuy(Long[] skuIds, Model model, HttpServletRequest request, HttpServletResponse response) {
		// 判断购物车中是否有商品
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		BuyerCart buyerCart = skuService.selectBuyerCartFromRedis(username);
		List<BuyerItem> items = buyerCart.getItems();
		// 标记用于判断是否无货,true为无货
		Boolean flag = false;
		// 购物车始终都有实例化，所以不会存在空购物车对象，但是会有购物车内数据为空的状态
		// 而购物车内的数据是items,只需要判断items的长度就行
		if (items.size() > 0) {
			// 将购物车需要的数据装满
			for (BuyerItem buyerItem : items) {
				// buyerItem.getSku().getId();
				// 塞进购物车
				buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
				// 使用数量和库存比较
				if (buyerItem.getAmount() > buyerItem.getSku().getStock()) {
					// 无货
					buyerItem.setIsHave(false);
					flag = true;
				}
			}
			// 至少有一款无货flag=true
			if (flag) {
				// 视图有一个无货就不能进入下个订单页面
				model.addAttribute("buyerCart", buyerCart);
				return "cart";
			}
		} else {
			return "redirect:/toCart";
		}
		// 视图，如果都有货直接进入下个订单页面
		return "order";
	}

	// 提交订单
	@RequestMapping("/buyer/submitOrder")
	public String submitOrder(Order order,Model model, HttpServletRequest request, HttpServletResponse response) {
		//用户名
		String username = sessionProvider.getAttributeForUsername(RequestUtils.getCSESSIONID(request, response));
		buyerService.insertOrder(order, username);
		return "success";
	}
}
