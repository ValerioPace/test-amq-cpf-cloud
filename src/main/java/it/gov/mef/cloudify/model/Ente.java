package it.gov.mef.cloudify.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "d_enti")
public class Ente {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="SEQU_ENTE")
    private Long sequEnte;
	
	@Column(name="NOME", nullable = false)
	private String name;
	
	@Column(name="DATA_INIZIO_VAL", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name="INDIRIZZO")
	private String address;
	
	public Long getSequEnte() {
		return sequEnte;
	}

	public void setSequEnte(Long sequEnte) {
		this.sequEnte = sequEnte;
	}

	public String getName() {
		return name;
	}

    public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((sequEnte == null) ? 0 : sequEnte.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		Ente other = (Ente) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sequEnte == null) {
			if (other.sequEnte != null)
				return false;
		} else if (!sequEnte.equals(other.sequEnte))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

}
