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
import com.mongodb.WriteResult;

public class MongoService {
	private static DB db;
	
	static {
		db = MongoConnection.getInstance().getDatabase();
	}
	
	public static List<String> findLinks(String source, Integer limit) {
		List<String> links = new ArrayList<String>();
		DBCollection collection = db.getCollection("links");
		
		BasicDBObject query = new BasicDBObject("source", source.toLowerCase());
		
	    DBCursor cursor = collection.find(query).sort(new BasicDBObject("created_at",-1)).limit(limit);
	    
	    while (cursor.hasNext()) {
	    	links.add(cursor.next().get("url").toString());
	    }
		
		return links;
	}
	
	public static List<String> findTitles(String source, Integer limit) {
		List<String> links = new ArrayList<String>();
		DBCollection collection = db.getCollection("articles");
		
		BasicDBObject query = new BasicDBObject("source", source.toLowerCase());
		
	    DBCursor cursor = collection.find(query).sort(new BasicDBObject("created_at",-1)).limit(limit);
	    
	    while (cursor.hasNext()) {
	    	links.add(cursor.next().get("title").toString());
	    }
		
		return links;
	}
	
	public static void insertLinks(List<LinkDocument> links) {
		DBCollection collection = db.getCollection("links");
		for (LinkDocument link : links) {
			collection.insert(link.createDBObject());
		}
	}
	
	public static void insertLink(LinkDocument link) {
		DBCollection collection = db.getCollection("links");
		collection.insert(link.createDBObject());
	}
	
	public static void insertArticles(List<ArticleDocument> articles) {
		DBCollection collection = db.getCollection("articles");
		for (ArticleDocument article: articles) {
			collection.insert(article.createDBObject());
		}
	}

	public static void insertArticle(ArticleDocument article) {
		DBCollection collection = db.getCollection("articles");
		collection.insert(article.createDBObject());
	}
	
	public static List<ArticleDocument> searchArticles(String source, String query) {
		DBCollection collection = db.getCollection("articles");
		
		DBObject findCommand = new BasicDBObject(new BasicDBObject("source", source).append("$text", new BasicDBObject("$search", query)));
		DBObject projectCommand = new BasicDBObject("score", new BasicDBObject("$meta", "textScore"));
		DBObject sortCommand = new 	BasicDBObject("score", new BasicDBObject("$meta", "textScore"));
		
		
		DBCursor cursor = collection.find(findCommand, projectCommand).sort(sortCommand);
		List<ArticleDocument> articles = new ArrayList<ArticleDocument>();
		
		while(cursor.hasNext()) {
			ArticleDocument article = new ArticleDocument();
			DBObject object = cursor.next();
			article.setTitle((String)object.get("title"));
			article.setDescription((String)object.get("description"));
			article.setContent((String)object.get("content"));
			article.setHtml((String)object.get("html"));
			article.setImage((String)object.get("image"));
			article.setSource((String)object.get("source"));
			article.setUrl((String)object.get("url"));
			articles.add(article);
		}
		
		return articles;
	}
	
	public static List<ArticleDocument> findArticleByID(List<String> ids) {
		List<ArticleDocument> articles = new ArrayList<ArticleDocument>();
		List<ObjectId> vals = new ArrayList<ObjectId>();
		for (String id : ids) {
			vals.add(new ObjectId(id));
		}
		
		DBCollection collection = db.getCollection("articles");
		BasicDBObject query = new BasicDBObject();
	    query.put("_id", new BasicDBObject("$in", vals));
	    
	    DBCursor cursor = collection.find(query);
	    
	    while(cursor.hasNext()) {
			ArticleDocument article = new ArticleDocument();
			DBObject object = cursor.next();
			article.setTitle((String)object.get("title"));
			article.setDescription((String)object.get("description"));
			article.setContent((String)object.get("content"));
			article.setHtml((String)object.get("html"));
			article.setImage((String)object.get("image"));
			article.setSource((String)object.get("source"));
			article.setUrl((String)object.get("url"));
			article.setCreatedAt((Long)object.get("created_at"));
			articles.add(article);
		}
		
		return articles;
	}
	
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
	
	public static void setLastSearchTime(String domain, String searchedAt) {
		DBCollection collection = db.getCollection("domains");
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.append("$set", new BasicDBObject().append("searched_at", searchedAt));
		BasicDBObject searchQuery = new BasicDBObject().append("domain", domain);
		collection.update(searchQuery, newDocument);
	}
	
	public static int removeArticles(Long since) {
		DBCollection collection = db.getCollection("articles");
		BasicDBObject query = new BasicDBObject();
		query.append("created_at", new BasicDBObject("$lte", since));
		WriteResult result = collection.remove(query);
		return result.getN();
	}
	
	public static int removeLinks(Long since) {
		DBCollection collection = db.getCollection("links");
		BasicDBObject query = new BasicDBObject();
		query.append("created_at", new BasicDBObject("$lte", since));
		WriteResult result = collection.remove(query);
		return result.getN();
	}
}
