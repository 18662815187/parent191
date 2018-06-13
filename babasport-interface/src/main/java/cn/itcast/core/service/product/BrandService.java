package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;

public interface BrandService {
	// 查询所有品牌数据
	public Pagination selectPaginationByQuery(String name, Boolean isDisplay, Integer pageNo);

	// 通过ID查询品牌
	public Brand selectBrandById(Long id);

	// 修改
	public void updateBrandById(Brand brand);

	// 新增
	public void addNewBrand(Brand brand);

	// 删除
	public void deleteById(Long id);

	// 批量删除
	public void deletes(Long[] ids);
	//所有数据集
	public List<Brand> selectBrandListByQuery(Boolean isDisplay);
	//从redis中查询
	public List<Brand> selectBrandListFromRedis();
}
