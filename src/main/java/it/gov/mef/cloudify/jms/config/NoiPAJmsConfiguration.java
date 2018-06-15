package it.gov.mef.cloudify.jms.config;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
@EnableJms
@ComponentScan(basePackages = "it.gov.mef.cloudify.jms")
public class NoiPAJmsConfiguration {

	@Value("${spring.jms.connectionfactory.jndi-name}")
	private String connectionFactoryJndiName;
	
	@Value("${spring.jms.queue.jndi-name}")
	private String queueJndiName;
	
//	@Autowired
//	@Qualifier("connectionFactory")
//	private ConnectionFactory connectionFactory;
	
	@Bean
	@Qualifier("connectionFactory")
	//@Lazy
	public JndiObjectFactoryBean connectionFactoryJndi() {
	
		JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
		jndiFactory.setJndiName(connectionFactoryJndiName);
		jndiFactory.setLookupOnStartup(false);
		jndiFactory.setCache(true);
		jndiFactory.setProxyInterface(ConnectionFactory.class);
		
		return jndiFactory;
	}
	
	@Bean
	@Qualifier("queue")
	//@Lazy
	public JndiObjectFactoryBean queueJndi() {
	
		JndiObjectFactoryBean jndiFactory = new JndiObjectFactoryBean();
		jndiFactory.setJndiName(queueJndiName);
		jndiFactory.setLookupOnStartup(false);
		jndiFactory.setCache(true);
		jndiFactory.setProxyInterface(Queue.class);
		
		return jndiFactory;
	}
	
	public ConnectionFactory connectionFactory() {
		return ConnectionFactory.class.cast(connectionFactoryJndi().getObject());
	}
	
	public Queue queue() {
		return Queue.class.cast(queueJndi().getObject());
	}
	
	/*
	 * We don't want an active mq artemis tcp connection directly because it violates the JMS 1.2 specification of a JCE connector
	 * in J2EE integrated environments;  this Active MQ Artemis server could be reachable only through Jndi inside Java EE container (Jboss EAP)
	@Bean
	public ConnectionFactory connectionFactory() {
		
		String BROKER_URL = "tcp://localhost:61616"; 
		String BROKER_USERNAME = "admin"; 
		String BROKER_PASSWORD = "admin";
		
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	    connectionFactory.setBrokerURL(BROKER_URL);
	    connectionFactory.setPassword(BROKER_USERNAME);
	    connectionFactory.setUserName(BROKER_PASSWORD);
	    return connectionFactory;
	}
	*/
	
	@Bean
	@Lazy
	public JmsTemplate jmsTemplate(){
	    
		//JmsTemplate template = new NoiPAJmsTemplate();
		JmsTemplate template = new JmsTemplate();
	    
	    template.setConnectionFactory(connectionFactory());
	    template.setReceiveTimeout(5000L);
	    return template;
	}

	@Bean
	@Lazy
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(connectionFactory());
	    factory.setConcurrency("1-1");
	    return factory;
	}

}
