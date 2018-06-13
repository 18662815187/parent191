package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.product.Sku;

/**
 * 库存管理
 * 
 * @author john
 *
 */
public interface SkuService {
	// 列表
	public List<Sku> selectSkuListByProductId(Long productId);

	// 修改保存
	public void updateSkuById(Sku sku);

	// 查询sku对象
	public Sku selectSkuById(Long id);

	// 将购物车保存到redis中
	public void insertBuyerCartToRedis(BuyerCart buyerCart, String username);

	// 从redis中取出购物车
	public BuyerCart selectBuyerCartFromRedis(String username);
}
