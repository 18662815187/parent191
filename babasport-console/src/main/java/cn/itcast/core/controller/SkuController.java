package cn.itcast.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.service.product.SkuService;

/**
 * 库存管理 去库存 修改 保存
 * 
 * @author john
 *
 */

@Controller
@RequestMapping("/sku")
public class SkuController {
	@Resource
	private SkuService skuService;

	// 去库存页面
	@RequestMapping("/list.do")
	public String list(Long productId, Model model) {
		List<Sku> skus = skuService.selectSkuListByProductId(productId);
		model.addAttribute("skus", skus);
		return "sku/list";
	}
	
	//异步修改保存
	@RequestMapping("/addSku.do")
	@ResponseBody
	public Map<String, String> addSku(Sku sku){
		Map<String, String> map = new HashMap<String, String>();
		skuService.updateSkuById(sku);
		map.put("message", "保存成功！");
		return map;
	}
}
