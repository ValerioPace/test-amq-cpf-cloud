package it.gov.mef.cloudify.jms.consumer;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import it.gov.mef.cloudify.dto.CRPMessage;

@Component
public class NoiPAJmsConsumer {

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	Queue queue;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public CRPMessage receiveMessage() throws JMSException {
		
		Message message = jmsTemplate.receive(queue);
		
		CRPMessage result = new CRPMessage();
		
		if (message != null) {
			String jmsMessageID = message.getJMSMessageID();
			
			logger.info("receiving message #ID=" + jmsMessageID + " from queue: " + queue.getQueueName());
			
			TextMessage textMessage = ((TextMessage) message);
			String messageJson = textMessage.getText();
			
			if(logger.isInfoEnabled()) {
				logger.info("message from queue (JSON): " + messageJson + "\nprocessing....");
			}
			
			Map map = new Gson().fromJson(messageJson, Map.class);
			String response = "Hello " + map.get("name") + ", I'm " + map.get("talkTo");
			Long messageSeqNumber = Long.parseLong((String) map.get("seqNumber"));
			System.out.println("Response from JMS Queue consumer: " + response);
			System.out.println("Message [" + jmsMessageID + "] #" + messageSeqNumber + " processed");
			
			result.setName((String) map.get("name"));
			result.setTalkTo((String) map.get("talkTo"));
			result.setSequenceNumber(Long.parseLong((String) map.get("seqNumber")));
			result.setStatus("ok");
		}
		else {
			logger.info("receiving no message from queue: " + queue.getQueueName() + " probabily because timeout when accessing no item");
			result.setStatus("failure (timeout?)");
		}
		
		return result;
	}
	
}
