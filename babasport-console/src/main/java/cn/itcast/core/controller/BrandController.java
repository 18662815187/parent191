package cn.itcast.core.controller;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.service.product.BrandService;

/**
 * 品牌管理 列表 删除 修改 添加 删除（单）
 * 
 * @author john
 *
 */


@Controller
@RequestMapping("/brand")
public class BrandController {

	@Resource
	private BrandService brandService;

	// 查询
	@RequestMapping("/list")
	public String list(String name, Boolean isDisplay, Integer pageNo, Model model) {
		Pagination pagination = brandService.selectPaginationByQuery(name, isDisplay, pageNo);
		// 回显结果集
		model.addAttribute("pagination", pagination);
		// 回显name
		model.addAttribute("name", name);
		// 判断传入的isDisplay是否为空，如果是空的则设置成true，true代表“是”
		if (null != isDisplay) {
			model.addAttribute("isDisplay", isDisplay);
		} else {
			model.addAttribute("isDisplay", true);
		}

		return "/brand/list";
	}

	// 跳转到编辑页面，通过id查询所有详情
	@RequestMapping("/toEdit.do")
	public String toEdit(Long id, Model model, String name, Boolean isDisplay, Integer pageNo) {
		Brand brand = brandService.selectBrandById(id);
		model.addAttribute("brand", brand);
		model.addAttribute("name1", name);
		model.addAttribute("isDisplay1", isDisplay);
		model.addAttribute("pageNo1", pageNo);
		return "/brand/edit";
	}

	// 提交修改,修改成功后通过传进来的之前列表页当前页数和查询条件跳转回去
	@RequestMapping("/edit.do")
	public String edit(Brand brand, String name1, Boolean isDisplay1, Integer pageNo1, Model model) {
		System.out.println(brand.getName());
		brandService.updateBrandById(brand);
		model.addAttribute("name", name1);
		model.addAttribute("isDisplay", isDisplay1);
		model.addAttribute("pageNo", pageNo1);
		return "redirect:list.do";
	}

	// 跳转到添加页面
	@RequestMapping("/toadd")
	public String toAdd(Model model) {
		return "/brand/add";
	}

	// 添加更新两用控制器
	@RequestMapping("/addOrEdit.do")
	public String addOrEdit(Brand brand, String name1, Boolean isDisplay1, Integer pageNo1, Model model) {
		if (brand.getId() > 0L && brand.getId()!=null) {
			brandService.updateBrandById(brand);
		} else {
			brandService.addNewBrand(brand);
		}
		model.addAttribute("name", name1);
		model.addAttribute("isDisplay", isDisplay1);
		model.addAttribute("pageNo", pageNo1);
		return "redirect:list.do";
	}

	// 添加
	@RequestMapping("/add.do")
	public String Add(Brand brand) {
		brandService.addNewBrand(brand);
		return "redirect:list.do";
	}

	// 删除
	@RequestMapping("/delete")
	public String delete(Long id) {
		brandService.deleteById(id);
		return "redirect:list.do";
	}

	// 批量删除,使用forward方式跳转会自动携带变量,String name,Integer isDisplay,Integer pageNo
	@RequestMapping("/deletes.do")
	public String deletes(Long[] ids) {
		brandService.deletes(ids);
		return "forward:list.do";
	}
}
