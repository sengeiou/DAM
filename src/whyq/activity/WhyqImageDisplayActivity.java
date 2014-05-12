package whyq.activity;

import whyq.utils.ImageViewHelper;
import whyq.utils.ImageViewHelper.OnCompleteListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.whyq.R;

public class WhyqImageDisplayActivity extends Activity {

	public static final String ARG_IMAGE_URL = "arg_image_url";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_image);
		
		final String imageUrl = getIntent().getStringExtra(ARG_IMAGE_URL);
		if (imageUrl == null) {
			finish();
			return;
		}
		
		ImageView imageView = (ImageView) findViewById(R.id.image);
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		ImageViewHelper imageWorker = new ImageViewHelper();
		imageWorker.setOnCompleteListener(new OnCompleteListener() {
			
			@Override
			public void onComplete() {
				progressBar.setVisibility(View.GONE);
			}
		});
		imageWorker.downloadImage(imageUrl, imageView);
	}
}
