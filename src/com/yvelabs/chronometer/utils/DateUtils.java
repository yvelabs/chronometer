package com.yvelabs.chronometer.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class DateUtils {

	public static String formatPattern = "HH:mm:ss";

	private static final String ACTION_TIMEZONE_CHANGED = "android.intent.action.ACTION_TIMEZONE_CHANGED";

	private static long timeZoneAdjust;
	private static TimeZoneChanged timeZoneChanged;

	public DateUtils() {
		setTimeZoneAdjust();
	}
	
	public void registerTimeZoneBroadcast (Context context) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_TIMEZONE_CHANGED); // 为BroadcastReceiver指定action，使之用于接收同action的广播
		timeZoneChanged = new TimeZoneChanged();
		context.registerReceiver(timeZoneChanged, intentFilter);
	}
	
	public void unregisterTimeZoneBroadcast (Context context) {
		if (timeZoneChanged != null) {
			context.unregisterReceiver(timeZoneChanged);
		}
	}

	public static void setTimeZoneAdjust() {
		Calendar calendar = Calendar.getInstance();
		long unixTime = calendar.getTimeInMillis();
		long unixTimeGMT = unixTime - TimeZone.getDefault().getRawOffset();
		timeZoneAdjust = unixTimeGMT - unixTime;
	}

	public static long getTimeZoneAdjust() {
		return timeZoneAdjust;
	}

	public String formatTime(long time, String pattern) {
		return new SimpleDateFormat(pattern).format(timeZoneAdjust + time);
	}

	public String formatTime(long time) {
		return formatTime(time, formatPattern);
	}
}

class TimeZoneChanged extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		DateUtils.setTimeZoneAdjust();
	}
}
