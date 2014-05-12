package whyq.activity;

import com.whyq.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WhyqProfileChildDotsMean extends Activity {
	private TextView tvTitle;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile_child_dots_mean);
		tvTitle = (TextView) findViewById(R.id.tvHeaderTitle);
		title = getIntent().getStringExtra("question");
		if (title != null) {
			tvTitle.setText(title);
		} else {
			tvTitle.setText("");
		}
	}

	public void onBack(View v) {
		finish();
	}
}
