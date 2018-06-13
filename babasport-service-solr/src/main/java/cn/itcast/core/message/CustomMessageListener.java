package cn.itcast.core.message;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import cn.itcast.core.service.SearchService;

/**
 * 消息处理类
 * 
 * @author john
 *
 */

public class CustomMessageListener implements MessageListener {

	@Resource
	private SearchService searchService;
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		ActiveMQTextMessage am = (ActiveMQTextMessage) message;
		try {
			System.out.println("ActiveMq中的消息是--solr：" + am.getText());
			//取出消息中的id传给solr服务实现类
			searchService.insertProductToSolr(Long.parseLong(am.getText()));
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
