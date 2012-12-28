package com.yvelabs.chronometer;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;

import com.yvelabs.chronometer.utils.DateUtils;
import com.yvelabs.chronometer.utils.FontUtils;


@RemoteView
public class Chronometer extends TextView {

    /***
     * A callback that notifies when the chronometer has incremented on its own.
     */
    public interface OnChronometerTickListener {

        /***
         * Notification that the chronometer has changed.
         */
        void onChronometerTick(Chronometer chronometer);

    }

    private boolean mStarted;
    private OnChronometerTickListener mOnChronometerTickListener;
    private long startTime;
    private long stopTime;
    private long pauseDuringTime = 0;
    private DateUtils dateUtis;
    
    private static final int TICK_WHAT = 2;
    
    /***
     * Initialize this Chronometer object.
     * Sets the base to the current time.
     */
    public Chronometer(Context context) {
        this(context, null, 0);
    }

    /***
     * Initialize with standard view layout information.
     * Sets the base to the current time.
     */
    public Chronometer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /***
     * Initialize with standard view layout information and style.
     * Sets the base to the current time.
     */
    public Chronometer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    	dateUtis = new DateUtils();
		//set font
		setTypeface(FontUtils.getTypeFace(this.getContext()));
		//监听时区变更
		dateUtis.registerTimeZoneBroadcast(this.getContext());
		
		//pause动画 是否播放
    }

    public void setFont (String fontName) {
    	setTypeface(FontUtils.getTypeFace(this.getContext(), fontName));
    }

    public void setFormat(String pattern) {
    	DateUtils.formatPattern = pattern;
    }

    public String getFormat() {
    	return DateUtils.formatPattern;
    }

    public void setOnChronometerTickListener(OnChronometerTickListener listener) {
        mOnChronometerTickListener = listener;
    }

    /***
     * @return The listener (may be null) that is listening for chronometer change
     *         events.
     */
    public OnChronometerTickListener getOnChronometerTickListener() {
        return mOnChronometerTickListener;
    }
    
    void dispatchChronometerTick() {
        if (mOnChronometerTickListener != null) {
            mOnChronometerTickListener.onChronometerTick(this);
        }
    }
    
    public long duringTime() {
		if (startTime == 0 && stopTime == 0)
			return 0;

		if (startTime > 0 && stopTime == 0)
			return SystemClock.elapsedRealtime() - startTime;

		if (startTime > 0 && stopTime > 0)
			return stopTime - startTime;

		return 0;
	}
    
    public String druingTimeFormat () {
		return new DateUtils().formatTime(duringTime());
	}
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        dateUtis.unregisterTimeZoneBroadcast(this.getContext());
        stop();
    }
    
    public void reset() {
    	startTime = 0;
    	pauseDuringTime = 0;
    	stopTime = 0;
    	mStarted = false;
    	
    	//显示归零
    	mHandler.removeMessages(TICK_WHAT);
    	updateText(startTime);
    }

    public void start() {
    	if (mStarted == false) { 
    		startTime = SystemClock.elapsedRealtime() - pauseDuringTime;
    		pauseDuringTime = 0;
        	stopTime = 0;
    		mStarted = true;
    		
    		updateText(SystemClock.elapsedRealtime());
			dispatchChronometerTick();
			mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), 1000);
    	}
    }
    
	public void stop() {
		pauseDuringTime = 0;
		
		if (mStarted == true) {
			stopTime = SystemClock.elapsedRealtime();
			mStarted = false;
			mHandler.removeMessages(TICK_WHAT);
			dispatchChronometerTick();
			updateText(stopTime);
		}
	}
	
	public void pause () {
		if (mStarted == true) {
			stop();
			pauseDuringTime = duringTime();
		}
	}

    private synchronized void updateText(long now) {
        setText(new DateUtils().formatTime(now - startTime));
    }
    
    private Handler mHandler = new Handler() {
        public void handleMessage(Message m) {
            if (mStarted) {
                updateText(SystemClock.elapsedRealtime());
                dispatchChronometerTick();
                mHandler.sendMessageDelayed(Message.obtain(this, TICK_WHAT), 1000);
            }
        }
    };

}