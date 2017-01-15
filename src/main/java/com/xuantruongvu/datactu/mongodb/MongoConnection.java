package com.xuantruongvu.datactu.mongodb;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.xuantruongvu.datactu.crawler.CrawlerConfig;

@SuppressWarnings("deprecation")
public class MongoConnection { 
	private static MongoConnection INSTANCE;  
	
	private static MongoClient mongo = null; 
	
	private static DB db = null;  
	
	static {
		Properties properties = new Properties();
		
		InputStream is = CrawlerConfig.class.getClassLoader().getResourceAsStream("mongodbConnection.properties");
		
		try {
			properties.load(is);

			MongoCredential credential = MongoCredential.createCredential(properties.getProperty("username"), properties.getProperty("database"), properties.getProperty("password").toCharArray());
			mongo = new MongoClient(new ServerAddress(properties.getProperty("host"), Integer.parseInt(properties.getProperty("port"))), Arrays.asList(credential));
			db = mongo.getDB( "datactu" );	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private MongoConnection() {}  
	
	public synchronized static MongoConnection getInstance() { 
		if (INSTANCE == null) { 
			INSTANCE = new MongoConnection(); 
		} 
		return INSTANCE;
	}    
	
	public MongoClient getConnection() {
		if (mongo == null) {
			return null;
		}
		return mongo; 
	}  
	
	public DB getDatabase() { 
		return db; 
	} 
}