package it.gov.mef.cloudify.jms.config;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.JmsUtils;

public class NoiPAJmsTemplate extends JmsTemplate {

	private boolean nonTransactionalForceSessionFlag = false;
		
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
}
