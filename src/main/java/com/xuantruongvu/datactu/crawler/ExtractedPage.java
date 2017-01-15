package com.xuantruongvu.datactu.crawler;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;

/**
 * @author xuantruongvu
 * This class stores the article meta-data and main content  
 */
public class ExtractedPage {
	private MetaData metadata;
	private String content;
	
	public ExtractedPage(String html) {
		this.metadata = MetaDataExtractor.INSTANCE.extract(html);
		try {
			this.content = ArticleExtractor.INSTANCE.getText(html);
		} catch (BoilerpipeProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MetaData getMetaData() {
		return this.metadata;
	}
	
	public String getContent() {
		return this.content;
	}
	
}
