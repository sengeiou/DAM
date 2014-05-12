/**
 * 
 */
package whyq.activity;

import whyq.TabGroupActivity;
import whyq.WhyqMain;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;


/**
 * @author Linh Nguyen
 *
 */
public class ProfileActivityGroup extends TabGroupActivity {
	public static boolean isTabChanged = false;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = getLocalActivityManager().startActivity( "ProfileActivity", new Intent(this, ProfileWhyQActivty.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		setTabGroup(this);
		replaceView(view);
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public void onResume(){
		super.onResume();
		if(isTabChanged) {
			createUI();
		}
	}
	
	public void createUI() {
		ProfileActivity.commentData = null;
		ProfileActivity.isUserProfile = true;
//		User user = WhyqUtils.isAuthenticated(getApplicationContext());
//		setTabGroup(this);
//        if (user != null) {
//        	Comment comment = new Comment(user.getId());
//			comment.setAuthor(user);
//			ProfileActivity.commentData = comment;
        	View view = getLocalActivityManager().startActivity( "ProfileActivity", new Intent(this, ProfileWhyQActivty.class)).getDecorView();//.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//    		replaceView(view);
//    		clearHistory(); 
//    	} else {
//    		clearHistory();
//			WhyqMain.showLogin();
//		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	    	Activity accountActivity = ProfileActivityGroup.group.getCurrentActivity();
	    	if(accountActivity instanceof AccountActivity) {
	    		return accountActivity.onKeyDown(keyCode, event);
	    	}
	        WhyqMain.back();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
