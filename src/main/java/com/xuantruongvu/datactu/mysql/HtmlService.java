package com.xuantruongvu.datactu.mysql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class HtmlService {
	public static void insert(Html html) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("datactu");
		EntityManager em = factory.createEntityManager();
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			em.persist(html);
			et.commit();
		} catch (Exception e) {
			if(et != null && et.isActive()) et.rollback();
		} finally {
			em.close();
			factory.close();
		}
	}
}
