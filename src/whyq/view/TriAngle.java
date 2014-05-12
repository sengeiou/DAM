package whyq.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class TriAngle extends View {

	public TriAngle(Context context) {
		super(context);
		init();
	}

	public TriAngle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TriAngle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPaint.setColor(android.graphics.Color.BLACK);
		mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		mPaint.setAntiAlias(true);
	}

	private static final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Path sPath = new Path();

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final int w = getMeasuredWidth();
		final int h = getMeasuredHeight();
		sPath.moveTo(w, 0);
		sPath.lineTo(w, h);
		sPath.lineTo(0, h);
		sPath.lineTo(w, 0);
		sPath.close();

		canvas.drawPath(sPath, mPaint);
	}

}
