package com.yvelabs.chronometer.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {

	public static final String FONT_BREAKDOWN = "fonts/font_breakdown.ttf";
	public static final String FONT_DUPLEX = "fonts/font_duplex.ttf";
	public static final String SQUID_REGULAR = "fonts/squid_regular.ttf";
	public static final String SQUID_SMALL_CAPS = "fonts/squid_small_caps.ttf";
	public static final String VTKS_UNTITLED = "fonts/vtks_untitled.ttf";
	public static final String N_GAGE = "fonts/n-gage.ttf";

	public static Typeface getTypeFace(Context context, String typeFaceName) {
		return Typeface.createFromAsset(context.getAssets(), typeFaceName);
	}

	public static Typeface getTypeFace(Context context) {
		return getTypeFace(context, N_GAGE);
	}
}
