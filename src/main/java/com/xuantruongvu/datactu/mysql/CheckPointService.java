package com.xuantruongvu.datactu.mysql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class CheckPointService {

	public static List<CheckPoint> findAll() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("datactu");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Query query = em.createQuery("SELECT s FROM CheckPoint s WHERE 1");
		@SuppressWarnings("unchecked")
		List<CheckPoint> checkpoints = query.getResultList();
		em.getTransaction().commit();
		em.close();
		factory.close();
		return checkpoints;
	}

	public static boolean isScheduled(int hour) {
		boolean isScheduled = false;
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("datactu");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Query query = em.createQuery(
				"SELECT s FROM CheckPoint s WHERE s.checkpoint = :hour")
				.setParameter("hour", hour);
		@SuppressWarnings("unchecked")
		List<CheckPoint> checkpoints = query.getResultList();
		em.getTransaction().commit();
		em.close();
		factory.close();
		
		if (checkpoints.size() > 0) {
			isScheduled = true;
		}
		
		return isScheduled;
	}
}
