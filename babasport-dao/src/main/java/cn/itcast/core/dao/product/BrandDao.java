package cn.itcast.core.dao.product;

import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BrandDao {
	int countByExample(BrandQuery example);

	int deleteByExample(BrandQuery example);

	int deleteByPrimaryKey(Long id);

	int insert(Brand record);

	int insertSelective(Brand record);

	List<Brand> selectByExample(BrandQuery example);

	Brand selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Brand record, @Param("example") BrandQuery example);

	int updateByExample(@Param("record") Brand record, @Param("example") BrandQuery example);

	int updateByPrimaryKeySelective(Brand record);

	int updateByPrimaryKey(Brand record);

	// 批量删除
	public void deletes(Long[] ids);// List<Long> ids
}