package com.yvelabs.chronometer.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * time zone broadcast receiver
 * @author Yve
 *
 */
public class TimeZoneChangedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		DateUtils.setTimeZoneAdjust();
		Log.d("com.yvelabs.chronometer", "TimeZoneChangedReceiver, time zone has been changed");
	}

}
