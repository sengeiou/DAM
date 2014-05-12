package whyq.controller;

import whyq.WhyqApplication;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

public class SreenGestureControllerwithParams implements
		GestureDetector.OnGestureListener {
	int width, height;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	public static boolean isLeftToRight = false;
	private Context context;
	private int type;
	public SreenGestureControllerwithParams(Context context, int type) {
		WindowManager wm = (WindowManager) WhyqApplication.Instance()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		height = display.getHeight();
		this.context = context;
		width = display.getWidth();
		this.type = type;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		Intent broadcast = new Intent();
		Log.d("===>", "Get onScroll event");

//		Log.d("onScroll", "e1 x = " + e1.getX() + " and e2x" + e2.getX());
//		try {
//			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//				return false;
//			// right to left swipe
//			if (e1.getX() - e2.getX() > 0) {
//				// do your code
//				isLeftToRight = false;
//
//			} else {
//				// left to right flip
//				isLeftToRight = true;
//			}
//		} catch (Exception e) {
//			// nothing
//		}
//		Log.d("onFling", "isLeftToright x = "+isLeftToRight);
		if(distanceY > 0 )
			return true;
		else return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		Log.d("===>", "SingleTap");
		if(type==0){
			
		}
		return false;
	}
}