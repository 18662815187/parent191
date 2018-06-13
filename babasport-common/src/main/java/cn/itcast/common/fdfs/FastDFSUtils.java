package cn.itcast.common.fdfs;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.core.io.ClassPathResource;

/**
 * 上传图片到FastFDS
 * 
 * @author john
 *
 */
public class FastDFSUtils {

	public static String uploadPic(byte[] pic, String name, long size) {
		String path = null;
		//文件读取
		ClassPathResource resource = new ClassPathResource("fdfs_client.conf");
		try {
			// ClientGloble 读取配置文件
			ClientGlobal.init(resource.getClassLoader().getResource("fdfs_client.conf").getPath());
			// 主机老大客户端，用于分配存储服务器
			TrackerClient trackerClient = new TrackerClient();
			TrackerServer trackerServer = trackerClient.getConnection();
			// 从机小弟，即存储服务器
			StorageServer storageServer = null;
			StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
			// 获取扩展名的方法，来自apache.commons.io.FilenameUtils
			String ext = FilenameUtils.getExtension(name);
			// 文件的扩展属性信息,此处只给三个信息
			NameValuePair[] meta_list = new NameValuePair[3];
			meta_list[0] = new NameValuePair("fileName", name);
			meta_list[1] = new NameValuePair("fileExt", ext);
			meta_list[2] = new NameValuePair("fileSize", String.valueOf(size));
			// 开始上传,返回的路径只有除去域名的链接，如：/group1/M00/00/01/wKjIgFWOYc6APpjAAAD-qk29i78248.jpg
			path = storageClient1.upload_file1(pic, ext, meta_list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;
	}
}
