package com.xuantruongvu.datactu.searcher;

import java.util.Date;

import com.xuantruongvu.datactu.mongodb.ArticleDocument;
import com.xuantruongvu.datactu.mongodb.MongoService;
import com.xuantruongvu.datactu.mysql.Article;

public class SearchResult {
	private String id;
	private ArticleDocument doc;
	private Article article;

	public SearchResult(String id) {
		this.id = id;
		article = new Article();		
		doc = MongoService.findArticleByID(id);
		article = new Article();
		article.setTitle(doc.getTitle());
		article.setDescription(doc.getDescription());
		article.setContent(doc.getContent());
		article.setHtml(doc.getHtml());
		article.setImage(doc.getImage());
		article.setUrl(doc.getUrl());
		article.setPublishedDate(new Date(doc.getCreatedAt()*1000L));
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the doc
	 */
	public ArticleDocument getDoc() {
		return doc;
	}

	/**
	 * @param doc the doc to set
	 */
	public void setDoc(ArticleDocument doc) {
		this.doc = doc;
	}

	/**
	 * @return the result
	 */
	public Article getArticle() {
		return article;
	}

	/**
	 * @param article the result to set
	 */
	public void setArticle(Article article) {
		this.article = article;
	}
	
	

}
