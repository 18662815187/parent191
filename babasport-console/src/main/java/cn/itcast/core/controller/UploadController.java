package cn.itcast.core.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;


import cn.itcast.common.web.Constants;
import cn.itcast.core.service.product.UploadService;

/**
 * 上传图片
 * 
 * 
 * @author john
 * @RestController作用相当于@controller加上@ResponseBody
 * @CrossOrigin加上此注解支持跨域,spring 4.2以上才支持
 */
//@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/upload")
public class UploadController {
	@Resource
	private UploadService uploadService;

	//第一种写法，void方式通过JSONObject实现返回json
	// 上传图片,ajax异步上传使用void类型,使用@RequestParam(value="pic",required=false)绑定pic字段，
	// 控制是否允许为空，默认是不能为空的，这里设为false允许为空，可以简化成@RequestParam(required=false)
	@RequestMapping("/uploadPic.do")
	public void uploadPic(@RequestParam(value = "pic", required = false) MultipartFile pic,
			HttpServletResponse response) throws IOException {
		// 控制台输出原文件名字
		System.out.println(pic.getOriginalFilename());
		// 图片服务器返回路径
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		// 拼接全路径
		String url = Constants.IMG_URL + path;
		System.out.println(url);
		// 实例化json对象
		JSONObject jo = new JSONObject();
		jo.put("url", url);
		// 解决跨域调取接口无权限的问题
//		response.setCharacterEncoding("UTF-8");
//
//        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
//        response.setHeader("Pragma", "no-cache"); // HTTP 1.0

        /**
         * for ajax-cross-domain request TODO get the ip address from
         * configration(ajax-cross-domain.properties)
         */
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Origin", "*");
//
//        response.setDateHeader("Expires", 0); // Proxies.
		// 设置返回值类型为json
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(jo.toString());
	

	}

	// 图片上传第二种控制器，以下是使用@ResponseBody注解实现返回json
	@RequestMapping("/uploadPic1.do")
	@ResponseBody
	public Object uploadPic1(@RequestParam(value = "pic", required = false) MultipartFile pic,
			HttpServletResponse response) throws IOException {
		// 控制台输出原文件名字
		System.out.println(pic.getOriginalFilename());
		// 图片服务器返回路径
		String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
		// 拼接全路径
		String url = Constants.IMG_URL + path;
		System.out.println(url);
		// 实例化json对象
		Map<String, String> map = new HashMap<>();
		map.put("url", url);
		// 解决跨域调取接口无权限的问题
//		response.setHeader("Access-Control-Allow-Origin", "*");
//		return new JSONPObject(callback, map);
		return map;
	}

	// 批量异步上传图片
	@RequestMapping("/uploadPics.do")
	@ResponseBody
	public List<String> uploadPics(@RequestParam(required = false) MultipartFile[] pics) throws IOException {
		// 声明一个list对象用于组装多个图片的路径
		List<String> urls = new ArrayList<String>();
		if (pics != null) {
			for (MultipartFile pic : pics) {
				// 控制台输出原文件名字
				System.out.println(pic.getOriginalFilename());
				// 图片服务器返回路径
				String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
				// 拼接全路径
				String url = Constants.IMG_URL + path;
				System.out.println(url);
				// 组装多张图片的路径
				urls.add(url);
			}
			return urls;
		}
		urls.add("图片上传的值为空");
		return urls;

	}
	
	
	//无敌图片上传，单图和批量都可以
	// 富文本接收器上传图片，可接受不清楚名称的图片
	@RequestMapping("/uploadFck.do")
	@ResponseBody
	public Object uploadFck(HttpServletRequest request) throws IOException {
		// 无敌接收图片
		// 强转spring提供MultpartRequest,此处只接收图片
		MultipartRequest mr = (MultipartRequest) request;
		Map<String, MultipartFile> fileMap = mr.getFileMap();
		Set<Entry<String, MultipartFile>> entrySet = fileMap.entrySet();
		Map<String, Object> map = new HashMap<>();
		for (Entry<String, MultipartFile> entry : entrySet) {
			MultipartFile pic = entry.getValue();
			// 控制台输出原文件名字
			System.out.println(pic.getOriginalFilename());
			// 图片服务器返回路径
			String path = uploadService.uploadPic(pic.getBytes(), pic.getOriginalFilename(), pic.getSize());
			// 拼接全路径
			String url = Constants.IMG_URL + path;
			System.out.println(url);
//			response.setHeader("Access-Control-Allow-Origin", "*");
//	        response.addHeader("Access-Control-Allow-Origin", "*");
			// 实例化json对象
			map.put("url", url);
			map.put("error", 0);
			// JSONObject jo = new JSONObject();
			// jo.put("url", url);
			// jo.put("error", 0);
			// response.setContentType("application/json;charset=UTF-8");
			// response.getWriter().write(jo.toString());
		}
		return map;
	}
	@RequestMapping(value = "/upload222", method = RequestMethod.POST)
	@ResponseBody
	private Object fildUpload(@RequestParam(value = "file", required = false) MultipartFile[] file,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		// 基本表单
		// System.out.println(users.toString());

		// 获得物理路径webapp所在路径
		String pathRoot = request.getSession().getServletContext().getRealPath("");
		String path = "";
		List<String> listImagePath = new ArrayList<String>();
		for (MultipartFile mf : file) {
			if (!mf.isEmpty()) {
				// 生成uuid作为文件名称
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				// 获得文件类型（可以判断如果不是图片，禁止上传）
				String contentType = mf.getContentType();
				// 获得文件后缀名称
				String imageName = contentType.substring(contentType.indexOf("/") + 1);
				path = "/static/images/" + uuid + "." + imageName;
				mf.transferTo(new File(pathRoot + path));
				listImagePath.add(path);
				
				System.out.println(path);
			}
		}
		map.put("imagesPathList", path);
//		request.setAttribute("imagesPathList", listImagePath);
		
		return map;
	}

}
