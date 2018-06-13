package cn.itcast.core.service.product;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.solr.client.solrj.SolrServer;
//import org.apache.solr.common.SolrInputDocument;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.ColorQuery;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.ProductQuery.Criteria;
import cn.itcast.core.bean.product.Sku;
//import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ColorDao;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;
import redis.clients.jedis.Jedis;

/**
 * 商品
 * 
 * @author john
 *
 */
@Service("productService")
@Transactional
public class ProductServiceImpl implements ProductService {
	@Resource
	private ProductDao productDao;
	@Resource
	private ColorDao colorDao;
	@Resource
	private SkuDao skuDao;
	// 注入redis的jedis
	@Resource
	private Jedis jedis;
	// 注入SolrJ
	@Resource
	private SolrServer solrServer;
	// 注入ActiveMQ jmsTemplate
	@Resource
	private JmsTemplate jmsTemplate;

	// 分页对象
	public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow) {
		ProductQuery productQuery = new ProductQuery();
		productQuery.setPageNo(Pagination.cpn(pageNo));
		// 设置排序为倒序
		productQuery.setOrderByClause("id desc");
		Criteria createCriteria = productQuery.createCriteria();
		StringBuilder params = new StringBuilder();
		if (null != name) {
			createCriteria.andNameLike("%" + name + "%");
			params.append("name=").append(name);
		}
		if (null != brandId) {
			createCriteria.andBrandIdEqualTo(brandId);
			params.append("&brandId=").append(brandId);
		}
		if (null != isShow) {
			createCriteria.andIsShowEqualTo(isShow);
			params.append("&isShow=").append(isShow);
		} else {
			// 如未选择查询条件则默认查询条件为不上架
			createCriteria.andIsShowEqualTo(false);
			params.append("&isShow=").append(false);
		}
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(),
				productDao.countByExample(productQuery), productDao.selectByExample(productQuery));

		// 分页展示,需要传入链接和拼接条件
		String url = "/product/list.do";
		pagination.pageView(url, params.toString());
		return pagination;
	}

	// 颜色结果集
	@Override
	public List<Color> selectColorList() {
		ColorQuery colorQuery = new ColorQuery();
		colorQuery.createCriteria().andParentIdNotEqualTo(0L);
		return colorDao.selectByExample(colorQuery);
	}

	/**
	 * 保存、下架状态，假删除
	 */
	@Override
	public void insertProduct(Product product) {
		// 使用redis生成id
		Long id = jedis.incr("pno");
		product.setId(id);
		// 设置下架状态为下架
		product.setIsShow(false);
		// 设置删除状态为不删除
		product.setIsDel(true);
		// 设置创建时间
		product.setCreateTime(new Date());
		productDao.insertSelective(product);
		// 返回id并保存sku
		String[] colors = product.getColors().split(",");
		String[] sizes = product.getSizes().split(",");
		// 每个颜色都有不同尺码，需要循环
		for (String color : colors) {
			for (String size : sizes) {
				// 保存sku
				Sku sku = new Sku();
				// 商品id
				sku.setProductId(product.getId());
				// 颜色,原本传进来的color就是id，但不是long类型，需要强制转型
				sku.setColorId(Long.parseLong(color));
				// 尺码
				sku.setSize(size);
				// 市场价
				sku.setMarketPrice(0f);
				// 售价
				sku.setPrice(0f);
				// 运费
				sku.setDeliveFee(8f);
				// 库存
				sku.setStock(0);
				// 购买限制
				sku.setUpperLimit(200);
				// 创建时间
				sku.setCreateTime(new Date());
				skuDao.insertSelective(sku);
			}
		}

	}

	// 上架支持批量
	@Override
	public void isShow(Long[] ids) {
		Product product = new Product();
		// 上架
		product.setIsShow(true);
		for (Long id : ids) {
			product.setId(id);
			// 商品状态变更
			productDao.updateByPrimaryKeySelective(product);
			// 发送信息到ActiveMQ中
			// 如果不使用配置文件默认的目标时使用下方注释中的方法
			// jmsTemplate.send("brandId", messageCreator);
			// 此次使用配置文件中默认的目标
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub

					return session.createTextMessage(String.valueOf(id));
				}
			});

			// solr不在product服务器上，需要ActiveMQ消息队列提供支持

			// TODO静态化
		}

	}

	@Override
	public void unShow(Long[] ids) {

		Product product = new Product();
		// 下架
		product.setIsShow(false);
		for (Long id : ids) {
			product.setId(id);
			// 商品状态变更
			productDao.updateByPrimaryKeySelective(product);

			try {
				// 下架需要清除solr服务器上相应数据
				solrServer.deleteById(String.valueOf(id));
				// solrServer.add(doc);
				solrServer.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// solr不在product服务器上，需要ActiveMQ消息队列提供支持

			// TODO静态化
		}
	}

}
