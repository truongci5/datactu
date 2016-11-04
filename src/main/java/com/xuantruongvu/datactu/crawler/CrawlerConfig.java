package com.xuantruongvu.datactu.crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class CrawlerConfig {
	private static Properties properties;
	
	static {
		properties = new Properties();
		
		InputStream is = CrawlerConfig.class.getClassLoader().getResourceAsStream("crawler.properties");
		
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getCrawlStorageFoler() {
		return properties.getProperty("crawlStorageFolder");
	}
	
	public static Integer getPolitenessDelay() {
		return Integer.valueOf(properties.getProperty("politenessDelay"));
	}
	
	public static Integer getMaxPagesToFetch() {
		return Integer.valueOf(properties.getProperty("maxPagesToFetch"));
	}
	
	public static Integer getConcurrentThreadNumber() {
		return Integer.valueOf(properties.getProperty("concurrentThreadNumber"));
	}
	
	public static Integer getMaxDepthOfCrawling() {
		return Integer.valueOf(properties.getProperty("maxDepthOfCrawling"));
	}
	
	public static String getUserAgent() {
		return properties.getProperty("userAgent");
	}
}
