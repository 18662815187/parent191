package cn.itcast.core.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.service.product.TestCountDataService;


@Controller
public class TestController {
	@Resource
	private TestCountDataService testCountDataService;

	@RequestMapping("/test")
	@ResponseBody
	public Object Test() {
		Map<String, Object> map = new HashMap<>();
		int count = testCountDataService.countData();
		map.put("count", count);
		return map;
	}
}
