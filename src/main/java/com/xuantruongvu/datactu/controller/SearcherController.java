package com.xuantruongvu.datactu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuantruongvu.datactu.mongodb.ArticleDocument;
import com.xuantruongvu.datactu.mongodb.MongoService;
import com.xuantruongvu.datactu.mysql.Article;
import com.xuantruongvu.datactu.mysql.ArticleService;
import com.xuantruongvu.datactu.mysql.Domain;
import com.xuantruongvu.datactu.mysql.DomainService;
import com.xuantruongvu.datactu.mysql.Html;
import com.xuantruongvu.datactu.mysql.HtmlService;
import com.xuantruongvu.datactu.mysql.Source;
import com.xuantruongvu.datactu.mysql.SourceService;
import com.xuantruongvu.datactu.mysql.Topic;
import com.xuantruongvu.datactu.searcher.SearchResult;
import com.xuantruongvu.datactu.searcher.Searcher;
import com.xuantruongvu.datactu.util.DatetimeUtil;

/**
 * @author xuantruongvu
 * This class calls and handles the searching steps
 */
public class SearcherController {
	private static final Logger logger = LoggerFactory.getLogger("searcher");
	
	
	/**
	 * This method does:
	 * - Retrieve from Mysql DB the list of active domains (Linh vuc)
	 * - For each domain, get its topics (Chu de), if any, and create the corresponding queries, 
	 * match these queries with Solr and save the results into Mysql DB, if any.
	 */
	public static void search() {
		List<Domain> domains = DomainService.findAll();
		
		logger.info("{} domains selected to search", domains.size());
		
		Map<String, Integer> sourceMap = getSourceMap();
		String currentDomain = "";
		
		Map<String, SearchResult> resultMap = new HashMap<String, SearchResult>();
		
		for (Domain domain : domains) {
			currentDomain = domain.getName();
			
			logger.info("{} domain searching started", currentDomain);
			
			// The last search time prevents getting the same articles which were already retrieved at the last search
			String lastSearch = MongoService.getLastSearchTime(currentDomain);			
			MongoService.setLastSearchTime(currentDomain, DatetimeUtil.getUTCDatetime());
			
			logger.info("{} domain last searched {}", currentDomain, lastSearch);
			
			List<String> domainKeywords = domain.getKeywords();
			List<Source> sources = domain.getSources();
			
			List<String> sourceNames = new ArrayList<String>();
			for (Source source : sources) {
				sourceNames.add(source.getDomain());
			}
			
			List<Topic> topics = domain.getTopics();
			
			// If the domain contains any topic, get the topic keywords and merge them with the domain keywords.   
			if (topics.size() > 0) {
				for (Topic topic : topics) {
					List<String> topicKeywords = topic.getKeywords();
					
					List<String> ids = Searcher.search(sourceNames, lastSearch, domainKeywords, topicKeywords);
					
					for (String id : ids) {
						if (resultMap.containsKey(id)) {
							SearchResult result = resultMap.get(id);
							Article article = result.getArticle();
							article.attachTopic(topic);
							article.attachDomain(domain);
						} else {
							SearchResult result;							
							try {
								result = new SearchResult(id);
							} catch (Exception e) {
								continue;
							}
							
							Article article = result.getArticle();
							ArticleDocument doc = result.getDoc();
							article.attachDomain(domain);
							article.setSourceId(sourceMap.get(doc.getSource()));
							article.attachTopic(topic);
							resultMap.put(id, result);
						}
					}
				}
			} else {
				List<String> ids = Searcher.search(sourceNames, lastSearch, domainKeywords, null);
				
				for (String id : ids) {
					SearchResult result;
					
					try {
						result = new SearchResult(id);
					} catch (Exception e) {
						continue;
					}
					
					Article article = result.getArticle();
					ArticleDocument doc = result.getDoc();
					article.attachDomain(domain);
					article.setSourceId(sourceMap.get(doc.getSource()));
					resultMap.put(id, result);
				}
			}
			
			
			for (Map.Entry<String, SearchResult> entry : resultMap.entrySet())
			{
				logger.info(" {} : {}", entry.getValue().getArticle().getTitle(), entry.getValue().getArticle().getUrl());
			}
		}
		
		insertArticles(resultMap);
	}

	
	/**
	 * Insert matching articles into Mysql DB
	 * @param resultMap
	 */
	private static void insertArticles(Map<String, SearchResult> resultMap) {
		List<SearchResult> list = new ArrayList<SearchResult>(resultMap.values());
		
		for (SearchResult result : list) {
			Article article = result.getArticle();
			ArticleService.insert(article);
			Html html = result.getHtml();
			html.setArticleId(article.getId());
			HtmlService.insert(html);
		}
	}

	/**
	 * Store temporarily the (url, id) pairs of the sources
	 * @return
	 */
	private static Map<String, Integer> getSourceMap() {
		List<Source> sources = SourceService.findAll();
		Map<String, Integer> sourceMap = new HashMap<String, Integer>();
		
		for (Source source : sources) {
			sourceMap.put(source.getDomain(), source.getId());
		}
		
		return sourceMap;
	}
}
