package cn.itcast.core.service.product;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
//import java.util.Set;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.BrandQuery;
import cn.itcast.core.bean.product.BrandQuery.Criteria;
import cn.itcast.core.dao.product.BrandDao;
import redis.clients.jedis.Jedis;

@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService {
	@Resource
	private BrandDao brandDao;
	// 注入jedis
	@Resource
	private Jedis jedis;

	// 查询分页对象,传入页面中的参数：name、isDisplay、pageNo
	public Pagination selectPaginationByQuery(String name, Boolean isDisplay, Integer pageNo) {
		BrandQuery brandQuery = new BrandQuery();
		// 设置当前页，调用pagination的cpn方法来处理当pageNo传入的null时将pageNo设置成1
		brandQuery.setPageNo(Pagination.cpn(pageNo));
		// 设置排序为倒序
		brandQuery.setOrderByClause("id desc");
		// 设置每页显示数量5条，不传则按照初始设置的10条来返回
		brandQuery.setPageSize(5);
		// 初始化一个StringBuilder用于拼接分页请求参数
		StringBuilder params = new StringBuilder();

		Criteria createCriteria = brandQuery.createCriteria();
		// 查询条件,为了列表的是否可用列看起来比较整齐，设置isDisplay默认值为1,
		// 因需要准备分页展示，这里需要使用上面初始化的StringBuilder进行拼接
		if (null != name) {
			createCriteria.andNameLike("%" + name + "%");
			params.append("name=").append(name);
		}
		if (null != isDisplay) {
			createCriteria.andIsDisplayEqualTo(isDisplay);
			// 由于前面已经有一个拼接的条件，所以这里需要加上&符号
			params.append("&isDisplay=").append(isDisplay);
		} else {
			createCriteria.andIsDisplayEqualTo(true);
			params.append("&isDisplay=").append(true);
		}
		// 查询结果集,传入页码、每页显示的数量、总条数、结果集范围
		Pagination pagination = new Pagination(brandQuery.getPageNo(), brandQuery.getPageSize(),
				brandDao.countByExample(brandQuery), brandDao.selectByExample(brandQuery));
		// 设置结果集
		// pagination.setList(brandDao.selectByExample(brandQuery));
		// 分页展示,声明请求链接
		String url = "/brand/list.do";
		pagination.pageView(url, params.toString());
		// 返回结果集
		return pagination;
	}

	// 通过id查询品牌信息
	@Override
	public Brand selectBrandById(Long id) {

		return brandDao.selectByPrimaryKey(id);
	}

	// 修改
	@Override
	public void updateBrandById(Brand brand) {
		// 修改redis,此句放在前面一旦抛异常时会结束运行，下面的更新数据库也不执行，只有
		// 不抛异常时才会执行数据库操作
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
		brandDao.updateByPrimaryKeySelective(brand);
	}

	// 新增
	@Override
	public void addNewBrand(Brand brand) {
		// 使用redis生成id
		Long id = jedis.incr("bno");
		brand.setId(id);
		jedis.hset("brand", String.valueOf(brand.getId()), brand.getName());
		brandDao.insertSelective(brand);
	}

	// 单删
	@Override
	public void deleteById(Long id) {
		//删除redis中的记录
		jedis.hdel("brand", String.valueOf(id));
		brandDao.deleteByPrimaryKey(id);
	}

	// 批量删除
	@Override
	public void deletes(Long[] ids) {
		//删除redis中的记录
		for (Long id : ids) {
			jedis.hdel("brand", String.valueOf(id));
		}
		brandDao.deletes(ids);
	}

	@Override
	public List<Brand> selectBrandListByQuery(Boolean isDisplay) {
		BrandQuery brandQuery = new BrandQuery();
		Criteria createCriteria = brandQuery.createCriteria();
		createCriteria.andIsDisplayEqualTo(isDisplay);
		return brandDao.selectByExample(brandQuery);
	}

	// 从redis中查询
	public List<Brand> selectBrandListFromRedis() {
		List<Brand> brands = new ArrayList<Brand>();
		// 从redis查
		Map<String, String> hgetAll = jedis.hgetAll("brand");
		// 遍历前需要entrySet
//		Set<Entry<String, String>> entrySet = hgetAll.entrySet();
		//以下遍历方式效率高
		Iterator<Entry<String, String>> iterator = hgetAll.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = iterator.next();
			Brand brand = new Brand();
			brand.setId(Long.parseLong(entry.getKey()));
			brand.setName(entry.getValue());
			brands.add(brand);
		}
//		for (Entry<String, String> entry : entrySet) {
//			Brand brand = new Brand();
//			brand.setId(Long.parseLong(entry.getKey()));
//			brand.setName(entry.getValue());
//			brands.add(brand);
//		}
		return brands;
	}

}
