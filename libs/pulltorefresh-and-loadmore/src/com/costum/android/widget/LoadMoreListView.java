package com.costum.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.widget.R;

/*
 * Copyright (C) 2012 Fabian Leon Ortega <http://orleonsoft.blogspot.com/,
 *  http://yelamablog.blogspot.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class LoadMoreListView extends ListView implements OnScrollListener {

	private static final String TAG = "LoadMoreListView";

	/**
	 * Listener that will receive notifications every time the list scrolls.
	 */
	private OnScrollListener mOnScrollListener;
	private LayoutInflater mInflater;

	// footer view
	private RelativeLayout mFooterView;
	// private TextView mLabLoadMore;
	private ProgressBar mProgressBarLoadMore;

	// Listener to process load more items when user reaches the end of the list
	private OnLoadMoreListener mOnLoadMoreListener;
	// To know if the list is loading more items
	private boolean mIsLoadingMore = false;
	private int mCurrentScrollState;

	
	/***
	 * Clock listview
	 * @param context
	 */
	public static interface OnPositionChangedListener {

		public void onPositionChanged(LoadMoreListView listView, int position, View scrollBarPanel);

	}


	private View mScrollBarPanel = null;
	private int mScrollBarPanelPosition = 0;

	private OnPositionChangedListener mPositionChangedListener;
	private int mLastPosition = -1;

	private Animation mInAnimation = null;
	private Animation mOutAnimation = null;

	private final Handler mHandler = new Handler();

	private final Runnable mScrollBarPanelFadeRunnable = new Runnable() {

		@Override
		public void run() {
			if (mOutAnimation != null) {
				mScrollBarPanel.startAnimation(mOutAnimation);
			}
		}
	};

	/*
	 * keep track of Measure Spec
	 */
	private int mWidthMeasureSpec;
	private int mHeightMeasureSpec;

	
	public void init(Context context, AttributeSet attrs, int listviewstyle){
		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedListView);
		final int scrollBarPanelLayoutId = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanel, -1);
		final int scrollBarPanelInAnimation = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanelInAnimation, R.anim.in_animation);
		final int scrollBarPanelOutAnimation = a.getResourceId(R.styleable.ExtendedListView_scrollBarPanelOutAnimation, R.anim.out_animation);
		a.recycle();

		if (scrollBarPanelLayoutId != -1) {
			setScrollBarPanel(scrollBarPanelLayoutId);
		}

		final int scrollBarPanelFadeDuration = ViewConfiguration.getScrollBarFadeDuration();

		if (scrollBarPanelInAnimation > 0) {
			mInAnimation = AnimationUtils.loadAnimation(getContext(), scrollBarPanelInAnimation);
		}
		
		if (scrollBarPanelOutAnimation > 0) {
			mOutAnimation = AnimationUtils.loadAnimation(getContext(), scrollBarPanelOutAnimation);
			mOutAnimation.setDuration(scrollBarPanelFadeDuration);

			mOutAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (mScrollBarPanel != null) {
						mScrollBarPanel.setVisibility(View.GONE);
					}
				}
			});
		}
	}
	
	public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
		mPositionChangedListener = onPositionChangedListener;
	}


	public void setScrollBarPanel(View scrollBarPanel) {
		mScrollBarPanel = scrollBarPanel;
		mScrollBarPanel.setVisibility(View.GONE);
		requestLayout();
	}

	public void setScrollBarPanel(int resId) {
		setScrollBarPanel(LayoutInflater.from(getContext()).inflate(resId, this, false));
	}

	public View getScrollBarPanel() {
		return mScrollBarPanel;
	}
	
	@Override
	protected boolean awakenScrollBars(int startDelay, boolean invalidate) {
		final boolean isAnimationPlayed = super.awakenScrollBars(startDelay, invalidate);
		
		if (isAnimationPlayed == true && mScrollBarPanel != null) {
			if (mScrollBarPanel.getVisibility() == View.GONE) {
				mScrollBarPanel.setVisibility(View.VISIBLE);
				if (mInAnimation != null) {
					mScrollBarPanel.startAnimation(mInAnimation);
				}
			}
			
			mHandler.removeCallbacks(mScrollBarPanelFadeRunnable);
			mHandler.postAtTime(mScrollBarPanelFadeRunnable, AnimationUtils.currentAnimationTimeMillis() + startDelay);
		}

		return isAnimationPlayed;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mScrollBarPanel != null && getAdapter() != null) {
			mWidthMeasureSpec = widthMeasureSpec;
			mHeightMeasureSpec = heightMeasureSpec;
			measureChild(mScrollBarPanel, widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if (mScrollBarPanel != null) {
			final int x = getMeasuredWidth() - mScrollBarPanel.getMeasuredWidth() - getVerticalScrollbarWidth();
			mScrollBarPanel.layout(x, mScrollBarPanelPosition, x + mScrollBarPanel.getMeasuredWidth(),
					mScrollBarPanelPosition + mScrollBarPanel.getMeasuredHeight());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (mScrollBarPanel != null && mScrollBarPanel.getVisibility() == View.VISIBLE) {
			drawChild(canvas, mScrollBarPanel, getDrawingTime());
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		mHandler.removeCallbacks(mScrollBarPanelFadeRunnable);
	}
	
	/***
	 * End code
	 * @param context
	 */
	
	public LoadMoreListView(Context context) {
		super(context);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		
		/**
		 * ListView clock
		 */
		
		/***
		 * Clock
		 */
		init(context, attrs, android.R.attr.listViewStyle);
		/**
		 * End clock
		 */
		/**
		 * End
		 */
	}

	public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// footer
		mFooterView = (RelativeLayout) mInflater.inflate(
				R.layout.load_more_footer, this, false);
		/*
		 * mLabLoadMore = (TextView) mFooterView
		 * .findViewById(R.id.load_more_lab_view);
		 */
		mProgressBarLoadMore = (ProgressBar) mFooterView
				.findViewById(R.id.load_more_progressBar);

		addFooterView(mFooterView);

		super.setOnScrollListener(this);

		
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	/**
	 * Set the listener that will receive notifications every time the list
	 * scrolls.
	 * 
	 * @param l
	 *            The scroll listener.
	 */
	@Override
	public void setOnScrollListener(AbsListView.OnScrollListener l) {
		mOnScrollListener = l;
	}

	/**
	 * Register a callback to be invoked when this list reaches the end (last
	 * item be visible)
	 * 
	 * @param onLoadMoreListener
	 *            The callback to run.
	 */

	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
		mOnLoadMoreListener = onLoadMoreListener;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}

		if (mOnLoadMoreListener != null) {

			if (visibleItemCount == totalItemCount) {
				mProgressBarLoadMore.setVisibility(View.GONE);
				// mLabLoadMore.setVisibility(View.GONE);
				return;
			}

			boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

			if (!mIsLoadingMore && loadMore
					&& mCurrentScrollState != SCROLL_STATE_IDLE) {
				mProgressBarLoadMore.setVisibility(View.VISIBLE);
				// mLabLoadMore.setVisibility(View.VISIBLE);
				mIsLoadingMore = true;
				onLoadMore();
			}

		}
		
		/**
		 * Clock scroll
		 */

		if (null != mPositionChangedListener && null != mScrollBarPanel) {

			// Don't do anything if there is no itemviews
			if (totalItemCount > 0) {
				/*
				 * from android source code (ScrollBarDrawable.java)
				 */
				final int thickness = getVerticalScrollbarWidth();
				int height = Math.round((float) getMeasuredHeight() * computeVerticalScrollExtent() / computeVerticalScrollRange());
				int thumbOffset = Math.round((float) (getMeasuredHeight() - height) * computeVerticalScrollOffset() / (computeVerticalScrollRange() - computeVerticalScrollExtent()));
				final int minLength = thickness * 2;
				if (height < minLength) {
					height = minLength;
				}
				thumbOffset += height / 2;
				
				/*
				 * find out which itemviews the center of thumb is on
				 */
				final int count = getChildCount();
				for (int i = 0; i < count; ++i) {
					final View childView = getChildAt(i);
					if (childView != null) {
						if (thumbOffset > childView.getTop() && thumbOffset < childView.getBottom()) {
							/* 
							 * we have our candidate
							 */
							if (mLastPosition != firstVisibleItem + i) {
								mLastPosition = firstVisibleItem + i;
								
								/*
								 * inform the position of the panel has changed
								 */
								mPositionChangedListener.onPositionChanged(this, mLastPosition, mScrollBarPanel);
								
								/*
								 * measure panel right now since it has just changed
								 * 
								 * INFO: quick hack to handle TextView has ScrollBarPanel (to wrap text in
								 * case TextView's content has changed)
								 */
								measureChild(mScrollBarPanel, mWidthMeasureSpec, mHeightMeasureSpec);
							}
							break;
						}
					}
				}

				/*
				 * update panel position
				 */
				mScrollBarPanelPosition = thumbOffset - mScrollBarPanel.getMeasuredHeight() / 2;
				final int x = getMeasuredWidth() - mScrollBarPanel.getMeasuredWidth() - getVerticalScrollbarWidth();
				mScrollBarPanel.layout(x, mScrollBarPanelPosition, x + mScrollBarPanel.getMeasuredWidth(),
						mScrollBarPanelPosition + mScrollBarPanel.getMeasuredHeight());
			}
		}

		if (mOnScrollListener != null) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	
		/**
		 * End clock scroll
		 */

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
	
		//bug fix: listview was not clickable after scroll
		if ( scrollState == OnScrollListener.SCROLL_STATE_IDLE )
        	{
          		view.invalidateViews();
        	}
        	
		mCurrentScrollState = scrollState;

		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
		
		/**
		 * Clock scroll
		 */
		if (mOnScrollListener != null) {
			mOnScrollListener.onScrollStateChanged(view, scrollState);
		}
		/**
		 * End clock scroll
		 */

	}

	public void onLoadMore() {
		Log.d(TAG, "onLoadMore");
		if (mOnLoadMoreListener != null) {
			mOnLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * Notify the loading more operation has finished
	 */
	public void onLoadMoreComplete() {
		mIsLoadingMore = false;
		mProgressBarLoadMore.setVisibility(View.GONE);
	}

	/**
	 * Interface definition for a callback to be invoked when list reaches the
	 * last item (the user load more items in the list)
	 */
	public interface OnLoadMoreListener {
		/**
		 * Called when the list reaches the last item (the last item is visible
		 * to the user)
		 */
		public void onLoadMore();
	}

}
