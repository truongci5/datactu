package com.xuantruongvu.datactu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuantruongvu.datactu.mongodb.MongoService;
import com.xuantruongvu.datactu.util.DatetimeUtil;

/**
 * @author xuantruongvu
 * This class remove old data from MongoDB after certain time.
 */
public class CleanerController {
	private static final Logger logger = LoggerFactory.getLogger("cleaner");
	
	private final static Long delta = 1209600000l; //14 days in milliseconds
	
	public static void clean() {
		Long since = (System.currentTimeMillis() - delta)/1000l;
		
		int removedArticles = MongoService.removeArticles(since);
		logger.info("{} articles are removed at {}", removedArticles, DatetimeUtil.getVnDatetime());
		
		int removedLinks = MongoService.removeLinks(since);
		logger.info("{} links are removed at {}", removedLinks, DatetimeUtil.getVnDatetime());
	}
}
