package com.xuantruongvu.datactu.mysql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class ArticleService {
	public static void insert(Article article) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("datactu");
		EntityManager em = factory.createEntityManager();
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			em.persist(article);
			et.commit();
		} catch (Exception e) {
			if(et != null && et.isActive()) et.rollback();
		} finally {
			em.close();
			factory.close();
		}
	}
	
	public static void insert(List<Article> articles) {
		for (Article article : articles) {
			insert(article);
		}
	}
}
