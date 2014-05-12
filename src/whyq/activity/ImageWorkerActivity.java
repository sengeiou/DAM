package whyq.activity;

import whyq.utils.ImageViewHelper;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.whyq.R;

public class ImageWorkerActivity extends ConsumeServiceActivity {

	protected ImageViewHelper mImageWorker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageWorker = new ImageViewHelper();
		mImageWorker.setLoading(BitmapFactory.decodeResource(getResources(),
				R.drawable.transparent_container));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mImageWorker.cleanCache();
	}

}
