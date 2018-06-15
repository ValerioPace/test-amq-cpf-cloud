package it.gov.mef.cloudify;

import javax.jms.JMSException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.gov.mef.cloudify.dto.CRPMessage;
import it.gov.mef.cloudify.jms.consumer.NoiPAJmsConsumer;
import it.gov.mef.cloudify.jms.producer.NoiPAJmsProducer;

@RestController
public class ServiceController {
	
	@Autowired
	private NoiPAJmsProducer jmsProducer;
	
	@Autowired
	private NoiPAJmsConsumer jmsConsumer;
		
	@RequestMapping("/")
	public String hello() {
		return "L'inverno è arrivato, è in coda..";
	}
	
	@RequestMapping(value = "/sendMessage/queue", produces = MediaType.APPLICATION_JSON_VALUE)	
	public ResponseEntity<String> sendMessage(@RequestParam(name="", required = false) String name,
			@RequestParam(name="talkTo", required = false) String talkTo) {
		
		String messageJson = null;
		
		if(StringUtils.isEmpty(name)) {
			messageJson = jmsProducer.produceSimpleMessageFromMap();
		}
		
		try {
			jmsProducer.sendMessage(messageJson);
		} catch (JMSException e) {
			return new ResponseEntity<String>("{\"status\": \"failure\"}", HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>("{\"status\": \"ok\"}", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/retrieveMessage/queue", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CRPMessage> retrieveMessage(){
		
		CRPMessage crpMessage;
		try {
			crpMessage = jmsConsumer.receiveMessage();
		} catch (JMSException e) {
			CRPMessage errorCrpMessage = new CRPMessage();
			errorCrpMessage.setStatus("failure");
			// TODO Auto-generated catch block
			return new ResponseEntity<CRPMessage>(errorCrpMessage, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<CRPMessage>(crpMessage, HttpStatus.OK);
	}
	
}
