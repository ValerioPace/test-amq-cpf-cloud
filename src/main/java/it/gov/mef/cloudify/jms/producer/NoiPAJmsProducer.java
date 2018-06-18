package it.gov.mef.cloudify.jms.producer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class NoiPAJmsProducer {

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	Queue queue;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static AtomicLong sequencer = new AtomicLong(0L);

	public void sendMessage(final String message) throws JMSException {
		
		logger.info("Sending message " + message + "to queue - " + queue.getQueueName());
		
		jmsTemplate.send(queue, new MessageCreator() {

			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(message);
				
				logger.info("inside an active JMS Session create text message: " + textMessage.getText());
				
				return textMessage;
			}
		});
	}
	
	public String produceSimpleMessageFromMap() {
				
		long seq = sequencer.incrementAndGet();
		
		Map<String,String> origMap = new LinkedHashMap<String,String>();
		origMap.put("name", "Valerio");
		origMap.put("talkTo", "Roberta");
		origMap.put("seqNumber", String.valueOf(seq));
		
		return new Gson().toJson(origMap, Map.class);
	}
}
