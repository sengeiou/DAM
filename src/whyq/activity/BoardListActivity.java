package whyq.activity;

import java.util.ArrayList;

import whyq.WhyqMain;
import whyq.adapter.BoardAdapter;
import whyq.controller.BoardController;
import whyq.model.Category;
import whyq.model.WhyqBoard;
import whyq.utils.API;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.whyq.R;

public class BoardListActivity extends Activity {
	
	private Category cat = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		this.cat = (Category) extras.get("Category");
		
		setContentView(R.layout.explorer_layout);
		
		TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
		ListView boardView = (ListView) findViewById(R.id.categories); //Reuse category layout
		BoardController boardController = new BoardController();
		final ArrayList<WhyqBoard> boards = boardController.getBoardList( this.cat.getId() );

		BoardAdapter boardAdapter = new BoardAdapter(this, R.layout.category_item, boards);
		boardView.setAdapter(boardAdapter);

		boardView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@SuppressWarnings("deprecation")
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent myIntent = new Intent(view.getContext(), ListActivity.class);
				WhyqBoard board = boards.get(position);
				String boardUrl = API.permListFromBoardUrl + board.getId();
				myIntent.putExtra("boardUrl", boardUrl);
				View boardListView = FavouritesActivityGroup.group.getLocalActivityManager() .startActivity("BoardListActivity", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
				FavouritesActivityGroup.group.replaceView(boardListView);
				//Toast.makeText(getApplicationContext(), boards.get(position).getName() , Toast.LENGTH_SHORT).show();
			}
		});


	}
	
	public void onBackPressed(){
		WhyqMain.back();
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
