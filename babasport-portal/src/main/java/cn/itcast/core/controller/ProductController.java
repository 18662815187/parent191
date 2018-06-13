package cn.itcast.core.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.CmsService;
import cn.itcast.core.service.SearchService;
import cn.itcast.core.service.product.BrandService;

/**
 * 前台商品
 * 
 * @author john
 *
 */
@Controller
public class ProductController {
	@Resource
	private SearchService searchService;
	@Resource
	private BrandService brandService;
	@Resource
	private CmsService cmsService;

	// 首页
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	// 搜索
	@RequestMapping("/search")
	public String search(Integer pageNo, String keyword, Long brandId, String price, Model model) throws Exception {
		// 查询品牌结果集
		List<Brand> brands = brandService.selectBrandListFromRedis();
		model.addAttribute("brandId", brandId);
		model.addAttribute("price", price);
		model.addAttribute("brands", brands);

		// 已选条件Map,条件的名称和值是K,V对，所以需要map组装
		Map<String, String> map = new HashMap<String, String>();
		// 品牌
		if (null != brandId) {
			for (Brand brand : brands) {
				if (brandId == brand.getId()) {
					map.put("品牌", brand.getName());
					break;
				}
			}
		}
		// 价格 0-99 1600,最大价格没有区间，例如1600以上，所以判断传过来的价格中是否带有区间分隔符“-”，如果没有则连接“以上”
		if (null != price) {
			if (price.contains("-")) {
				map.put("价格", price);
			} else {
				map.put("价格", price + "以上");
			}
		}

		model.addAttribute("map", map);
		if (keyword == null || keyword == "") {
			Pagination pagination = searchService.selectPaginationByQuery(pageNo, "*", brandId, price);
			model.addAttribute("pagination", pagination);
			model.addAttribute("keyword", "");
		} else {
			Pagination pagination = searchService.selectPaginationByQuery(pageNo, keyword, brandId, price);
			model.addAttribute("pagination", pagination);
			model.addAttribute("keyword", keyword);
		}
		return "search";
	}

	// 去商品详细页面
	@RequestMapping("/product/detail")
	public String detail(Long id, Model model) {
		// 商品
		Product product = cmsService.selectProductById(id);
		// sku
		List<Sku> skus = cmsService.selectSkuListByProductId(id);
		
		//每个颜色都有不同尺码，在选择颜色时会出现重复颜色，
		//遍历一次 去除重复，color对象需要生成hashcode方法
		Set<Color> colors = new HashSet<>();
		for (Sku sku : skus) {
			colors.add(sku.getColor());
		}
		model.addAttribute("product", product);
		model.addAttribute("skus", skus);
		model.addAttribute("colors", colors);
		return "product";
	}
}
