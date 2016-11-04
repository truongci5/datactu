package com.xuantruongvu.datactu.crawler;

import java.util.HashSet;
import java.util.List;

public class CustomData {
	private String domain;
	private String domainUrl;
	private HashSet<String> recentUrls;
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public HashSet<String> getRecentUrls() {
		if (recentUrls == null) recentUrls = new HashSet<String>();
		return recentUrls;
	}
	
	public void setRecentUrls(List<String> urls) {
		this.recentUrls = new HashSet<String>(urls);
	}

	public String getDomainUrl() {
		return domainUrl;
	}
	
	public void setDomainUrl(String domainUrl) {
		this.domainUrl = domainUrl;
	}
}
