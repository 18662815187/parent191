package cn.itcast.core.service.staticpage;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 静态化
 * @author lx
 *
 */
public class StaticPageServiceImpl implements StaticPageService,ServletContextAware{

	//声明 
	//注入
	private Configuration conf;
	public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
		this.conf = freeMarkerConfigurer.getConfiguration();
	}

	//静态化 商品  ActiveMQ
	public void productStaticPage(Map<String,Object> root,String id){
		//输出的路径  全路径
		String path = getPath("/html/product/" + id + ".html");
		File f = new File(path);
		File parentFile = f.getParentFile();
		if(!parentFile.exists()){
			parentFile.mkdirs();
		}
		Writer out = null;
		try {
			//读文件  UTF-8
			Template template = conf.getTemplate("product.html");
			//输出  UTF-8
			out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			//处理
			template.process(root, out);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(null != out){
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	//获取路径
	public String getPath(String name){
		return servletContext.getRealPath(name);
	}
	//声明
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
	}
	
	//静态化订单
}
