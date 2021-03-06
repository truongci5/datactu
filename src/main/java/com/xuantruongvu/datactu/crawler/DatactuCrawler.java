package com.xuantruongvu.datactu.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuantruongvu.datactu.mongodb.ArticleDocument;
import com.xuantruongvu.datactu.mongodb.LinkDocument;
import com.xuantruongvu.datactu.mongodb.MongoService;
import com.xuantruongvu.datactu.util.HashUtil;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleSentencesExtractor;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.HTMLHighlighter;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author xuantruongvu
 * This class extends the default crawler fo Crawler4J library, 
 * it takes specific input and embeds more processing steps
 */
public class DatactuCrawler extends WebCrawler {
	private static final Logger logger = LoggerFactory.getLogger("crawler");
	
	private static final Pattern FILTERS = Pattern
			.compile(".*(\\.(css|rss|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private String domain;
	
	private String domainUrl;
	
	private boolean firstVisited = false;

	/**
	 * This variable contains hashed value of the urls which were discovered at the last searching time
	 */
	private HashSet<String> recentUrls;

	private HashSet<String> newUrls;

	@Override
	public void onStart() {
		CustomData customData = (CustomData) myController.getCustomData();
		domain = customData.getDomain();
		domainUrl = customData.getDomainUrl();
		recentUrls = customData.getRecentUrls();
		newUrls = new HashSet<String>();
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		
		// Check if the URL is the home page, then ignore it 
		if (!firstVisited) {
			firstVisited = true;
			return true;
		}
		
		// Trim the URL to its original form
		href = URLAnalyzer.removeUtmParameters(href);
		
		// Check if the URL is already visited, then ignore it 
		if (newUrls.contains(href)) {
			return false;
		} else {
			newUrls.add(href);
		}
		
		// Check if the URL contains filter patterns, then ignore it
		if (FILTERS.matcher(href).matches()) {
			return false;
		}

		// Check if the URL is the sub-domain of the souce, then ignore it 
		if (href.indexOf(domain) < 0) {
			return false;
		}

		try {
			// Check if the URL seems like a category link (not an article link), then ignore it
			if (URLAnalyzer.isCategory(href)) {
				return false;
			}

			// Check if the URL seems like a sub-domain of the source, then ignore it
			if (URLAnalyzer.isSubdomain(href, domain)) {
				return false;
			}
		} catch (MalformedURLException e) {
		}
	
		String hashedHref;
		try {
			hashedHref = HashUtil.hashString(href);
		} catch (Exception e) {
			hashedHref = href;
		}
		
		// Check if the URL was already discovered at the last searching time, then ignore it
		if (recentUrls.contains(hashedHref)) {
			return false;
		}

		return true;
	}

	@Override
	public void visit(Page page) {
		String href = page.getWebURL().getURL();
		href = URLAnalyzer.removeUtmParameters(href);
		
		if (href.equalsIgnoreCase(domainUrl)) {
			return; //No need to save the homepage
		}
		
		LinkDocument link = new LinkDocument();
		link.setUrl(href);
		link.setSource(domain);
		Long timestamp = System.currentTimeMillis() / 1000L;
		link.setCreatedAt(timestamp);
		
		MongoService.insertLink(link);

		if (page.getParseData() instanceof HtmlParseData) {			
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();

			// Get metadata of the article from html meta-tags 
			MetaData metadata = MetaDataExtractor.INSTANCE.extract(html);
			String title = metadata.getTitle();			
			String description = metadata.getDescription();
			String image = metadata.getImage();
			
			// Extract the main text and the containing html of the article
			try {
				String content = ArticleSentencesExtractor.INSTANCE.getText(html);
				String htmlFragment;
				
				try {
					URL url = new URL(href);
					BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR; 
					htmlFragment = HTMLHighlighter.newHighlightingInstance().process(url, extractor);
				} catch (Exception e) {
					htmlFragment = html;
				}
				
				ArticleDocument article = new ArticleDocument();
				article.setTitle(title);
				article.setDescription(description);
				article.setImage(image);
				article.setSource(domain);
				article.setUrl(href);
				article.setCreatedAt(timestamp);
				article.setContent(content);
				article.setHtml(htmlFragment);
				MongoService.insertArticle(article);
				logger.info("{} content retrieved", href);
			} catch (BoilerpipeProcessingException e) {
				logger.warn("{} content failed to retrieve", href);
			}
		}
	}
}
