package com.xuantruongvu.datactu.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MetaDataExtractor {
	public static final MetaDataExtractor INSTANCE = new MetaDataExtractor();

	public static MetaDataExtractor getInstance() {
		return INSTANCE;
	}
	
	public MetaData extract(String html) {
		Document document;
		document = Jsoup.parse(html);
		return extract(document);
	}
	
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
}
