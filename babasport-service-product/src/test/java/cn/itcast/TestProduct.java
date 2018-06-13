package cn.itcast;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.ProductQuery;
import cn.itcast.core.dao.product.ProductDao;

/**
 * 测试类 junit
 * 
 * @author yl
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestProduct {
	@Resource
	private ProductDao productDao;
	@Test
	public void testTb()throws Exception{
		//通过ID查商品
//		Product product = productDao.selectByPrimaryKey(441L);
//		System.out.println(product);
		/**
		 * 通过条件、分页、指定字段查询、排序
		 * 
		 */
		ProductQuery productQuery = new ProductQuery();
		//创建一个内部对象，即创建一个查询对象,这是搜索品牌id是4
		//productQuery.createCriteria().andBrandIdEqualTo(4L).andNameLike("%好莱坞%");
		//分页
		productQuery.setPageNo(1);
		productQuery.setPageSize(10);
		//排序
		productQuery.setOrderByClause("id desc");
		//指定字段查询
		productQuery.setFields("id,brand_id");
		List<Product> products = productDao.selectByExample(productQuery);
		for (Product product : products) {
			System.err.println(product);
		}
		int countByExample = productDao.countByExample(productQuery);
		System.err.println(countByExample);
	}

}
