package com.xuantruongvu.datactu.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuantruongvu.datactu.crawler.DatactuCrawler;
import com.xuantruongvu.datactu.crawler.CrawlerConfig;
import com.xuantruongvu.datactu.crawler.CustomData;
import com.xuantruongvu.datactu.mongodb.MongoService;
import com.xuantruongvu.datactu.mysql.Source;
import com.xuantruongvu.datactu.mysql.SourceService;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;


/**
 * @author xuantruongvu
 * This class calls and handles the crawling steps.
 */
public class CrawlerController {
	private static final Logger logger = LoggerFactory.getLogger("crawler");
	
	/**
	 * The number of last saved URLs of a source (to compare with newly discovered URL during crawling)
	 */
	private static final Integer recentUrlNumber = 800; 
	
	/**
	 * This method does:
	 * - Retrieve from MySQL DB the list of active sources (nguon tin)
	 * - For each source, get its recently published articles  
	 */
	public static void crawl() {
		List<Source> sources = SourceService.findAll();
		logger.info("{} sources selected to crawl", sources.size());
		
		String currentDomain = "";
		String currentUrl = "";

		for (Source source : sources) {
			currentDomain = source.getDomain();
			currentUrl = source.getUrl();
			
			//This block to catch bad URL 
			try {
				@SuppressWarnings("unused")
				URL ou = new URL(currentUrl);
			} catch (MalformedURLException e) {
				continue;
			}
			
			try {								
				logger.info("{} crawling started", currentDomain);
				
				CrawlConfig config = new CrawlConfig();
				
				config.setCrawlStorageFolder(CrawlerConfig.getCrawlStorageFoler()
						+ "/single");
				config.setPolitenessDelay(CrawlerConfig.getPolitenessDelay());
				config.setMaxPagesToFetch(CrawlerConfig.getMaxPagesToFetch());
				config.setUserAgentString(CrawlerConfig.getUserAgent());
				config.setMaxDepthOfCrawling(CrawlerConfig.getMaxDepthOfCrawling());

				PageFetcher pageFetcher = new PageFetcher(config);

				RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
				robotstxtConfig.setEnabled(false);
				RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
						pageFetcher);
				
				CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
				
				CustomData customData = new CustomData();
				
				customData.setDomain(currentDomain);
				customData.setDomainUrl(currentUrl);
				customData.setRecentUrls(getRecentUrls(currentDomain));
				
				controller.setCustomData(customData);
				controller.addSeed(currentUrl);
				controller.startNonBlocking(DatactuCrawler.class, CrawlerConfig.getConcurrentThreadNumber());
				controller.waitUntilFinish();
				
				logger.info("{} crawling ended", source.getDomain());
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				logger.warn("{} crawling stopped caused by {}", currentDomain, errors.toString());
			}
		}
	}

	
	/**
	 * This method retreives from MongoDB the list of last saved URLs of a domain
	 * @param domain
	 * @return
	 */
	private static List<String> getRecentUrls(String domain) {
		List<String> links = MongoService.findLinks(domain, recentUrlNumber);
		return links;
	}
}
