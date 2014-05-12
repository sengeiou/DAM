package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import whyq.adapter.WhyqProfileFAQItemAdapter;
import whyq.adapter.WhyqProfileFAQItemAdapter.FAQViewHolder;
import whyq.interfaces.IServiceListener;
import whyq.model.Faq;
import whyq.model.ResponseData;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

public class WhyqProflleFAQActivity extends Activity implements IServiceListener{
	public static final String HOW_WHYQ_WORK = "How does WHY Q work?";
	public static final String WHAT_DOTS_MEAN = "What do the coloured dots mean?";
	public static final String WHY_AM_NOT_FINISH = "Why am I not able to finish paying for my order?";
	public static final String HOW_AUTHORISIZE = "How does Authorise and Capture work";
	public static final String HOW_MAKE_BAR_FAVOURITE = "How do I make a bar a favourite?";
	public static final String WHEN_WILL_I_KNOW_ORDER = "When will I know that my order is ready";
	private TextView tvTitle;
	private ListView lvContent;
//	private List<String> list = new ArrayList<String>();
	private Service service;
	private ProgressBar progressBar;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whyq_profile_faq_screen);
		tvTitle = (TextView)findViewById(R.id.tvHeaderTitle);
		lvContent = (ListView)findViewById(R.id.lvContent);
		progressBar = (ProgressBar)findViewById(R.id.prgBar);
//		String title = getIntent().getStringExtra("title");
//		if(title!=null){
//			tvTitle.setText(title);
//		}else{
			tvTitle.setText("FAQ");
//		}
		context = this;
		service = new Service(this);
		exeGetData();
//		bindDatatoListview(); Hardcode data
	}

	private void exeGetData() {
		// TODO Auto-generated method stub
		showDialog();
		service.getFaqs("");
	}

	private void bindDatatoListview(ArrayList<Faq> faqs) {
		// TODO Auto-generated method stub
//		list.add(HOW_WHYQ_WORK);
//		list.add(WHAT_DOTS_MEAN);
//		list.add(WHY_AM_NOT_FINISH);
//		list.add(HOW_AUTHORISIZE);
//		list.add(HOW_MAKE_BAR_FAVOURITE);
//		list.add(WHEN_WILL_I_KNOW_ORDER);
		
		WhyqProfileFAQItemAdapter adapter = new WhyqProfileFAQItemAdapter(WhyqProflleFAQActivity.this, faqs);
		lvContent.setAdapter(adapter);
	}
	public void onItemClicked(View v){
		Faq item = (Faq)v.getTag();
		if(!item.getContentQuestion().equals(WHAT_DOTS_MEAN)){
			if(item.getContentQuestion()!=null){
				Intent i = new Intent(WhyqProflleFAQActivity.this, WhyqProfileChildBasic.class);
				i.putExtra("question", item.getContentQuestion());
				i.putExtra("answer", item.getAnswerQuestion());
				i.putExtra("isWebview", "0");
				startActivity(i);				
			}
		}else{
			if(item.getContentQuestion()!=null){
				Intent i = new Intent(WhyqProflleFAQActivity.this, WhyqProfileChildDotsMean.class);
				i.putExtra("question", item.getContentQuestion());
				i.putExtra("answer", item.getAnswerQuestion());
				i.putExtra("isWebview", "0");
				startActivity(i);	
			}
		}
		
	}
	public void onBack(View v) {
		// WhyqMain.back();
		finish();
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		hideDialog();
		if(result.getAction() == ServiceAction.ActionGetFaqs && result.isSuccess()){
			ResponseData data = (ResponseData)result.getData();
			if(data!=null){
				if(data.getStatus().equals("200")){
					ArrayList<Faq> fraqList = (ArrayList<Faq>)data.getData();
					bindDatatoListview(fraqList);
//					Util.showDialog(context, data.getMessage());
				}else if(data.getStatus().equals("401")){
					Util.loginAgain(context, data.getMessage());
				}else{
					Util.showDialog(context, data.getMessage());
				}
			}
		}
	}
	private void showDialog() {
		// dialog.show();
		progressBar.setVisibility(View.VISIBLE);
	}

	private void hideDialog() {
		// dialog.dismiss();
		progressBar.setVisibility(View.GONE);
	}
}
