package com.xuantruongvu.datactu.controller;

public class ManualMain {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Missed option !");
			System.err.println("Syntax: Main --option");
			System.err.println("crawler \t Crawl news from news souces");
			System.err.println("cleaner \t Remove news older than 30 days");
			System.err.println("seacher \t Search relevant news");
		} else {
		
			String option = args[0];
			
			switch (option) {
				case "crawler": 
					CrawlerController.crawl();
					break;
				case "cleaner":
					CleanerController.clean();
					break;
				case "searcher":
					SearcherController.search();
					break;					
			}
		}
	}
}
