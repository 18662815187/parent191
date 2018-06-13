package cn.itcast.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.product.SkuQuery;
import cn.itcast.core.dao.product.ProductDao;
import cn.itcast.core.dao.product.SkuDao;

/**
 * 全文检索Solr
 * 
 * @author john
 *
 */

@Service("searchService")
public class SearchServiceImpl implements SearchService {
	// 注入solr
	@Resource
	private SolrServer solrServer;
	@Resource
	private ProductDao productDao;
	@Resource
	private SkuDao skuDao;

	// 全文检索
	public Pagination selectPaginationByQuery(Integer pageNo, String keyword, Long brandId, String price)
			throws Exception {

		// 创建包装类
		ProductQuery productQuery = new ProductQuery();
		// 当前页
		productQuery.setPageNo(Pagination.cpn(pageNo));
		// 每页显示数量
		productQuery.setPageSize(15);
		// 拼接分页条件
		StringBuilder params = new StringBuilder();

		List<Product> products = new ArrayList<Product>();

		SolrQuery solrQuery = new SolrQuery();
		// if (keyword == null || keyword == "") {
		// 空值时查询*
		// params.append("keyword=").append("*");
		// solrQuery.set("q", "name_ik:*");
		// } else {
		// }
		// 关键词
		params.append("keyword=").append(keyword);
		solrQuery.set("q", "name_ik:" + keyword);
		// 过滤条件
		// 过滤品牌
		if (null != brandId) {
			solrQuery.addFilterQuery("brandId:" + brandId);
		}
		// 过滤价格 0-99 1600 0 TO 99 通过切割“-”或者起始和结束价格，最大长度是2，最小是1
		if (null != price) {
			String[] p = price.split("-");
			if (p.length == 2) {
				solrQuery.addFilterQuery("price:[" + p[0] + " TO " + p[1] + "]");
			} else {
				solrQuery.addFilterQuery("price:[" + p[0] + " TO *]");
			}

		}

		// 高亮开关
		solrQuery.setHighlight(true);
		// 设置高亮字段，此字段是在solr的搜索字段
		solrQuery.addHighlightField("name_ik");
		// 设置样式前缀
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		// 设置高亮后缀
		solrQuery.setHighlightSimplePost("</span>");
		// 排序价格低到高
		solrQuery.addSort("price", ORDER.asc);
		// 分页limit 开始行和size
		solrQuery.setStart(productQuery.getStartRow());
		solrQuery.setRows(productQuery.getPageSize());
		// 执行查询
		QueryResponse response = solrServer.query(solrQuery);
		// 取高亮外层map是指 id：map 内层map 刚才id产品高亮部分的 name_ik :
		// list<String>,list是指产品名称,
		// 用list是因为名称内可能存在多个名字用逗号隔开
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		// 结果集
		SolrDocumentList docs = response.getResults();
		// 发现的条数（总条数）后期分页需要用
		long numFound = docs.getNumFound();
		for (SolrDocument doc : docs) {
			// 创建商品对象
			Product product = new Product();
			// 商品ID
			String id = (String) doc.get("id");
			product.setId(Long.parseLong(id));
			// 商品名称ik
			Map<String, List<String>> map = highlighting.get(id);
			List<String> list = map.get("name_ik");
			product.setName(list.get(0));
			/*
			 * String name = (String) doc.get("name_ik"); product.setName(name);
			 */
			// 图片
			String url = (String) doc.get("url");
			product.setPic(url);
			// 价格
			product.setPrice((Float) doc.get("price"));
			// 品牌ID
			product.setBrandId(Long.parseLong(String.valueOf((Integer) doc.get("brandId"))));
			// 组装进结果集
			products.add(product);
		}
		// 构建分页对象
		Pagination pagination = new Pagination(productQuery.getPageNo(), productQuery.getPageSize(), (int) numFound,
				products);

		// 页面展示
		String url = "/search";
		pagination.pageView(url, params.toString());
		return pagination;
	}

	// 保存商品信息到solr服务器
	public void insertProductToSolr(Long id) {
		// TODO保存商品信息到SOLR服务器
		SolrInputDocument doc = new SolrInputDocument();
		Product p = productDao.selectByPrimaryKey(id);
		// id
		doc.addField("id", id);
		// 商品名称
		doc.addField("name_ik", p.getName());
		// 图片URL
		doc.addField("url", p.getImages()[0]);
//		doc.addField("url", p.getPic());
		// 价格售价select price from bbs_sku where product_id=442 order by price
		// asc limit 1
		// 每个商品不同颜色尺寸价格不一样，只取价格字段排序为顺序，取第一条
		SkuQuery skuQuery = new SkuQuery();
		skuQuery.createCriteria().andProductIdEqualTo(id);
		skuQuery.setOrderByClause("price asc");
		skuQuery.setPageNo(1);
		skuQuery.setPageSize(1);
		skuQuery.setFields("price");
		List<Sku> skus = skuDao.selectByExample(skuQuery);
		doc.addField("price", skus.get(0).getPrice());
		// 品牌ID
		doc.setField("brandId", p.getBrandId());
		/*
		 * System.out.println(doc.getFieldValue("name_ik") + "-----------" +
		 * doc.getFieldValue("id") + "-----------" + doc.getFieldValue("url") +
		 * "-----------" + doc.getFieldValue("price") + "-----------" +
		 * doc.getFieldValue("brandId"));
		 */
		// 时间暂时不用
		try {
			solrServer.add(doc);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
