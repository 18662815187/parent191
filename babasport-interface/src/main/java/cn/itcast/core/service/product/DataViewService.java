package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.DataView;

public interface DataViewService {
	// 列表分页对象
	public Pagination selectPaginationByQuery(String optionName, Boolean isshow, Integer pageNo);

	// 查询所有数据字典数据集
	public List<DataView> selectAllDataByIsShow(Boolean isshow);
	
}
