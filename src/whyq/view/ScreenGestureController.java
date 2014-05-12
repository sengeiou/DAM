package whyq.view;

import whyq.WhyqApplication;
import whyq.activity.ListDetailActivity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.OverScroller;

public class ScreenGestureController implements
		GestureDetector.OnGestureListener {
	int width, height;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	public static boolean isUp = false;
	private Context context;
	private int type;

	public ScreenGestureController(Context context, int type) {
		WindowManager wm = (WindowManager) WhyqApplication.Instance()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		height = display.getHeight();
		this.context = context;
		mScroller = new OverScroller(this.context);
		width = display.getWidth();
		this.type = type;
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		mScroller.forceFinished(true);
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		mScroller.forceFinished(true);
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}
	private OverScroller mScroller;
	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub

		if(!mScroller.isFinished()){
			Intent broadcast = new Intent();
			Log.d("===>", "Get onScroll event");
			// broadcast.setAction(IssueListActivity.DOUBLE_CLICK_WEB_ACTION);
			// AMReaderApplication.Instance().sendBroadcast(broadcast);
			Log.d("onScroll", "e1 y = " + e1.getY() + " and e2Y" + e2.getY());
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getY() - e2.getY() > 0) {
					// do your code
					isUp = true;

				} else {
					// left to right flip
					isUp = false;
				}
			} catch (Exception e) {
				// nothing
			}
			Log.d("onFling", "isUp x = "+isUp);
		}else{
			((ListDetailActivity)context).refreshDataAfterScroll();
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}