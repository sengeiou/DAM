package whyq.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;

public class ScrollviewCustom extends ScrollView {

	public ScrollviewCustom(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollviewCustom(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollviewCustom(Context context) {
		super(context);
	}

	public void scrollToDeepChild(View child) {
		Point childOffset = new Point();

		getDeepChildOffset(child.getParent(), child, childOffset);

		Rect childRect = new Rect(childOffset.x, childOffset.y, childOffset.x
				+ child.getWidth(), childOffset.y + child.getHeight());
		int deltay = computeScrollDeltaToGetChildRectOnScreen(childRect);
		smoothScrollBy(0, deltay);
	}

	private void getDeepChildOffset(ViewParent nextParent, View nextChild,
			Point accumulatedOffset) {
		ViewGroup parent = (ViewGroup) nextParent;
		accumulatedOffset.x += nextChild.getLeft();
		accumulatedOffset.y += nextChild.getTop();
		if (parent == this) {
			return;
		}
		getDeepChildOffset(parent.getParent(), parent, accumulatedOffset);
	}
	
	
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        Log.e("onScrollChanged","IsUp "+ScreenGestureController.isUp);
    }
}