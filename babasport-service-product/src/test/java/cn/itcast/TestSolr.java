package cn.itcast;

import javax.annotation.Resource;

import org.apache.solr.client.solrj.SolrServer;
//import org.apache.solr.client.solrj.response.UpdateResponse;
//import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class TestSolr {
	@Resource
	private SolrServer solrServer;

	@Test
	public void testSorjSpring() throws Exception {
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", 5);
		doc.setField("name", "测试5");
		solrServer.add(doc);
		solrServer.commit();
	}

//	@Test
//	public void testSolrJ() throws Exception {
//		String baseUrl = "http://192.168.200.128:8080/solr";
//		SolrServer solrServer = new HttpSolrServer(baseUrl);
//		SolrInputDocument doc = new SolrInputDocument();
//		doc.setField("id", 3);
//		doc.setField("name", "测试");
//		solrServer.add(doc);
//		solrServer.commit();
//	}
}
