package com.xuantruongvu.datactu.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author xuantruongvu
 * This class allows to extract the meta-data from html code of the article
 */
public class MetaDataExtractor {
	public static final MetaDataExtractor INSTANCE = new MetaDataExtractor();

	public static MetaDataExtractor getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Parses html code to dom document
	 * @param html HTML code
	 * @return Dom document
	 */
	public MetaData extract(String html) {
		Document document;
		document = Jsoup.parse(html);
		return extract(document);
	}
	
	/**
	 * Extract meta-data
	 * @param document DOM document
	 * @return Extracted values
	 */
	public MetaData extract(Document document) {
		MetaData metaData = new MetaData();
		
		metaData.setTitle(document.title());
		
		for (Element meta : document.select("meta")) {
			if (meta.attr("name").equals("description")) {
				metaData.setDescription(meta.attr("content"));
			}

			if (meta.attr("property").contains("og")) {
				if (meta.attr("property").substring(meta.attr("property").lastIndexOf(":") + 1).equals("image")) {
					metaData.setImage(meta.attr("content"));
				}

				if (meta.attr("property").substring(meta.attr("property").lastIndexOf(":") + 1).equals("description")) {
					metaData.setDescription(meta.attr("content"));
				}
				
				if (meta.attr("property").substring(meta.attr("property").lastIndexOf(":") + 1).equals("title")) {
					metaData.setTitle(meta.attr("content"));
				}
			}
		}
		
		return metaData;
	}
	
	/**
	 * Extract value of a specified tag using Jsoup selector syntax
	 * @param document DOM document
	 * @param selector Jsoup selector syntax
	 * @return the extracted value
	 */
	public String extractElementValue(Document document, String selector) {
		String value = "";
		
		Elements elements = document.select(selector);
		if (elements.size() > 0) {
			Element element = elements.first();
			value = element.text();
		}
		
		return value;
	}
}
