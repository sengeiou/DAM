package whyq.activity;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class WhyqProfileChildBasic extends Activity {

	private TextView tvTitle;
	private TextView tvContent;
	private WebView wvContent;
	private String answer;
	private String question;
	private String isWebview;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile_child_basic);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		tvContent = (TextView)findViewById(R.id.tvContent);
		wvContent = (WebView)findViewById(R.id.wvContent);
		question = getIntent().getStringExtra("question");
		answer = getIntent().getStringExtra("answer");
		isWebview = getIntent().getStringExtra("isWebview");
		
		tvTitle.setText(question);
		if(isWebview.equals("1")){
			url = getIntent().getStringExtra("url");
			tvContent.setVisibility(View.GONE);
			wvContent.setVisibility(View.VISIBLE);
			wvContent.loadUrl(url);
		}else{
			wvContent.setVisibility(View.GONE);
			tvContent.setVisibility(View.VISIBLE);
			if(answer!=null)
				tvContent.setText(answer);
			
		}
//		if(title!=null){
//			tvTitle.setText(title);
//		}else{
//			tvTitle.setText("");
//		}
//		if(title.equals(WhyqProflleFAQActivity.HOW_WHYQ_WORK)){
//			tvContent.setText(getResources().getString(R.string.About));
//		}else if(title.equals(WhyqProflleFAQActivity.WHAT_DOTS_MEAN)){
//			tvContent.setText(getResources().getString(R.string.About));
//		}else if(title.equals(WhyqProflleFAQActivity.WHY_AM_NOT_FINISH)){
//			tvContent.setText(getResources().getString(R.string.About));
//		}else if(title.equals(WhyqProflleFAQActivity.HOW_AUTHORISIZE)){
//			tvContent.setText(getResources().getString(R.string.About));
//		}else if(title.equals(WhyqProflleFAQActivity.HOW_MAKE_BAR_FAVOURITE)){
//			tvContent.setText(getResources().getString(R.string.About));
//		}else if(title.equals(WhyqProflleFAQActivity.WHEN_WILL_I_KNOW_ORDER)){
//			tvContent.setText(getResources().getString(R.string.About));
//		}
	}
	public void onBack(View v) {
		// WhyqMain.back();
		finish();
	}
}
