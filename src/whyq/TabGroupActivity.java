package whyq;

import java.util.ArrayList;

import whyq.activity.ListActivity;
import whyq.activity.ListActivityGroup;
import whyq.model.User;
import whyq.utils.WhyqUtils;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.whyq.R;
public class TabGroupActivity extends ActivityGroup {

	// Keep this in a static variable to make it accessible for all the nesten
	// activities,
	// lets them manipulate the view
	public static TabGroupActivity group;

	// Need to keep track of the history if you want the back-button to
	// work properly, don't use this if your activities requires a lot of
	// memory.
	private ArrayList<View> history;
	
	private boolean isAddedToHistory = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.history = new ArrayList<View>();
		group = this;
		

	}
	
	@Override
	public void onResume() {
		super.onResume();
		/*long lastTimeLogin = XMLParser.getLastTimeLogin(this);
		long timeout = System.currentTimeMillis() - lastTimeLogin;
		if(timeout >= XMLParser.ACCOUNT_TIME_OUT) {
			PermpingApplication state = (PermpingApplication) getApplicationContext();
			User user = state.getUser();
			if (user != null) {
				AuthorizeController authorizeController = new AuthorizeController();
				authorizeController.logout(user.getId());
				state.setUser(null);
				XMLParser.storePermpingAccount(this, "", "");
			}
		}*/
	}	
	
	public static void setTabGroup(TabGroupActivity tabGroup) {
		if(group instanceof ListActivityGroup) {
			if(!(tabGroup instanceof ListActivityGroup)) {
				((ListActivityGroup)group).removeAllData();
			}
		}
		group = tabGroup;
	}
	
	public void clearHistory(){
		//this.history.clear();
		for( int i = 1; i < history.size(); i++ ){
			history.remove(i);
		}
	}

	public void replaceView(View v) {
		// Adds the old one to history
		history.add(v);
		isAddedToHistory = true;
		// Changes this Groups View to the new View.
		setContentView(v);
	}
	
	public void replaceViewWithoutHistory(View v) {
		// Changes this Groups View to the new View.
		isAddedToHistory = false;
		setContentView(v);
	}
	
	public void addViewToHistory(View v) {
		// Adds the old one to history
		history.add(v);
	}
	
	public void overrideView(View v) {
		// Adds the old one to history
		history.add(v);
		setContentView(v);
		if(history.size() >= 2)
			history.remove(history.size()- 2);
	}
	public void back() {
		if (history.size() > 0) {
			if (history.size() == 1) {
//				setContentView(history.get(history.size() - 1));
				if(isAddedToHistory == false) {
					//login activity
					User user = WhyqUtils.isAuthenticated(getApplicationContext());
					if(user != null) {
						setContentView(history.get(history.size() - 1));
					} else {
						showDialogToExit();
					}					
				} else {
					showDialogToExit();
				}
			} else {
				history.remove(history.size() - 1);
				setContentView(history.get(history.size() - 1));
				/*if(PermpingMain.getCurrentTab() == 0 || PermpingMain.getCurrentTab() == 1) {
					sendBroadcast("", "");					
				}*/
				sendBroadcast("", "");	
			}
		} else {

			showDialogToExit();
		}
	}
	
	public void sendBroadcast(String issueId, String storyId) {
	    Intent new_intent = new Intent();
	    new_intent.putExtra("issueId", issueId);
	    new_intent.putExtra("storyId", storyId);
	    new_intent.setAction(ListActivity.DOWNLOAD_COMPLETED);
	    sendBroadcast(new_intent);
	}
	
	public void showDialogToExit(){
		Resources res = this.getApplicationContext().getResources();
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();		
		alertDialog.setTitle(res.getString(R.string.exit_title));
		alertDialog.setMessage(res.getString(R.string.exit_content));
		alertDialog.setButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      // here you can add functions
			  finish();
		   }
		});
		alertDialog.setButton2(res.getString(R.string.no), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
				      
		   }
		});
		alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	        WhyqMain.back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}