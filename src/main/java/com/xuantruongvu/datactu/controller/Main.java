package com.xuantruongvu.datactu.controller;

import java.util.Timer;
import java.util.TimerTask;

import com.xuantruongvu.datactu.mysql.CheckPointService;
import com.xuantruongvu.datactu.util.DatetimeUtil;

public class Main {
	Timer timer1, timer2;

	public Main() {
		timer1 = new Timer();
		timer1.scheduleAtFixedRate(new HourlyTask(), 0, 1000 * 60 * 60);

		timer2 = new Timer();
		timer2.scheduleAtFixedRate(new DailyTask(), 0, 1000 * 60 * 60 * 24);
	}

	class HourlyTask extends TimerTask {

		@Override
		public void run() {
			int hour = DatetimeUtil.getCurrentHour();

			boolean toCheck = CheckPointService.isScheduled(hour);

			if (toCheck) {
				CrawlerController.crawl();
				SearcherController.search();
			}
		}
	}

	class DailyTask extends TimerTask {
		@Override
		public void run() {
			CleanerController.clean();
		}
	}

	public static void main(String args[]) {
		new Main();
	}
}
