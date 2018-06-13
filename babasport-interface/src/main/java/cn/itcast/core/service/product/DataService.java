package cn.itcast.core.service.product;

import java.util.List;

import cn.itcast.core.bean.product.Data;

public interface DataService {
	// 查询可用数据字典集合
	public List<Data> selectAllDataByIsShow(Boolean isshow);

	// 新增
	public void addNewData(Data data);

	// 修改
	public void UpdateById(Data data);

	// 删除
	public void deleteById(Long id);

	// 根据ID查询
	public Data selectDataById(Long id);

	// 批量删除
	public void deletes(Long[] ids);
	//根据option_level查询所有子类
	public List<Data> selectByLevel(Long id); 
	//查找所有可用和option_level=0的数据
	public List<Data> selectByExample();
}
