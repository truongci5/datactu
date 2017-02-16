package com.xuantruongvu.datactu.mysql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class ArticleService {
	public static void insert(Article article) {
		if (check(article)) return; 
		
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("datactu");
		EntityManager em = factory.createEntityManager();
		EntityTransaction et = em.getTransaction();
		
		try {
			et.begin();
			em.persist(article);
			em.flush();
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
	
	private static boolean check(Article article) {
		boolean existed = false;
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("datactu");
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Query query = em
				.createQuery(
						"SELECT a FROM Article a WHERE a.url = :url")
				.setParameter("url", article.getUrl());
		@SuppressWarnings("unchecked")
		List<Article> articles = query.getResultList();
		if (articles.size() > 0) existed = true;
		em.getTransaction().commit();
		em.close();
		factory.close();
		return existed;
	}
}
