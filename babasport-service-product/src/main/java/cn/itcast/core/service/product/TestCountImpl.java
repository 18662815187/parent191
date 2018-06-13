package cn.itcast.core.service.product;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.core.dao.product.DataViewDao;

@Service("TcService")
public class TestCountImpl implements TestCountDataService {
	@Resource
	private DataViewDao dataViewDao;

	@Override
	public int countData() {

		return dataViewDao.countByExample(null);
	}

}
