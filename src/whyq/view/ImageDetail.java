package whyq.view;

import whyq.activity.ListActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.custom.WebImageView;
import com.whyq.R;

public class ImageDetail extends Activity {
	private String url;
	private Button btnClose;
	private WebImageView webImageView;
	public ImageDetail() {
		// TODO Auto-generated constructor stub

	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setStyle(DialogFragment.STYLE_NO_TITLE, STYLE_NO_TITLE);
//		setStyle(DialogFragment.STYLE_NO_FRAME, DialogFragment.STYLE_NORMAL);
//		View v = LayoutInflater.from(getParent()).inflate(R.layout.image_detail, null);
		setContentView(R.layout.image_detail);
		ListActivity.isRefesh = false;
		this.url = getIntent().getExtras().getString("url");
		btnClose = (Button)findViewById(R.id.btnClose);
		webImageView = (WebImageView)findViewById(R.id.thumbnail);
		if(url != null){
			webImageView.setImageUrl(url);
			webImageView.loadImage();
		}
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View v = LayoutInflater.from(getActivity().getParent()).inflate(R.layout.image_detail, null);
//		btnClose = (Button)v.findViewById(R.id.btnClose);
//		webImageView = (WebImageView)v.findViewById(R.id.thumbnail);
//		if(url != null){
//			webImageView.setImageUrl(url);
//			webImageView.loadImage();
//		}
//		btnClose.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				dismiss();
//			}
//		});
//		return v;
//	}

}
