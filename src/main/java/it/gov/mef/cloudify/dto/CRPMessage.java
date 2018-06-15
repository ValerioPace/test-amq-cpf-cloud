package it.gov.mef.cloudify.dto;

import java.io.Serializable;

public class CRPMessage extends BaseMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4159213611537747637L;
	private String name;
	private String talkTo;
	private Long sequenceNumber;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTalkTo() {
		return talkTo;
	}
	public void setTalkTo(String talkTo) {
		this.talkTo = talkTo;
	}
	public Long getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result + ((talkTo == null) ? 0 : talkTo.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CRPMessage other = (CRPMessage) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (talkTo == null) {
			if (other.talkTo != null)
				return false;
		} else if (!talkTo.equals(other.talkTo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CRPMessage [name=" + name + ", talkTo=" + talkTo + ", sequenceNumber=" + sequenceNumber + "]";
	}
	
}
