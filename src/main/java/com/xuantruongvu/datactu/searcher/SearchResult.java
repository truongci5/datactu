package com.xuantruongvu.datactu.searcher;

import java.util.Date;

import com.xuantruongvu.datactu.mongodb.ArticleDocument;
import com.xuantruongvu.datactu.mongodb.MongoService;
import com.xuantruongvu.datactu.mysql.Article;
import com.xuantruongvu.datactu.mysql.Html;

/**
 * @author xuantruongvu
 * This class represents a result matching a query, returned by Solr Service
 */
public class SearchResult {
	private String id;
	private ArticleDocument doc;
	private Article article;
	private Html html;

	public SearchResult(String id) {
		this.id = id;
		article = new Article();		
		html = new Html();
		doc = MongoService.findArticleByID(id);
		article = new Article();
		article.setTitle(doc.getTitle());
		article.setDescription(doc.getDescription());
		article.setContent(doc.getContent());
		article.setImage(doc.getImage());
		article.setUrl(doc.getUrl());
		article.setPublishedDate(new Date(doc.getCreatedAt()*1000L + 7*3600*1000L));
		html.setHtml(doc.getHtml());
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

	/**
	 * @return the html
	 */
	public Html getHtml() {
		return html;
	}
	
	

}
