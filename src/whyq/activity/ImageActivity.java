package whyq.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import whyq.WhyqMain;
import whyq.model.User;
import whyq.utils.WhyqUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whyq.R;

public class ImageActivity extends Activity {
	private int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1224;
	
	private int SELECT_PICTURE = 1;
	public static String imagePath="";
	public static String strCurDate="";
	public static Uri mCapturedImageURI;
	//private ProgressDialog dialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_layout);
		
		TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
		final LinearLayout takePhoto = (LinearLayout) findViewById(R.id.takePhoto);
		takePhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				User user = WhyqUtils.isAuthenticated(getApplicationContext());
//				if (user != null) {
					// define the file-name to save photo taken by Camera activity
					showCamera();
//				} else {
//
//					WhyqMain.showLogin();
//				}
			}
		});
		
		
		
		final LinearLayout openGalerry = (LinearLayout) findViewById(R.id.gallery);
		openGalerry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				   // select a file
//				User user = WhyqUtils.isAuthenticated(getApplicationContext());
//				if (user != null) {
					Intent intent = new Intent();
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
//				} else {
//					// Go to login screen
////					Intent i = new Intent(v.getContext(), LoginPermActivity.class);
////					v.getContext().startActivity(i);
//					WhyqMain.showLogin();
//				}
                
			}
		});
		
		final LinearLayout createBoard = (LinearLayout) findViewById(R.id.createBoard);
		createBoard.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				User user = WhyqUtils.isAuthenticated(getApplicationContext());
				if (user != null) {
					// Go to the Create Board screen.
					Intent i = new Intent(v.getContext(), CreateBoardActivity.class);
					View view = ImageActivityGroup.group.getLocalActivityManager().startActivity("CreateBoardActivity", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					ImageActivityGroup.group.replaceView(view);
				} else {
					// Go to login screen
//					Intent i = new Intent(v.getContext(), LoginPermActivity.class);
//					v.getContext().startActivity(i);
					WhyqMain.showLogin();
				}
				
			}
		});
		
		WhyqUtils.clearViewHistory();

	}

	public void showCamera(){
		try {
			FileOutputStream fos = openFileOutput("MyFile.jpg", Context.MODE_WORLD_WRITEABLE);
			fos.close();
			File f = new File(getFilesDir() + File.separator + "MyFile.jpg");
			if(f != null){
				imagePath = f.getPath();
				Uri uri = Uri.fromFile(f);
				if(imagePath != null && uri != null){
					startActivityForResult(
					        new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
					            .putExtra(MediaStore.EXTRA_OUTPUT, uri)
					        , CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
			}
			}
			catch(IOException e) {

			}
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK) {
	        if (requestCode == SELECT_PICTURE || requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ) {
	        	String selectedImagePath = "";
	        	if( data == null  ) {	        		
	        		selectedImagePath = ImageActivity.imagePath;
	        		try {
	        			InputStream is = openFileInput("MyFile.jpg");
	        			BitmapFactory.Options options = new BitmapFactory.Options();
	        			//options.inSampleSize = 4;
	        			Bitmap retrievedBitmap = BitmapFactory.decodeStream(is, null, options);
	        			}
	        			catch(IOException e) {

	        			}
	        	} else {
		            Uri selectedImageUri = data.getData();
		            
		            if( requestCode == SELECT_PICTURE ){
		            	selectedImagePath = getPath(selectedImageUri);
		            } else {

		            	selectedImagePath = ImageActivity.imagePath;
		            	
		            }

	        	}

	    	    Intent intent = new Intent();
	    	    intent.putExtra("path", selectedImagePath);
	    	    setResult(RESULT_OK,intent);
	    	    ImageActivity.imagePath = "";
	    	    finish();
	        }
	    }

	}
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{		
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
//	        WhyqMain.back();
	    	finish();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
