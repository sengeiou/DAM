package whyq.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.whyq.R;

public class SearchField extends FrameLayout {

	public interface QueryCallback {
		void onQuery(String queryString);
	}

	QueryCallback callback = null;
	QueryRunnable runnable = new QueryRunnable();
	private EditText mTextview;

	public void setQueryCallback(QueryCallback callback) {
		this.callback = callback;
	}

	public SearchField(Context context) {
		super(context);
		createLayout(context);
	}

	public SearchField(Context context, AttributeSet attrs) {
		super(context, attrs);
		createLayout(context);
	}

	public SearchField(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		createLayout(context);
	}

	public EditText getEditTextView() {
		return mTextview;
	}

	private void createLayout(Context context) {
		final FrameLayout layout = (FrameLayout) LayoutInflater.from(context)
				.inflate(R.layout.search_field, null);
		while (layout.getChildCount() > 0) {
			final View child = layout.getChildAt(0);
			layout.removeView(child);
			addView(child);
		}
		setPadding(layout.getPaddingLeft(), layout.getPaddingTop(),
				layout.getPaddingRight(), layout.getPaddingBottom());

		mTextview = (EditText) findViewById(R.id.tvSearch);
		mTextview.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				removeCallbacks(runnable);
				runnable.queryString = s.toString();
				postDelayed(runnable, 1000); // Delay 1s while user typing.
			}
		});
	}

	private class QueryRunnable implements Runnable {

		String queryString;

		@Override
		public void run() {
			if (callback != null) {
				callback.onQuery(queryString);
			}
		}

	}

}
