package cn.itcast.core.service.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.product.Data;
import cn.itcast.core.bean.product.DataQuery;
import cn.itcast.core.bean.product.DataQuery.Criteria;
import cn.itcast.core.dao.product.DataDao;

/**
 * 数据字典service
 * 
 * @author john
 *
 */

@Service("dataService")
@Transactional
public class DataServiceImpl implements DataService {
	@Resource
	private DataDao dataDao;

	// 查询可用数据字典集合
	@Override
	public List<Data> selectAllDataByIsShow(Boolean isshow) {
		DataQuery dataQuery = new DataQuery();
		Criteria createCriteria = dataQuery.createCriteria();
		createCriteria.andIsshowEqualTo(isshow);
		return dataDao.selectByExample(dataQuery);
	}

	@Override
	public void addNewData(Data data) {
		dataDao.insertSelective(data);
	}

	@Override
	public void UpdateById(Data data) {
		dataDao.updateByPrimaryKeySelective(data);
	}

	// 单删
	@Override
	public void deleteById(Long id) {
		dataDao.deleteByPrimaryKey(id);
	}

	// 根据ID查询
	@Override
	public Data selectDataById(Long id) {

		return dataDao.selectByPrimaryKey(id);
	}

	// 批量删除
	@Override
	public void deletes(Long[] ids) {
		dataDao.deletes(ids);

	}

	// 根据option_level查询所有子类
	@Override
	public List<Data> selectByLevel(Long id) {
		DataQuery dataQuery = new DataQuery();
		Criteria createCriteria = dataQuery.createCriteria();
		createCriteria.andOptionLevelEqualTo(id);
		return dataDao.selectByExample(dataQuery);
	}

	// 查找所有可用和option_level=0的数据
	@Override
	public List<Data> selectByExample() {
		DataQuery dataQuery = new DataQuery();
		Criteria createCriteria = dataQuery.createCriteria();
		createCriteria.andIsshowEqualTo(true);
		createCriteria.andOptionLevelEqualTo(0L);
		return dataDao.selectByExample(dataQuery);
	}

}
