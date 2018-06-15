package it.gov.mef.cloudify.jms.consumer;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class NoiPAJmsConsumer {

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	Queue queue;
	
	public void receiveMessage() throws JMSException {
		
		Message message = jmsTemplate.receive(queue);
		
		String jmsMessageID = message.getJMSMessageID();
		TextMessage textMessage = ((TextMessage) message);
		
		String messageJson = textMessage.getText();
		Map map = new Gson().fromJson(messageJson, Map.class);
		
		String response  = "Hello " + map.get("name") + ", I'm " + map.get("talkTo");
		Long messageSeqNumber = Long.parseLong((String) map.get("seqNumber"));
		
		System.out.println("Response from JMS Queue consumer: " + response);
		System.out.println("Message ["+ jmsMessageID +"] #" + messageSeqNumber + " processed");
	}
	
}
