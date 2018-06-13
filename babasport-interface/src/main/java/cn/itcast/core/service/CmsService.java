package cn.itcast.core.service;

import java.util.List;

import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;

public interface CmsService {
	//查询商品
	public Product selectProductById(Long productId);
	//查询SKU结果集半酣颜色，只显示有货
	public List<Sku> selectSkuListByProductId(Long productId);
}
