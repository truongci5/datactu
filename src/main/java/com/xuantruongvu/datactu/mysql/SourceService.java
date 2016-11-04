package com.xuantruongvu.datactu.mysql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class SourceService {
	@SuppressWarnings("unchecked")
	public static List<Source> findAll() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("datactu");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Query query = em
				.createQuery(
						"SELECT s FROM Source s WHERE s.state = :state AND s.isDeleted = :isDeleted")
				.setParameter("state", 1).setParameter("isDeleted", 0);
		List<Source> sources = query.getResultList();
		em.getTransaction().commit();
		em.close();
		factory.close();
		return sources;
	}
}
