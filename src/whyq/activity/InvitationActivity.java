package whyq.activity;

import java.util.ArrayList;

import whyq.WhyqApplication;
import whyq.adapter.InvitationAdapter;
import whyq.interfaces.IServiceListener;
import whyq.model.ResponseData;
import whyq.model.User;
import whyq.service.Service;
import whyq.service.ServiceAction;
import whyq.service.ServiceResponse;
import whyq.utils.Util;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

public class InvitationActivity extends Activity implements IServiceListener {

	private ListView lvInvitation;
	private InvitationAdapter adapter;
	private TextView btnHeader;
	private ProgressBar prbar;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invitation_screen);
		context = this;
		btnHeader = (TextView) findViewById(R.id.permpingTitle);
		btnHeader.setText("Invitations");
		lvInvitation = (ListView) findViewById(R.id.lvInvitation);
		prbar = (ProgressBar) findViewById(R.id.prgBar);
		getInvitationData("1");
	}

	private void getInvitationData(String listInvited) {
		// TODO Auto-generated method stub
		setLoading(true);
		Service service = new Service(InvitationActivity.this);
		service.getInvitation(WhyqApplication.Instance().getRSAToken(),
				listInvited);
	}

	private void bindData(ArrayList<User> userList) {
		// TODO Auto-generated method stub
		adapter = new InvitationAdapter(this, userList);
		lvInvitation.setAdapter(adapter);
	}

	@Override
	public void onCompleted(Service service, ServiceResponse result) {
		// TODO Auto-generated method stub
		
		if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionGetInvitations) {
			setLoading(false);
			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {
				ArrayList<User> userList = (ArrayList<User>) data.getData();
				if (userList != null) {
					bindData(userList);
				}else{
					adapter.setData(new ArrayList<User>());
					adapter.notifyDataSetChanged();
					lvInvitation.setAdapter(adapter);
				}
			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(getParent(), data.getMessage());
			} else if (data.getStatus().equals("204")) {

			} else {
			}
		} else if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionAcceptInvitation) {
			setLoading(false);
			ResponseData data = (ResponseData) result.getData();
			if (data.getStatus().equals("200")) {
//				showToast(data.getMessage());
				Util.showDialog(context, data.getMessage());
				getInvitationData("1");
			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(getParent(), data.getMessage());
			} else if (data.getStatus().equals("204")) {

			} else {
			}
		}else if (result.isSuccess()
				&& result.getAction() == ServiceAction.ActionDeclineInvitation) {
			ResponseData data = (ResponseData) result.getData();
			setLoading(false);
			if (data.getStatus().equals("200")) {
//				showToast(data.getMessage());
				Util.showDialog(context, data.getMessage());
				getInvitationData("1");
			} else if (data.getStatus().equals("401")) {
				Util.loginAgain(getParent(), data.getMessage());
			} else if (data.getStatus().equals("204")) {

			} else {
			}
		}else{
			showToast("Fail!Try again!");
			setLoading(false);
		}
	}

	private void showToast(String message) {
		// TODO Auto-generated method stub
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public void setLoading(boolean isShowing) {
		if (isShowing) {
			prbar.setVisibility(View.VISIBLE);
		} else {
			prbar.setVisibility(View.INVISIBLE);
		}
	}

	public void onBack(View v) {
		finish();
	}

	public void onDoneClicked(View v) {

	}

	public void onDeleteClicked(View v) {
		Log.d("onDeleteClicked","onDeleteClicked");
		setLoading(true);
		User user = (User) v.getTag();
		Service service = new Service(InvitationActivity.this);
		service.acceptInvitation(WhyqApplication.Instance().getRSAToken(),
				user.getId()); 
	}

	public void onAcceptClicked(View v) {
		Log.d("onAcceptClicked","onAcceptClicked");
		setLoading(true);
		User user = (User) v.getTag();
		Service service = new Service(InvitationActivity.this);
		service.declineInvitation(WhyqApplication.Instance().getRSAToken(),
				user.getId());
	}
}
