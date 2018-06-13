package cn.itcast.core.service.user;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.user.Buyer;

public interface BuyerService {
	// 通过用户名查询用户对象
	public Buyer selectBuyerByUsername(String username);

	// 保存订单
	public void insertOrder(Order order, String username);

	// 通过skuid查询sku对象
	public Sku selectSkuById(Long id);

	// 从redis中取出购物车
	public BuyerCart selectBuyerCartFromRedis(String username);
}
