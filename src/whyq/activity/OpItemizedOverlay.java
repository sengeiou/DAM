//package whyq.activity;
//
//import java.util.ArrayList;
//
//import android.content.Context;
//import android.graphics.Paint;
//import android.graphics.Paint.Style;
//import android.graphics.drawable.Drawable;
//
//import com.google.android.maps.ItemizedOverlay;
//import com.google.android.maps.OverlayItem;
//
//public class OpItemizedOverlay extends ItemizedOverlay<OverlayItem> {
//	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
//	private Context mContext;
//	private Paint	innerPaint, borderPaint, textPaint;
//	public OpItemizedOverlay(Drawable defaultMarker, Context context) {
//		super(boundCenterBottom(defaultMarker));
//		mContext = context;
//	}
//
//	public void addOverlay(OverlayItem overlay) {
//		mOverlays.add(overlay);
//		populate();
//	}
//	public Paint getInnerPaint() {
//		if ( innerPaint == null) {
//			innerPaint = new Paint();
//			innerPaint.setARGB(225, 75, 75, 75); //gray
//			innerPaint.setAntiAlias(true);
//		}
//		return innerPaint;
//	}
//
//	public Paint getBorderPaint() {
//		if ( borderPaint == null) {
//			borderPaint = new Paint();
//			borderPaint.setARGB(255, 255, 255, 255);
//			borderPaint.setAntiAlias(true);
//			borderPaint.setStyle(Style.STROKE);
//			borderPaint.setStrokeWidth(2);
//		}
//		return borderPaint;
//	}
//
//	public Paint getTextPaint() {
//		if ( textPaint == null) {
//			textPaint = new Paint();
//			textPaint.setARGB(255, 255, 255, 255);
//			textPaint.setAntiAlias(true);
//		}
//		return textPaint;
//	}
//	@Override
//	protected OverlayItem createItem(int i) {
//		return mOverlays.get(i);
//	}
//
//	@Override
//	public int size() {
//		return mOverlays.size();
//	}
//
//	@Override
//	protected boolean onTap(int index) {
//		OverlayItem item = mOverlays.get(index);
////		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
////		dialog.setTitle(item.getTitle());
////		dialog.setMessage(item.getSnippet());
////		dialog.show();
//		return true;
//	}
//}