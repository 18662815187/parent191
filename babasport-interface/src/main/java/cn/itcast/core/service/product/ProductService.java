package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;

public interface ProductService {
	//列表页返回分页对象
	public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow);
	//颜色结果集
	public List<Color> selectColorList();
	//保存
	public void insertProduct(Product product);
	//上架支持批量
	public void isShow(Long[] ids);
	//下架支持批量
	public void unShow(Long[] ids);
}
