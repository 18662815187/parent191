package cn.itcast.core.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.itcast.common.page.Pagination;
import cn.itcast.core.bean.product.Data;
import cn.itcast.core.service.product.DataService;
import cn.itcast.core.service.product.DataViewService;

/**
 * 数据字典 增 删 改 查 列表，分页
 * 
 * @author john
 *
 *
 */
@Controller
@RequestMapping("/type")
public class DataViewController {

	@Resource
	private DataViewService dataViewService;
	@Resource
	private DataService dataService;

	// list列表
	@RequestMapping("/list.do")
	public String list(String optionName, Boolean isshow, Integer pageNo, Model model) {
		Pagination pagination = dataViewService.selectPaginationByQuery(optionName, isshow, pageNo);
		// 回显数据集
		model.addAttribute("pagination", pagination);
		model.addAttribute("optionName", optionName);
		if (null != isshow) {
			model.addAttribute("isshow", isshow);
		} else {
			model.addAttribute("isshow", true);
		}
		model.addAttribute("flag", 1);
		return "/type/list";
	}

	// 跳转到添加页面
	@RequestMapping("/toAdd.do")
	public String toAdd(Model model, String optionName, Boolean isshow, Integer pageNo) {
		List<Data> datas = dataService.selectAllDataByIsShow(true);
		model.addAttribute("datas", datas);
		model.addAttribute("optionName1", optionName);
		model.addAttribute("isshow1", isshow);
		model.addAttribute("pageNo1", pageNo);
		System.out.println(optionName + isshow + pageNo);
		return "/type/add";
	}

	// 新增or修改
	@RequestMapping("/saveOrUpdate.do")

	public String saveOrUp(Data data, String optionName1, Boolean isshow1, Integer pageNo1, Model model) {
		if (data.getId() != null && data.getId() > 0L) {
			dataService.UpdateById(data);
		} else {
			if(data.getOptionLevel()==null){
				data.setOptionLevel(0L);
				dataService.addNewData(data);
			}else {
				dataService.addNewData(data);
			}
		}
		model.addAttribute("optionName", optionName1);
		model.addAttribute("isshow", isshow1);
		model.addAttribute("pageNo", pageNo1);
		System.out.println(optionName1 + isshow1 + pageNo1);
		return "redirect:list.do";
	}

	// 跳转到编辑页面
	@RequestMapping("/toEdit.do")
	public String toEdit(Model model, Long id, String optionName, Boolean isshow, Integer pageNo) {
		List<Data> datas = dataService.selectAllDataByIsShow(true);
		Data data = dataService.selectDataById(id);
		model.addAttribute("data", data);
		model.addAttribute("datas", datas);
		model.addAttribute("optionName1", optionName);
		model.addAttribute("isshow1", isshow);
		model.addAttribute("pageNo1", pageNo);
		System.out.println(optionName + "----" + isshow + "------" + pageNo + "-------------" + id);
		return "/type/edit";
	}

	// 单删
	@RequestMapping("delete.do")
	public String deleteById(Long id) {
		dataService.deleteById(id);
		// model.addAttribute("optionName", optionName);
		// model.addAttribute("pageNo", pageNo);
		// model.addAttribute("isshow", isshow);
		return "forward:list.do";
	}

	// 批量删除
	@RequestMapping("deletes.do")
	public String deleteByIds(Long[] ids) {
		dataService.deletes(ids);
		return "forward:list.do";
	}

	// 根据option_level查询optionName
	@RequestMapping("queryByLevel.do")
	@ResponseBody
	public List<Data> queryByLevel(Long id) {
		List<Data> das = dataService.selectByLevel(id);
		return das;
	}

}
