package cn.itcast.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.bean.product.Color;
import cn.itcast.core.bean.product.Data;
import cn.itcast.core.bean.product.Product;
import cn.itcast.core.service.product.BrandService;
import cn.itcast.core.service.product.DataService;
import cn.itcast.core.service.product.ProductService;

/**
 * 商品管理 列表 添加 上架
 * 
 * @author john
 *
 *
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	@Resource
	private ProductService productService;
	@Resource
	private BrandService brandService;
	@Resource
	private DataService dataService;

	// 查询
	@RequestMapping("/list.do")
	public String list(Integer pageNo, String name, Long brandId, Boolean isShow, Model model) {
		// 品牌的结果集,返回可用的
		List<Brand> brands = brandService.selectBrandListByQuery(true);
		model.addAttribute("brands", brands);
		// 分页对象
		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
		// System.out.println(pagination.getList().get(0));
		pagination.getList();
		model.addAttribute("pagination", pagination);

		model.addAttribute("name", name);
		model.addAttribute("brandId", brandId);
		if (null != isShow) {
			model.addAttribute("isShow", isShow);
		} else {
			model.addAttribute("isShow", false);
		}
		return "product/list";
	}

	@RequestMapping("/list1.do")
	@ResponseBody
	public Object list1(Integer pageNo, String name, Long brandId, Boolean isShow) {
		Map<String, Object> map = new HashMap<>();
		// 品牌的结果集,返回可用的
		List<Brand> brands = brandService.selectBrandListByQuery(true);
		map.put("brands", brands);
		// 分页对象
		Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
		map.put("pagination", pagination);
		map.put("name", name);
		map.put("brandId", brandId);
		if (null != isShow) {
			map.put("isShow", isShow);
		} else {
			map.put("isShow", false);
		}
		return map;
	}

	// 去添加页面
	@RequestMapping("/toAdd.do")
	public String toAdd(Model model) {
		List<Brand> brands = brandService.selectBrandListByQuery(true);
		model.addAttribute("brands", brands);
		List<Data> datas = dataService.selectByExample();
		model.addAttribute("datas", datas);
		List<Color> colors = productService.selectColorList();
		model.addAttribute("colors", colors);
		return "product/add";
	}

	// 保存、更新
	@RequestMapping("/saveOrUpdate.do")
	public String saveOrUpate(Product product) {
		System.out.println(product.getOptionId());
		productService.insertProduct(product);
		return "redirect:list.do";
	}

	// 上架支持批量
	@RequestMapping("/isShow.do")
	public String isShow(Long[] ids) {
		productService.isShow(ids);
		return "forward:list.do";
	}

	// 下架支持批量
	@RequestMapping("/unshow.do")
	public String unShow(Long[] ids) {
		productService.unShow(ids);
		return "forward:list.do";
	}
}
