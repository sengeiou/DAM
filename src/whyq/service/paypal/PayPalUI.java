package whyq.service.paypal;


import java.util.HashMap;
import java.util.StringTokenizer;

import whyq.WhyqApplication;
import whyq.interfaces.IPaypalListener;
import whyq.interfaces.IServiceListener;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.view.Whyq;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.whyq.R;

public class PayPalUI extends DialogFragment implements IServiceListener{
	private static final String TAG = "DialogFragment";
	private WhyqApplication app = WhyqApplication.Instance();
	private String authorizeUri; 
	private Service service;
	private WebView wv;
	private ProgressBar progressBar;
	private IPaypalListener listener;
	private String billId;
	private Context context;	
	
	public PayPalUI() {
	}
	
	public PayPalUI(IPaypalListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, STYLE_NO_TITLE);
		setStyle(DialogFragment.STYLE_NO_FRAME, DialogFragment.STYLE_NORMAL);
		String rsaToken = WhyqApplication.Instance().getRSAToken();
		context = WhyqApplication.Instance().getApplicationContext();
		billId = getArguments().getString("bill_id");
		service = new Service(this);
		service.getPaypalURI(rsaToken, billId);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.paypal_ui, null);
		
		wv = (WebView) v.findViewById(R.id.login_web);
		progressBar = (ProgressBar) v.findViewById(R.id.login_progress);
		getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(wvClient);
		ImageButton imgButtonClose = (ImageButton)v.findViewById(R.id.imgBtnClose);
		imgButtonClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onCloseClicked();
			}
		});
		loadURI();
		return v;
	}
	
	private void loadURI(){
		wv.getSettings().setJavaScriptEnabled(true);
		if (authorizeUri != null)
			wv.loadUrl(authorizeUri);
		Log.d(TAG, "loadURI : " + authorizeUri);
	}
	
	private WebViewClient wvClient = new WebViewClient(){
		 public boolean shouldOverrideUrlLoading(WebView view, String url) {
			 Log.d("@bo", "shouldOverrideUrlLoading : " +  url);
			 return false;
		 };
		 
		 public void onPageFinished(WebView view, String url) {
			 Log.d(TAG, "onPageFinished : " + url);
			 HashMap<String, String> params = new HashMap<String, String>();
			 
			 if (url != null && url.contains("access_token") && url.contains("refresh_token")){
				 String requestParams = url.substring(url.indexOf("?")+1);
				 StringTokenizer st = new StringTokenizer(requestParams, "&");
				 while(st.hasMoreTokens()){
					 String param = st.nextToken();
					 StringTokenizer st1 = new StringTokenizer(param, "=");
					 params.put(st1.nextToken(), st1.nextToken());
				 }

				 int expiredTime = 3600;
				 try {
					 expiredTime = Integer.parseInt(params.get(WhyqApplication.TOKEN_EXPIRED_TIME));
				} catch (Exception e) {
					expiredTime = 3600;
				}
//				 app.saveToken(params.get(WhyqApplication.ACCESS_TOKEN), expiredTime );
				 if (listener != null) listener.onLoginSuccess();
				 dismiss();
//				 wv.setVisibility(View.GONE);
//				 
//				 Log.d(TAG, "access_token : " + params.get("access_token"));
//				 Log.d(TAG, "refresh_token : " + params.get("refresh_token"));
//				 Log.d(TAG, "expires_in : " + params.get("expires_in"));
			  } else {
				  wv.setVisibility(View.VISIBLE);
				  wv.requestFocus();
			  }
			
			 progressBar.setVisibility(View.GONE);
			 
		 };
		 
	};
	
	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		if (result.getAction() == ServiceAction.ActionGetPaypalURI){
			if (result.isSuccess()){
				this.authorizeUri = (String) result.getData();
				loadURI();
			}
		}
	}
	
	public void onCloseClicked(){
		dismiss();
	}
}

