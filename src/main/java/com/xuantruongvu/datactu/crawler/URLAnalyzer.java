package com.xuantruongvu.datactu.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLAnalyzer {
	public static boolean isCategory(String url) throws MalformedURLException {
		URL aURL = new URL(url);
		
		if (hasPostTypeQuery(aURL)) {
			return false;
		}
		
		if (hasCategoryTypeQuery(aURL)){
//			System.out.print("3.1");
			return true;
		}
		if (hasTag(aURL) || hasQuery(aURL)) {
//			System.out.print("3.2");
			return true;
		}
		if (hasCategoryTypePath(aURL)) {
//			System.out.print("3.3");
			return true;
		}
		return false;
	}
	
	private static boolean hasCategoryTypePath(URL url) {
		String path = url.getPath();
		
		if (path == "") return true;
		path = path.substring(1);
		
		String[] levels = path.split("/");
		
		if (levels.length > 0) {
			if (levels.length == 1 && levels[levels.length-1].length() < 35) return true;
			else if (levels[levels.length-1].length() < 35 && levels[levels.length-2].length() < 30) return true;
		}
		return false;
	}
	
	private static boolean hasCategoryTypeQuery(URL url) {
		String query = url.getQuery();
		if (query != null && query != "") {
			String[] variables = query.split("&");
			for (String var : variables) {
				var = var.toLowerCase();
				if (var.length() > 30) continue;
				if (var.indexOf("category") > 0) {
					return true;
				}
				
				if (var.indexOf("section") > 0) {
					return true;
				}
				
				if (var.indexOf("cat") > 0) {
					return true;
				}
				
				if (var.indexOf("folder") > 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean hasTag(URL url) {
		String file = url.getFile();
		if (file != null && file != "") {
			String[] terms = file.split("[/,-,/.]");
			for (String term : terms) {
				if (term.toLowerCase() == "tag") {
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean hasQuery(URL url) {
		String query = url.getQuery();
				
		if (query != null && !query.isEmpty()) {
			String[] pairs = query.split("&");
			if (pairs.length > 3) {
				return true;
			}
		}
		
		return false;
	}
	
	private static boolean hasPostTypeQuery(URL url) {
		String[] patterns = {"article","page","post"};
		String specialPattern = "id";
		String query = url.getQuery();
		
		if (query != null && !query.isEmpty()) {
			String[] pairs = query.split("&");
			
			for (String pair : pairs) {
		        int idx = pair.indexOf("=");
		        if (idx > 0) {
		        	String key = pair.substring(0,idx).toLowerCase();
		        	if (key.equals(specialPattern)) {
			        	return true;
			        }
			        
			        for (String pattern : patterns) {
			        	if (key.contains(pattern) || key.equals(specialPattern)) {
			        		return true;
			        	}
			        }
		        }
		    }
		}
		
		return false;
	}
	
	
	public static boolean isSubdomain(String url, String domain) throws MalformedURLException {
		URL aURL = new URL(url);
		String path = aURL.getPath();
		if (path != null && path == "") {
			if (aURL.getHost().indexOf(domain) >= 0) {
				return true;
			}
		}
		return false;
	}

	public static String removeUtmParameters(String url) {
		String regex = "(\\&|\\?)utm([_a-zA-Z0-9=]+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(url);		
		return matcher.replaceAll("");
	}
}
