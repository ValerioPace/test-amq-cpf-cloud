package it.gov.mef.cloudify.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gov.mef.cloudify.model.Ente;

@Service
@Transactional
public class EnteDaoImpl implements EnteDao {

	@Autowired
	private EntityManager em;
	
	@Override
	public List<Ente> findAllEnti() {
		
		List<Ente> enti = em.createQuery("select e from Ente e").getResultList();
		
		return enti;
	}

}
