package whyq.activity;

import whyq.TabGroupActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class FavouritesActivityGroup extends TabGroupActivity {
	public static boolean isTabChanged = false;
	private FavouritesActivityGroup context;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//View view = getLocalActivityManager().startActivity( "ExplorerActivity", new Intent(this, ExplorerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		setTabGroup(this);
		//replaceView(view);
		createUI();
		this.context = this;
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public void onResume() {
		super.onResume();
		if(isTabChanged) {
			createUI();
		}
	}
	
	public void createUI() {
//		View view = getLocalActivityManager().startActivity( "FavouriteActivity", new Intent(this, FavouriteActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
//		Fix for Whyqapp
		Intent intent = new Intent(this, FavouriteActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("favourite", "true");
		View view = getLocalActivityManager().startActivity( "FavouriteActivity", intent).getDecorView();
		setTabGroup(this);
		replaceView(view);
		clearHistory();
	}
	

}
