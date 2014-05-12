
package whyq.activity;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.adapter.BoardAdapter;
import whyq.interfaces.Get_Board_delegate;
import whyq.model.Comment;
import whyq.model.Store;
import whyq.model.Transporter;
import whyq.model.User;
import whyq.model.WhyqBoard;
import whyq.model.WhyqImage;
import whyq.utils.API;
import whyq.utils.Constants;
import whyq.utils.UrlImageViewHelper;
import whyq.utils.WhyqUtils;
import whyq.utils.XMLParser;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whyq.R;

public class ProfileActivity extends Activity implements Get_Board_delegate{
	
	private ImageView authorAvatar;
	private TextView authorName;
	private TextView friends;
	private TextView followings;
	private Button btnAccount;
	private Button btnFollow; 
	public User user;
	public static Comment commentData = null;
	public static boolean isUserProfile = true;
	public static int userfollowcount;
	public static int pinCount;
	public static int followerCount;
	
	public static int UPDATE_BUTTON = 1;
	public boolean isFirst=true;
//	public ProgressDialog loadingDialog;
	ProgressBar progressBar;
	public WhyqBoard board;
	public Context context;
	//private int selectedBoardId = -1;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(ListActivity.DOWNLOAD_COMPLETED)) {
				//Log.d("thien", "======>>>>??????");
				exeUserProfile();
			} 
		}
	};
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == UPDATE_BUTTON) {
				btnAccount.invalidate();
				btnFollow.invalidate();
			}
		}
	};
	private TextView tvHeader;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        progressBar = (ProgressBar)findViewById(R.id.progressBarProfile);
        TextView textView = (TextView)findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(), "ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if(textView != null) {
			textView.setTypeface(tf);
		}
		
		progressBar.setVisibility(View.VISIBLE);
//        showLoadingDialog("Loading", "Please wait...");
        authorAvatar = (ImageView) findViewById(R.id.authorAvatar);
        authorName = (TextView) findViewById(R.id.authorName);
        friends = (TextView) findViewById(R.id.friends);
        followings = (TextView) findViewById(R.id.followings);
        btnAccount = (Button) findViewById(R.id.btAccount);
        btnFollow = (Button) findViewById(R.id.btnFollow);
        tvHeader = (TextView)findViewById(R.id.tvHeaderTitle);
        tvHeader.setText("Profile");
		IntentFilter intentFilter = new IntentFilter(ListActivity.DOWNLOAD_COMPLETED);
		registerReceiver(receiver, intentFilter);
		context = ProfileActivity.this;
		WhyqApplication state = (WhyqApplication) context.getApplicationContext();
		user = state.getUser();
		initButtonStatus();
		btnAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				WhyqApplication state = (WhyqApplication) context.getApplicationContext();
				User user = state.getUser();
				String buttonType = btnAccount.getText().toString();
				if(buttonType.equals(context.getString(R.string.account))){
					Intent myIntent = new Intent(ProfileActivity.this, AccountActivity.class);
					View accountView = ProfileActivityGroup.group.getLocalActivityManager() .startActivity("AccountActivity", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					ProfileActivityGroup.group.replaceView(accountView);
					
					//View view = getLocalActivityManager().startActivity( "FollowerActivity", new Intent(this, FollowerActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
					
					/*PermUtils permUtils = new PermUtils();
					permUtils.logOutFacebook(getParent());
//					permUtils.saveTwitterAccess("twitter", "", getParent()):
					showLoadingDialog("Pregressing", "Please wait...");
					new exeFollow(API.logoutURL, false, true).execute(null);*/
				}
			}
		});
		btnFollow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WhyqApplication state = (WhyqApplication) context.getApplicationContext();
				User user = state.getUser();
				String buttonType = btnFollow.getText().toString();
				if(buttonType.equals(context.getString(R.string.follow))){
					if(user !=null){
//						showLoadingDialog("Pregressing", "Please wait...");
						progressBar.setVisibility(View.VISIBLE);
						new exeFollow(API.follow, true, false).execute(null,null);
						
					}else {
						WhyqMain.showLogin();
					}
					progressBar.setVisibility(View.VISIBLE);
//					showLoadingDialog("Pregressing", "Please wait...");
//					new exeFollow(API.follow, true, false).execute(null);
				}else if(buttonType.equals(context.getString(R.string.unfollow))){
					if(user !=null){
						progressBar.setVisibility(View.VISIBLE);
//						showLoadingDialog("Pregressing", "Please wait...");
						new exeFollow(API.follow, true, false).execute(null,null);
						
					}else {
						WhyqMain.showLogin();
					}
				}else if(buttonType.equals(context.getString(R.string.login))){
					WhyqMain.showLogin();
				}
			}
		});
		exeUserProfile();
		WhyqUtils.clearViewHistory();       
    }
	
    private void initButtonStatus() {
		// TODO Auto-generated method stub

    	if(commentData == null){
    		if(user !=null){
    			btnAccount.setText(context.getString(R.string.account));	
    		}else{
//    			btnAccount.setText(context.getString(R.string.login));
    		}
    	}else{
    		if(user == null){
    			btnAccount.setVisibility(View.INVISIBLE);
    			findViewById(R.id.imageBeforRefeshbtnNew2).setVisibility(View.INVISIBLE);
    		}else{
    			if(user.getId().equals(commentData.getUser().getId())){
    				btnAccount.setVisibility(View.VISIBLE);
    				findViewById(R.id.imageBeforRefeshbtnNew2).setVisibility(View.VISIBLE);
    				btnFollow.setVisibility(View.GONE);
    			}else{
    				btnAccount.setVisibility(View.INVISIBLE);
    				findViewById(R.id.imageBeforRefeshbtnNew2).setVisibility(View.INVISIBLE);
    				btnFollow.setVisibility(View.VISIBLE);
    			}
    		}
    		
    	}

	}

	protected void exeUserProfile() {
		// TODO Auto-generated method stub
		if( commentData != null) {
			if(commentData.getUser() != null) {
				new getUserProfile(API.getProfileURL+commentData.getUser().getId()).execute(null,null);
			} else {
				new getUserProfile(API.getProfileURL+commentData.getId()).execute(null,null);
			}
		}
	}
    
    @Override
    protected void onResume(){
    	super.onResume();
    	
    	 /** Load the information from Application (user info) when the page is loaded. */
//    	if(isFirst)
    	{
    	  	execGetUserProfile();
        	isFirst = false;  		
    	}
  
    }
    public void execGetUserProfile(){

    	if(isUserProfile){
    		
    		user = WhyqUtils.isAuthenticated(getApplicationContext());
    		if(user != null){
    			btnAccount.setText(context.getString(R.string.account));
    			ArrayList<WhyqBoard> boards = (ArrayList<WhyqBoard>) user.getBoards();
            	BoardAdapter boardAdapter = new BoardAdapter(ProfileActivity.this,R.layout.board_item, boards);
            	exeGet(boardAdapter);
            	btnAccount.setVisibility(View.VISIBLE);
            	findViewById(R.id.imageBeforRefeshbtnNew2).setVisibility(View.VISIBLE);
            	btnAccount.invalidate();
    		}else{
//    			btnAccount.setText(context.getString(R.string.login));
    			btnAccount.invalidate();
    			WhyqMain.showLogin();
    		}
    		btnFollow.setVisibility(View.GONE);
//    		dismissLoadingDialog();
    		progressBar.setVisibility(View.INVISIBLE);
    	}else{
//    		btnFollow.setVisibility(View.VISIBLE);
//    		btnAccount.setVisibility(View.INVISIBLE);
    		initButtonStatus();
    		if(commentData != null){
    			if(commentData.getUser() != null) {
    				if(commentData.getUser().getId() != null){
//    		    		showLoadingDialog("Loading", "Please wait");
    		    			new getUserProfile(API.getProfileURL+commentData.getUser().getId()).execute(null,null);
    					
    				}
    			} else {
    				new getUserProfile(API.getProfileURL+commentData.getId()).execute(null,null);
    			}
    		}
    	}
    }
 
    public void exeGet( BoardAdapter boardAdapter){
    	try {
    	   
//            if (user != null) {
            	if(commentData == null) {
            		// The author name
            		if(user != null){
                    	String name = user.getName();
                        authorName.setText(name);
                        
                        // The author avatar
                    	WhyqImage avatar = user.getAvatar();
                        UrlImageViewHelper.setUrlDrawable(authorAvatar, avatar.getUrl());
                        
                        // The number of friends
                        friends.setText(String.valueOf(user.getPin() + ProfileActivity.this.getString(R.string.perm) + " " + user.getFriends()) + " "+ ProfileActivity.this.getString(R.string.followers));

            		}
            	} else {
	                authorName.setText(commentData.getUser().getName());
	                WhyqImage avatar = commentData.getUser().getAvatar();
	                UrlImageViewHelper.setUrlDrawable(authorAvatar, avatar.getUrl());
	                friends.setText(String.valueOf(ProfileActivity.this.getString(R.string.perm) + " " + ProfileActivity.pinCount + " " + ProfileActivity.this.getString(R.string.followers) + " " + ProfileActivity.followerCount));
            	}
                
                // The number of followings
//                followings.setText(String.valueOf(user.getFollowings() + " followings"));
                
                // Build the list of user's boards
                ListView userBoards = (ListView) findViewById(R.id.userBoards);
                userBoards.setAdapter(boardAdapter);
                userBoards.setOnItemClickListener(new BoardClickListener());
//            } else {
//            	PermpingMain.showLogin();
//            }        
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    class exeFollow extends AsyncTask<String, Void, Boolean> {

		private String filePath = "";
		public  String title = "";
		public boolean isSuccess =false;
		public boolean isFollow;
		public boolean isLogout;
		public exeFollow(String filePath, boolean isFollow, boolean isLogout) {
			this.filePath = filePath;
			this.isFollow = isFollow;
			this.isLogout = isLogout;
		}
		
		@Override
		protected Boolean doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {

				isSuccess = (Boolean)executeMultipartPost( filePath, isFollow, isLogout);
				return isSuccess;
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {
			

		}

		@Override
		protected void onPostExecute(Boolean result) {
//			dismissLoadingDialog();
			progressBar.setVisibility(View.INVISIBLE);
			if(result != null){
				if(result.booleanValue() && btnAccount.getText().equals(context.getString(R.string.logout))){
					WhyqApplication state = (WhyqApplication)context.getApplicationContext();
					state.setUser(null);
					XMLParser.storeUserAccount(context, null);
					WhyqMain.back();
					WhyqMain.showLogin();
//					btnAccount.setText(context.getString(R.string.login));
					btnAccount.invalidate();
				}
				else if(result.booleanValue() && btnFollow.getText().equals(context.getString(R.string.follow))) {
					btnFollow.setText(context.getString(R.string.unfollow));
					btnFollow.invalidate();
					ProfileActivity.followerCount--;
					friends.setText(String.valueOf(ProfileActivity.this.getString(R.string.perm) + " " + ProfileActivity.pinCount + " " + ProfileActivity.this.getString(R.string.followers) + " " + ProfileActivity.followerCount));
					Message message = handler.obtainMessage(UPDATE_BUTTON, "");
					handler.sendMessage(message);
				} else if(result.booleanValue() && btnFollow.getText().equals(context.getString(R.string.unfollow))) {
					btnFollow.setText(context.getString(R.string.follow));
					btnFollow.invalidate();
					ProfileActivity.followerCount++;
					friends.setText(String.valueOf(ProfileActivity.this.getString(R.string.perm) + " " + ProfileActivity.pinCount + " " + ProfileActivity.this.getString(R.string.followers) + " " + ProfileActivity.followerCount));
					Message message = handler.obtainMessage(UPDATE_BUTTON, "");
					handler.sendMessage(message);
				}
			}

		}

	}

    
	class getUserProfile extends AsyncTask<ArrayList<WhyqBoard>, Void, ArrayList<WhyqBoard>> {

		private String filePath = "";
		public  String title = "";
		public getUserProfile(String filePath) {
			this.filePath = filePath;
		}

		@Override
		protected ArrayList<WhyqBoard> doInBackground(
				ArrayList<WhyqBoard>... arg0) {
			// TODO Auto-generated method stub
			ArrayList<WhyqBoard> boards = null;
			try {

				boards = (ArrayList<WhyqBoard>)executeMultipartPost( filePath, false, false);
				return boards;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}


		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(ArrayList<WhyqBoard> boards) {
			if(ProfileActivity.userfollowcount <= 0) {
				btnFollow.setText(context.getString(R.string.follow));
				btnFollow.invalidate();
			} else {
				btnFollow.setText(context.getString(R.string.unfollow));
				btnFollow.invalidate();
			}
//			if(loadingDialog != null)
//			if(loadingDialog.isShowing()){
//				dismissLoadingDialog();
//				//PermpingMain.showLogin();				
//			}
			progressBar.setVisibility(View.INVISIBLE);
			
			if(boards != null){
				//Log.d("tttttt","OOOOOOO=======>>>>>"+boards);
	    		user = WhyqUtils.isAuthenticated(getApplicationContext());
	            BoardAdapter boardAdapter = new BoardAdapter(ProfileActivity.this,R.layout.board_item, boards);
	    		exeGet(boardAdapter);
			}
		} 
	}

	public Object executeMultipartPost(String filePath, boolean isFollow, boolean isLogout) throws Exception {
		Object boards = null ;
		try {

			WhyqApplication state = (WhyqApplication) getApplicationContext();
			User user = state.getUser();

//			if (user != null) 
			{

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = null;
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				if (filePath != null && !"".equals(filePath)) {
					postRequest = new HttpPost(filePath);
					if(isFollow){
						reqEntity.addPart("fuid", new StringBody(user.getId()));
						reqEntity.addPart("tuid", new StringBody(commentData.getUser().getId()));
						postRequest.setEntity(reqEntity);
					}else{
						if(isLogout){
							filePath = filePath+user.getId();
						}else{
							if(user != null){
								reqEntity.addPart("loggedinuid", new StringBody(user.getId()));
								postRequest.setEntity(reqEntity);
							}else{
								reqEntity.addPart("loggedinuid", new StringBody(commentData.getUser().getId()));
								postRequest.setEntity(reqEntity);
							}

						}

					}
								
					HttpResponse response = httpClient.execute(postRequest);
					HttpEntity entry = response.getEntity();
					String readFile = EntityUtils.toString(entry);
					if(isFollow){
						boards = parseXmlFollowFile(readFile);
					}else if(isLogout){
						boards = parseXmlFollowFile(readFile);
					}else{
						boards = parseXmlFile(readFile);
					}
				} 

				
			}
		} catch (Exception e) {
			return null;
		}
		return boards;
		// Toast.makeText(getApplicationContext(),"Please login first!",Toast.LENGTH_LONG).show();
	}
	boolean parseXmlFollowFile(String xmlFile) {
		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlFile));
			if (is != null)
				doc = db.parse(is);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("response");

			/** Assign textview array lenght by arraylist size */
			int length = nodeList.getLength();

			for (int i = 0; i < length; i++) {

				Node node = nodeList.item(i);
				Element fstElmnt = (Element) node;
				NodeList nameList = fstElmnt
						.getElementsByTagName("errorcode");
				Element nameElement = null;
				if (nameList != null) {
					nameElement = (Element) nameList.item(0);
					nameList = nameElement.getChildNodes();
					String status = ((Node) nameList.item(0))
							.getNodeValue();
					if (Integer.valueOf(status) == 200)
						return true;
				}
			
			}

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return false;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: "
					+ e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return false;
		}

		return false;
	}

	ArrayList<WhyqBoard> parseXmlFile(String xmlFile) {
		Document doc = null;
		ArrayList<WhyqBoard> boards = new ArrayList<WhyqBoard>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlFile));
			if (is != null)
				doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nodeList2 = doc.getElementsByTagName("userfollowcount");
			if(nodeList2 != null){
				if(nodeList2.item(0) != null){
					if(nodeList2.item(0).getChildNodes() != null){
						if(nodeList2.item(0).getChildNodes().item(0) != null){
							ProfileActivity.userfollowcount =  Integer.valueOf(nodeList2.item(0).getChildNodes().item(0).getNodeValue());
						}
					}
				}
			}
			
			NodeList nodeList3 = doc.getElementsByTagName("followerCount");
			if(nodeList3 != null){
				if(nodeList3.item(0) != null){
					if(nodeList3.item(0).getChildNodes() != null){
						if(nodeList3.item(0).getChildNodes().item(0) != null){
							ProfileActivity.followerCount =  Integer.valueOf(nodeList3.item(0).getChildNodes().item(0).getNodeValue());
						}
					}
				}
			}
			
			NodeList nodeList4 = doc.getElementsByTagName("pinCount");
			if(nodeList4 != null){
				if(nodeList4.item(0) != null){
					if(nodeList4.item(0).getChildNodes() != null){
						if(nodeList4.item(0).getChildNodes().item(0) != null){
							ProfileActivity.pinCount =  Integer.valueOf(nodeList4.item(0).getChildNodes().item(0).getNodeValue());
						}
					}
				}
			}
			
			NodeList nodeList = doc.getElementsByTagName("item");

			/** Assign textview array lenght by arraylist size */
			int length = nodeList.getLength();

			for (int i = 0; i < length; i++) {

				Node node = nodeList.item(i);
				
				Element fstElmnt = (Element) node;
				
				NodeList id = fstElmnt.getElementsByTagName("id");
				Element idElement = (Element) id.item(0);
				id = idElement.getChildNodes();
				String permId = ((Node) id.item(0)).getNodeValue();

				NodeList name = fstElmnt.getElementsByTagName("name");
				Element nameElement = (Element) name.item(0);
				name = nameElement.getChildNodes();
				String permName = ((Node) name.item(0)).getNodeValue();
//
//				NodeList description = fstElmnt.getElementsByTagName("description");
//				Element descriptionElement = (Element) description.item(0);
//				description = descriptionElement.getChildNodes();
//				String permDescriptionn = ((Node) description.item(0)).getNodeValue();
				String permDescriptionn = "";
				
				NodeList followers = fstElmnt.getElementsByTagName("followers");
				Element followersElement = (Element) followers.item(0);
				followers = followersElement.getChildNodes();
				String permFollowers = ((Node) followers.item(0)).getNodeValue();
				
				NodeList pin = fstElmnt.getElementsByTagName("pins");
				Element pinElement = (Element) pin.item(0);
				pin = pinElement.getChildNodes();
				String permPin = ((Node) pin.item(0)).getNodeValue();
				
				WhyqBoard whyqBoard = new WhyqBoard(permId, permName, permDescriptionn, Integer.valueOf(permFollowers), Integer.valueOf(permPin));
				if(whyqBoard != null)
					boards.add(whyqBoard);
			}

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: "
					+ e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return boards;
	}

  
    private class BoardClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			board = (WhyqBoard) parent.getItemAtPosition(pos);
			//BoardController boardController = new BoardController();
			//List<Perm> perms = boardController.getPermsByBoardId(board.getId(), ProfileActivity.this);			
			
			Intent myIntent = new Intent(view.getContext(), ListActivity.class);
			String boardUrl = API.permListFromBoardUrl + board.getId();
			myIntent.putExtra("categoryURL", boardUrl);
			View boardListView = ProfileActivityGroup.group.getLocalActivityManager() .startActivity("BoardListActivity", myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
			ProfileActivityGroup.group.replaceView(boardListView);
	
		}

    	
    }
//	private void showLoadingDialog(String title, String msg) {
//		loadingDialog = new ProgressDialog(getParent());
//		loadingDialog.setMessage(msg);
//		loadingDialog.setTitle(title);
//		loadingDialog.setCancelable(true );
//		this.loadingDialog.show();
//	}
//	private void dismissLoadingDialog() {
//		if (loadingDialog != null && loadingDialog.isShowing())
//			loadingDialog.dismiss();
//	}
	@Override
	public void onSuccess(ArrayList<Store> stores) {
		// TODO Auto-generated method stub
		
		Transporter transporter = new Transporter();
		transporter.setPerms(stores);
		transporter.setBoardName(board.getName());
		
		// Go to the Board Detail screen
		Intent i = new Intent(context, BoardDetailActivity.class);
		i.putExtra(Constants.TRANSPORTER, transporter);
		View boardDetail = ProfileActivityGroup.group.getLocalActivityManager() .startActivity("BoardDetailActivity", i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView();
		ProfileActivityGroup.group.replaceView(boardDetail);
	}
	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
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
