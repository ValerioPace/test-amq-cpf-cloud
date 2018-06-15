package it.gov.mef.cloudify.jms.config;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;
import org.springframework.util.Assert;

public class NoiPAJmsTemplate extends JmsTemplate {

	private boolean nonTransactionalForceSessionFlag = true;
		
	public boolean isNonTransactionalForceSessionFlag() {
		return nonTransactionalForceSessionFlag;
	}

	public void setNonTransactionalForceSessionFlag(boolean nonTransactionalForceSessionFlag) {
		this.nonTransactionalForceSessionFlag = nonTransactionalForceSessionFlag;
	}

	protected Message doReceive(Session session, MessageConsumer consumer) throws JMSException {
		try {
			// Use transaction timeout (if available).
			long timeout = getReceiveTimeout();
			
			Message message = receiveFromConsumer(consumer, timeout);
			
			// Commit necessary - but avoid commit call within a JTA transaction.
			
			if (!nonTransactionalForceSessionFlag ) {
				
				if (session.getTransacted() && isSessionLocallyTransacted(session)) {
					// Transacted session created by this template -> commit.
					JmsUtils.commitIfNecessary(session);
				}
			}
			else if (isClientAcknowledge(session)) {
				// Manually acknowledge message, if any.
				if (message != null) {
					message.acknowledge();
				}
			}
			return message;
		}
		finally {
			JmsUtils.closeMessageConsumer(consumer);
		}
	}
	
	/**
	 * Send the given JMS message.
	 * @param session the JMS Session to operate on
	 * @param destination the JMS Destination to send to
	 * @param messageCreator callback to create a JMS Message
	 * @throws JMSException if thrown by JMS API methods
	 */
	protected void doSend(Session session, Destination destination, MessageCreator messageCreator)
			throws JMSException {

		Assert.notNull(messageCreator, "MessageCreator must not be null");
		MessageProducer producer = createProducer(session, destination);
		try {
			Message message = messageCreator.createMessage(session);
			if (logger.isDebugEnabled()) {
				logger.debug("Sending created message: " + message);
			}
			doSend(producer, message);
			// Check commit - avoid commit call within a JTA transaction.
			if (!nonTransactionalForceSessionFlag && session.getTransacted() 
					&& isSessionLocallyTransacted(session)) {
				// Transacted session created by this template -> commit.
				JmsUtils.commitIfNecessary(session);
			}
		}
		finally {
			JmsUtils.closeMessageProducer(producer);
		}
	}
	
}
