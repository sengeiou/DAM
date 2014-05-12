/**
 * 
 */
package whyq.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;

import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.adapter.CategorySpinnerAdapter;
import whyq.controller.AuthorizeController;
import whyq.controller.CategoryController;
import whyq.interfaces.Create_Board_delegate;
import whyq.interfaces.get_category_delegate;
import whyq.model.Category;
import whyq.model.User;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.XMLParser;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.whyq.R;

/**
 * @author Linh Nguyen
 *
 */
public class CreateBoardActivity extends Activity implements Create_Board_delegate, get_category_delegate {

	private Spinner mainCategory;
	private EditText boardName;
	private EditText boardDescription;
	private Button createBoard;
	private int categoryId = -1;
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);	
	User user = null; 
	/**
	 * Initialize the View
	 */
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.createboard_layout);
		
		View contentView = LayoutInflater.from(getParent()).inflate(R.layout.createboard_layout, null);
        setContentView(contentView);
        
        TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
        WhyqApplication state = (WhyqApplication) getApplicationContext();
		if(state != null)
			user = state.getUser();
		CategoryController catController = new CategoryController(CreateBoardActivity.this);
		final ArrayList<Category> categories = catController.getCategoryList();
		
		// Build the category spinner
		mainCategory = (Spinner) findViewById(R.id.categorySpinner);
//		addItemsOnMainCategory(mainCategory, categories);
		mainCategory.setOnItemSelectedListener(new CategorySpinnerSelectedListener());		
		
		boardName = (EditText) findViewById(R.id.boardName);
		boardDescription = (EditText) findViewById(R.id.boardDescription);
		
		createBoard = (Button) findViewById(R.id.createBoard);
		createBoard.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				WhyqApplication state = (WhyqApplication) getApplicationContext();
	        	if (state != null) {

	        		if (user != null) {
	        			nameValuePairs.clear();
	        			nameValuePairs.add(new BasicNameValuePair(Constants.BOARD_NAME, 
	        					boardName.getText().toString()));
	        			nameValuePairs.add(new BasicNameValuePair(Constants.CATEGORY_ID, 
	        					String.valueOf(categoryId)));
	        			nameValuePairs.add(new BasicNameValuePair(Constants.BOARD_USER_ID, 
	        					user.getId()));
	        			nameValuePairs.add(new BasicNameValuePair(Constants.BOARD_DESCRIPTION, 
	        					boardDescription.getText().toString()));
	        			
	        			XMLParser parser = new XMLParser(XMLParser.CREATE_BOARD, CreateBoardActivity.this, API.createBoardURL, nameValuePairs);
	        		}
	        	}
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);
		if (view instanceof EditText) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
	        float y = event.getRawY() + w.getTop() - scrcoords[1];

	        /*Log.d("CreateBoard", "Touch event " + event.getRawX() + "," + event.getRawY() +
	        		" " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," +
	        		w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);*/
	        if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) { 
	            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
	        }
		}
		return ret;
	}

	private void addItemsOnMainCategory(Spinner spinner, ArrayList<Category> categories) {
		CategorySpinnerAdapter categorySpinnerAdapter = new CategorySpinnerAdapter(this, categories);
		spinner.setAdapter(categorySpinnerAdapter);
		Category initial = (Category) categorySpinnerAdapter.getItem(0);
		if (initial != null)
			categoryId = Integer.parseInt(initial.getId());		
	}
	
	private class CategorySpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Category category = (Category) parent.getItemAtPosition(pos);
			categoryId = Integer.parseInt(category.getId());		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
		}		
	}
	
	private void getUserProfileById(String userId) {
		
	}

	@Override
	public void onSucess(Document doc) {
		// TODO Auto-generated method stub
		if (doc != null) {
			AuthorizeController authorizeController = new AuthorizeController();
			authorizeController.updateUserProfileById(this, user.getId(), XMLParser.UPDATE_PROFILE, CreateBoardActivity.this);			
			Toast toast = Toast.makeText(getApplicationContext(), "Board is created successfully!", Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
        	toast.show();
        	ImageActivityGroup.group.back();
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "Create new board failed! Please try again.", Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
        	toast.show();
        	boardName.setText("");
        	boardDescription.setText("");
		}
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getApplicationContext(), "Create new board failed! Please try again.", Toast.LENGTH_LONG);
    	toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 300);
    	toast.show();
    	boardName.setText("");
    	boardDescription.setText("");
	}


	@Override
	public void onCompletedGetCategory(ArrayList<Category> categories) {
		// TODO Auto-generated method stub
		addItemsOnMainCategory(mainCategory, categories);
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


