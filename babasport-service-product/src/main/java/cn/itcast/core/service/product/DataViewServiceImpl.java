package cn.itcast.core.service.product;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.DataView;
import cn.itcast.core.bean.product.DataViewQuery;
import cn.itcast.core.bean.product.DataViewQuery.Criteria;
import cn.itcast.core.dao.product.DataViewDao;

@Service("dataViewService")
@Transactional
public class DataViewServiceImpl implements DataViewService {
	@Resource
	private DataViewDao dataViewDao;

	// list
	@Override
	public Pagination selectPaginationByQuery(String optionName, Boolean isshow, Integer pageNo) {
		DataViewQuery dataViewQuery = new DataViewQuery();
		dataViewQuery.setPageNo(Pagination.cpn(pageNo));
		// 此处不用倒序
		// dataViewQuery.setOrderByClause("id desc");
		dataViewQuery.setPageSize(5);
		StringBuilder params = new StringBuilder();
		Criteria createCriterid = dataViewQuery.createCriteria();
		if (null != optionName) {
			createCriterid.andOptionNameLike("%" + optionName + "%");
			params.append("optionName=").append(optionName);
		}
		if (null != isshow) {
			createCriterid.andIsshowEqualTo(isshow);
			params.append("&isshow=").append(isshow);
		} else {
			createCriterid.andIsshowEqualTo(true);
			params.append("&isshow=").append(true);
		}
		Pagination pagination = new Pagination(dataViewQuery.getPageNo(), dataViewQuery.getPageSize(),
				dataViewDao.countByExample(dataViewQuery), dataViewDao.selectByExample(dataViewQuery));
		String url = "/type/list.do";
		pagination.pageView(url, params.toString());
		return pagination;
	}

	// 查询所有可用类别
	@Override
	public List<DataView> selectAllDataByIsShow(Boolean isshow) {
		DataViewQuery dataViewQuery = new DataViewQuery();
		Criteria createCriterid = dataViewQuery.createCriteria();
		createCriterid.andIsshowEqualTo(isshow);
		return dataViewDao.selectByExample(dataViewQuery);
	}

}
