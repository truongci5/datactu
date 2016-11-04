package com.xuantruongvu.datactu.searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Searcher {
	private static final Logger logger = LoggerFactory.getLogger("searcher");
	private static int fetchSize = 1000;
	private static String url = "http://localhost:8983/solr/datactu";
	private static SolrClient solr;
	
	static {
		solr = new HttpSolrClient(url);
	}
	
	
	public static List<String> search(List<String> sources, String start, List<String> domainKeywords, List<String> topicKeywords) {
		SolrQuery query = new SolrQuery();
		query.setRequestHandler("/select");
		query.setQuery(getQuery(domainKeywords, topicKeywords));
		query.addFilterQuery(getSourceFilter(sources));
		query.addFilterQuery(getTimestampFilter(start));
		query.setRows(fetchSize);
		
		List<String> ids = new ArrayList<String>();
		
		try {
			QueryResponse response = solr.query(query);
			SolrDocumentList list = response.getResults();
			logger.info("Query {} with filters {} get {} results", query.getQuery(), query.getFilterQueries() , list.size());
		
			if (list.size() > 0) {
				Iterator<SolrDocument> iter = list.iterator();
				while (iter.hasNext()) {
					SolrDocument result = iter.next();
					String id = result.getFieldValue("id").toString();
					ids.add(id);
				}
			}
		} catch (SolrServerException e) {
			logger.error("Query {} with filters {} get no results as {}", query.getQuery(), query.getFilterQueries(), e.getMessage());
		} catch (IOException e) {
			logger.error("Something goes wrong with Solr: {}", e.getMessage());
		}
		
		return ids;
	}
	
	private static String getTimestampFilter(String start) {
		if (start == null) {
			return "timestamp:[* TO *]";
		}
		
		return "timestamp:[" + start + " TO NOW]";
	}

	private static String getSourceFilter(List<String> sources) {
		String filter = "source:(";
		for (String source: sources) {
			filter = filter + "\"" + source + "\",";
		}
		filter = filter.substring(0, filter.length()-1);
		filter = filter + ")";
		return filter;
	}

	private static String getQuery(List<String> domainKeywords, List<String> topicKeywords) {
		String domainPart = serialize(domainKeywords, "OR");
		String topicPart = serialize(topicKeywords, "OR");
		String query = domainPart;
		
		if (topicPart != "") {
			query = "(" + domainPart + ") AND (" + topicPart + ")";
		}

		return query;
	}
	
	private static String serialize(List<String> keywords, String operator) {
		String suffix = "\"";
		String prefix = "\" " + operator + " ";
		String serialized = "";
		if (keywords != null) {
			for (String keyword : keywords) {
				serialized = serialized + suffix + keyword + prefix;
			}

			serialized = serialized.substring(0, serialized.length()-prefix.length() + 1);
		}
		
		return serialized;
	}
}
