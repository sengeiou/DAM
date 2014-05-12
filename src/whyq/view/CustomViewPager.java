package whyq.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.Interpolator;

public class CustomViewPager extends ViewPager implements OnGestureListener {
	private GestureDetector gestureScanner;

	public CustomViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		gestureScanner = new GestureDetector(this);
		postInitViewPager();
	}
private MotionEvent ev ; 
	private boolean enabled;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		gestureScanner = new GestureDetector(this);
		postInitViewPager();
		this.enabled = true;
	}


	/**
	 * Override the Scroller instance with our own class so we can change the
	 * duration
	 */
	private void postInitViewPager() {
		try {
			Class<?> viewpager = ViewPager.class;
			Field scroller = viewpager.getDeclaredField("mScroller");
			scroller.setAccessible(true);
			Field interpolator = viewpager.getDeclaredField("sInterpolator");
			interpolator.setAccessible(true);


		} catch (Exception e) {
		}
	}

	/**
	 * Set the factor by which the duration will change
	 */
	public void setScrollDurationFactor(double scrollFactor) {
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// switch (ev.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// // enabled = false;
		// lastX = ev.getX();
		// lastY = ev.getY();
		// // break;
		// // this.disableScroll();
		// // break;
		// case MotionEvent.ACTION_UP:
		// deltaX = Math.abs(ev.getX() - lastX);
		// deltaY = Math.abs(ev.getY() - lastY);
		// enabled = false;
		// // break;
		// }
		// boolean swipe = false, touch = false;
		// if (swiper != null)
		// swipe = swiper.onTouch(this, ev);
		// touch = super.onTouchEvent(ev);
		// return swipe || touch;
		// if (gestureScanner != null)
		// return gestureScanner.onTouchEvent(ev);
		// else
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		// switch (ev.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// mNeedToRebase = true;
		// // enabled = true;
		// // mIgnore = false;
		// // lastX = ev.getX();
		// // lastY = ev.getY();
		// // return (deltaX <= deltaY) || enabled;
		// // // this.disableScroll();
		// // // break;
		// return false;
		//
		// case MotionEvent.ACTION_UP:
		// // enabled = true;
		// // deltaX = Math.abs(ev.getX() - lastX);
		// // deltaY = Math.abs(ev.getY() - lastY);
		// // return true;
		// // break;
		// case MotionEvent.ACTION_MOVE:
		//
		// deltaX = Math.abs(ev.getX() - lastX);
		// deltaY = Math.abs(ev.getY() - lastY);
		// enabled = !(deltaX > deltaY);
		// return !enabled;
		// default: {
		// return super.onInterceptTouchEvent(ev);
		// }
		// }
		 this.ev = ev ; 
		return gestureScanner.onTouchEvent(ev);
		// return false ;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnblePaging() {
		return this.enabled;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Log.d("Action", "onDown");
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		Log.d("Action", "onFling");
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		Log.d("Action", "onScroll");

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		Log.d("Action", "SingleTap");
		// enabled = true ;
		return false;
	}

}