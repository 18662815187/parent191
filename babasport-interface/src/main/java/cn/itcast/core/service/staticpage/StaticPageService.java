package cn.itcast.core.service.staticpage;

import java.util.Map;

public interface StaticPageService {
	// 静态化 商品 ActiveMQ
	public void productStaticPage(Map<String, Object> root, String id);
}
