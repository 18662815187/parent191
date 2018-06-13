package cn.itcast.core.service;
/**
 * 用户管理
 * @author john
 *
 */

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import cn.itcast.core.bean.BuyerCart;
import cn.itcast.core.bean.BuyerItem;
import cn.itcast.core.bean.order.Detail;
import cn.itcast.core.bean.order.Order;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.bean.user.BuyerQuery;
import cn.itcast.core.dao.order.DetailDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import cn.itcast.core.dao.user.BuyerDao;
import cn.itcast.core.service.user.BuyerService;
import redis.clients.jedis.Jedis;

@Service("buyerService")
public class BuyerServiceImpl implements BuyerService {
	@Resource
	private BuyerDao buyerDao;
	@Resource
	private Jedis jedis;
	@Resource
	private SkuDao skuDao;
	@Resource
	private ColorDao colorDao;
	@Resource
	private ProductDao productDao;
	@Resource
	private OrderDao orderDao;
	@Resource
	private DetailDao detailDao;

	// 通过用户名查询用户对象
	public Buyer selectBuyerByUsername(String username) {
		BuyerQuery buyerQuery = new BuyerQuery();
		buyerQuery.createCriteria().andUsernameEqualTo(username);
		List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
		if (null != buyers && buyers.size() > 0) {
			return buyers.get(0);
		}
		return null;
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

	// 保存订单
	public void insertOrder(Order order, String username) {
		// 订单ID全国唯一 redis
		Long id = jedis.incr("oid");
		order.setId(id);
		// 加载购物车,将购物车需要的数据加满
		BuyerCart buyerCart = selectBuyerCartFromRedis(username);
		List<BuyerItem> items = buyerCart.getItems();
		for (BuyerItem buyerItem : items) {
			buyerItem.setSku(selectSkuById(buyerItem.getSku().getId()));
		}
		// 运费 由购物车提供
		order.setDeliverFee(buyerCart.getFee());
		// 总价
		order.setTotalPrice(buyerCart.getTotalPrice());
		// 订单金额
		order.setOrderPrice(buyerCart.getProductPrice());
		// 支付状态 ： 0到付 ，1待付款，2已付款,3待退款，4退款成功，5退款成功，判断的数字由前端传回
		if (order.getPaymentWay() == 1) {
			order.setIsPaiy(0);
		} else {
			order.setIsPaiy(1);;
		}
		// 订单状态0.提交订单，1仓库配货，2商品出库，3等待收货，4完成，5待退货，6已退货,此处为刚提交订单
		order.setOrderState(0);
		// 时间 后台生成
		order.setCreateDate(new Date());
		// 用户ID:前台用户注册 由redis生成用户ID全国唯一 用户ID 用户名（K V）保存到redis中，本次用户名为K，ID为V
		String uid = jedis.get(username);
		order.setBuyerId(Long.parseLong(uid));
		// 保存订单
		orderDao.insertSelective(order);
		// 保存订单详情，遍历购物项
		for (BuyerItem buyerItem : items) {
			// 订单详情对象
			Detail detail = new Detail();
			// ID
			// 订单ID上面已生成
			detail.setOrderId(id);
			// 商品编号
			detail.setProductId(buyerItem.getSku().getProductId());
			// 商品名称
			detail.setProductName(buyerItem.getSku().getProduct().getName());
			// 颜色
			detail.setColor(buyerItem.getSku().getColor().getName());
			// 尺码
			detail.setSize(buyerItem.getSku().getSize());
			// 价格
			detail.setPrice(buyerItem.getSku().getPrice());
			// 数量
			detail.setAmount(buyerItem.getAmount());
			// 保存详情
			detailDao.insertSelective(detail);
		}
		//清空购物车，此处购物车保存在redis中
		System.out.println(username);
		jedis.del("buyerCart:"+username);
		//删除指定购物车内容,

	}
}
