package com.xuantruongvu.datactu.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DatetimeUtil {
	
	public static int getCurrentHour() {
		SimpleDateFormat dateFormatVn = new SimpleDateFormat("HH");  
		dateFormatVn.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));		        
		Date now = new Date();
		return Integer.parseInt(dateFormatVn.format(now));
	}
	
	public static String getVnDatetime() {
		SimpleDateFormat dateFormatVn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatVn.setTimeZone(TimeZone.getTimeZone("Asia/Saigon"));		        
		Date now = new Date();
		return dateFormatVn.format(now);
	}
	
	public static String getUTCDatetime() {
		SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date now = new Date();
		return dateFormatUTC.format(now);
	}
}
