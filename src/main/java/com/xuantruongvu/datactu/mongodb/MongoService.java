package com.xuantruongvu.datactu.mongodb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

/**
 * @author xuantruongvu
 * This class allows to interact with the MongoDB
 */
public class MongoService {
	private static DB db;
	
	static {
		db = MongoConnection.getInstance().getDatabase();
	}
	
	/**
	 * Get a number of links which were most recently discovered
	 * @param source The source that the links belong to
	 * @param limit The number of links to be returned
	 * @return
	 */
	public static List<String> findLinks(String source, Integer limit) {
		List<String> links = new ArrayList<String>();
		DBCollection collection = db.getCollection("links");
		
		BasicDBObject query = new BasicDBObject("source", source.toLowerCase());
		
	    DBCursor cursor = collection.find(query).sort(new BasicDBObject("created_at",-1)).limit(limit);
	    
	    try {
	    	while (cursor.hasNext()) {
		    	DBObject obj = cursor.next();		    	
		    	
		    	if (obj.containsField("hashed_url")) {
		    		links.add(obj.get("hashed_url").toString());
		    	} else if (obj.containsField("url")) {
		    		links.add(obj.get("url").toString());
		    	} else {
		    		continue;
		    	}
		    }
    	} catch (MongoException e) {
    		throw e;
    	}
	    	
		return links;
	}
	
	
	/**
	 * Saves new link documents
	 * @param links
	 */
	public static void insertLinks(List<LinkDocument> links) {
		DBCollection collection = db.getCollection("links");
		for (LinkDocument link : links) {
			collection.insert(link.createDBObject());
		}
	}
	
	/**
	 * Saves a new link document
	 * @param link
	 */
	public static void insertLink(LinkDocument link) {
		DBCollection collection = db.getCollection("links");
		collection.insert(link.createDBObject());
	}
	
	/**
	 * Saves new article documents
	 * @param articles
	 */
	public static void insertArticles(List<ArticleDocument> articles) {
		DBCollection collection = db.getCollection("articles");
		for (ArticleDocument article: articles) {
			collection.insert(article.createDBObject());
		}
	}

	/**
	 * Saves a new article document
	 * @param article
	 */
	public static void insertArticle(ArticleDocument article) {
		DBCollection collection = db.getCollection("articles");
		collection.insert(article.createDBObject());
	}
	
	/**
	 * Searches for an article by its ID
	 * @param id the article ID
	 * @return
	 */
	public static ArticleDocument findArticleByID(String id) {
		ArticleDocument article = new ArticleDocument();
		DBCollection collection = db.getCollection("articles");
		BasicDBObject query = new BasicDBObject("_id", new ObjectId(id));
		DBObject object = collection.findOne(query);
		article.setTitle((String)object.get("title"));
		article.setDescription((String)object.get("description"));
		article.setContent((String)object.get("content"));
		article.setHtml((String)object.get("html"));
		article.setImage((String)object.get("image"));
		article.setSource((String)object.get("source"));
		article.setUrl((String)object.get("url"));
		article.setCreatedAt((Long)object.get("created_at"));
		return article;
	}
	
	/**
	 * Gets the last time that a source was checked for new articles
	 * @param domain The domain to be checked
	 * @return
	 */
	public static String getLastSearchTime(String domain) {
		DBCollection collection = db.getCollection("domains");
		
		BasicDBObject query = new BasicDBObject("domain", domain);
		
		DBObject dom = collection.findOne(query);
		    
	    if (dom == null) {
	    	collection.insert(new BasicDBObject().append("domain", domain));
	    	
	    	SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date currentTime = new Date(System.currentTimeMillis() - 4*86400*1000l); //4 days ago
			return dateFormatUTC.format(currentTime);
	    }
	    
	    return (String)dom.get("searched_at");
	}
	
	/**
	 * Sets the new last time
	 * @param domain The domain to be checked
	 * @param searchedAt The new value (use the current time)
	 */
	public static void setLastSearchTime(String domain, String searchedAt) {
		DBCollection collection = db.getCollection("domains");
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("searched_at", searchedAt));
		BasicDBObject searchQuery = new BasicDBObject().append("domain", domain);
		collection.update(searchQuery, newDocument);
	}
	
	
	/**
	 * Removes old article documents
	 * @param since The time that every document which was inserted before, will be removed
	 * @return
	 */
	public static int removeArticles(Long since) {
		DBCollection collection = db.getCollection("articles");
		BasicDBObject query = new BasicDBObject();
		query.append("created_at", new BasicDBObject("$lte", since));
		WriteResult result = collection.remove(query);
		return result.getN();
	}
	
	/**
	 * Removes old link documents
	 * @param since The time that every document which was inserted before, will be removed
	 * @retur
	 */
	public static int removeLinks(Long since) {
		DBCollection collection = db.getCollection("links");
		BasicDBObject query = new BasicDBObject();
		query.append("created_at", new BasicDBObject("$lte", since));
		WriteResult result = collection.remove(query);
		return result.getN();
	}
}
