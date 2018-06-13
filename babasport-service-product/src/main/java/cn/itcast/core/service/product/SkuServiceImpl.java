package cn.itcast.core.service.product;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import redis.clients.jedis.Jedis;

/**
 * 库存管理
 * 
 * @author john
 *
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {
	@Resource
	private SkuDao skuDao;
	@Resource
	private ColorDao colorDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private Jedis jedis;

	// 查询库存 参数：商品ID
	public List<Sku> selectSkuListByProductId(Long productId) {
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(productId);
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		// 每个产品有多个尺寸和颜色，颜色在库存表中只是一个id，需要颜色的中午必须去颜色表查询，
		// 并且需要通过遍历设置颜色
		for (Sku sku : skus) {
			// 3条sql 一级缓存
			sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		}
		return skus;
	}

	// 修改保存
	@Override
	public void updateSkuById(Sku sku) {
		skuDao.updateByPrimaryKeySelective(sku);
	}

	// 通过skuid查询sku对象
	public Sku selectSkuById(Long id) {
		// Sku对象
		Sku sku = skuDao.selectByPrimaryKey(id);
		// 商品对象
		sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
		// 颜色对象
		sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
		// 返回sku
		return sku;
	}

	// 保存商品到redis中
	public void insertBuyerCartToRedis(BuyerCart buyerCart, String username) {
		// 判断购物项的长度大于0
		List<BuyerItem> items = buyerCart.getItems();
		if (items.size() > 0) {
			for (BuyerItem buyerItem : items) {
				// 判断是否已经存在
				if (jedis.hexists("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()))) {
					// 如果已存在则同款加数量
					jedis.hincrBy("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()),
							buyerItem.getAmount());
				} else {
					//如果redis不存在相同商品则直接添加
					jedis.hset("buyerCart:" + username, String.valueOf(buyerItem.getSku().getId()),
							String.valueOf(buyerItem.getAmount()));
				}
			}
		}
	}

	// 从redis中取出购物车
	public BuyerCart selectBuyerCartFromRedis(String username) {
		BuyerCart buyerCart = new BuyerCart();
		Map<String, String> hgetAll = jedis.hgetAll("buyerCart:" + username);
		if (null != hgetAll) {
			Set<Entry<String, String>> entrySet = hgetAll.entrySet();
			for (Entry<String, String> entry : entrySet) {
				// 追加当前商品到购物车
				Sku sku = new Sku();
				// id
				sku.setId(Long.parseLong(entry.getKey()));
				BuyerItem buyerItem = new BuyerItem();
				buyerItem.setSku(sku);
				// Amount
				buyerItem.setAmount(Integer.parseInt(entry.getValue()));
				// 塞进购物车
				buyerCart.addItem(buyerItem);
			}
		}
		return buyerCart;
	}
}
