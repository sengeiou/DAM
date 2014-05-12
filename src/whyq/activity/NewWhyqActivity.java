package whyq.activity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import twitter4j.http.AccessToken;
import whyq.WhyqApplication;
import whyq.WhyqMain;
import whyq.adapter.BoardSpinnerAdapter;
import whyq.model.Category;
import whyq.model.User;
import whyq.model.WhyqBoard;
import whyq.utils.API;
import whyq.utils.KakaoLink;
import whyq.utils.WhyqUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.whyq.R;

public class NewWhyqActivity extends Activity implements OnClickListener {

	public static List<WhyqBoard> boardList = new ArrayList<WhyqBoard>();
	private String boardIdRe = "";
	private String boardDescRe = "";
	private String permIdRe = "";
	private String userIdRe = "";
	private String imagePath = "";
	private int boardId = -1;
	private String permAndroidLink = "";
	private String permIphoneLink = "";
	public static int LOGIN_FACEBOOK = 1;
	public static int MESSAGE_LOGIN_FACEBOOK_ERROR = 100;
	public static int LOGIN_TWITTER = 2;
	public static int SHARE_FB = 1;
	public static int SHARE_TWITTER = 2;
	public int type = 0;
	private int permID = -1;
	private ToggleButton btnShareFacebook;
	private ToggleButton btnShareTwitter;
	private ToggleButton btnShareKakao;
//	private ToggleButton btnLocation;
	private ToggleButton btnRecordAudio;
	private EditText permDesc;
	private String facebookToken;
	private AccessToken twitterAccessToken;
	private double lat = 0.0;
	private double lon = 0.0;
	private WhyqUtils whyqUtils;
	// private LocationManager mlocManager;
	// private LocationListener mlocListener;
	private ArrayList<Category> categories;
	private String permId;
	private List<WhyqBoard> boards;
	private LinearLayout btnCatilogy;
	private ImageView rightArrow;
	// private ProgressDialog loadingDialog;
	ProgressBar progressBar;
	Button btnOk;
	private Context context;
	public static boolean isReperm = false;
	private boolean uploadStatus = false;
	String pathAudioFile;
	boolean isGetRecord = false;

	private LocationManager locManager;
	private LocationListener locListener;
	private boolean gps_enabled = false;
	private boolean network_enabled = false;
	public Handler handleFbLogin = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == LOGIN_FACEBOOK) {

				// showLoadingDialog("Processing", "Please wait...");
				// new LoadBoards().execute();
				// new ImageUpload(imagePath).execute();
				btnShareFacebook.setChecked(true);
			}

			if (msg.what == MESSAGE_LOGIN_FACEBOOK_ERROR) {
				btnShareFacebook.setChecked(false);
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.new_perm_layout);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = NewWhyqActivity.this;
		// Remove notification bar
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.new_perm_layout);

		TextView textView = (TextView) findViewById(R.id.permpingTitle);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"ufonts.com_franklin-gothic-demi-cond-2.ttf");
		if (textView != null) {
			textView.setTypeface(tf);
		}

		// initGetLocation();
		progressBar = (ProgressBar) findViewById(R.id.progressBar2);
		btnShareFacebook = (ToggleButton) findViewById(R.id.share_facebookr);
		btnShareTwitter = (ToggleButton) findViewById(R.id.share_twitter);
		btnShareKakao = (ToggleButton) findViewById(R.id.share_kakao);
//		btnLocation = (ToggleButton) findViewById(R.id.location);
		btnRecordAudio = (ToggleButton) findViewById(R.id.btnRecordAudio);
		btnCatilogy = (LinearLayout) findViewById(R.id.categoryItemLayout1);
		rightArrow = (ImageView) findViewById(R.id.rightArrow);
		rightArrow.setOnClickListener(this);
		btnShareFacebook.setOnClickListener(this);
		btnShareTwitter.setOnClickListener(this);
		btnShareKakao.setOnClickListener(this);
//		btnLocation.setOnClickListener(this);
		btnCatilogy.setOnClickListener(this);
		btnRecordAudio.setOnClickListener(this);
		whyqUtils = new WhyqUtils();
		final Button buttonCANCEL = (Button) findViewById(R.id.buttonCANCEL);
		buttonCANCEL.setOnClickListener(this);
		btnOk = (Button) findViewById(R.id.buttonOK);
		btnOk.setOnClickListener(this);
		permDesc = (EditText) findViewById(R.id.permDesc);
		initToggleStatus();

		// mlocManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// mlocListener = new MyLocationListener();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.get("reperm") != null) {
				isReperm = true;
				boardIdRe = (String) extras.getString("boardId");
				boardDescRe = (String) extras.getString("boardDesc");
				permIdRe = (String) extras.getString("permId");
				userIdRe = (String) extras.getString("userId");
				initValue();
				new LoadBoards().execute();
			} else {
				this.imagePath = (String) extras.get("imagePath");
				if (extras.get("permID") != null)
					this.permID = Integer.parseInt((String) extras
							.get("permID"));
				new LoadBoards().execute();
			}

		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (WhyqMain.isKakao) {
			WhyqMain.isKakao = false;
			finish();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (locManager != null && locListener != null)
			locManager.removeUpdates(locListener);
	}

	private void initValue() {
		// TODO Auto-generated method stub
		if (boardDescRe != null)
			permDesc.setText(this.boardDescRe);
		// btnShareFacebook.setEnabled(false);
		// btnShareTwitter.setEnabled(false);
		// btnShareKakao.setEnabled(false);
	}

//	public void initGetLocation() {
//		locManager = (LocationManager) context
//				.getSystemService(Context.LOCATION_SERVICE);
//		locListener = new MyLocationListener();
//		try {
//			gps_enabled = locManager
//					.isProviderEnabled(LocationManager.GPS_PROVIDER);
//		} catch (Exception ex) {
//		}
//
//		try {
//			network_enabled = locManager
//					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//		} catch (Exception ex) {
//		}
//
//		// don't start listeners if no provider is enabled
//
//		if (!gps_enabled & !network_enabled) {
//
//			AlertDialog.Builder builder = new Builder(context);
//			builder.setTitle("Attention!");
//			builder.setMessage("Sorry, location is not determined. Please enable location providers");
//			builder.create().show();
//
//		}
//
//		if (gps_enabled) {
//			locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//					0, locListener);
//		}
//
//		if (network_enabled) {
//			locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//					0, 0, locListener);
//		}
//	}

	private void initToggleStatus() {
		// TODO Auto-generated method stub
		facebookToken = whyqUtils.getFacebookToken(getApplicationContext());
		twitterAccessToken = whyqUtils.getTwitterAccess(NewWhyqActivity.this);
		if (facebookToken == null || facebookToken == "") {// &&
															// facebookToken.isEmpty()
			btnShareFacebook.setChecked(false);
		} else {
			btnShareFacebook.setChecked(true);
		}
		if (twitterAccessToken == null) {
			btnShareTwitter.setChecked(false);

		} else {
			btnShareTwitter.setChecked(true);
		}
		if (isGetRecord) {
			btnRecordAudio.setChecked(true);
		} else {
			btnRecordAudio.setChecked(false);
		}
		// if (btnLocation.isChecked()) {
		// mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		// 0, mlocListener);
		// } else {
		// lon = 0;
		// lat = 0;
		// }
	}

	public void onBackPressed() {
		super.onBackPressed();
	}

	/*
	 * (non-Javadoc)
	 * 
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

			/*
			 * Log.d("CreatePerm", "Touch event " + event.getRawX() + "," +
			 * event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() +
			 * "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() +
			 * " coords " + scrcoords[0] + "," + scrcoords[1]);
			 */
			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
			}
		}
		return ret;
	}

	public void setSpinnerData() {
		Spinner boardSelect = (Spinner) findViewById(R.id.boardSpinnerNewPerm);
		addItemsOnMainCategory(boardSelect, boards);
		boardSelect
				.setOnItemSelectedListener(new CategorySpinnerSelectedListener());
	}

	private void addItemsOnMainCategory(Spinner spinner, List<WhyqBoard> boards2) {
		BoardSpinnerAdapter boardSpinnerAdapter = new BoardSpinnerAdapter(this,
				boards2);
		spinner.setAdapter(boardSpinnerAdapter);
		WhyqBoard initial = (WhyqBoard) boardSpinnerAdapter.getItem(0);
		if (initial != null)
			boardId = Integer.parseInt(initial.getId());
	}

	private class CategorySpinnerSelectedListener implements
			OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			WhyqBoard board = (WhyqBoard) parent.getItemAtPosition(pos);
			boardId = Integer.parseInt(board.getId());
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}

	class LoadBoards extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// CategoryController catController = new CategoryController();
			// categories = catController.getCategoryList();
			// BoardController boardController = new BoardController();
			User user = WhyqUtils.isAuthenticated(getApplicationContext());
			if (user != null)
				// boards = boardController.getBoardList("121");
				boards = (ArrayList<WhyqBoard>) user.getBoards();
			else {
				// User has no boards created
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {

			setSpinnerData();
			dismissLoadingDialog();

		}
	}

	// AsyncTask task for upload file

	class ImageUpload extends AsyncTask<Void, Void, String> {

		String filePath = "";

		public ImageUpload(String filePath) {
			this.filePath = filePath;
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				executeMultipartPost();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		public void executeMultipartPost() throws Exception {
			try {

				WhyqApplication state = (WhyqApplication) getApplicationContext();
				User user = state.getUser();

				if (user != null) {

					HttpClient httpClient = new DefaultHttpClient();
					// HttpPost postRequest = new
					// HttpPost("http://10.0.2.2/perm/testupload.php");
					HttpPost postRequest = null;
					Charset chars = Charset.forName("UTF-8");
					facebookToken = whyqUtils
							.getFacebookToken(NewWhyqActivity.this);
					twitterAccessToken = whyqUtils
							.getTwitterAccess(NewWhyqActivity.this);

					MultipartEntity reqEntity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE, null,
							Charset.forName(HTTP.UTF_8));
					if (!isReperm && filePath != null && !"".equals(filePath)) {
						postRequest = new HttpPost(API.addNewPermUrl);

						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						// Bitmap bm = BitmapFactory.decodeFile(filePath);
						Bitmap bm = getBitmap2(filePath);
						bm.compress(CompressFormat.JPEG, 75, bos);
						byte[] data = bos.toByteArray();

						String fileName = new File(filePath).getName();
						ByteArrayBody bab = new ByteArrayBody(data, fileName);
						reqEntity.addPart("img", bab);
						reqEntity.addPart("photoCaption", new StringBody(
								fileName, chars));

						// Upload audio to server
						if (btnRecordAudio.isChecked()) {
							// ByteArrayOutputStream dos = new
							// ByteArrayOutputStream();
							// FileInputStream fileInputStream = null;
							byte[] buffer;
							// int maxBufferSize = 20 * 1024;
							// fileInputStream = new FileInputStream(new
							// File(pathAudioFile));
							// // create a buffer of maximum size
							// buffer = new byte[Math.min((int)
							// pathAudioFile.length(), maxBufferSize)];
							// int length;
							// // read file and write it into form...
							// while ((length = fileInputStream.read(buffer)) !=
							// -1) {
							// dos.write(buffer, 0, length);
							// }
							buffer = getByArray(pathAudioFile);
							ByteArrayBody baba = new ByteArrayBody(buffer,
									new File(pathAudioFile).getName());
							reqEntity.addPart("audio", baba);

						}

						reqEntity.addPart("uid", new StringBody(user.getId(),
								chars));
						reqEntity.addPart("board",
								new StringBody(String.valueOf(boardId), chars));
						reqEntity.addPart("board_desc", new StringBody(permDesc
								.getText().toString(), chars));
					} else if (isReperm) { // Reperm
						postRequest = new HttpPost(API.repermUrl);

						reqEntity
								.addPart(
										"pid",
										new StringBody(
												String.valueOf(permIdRe), chars));
						reqEntity
								.addPart(
										"uid",
										new StringBody(
												String.valueOf(userIdRe), chars));
						reqEntity.addPart("board",
								new StringBody(String.valueOf(boardId), chars));
						reqEntity.addPart("board_desc", new StringBody(permDesc
								.getText().toString(), chars));
					}
					String type = "";
					if (facebookToken != null && btnShareFacebook.isChecked()) {
						reqEntity.addPart("fb_oauth_token", new StringBody(
								facebookToken, chars));
						type = "facebook";
					}
					if (twitterAccessToken != null
							&& btnShareTwitter.isChecked()) {
						reqEntity.addPart("tw_oauth_token", new StringBody(
								twitterAccessToken.getToken(), chars));
						reqEntity.addPart(
								"tw_oauth_token_secret",
								new StringBody(twitterAccessToken
										.getTokenSecret(), chars));
						type = "twitter";
					}
					if (facebookToken != null && twitterAccessToken != null) {
						type = "all";
					}
					reqEntity.addPart("type", new StringBody("" + type, chars));
//					if (btnLocation.isChecked()) {
//						reqEntity.addPart("lat",
//								new StringBody("" + lat, chars));
//						reqEntity.addPart("long", new StringBody("" + lon,
//								chars));
//					} else {
//						reqEntity.addPart("lat", new StringBody("" + 0, chars));
//						reqEntity
//								.addPart("long", new StringBody("" + 0, chars));
//					}
					/*
					 * Log.d("======>", "======Lat, lon==========" + lat + "==="
					 * + lon);
					 */
					postRequest.setEntity(reqEntity);
					HttpResponse response = httpClient.execute(postRequest);
					HttpEntity entry = response.getEntity();
					String readFile = EntityUtils.toString(entry);

					parseXmlFile(readFile);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// Toast.makeText(getApplicationContext(),"Please login first!",Toast.LENGTH_LONG).show();
		}

		private Bitmap checkBitmapSize(Bitmap bm) {
			// TODO Auto-generated method stub
			Bitmap bmResult = null;
			return bmResult;
		}

		private Bitmap getBitmap(String path) {
			try {
				final int IMAGE_MAX_SIZE = 1200000; // 1.2MP
				// Decode image size
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				int scale = 1;
				while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
					scale++;
				}
				/*
				 * Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth
				 * + ", orig-height: " + o.outHeight);
				 */

				Bitmap b = null;
				if (scale > 1) {
					scale--;
					// scale to max possible inSampleSize that still yields an
					// image
					// larger than target
					o = new BitmapFactory.Options();
					o.inSampleSize = scale;
					b = BitmapFactory.decodeFile(path);

					// resize to desired dimensions
					int height = b.getHeight();
					int width = b.getWidth();
					/*
					 * Log.d("", "1th scale operation dimenions - width: " +
					 * width + ", height: " + height);
					 */

					double y = Math.sqrt(IMAGE_MAX_SIZE
							/ (((double) width) / height));
					double x = (y / height) * width;

					Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
							(int) y, true);
					b.recycle();
					b = scaledBitmap;

					System.gc();
				} else {
					b = BitmapFactory.decodeFile(path);
				}

				/*
				 * Log.d("", "bitmap size - width: " + b.getWidth() +
				 * ", height: " + b.getHeight() + "");
				 */
				return b;
			} catch (Exception e) {
				// Log.e("", e.getMessage(), e);
				return null;
			}
		}

		private Bitmap getBitmap2(String path) {
			try {

				final int IMAGE_MAX_SIZE = 1024;// 1200000; // 1.2MP
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				InputStream in = new FileInputStream(filePath);
				BitmapFactory.decodeStream(in, null, o);
				in.close();
				in = new FileInputStream(filePath);

				// Decode image size

				double scale = 1;
				int currentWidth = o.outWidth;
				if (currentWidth > IMAGE_MAX_SIZE) {
					scale = currentWidth / IMAGE_MAX_SIZE;

				}
				// int scale = 1;
				// while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2))
				// > IMAGE_MAX_SIZE) {
				// scale++;
				// }
				/*
				 * Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth
				 * + ", orig-height: " + o.outHeight);
				 */

				Bitmap b = null;
				if (scale > 1) {

					double y = (double) o.outHeight / scale;
					double x = 1024;
					b = BitmapFactory.decodeFile(path);
					Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
							(int) y, true);
					b = scaledBitmap;
					System.gc();
					// Scale without out of memory
					// scale--;
					// o = new BitmapFactory.Options();
					// o.inSampleSize = scale;
					// b = BitmapFactory.decodeFile(path);
					//
					// // resize to desired dimensions
					// int height = b.getHeight();
					// int width = b.getWidth();
					// Log.d("", "1th scale operation dimenions - width: " +
					// width
					// + ", height: " + height);
					//
					// double y = Math.sqrt(IMAGE_MAX_SIZE
					// / (((double) width) / height));
					// double x = (y / height) * width;
					//
					// Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int)
					// x,
					// (int) y, true);
					// b.recycle();
					// b = scaledBitmap;
					//
					// System.gc();
				} else {
					b = BitmapFactory.decodeFile(path);
				}

				/*
				 * Log.d("", "bitmap size - width: " + b.getWidth() +
				 * ", height: " + b.getHeight() + "");
				 */
				return b;
			} catch (Exception e) {
				// Log.e("", e.getMessage(), e);
				return null;
			}
		}

		boolean parseXmlFile(String xmlFile) {
			Document doc = null;

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {

				DocumentBuilder db = dbf.newDocumentBuilder();

				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(xmlFile));
				if (is != null)
					doc = db.parse(is);
				doc.getDocumentElement().normalize();

				NodeList nodeList = doc.getElementsByTagName("respond");

				/** Assign textview array lenght by arraylist size */
				int length = nodeList.getLength();

				for (int i = 0; i < length; i++) {

					Node node = nodeList.item(i);
					Element fstElmnt = (Element) node;

					NodeList nameListLink = fstElmnt
							.getElementsByTagName("permAndroidLink");
					Element nameElementLink = null;
					if (nameListLink != null) {
						nameElementLink = (Element) nameListLink.item(0);
						nameListLink = nameElementLink.getChildNodes();
						permAndroidLink = ((Node) nameListLink.item(0))
								.getNodeValue();

					}
					NodeList nameListLinkIphone = fstElmnt
							.getElementsByTagName("permIphoneLink");
					Element nameElementLinkIphone = null;
					if (nameListLinkIphone != null) {
						nameElementLinkIphone = (Element) nameListLinkIphone
								.item(0);
						nameListLinkIphone = nameElementLinkIphone
								.getChildNodes();
						permIphoneLink = ((Node) nameListLinkIphone.item(0))
								.getNodeValue();

					}
					NodeList nameListId = fstElmnt
							.getElementsByTagName("permId");
					Element nameElementId = null;
					if (nameListId != null) {
						nameElementId = (Element) nameListId.item(0);
						nameListId = nameElementId.getChildNodes();
						permId = ((Node) nameListId.item(0)).getNodeValue();

					}
					NodeList nameList = fstElmnt
							.getElementsByTagName("errorcode");
					Element nameElement = null;
					if (nameList != null) {
						nameElement = (Element) nameList.item(0);
						if (nameElement != null) {
							nameList = nameElement.getChildNodes();
							String status = ((Node) nameList.item(0))
									.getNodeValue();
							if (status.equals("200")) {
								uploadStatus = true;
								return true;
							}
						}
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

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {

			if (progressBar.getVisibility() == View.VISIBLE) {
				dismissLoadingDialog();
				// ImageActivityGroup.group.back();
				if (btnShareKakao.isChecked()) {
					if (isReperm) {
						Toast.makeText(getApplicationContext(),
								"Re-Permed  \n Let share to Kakao app!",
								Toast.LENGTH_LONG).show();
						isReperm = false;
					} else {
						Toast.makeText(getApplicationContext(),
								"Uploaded new perm \nLet share on Kakao app!",
								Toast.LENGTH_LONG).show();
					}
					if (uploadStatus && btnShareKakao.isChecked()) {// uploadStatus
						// &&
						try {
							finish();
							gotoKakao();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						finish();
					}
				} else {
					if (isReperm) {
						Toast.makeText(getApplicationContext(), "Re-Permed!",
								Toast.LENGTH_LONG).show();
						isReperm = false;
//						ImageActivityGroup.uploaded = true;
//						finish();
					} else {
						// Logger.appendLog("\n== NewPermActivity Uploaded new perm successfully",
						// "NewPermActivity");
						Toast.makeText(getApplicationContext(),
								"Uploaded new perm!", Toast.LENGTH_LONG).show();
					}
					ImageActivityGroup.uploaded = true;
					finish();
				}

			}

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dismissLoadingDialog();
		if (requestCode == 2) {
			Bundle bundle = data.getExtras();
			pathAudioFile = bundle.getString("pathFile");
			// Log.d("aaaa", "" + pathAudioFile);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		switch (id) {
		case R.id.buttonCANCEL:
			// ImageActivityGroup.group.back();
			finish();
			break;
		case R.id.buttonOK:
			uploadPerm();
			break;
		case R.id.share_facebookr:
			shareFb();
			break;
		case R.id.share_twitter:
			shareTwitter();
			break;
		case R.id.share_kakao:
			shareKakao();
			break;
//		case R.id.location:
//			locationChange();
//			break;
		case R.id.btnRecordAudio:
			audioOnChange();
			break;
		case R.id.rightArrow:
			// new LoadBoards().execute();
			new CategorySpinnerSelectedListener();
			break;
		default:
			break;
		}
	}

	private byte[] getByArray(String fileName) {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;
		String pathToOurFile = fileName;
		String urlServer = API.addNewPermUrl;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 512 * 512;

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(
					pathToOurFile));

			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
							+ pathToOurFile + "\"" + lineEnd);
			outputStream.writeBytes(lineEnd);

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// Read file
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			return buffer;
		} catch (Exception ex) {
			// Exception handling
			return null;
		}
	}

	private void audioOnChange() {
		// TODO Auto-generated method stub
		if (btnRecordAudio.isChecked()) {
			isGetRecord = true;
			Intent recordIntent = new Intent(NewWhyqActivity.this,
					RecorderActivity.class);
			startActivityForResult(recordIntent, 2);
		} else {
			isGetRecord = false;
		}

	}

	private void locationChange() {
		// TODO Auto-generated method stub
		// if (btnLocation.isChecked()) {
		//
//		if (btnLocation.isChecked()) {
//			initGetLocation();
//		} else {
//			locManager.removeUpdates(locListener);
//		}

	}

	private void shareKakao() {
		// TODO Auto-generated method stub
		if (btnShareKakao.isChecked())
			btnShareKakao.setChecked(true);
		else
			btnShareKakao.setChecked(false);
	}

	private void shareTwitter() {
		// TODO Auto-generated method stub
		if (btnShareTwitter.isChecked()) {
			twitterAccessToken = whyqUtils
					.getTwitterAccess(getApplicationContext());
			if (twitterAccessToken == null) {
				Intent i = new Intent(context,
						PrepareRequestTokenActivity.class);
				context.startActivity(i);

			} else {

			}
		} else {
			twitterAccessToken = null;
		}

	}

	private void shareFb() {
		if (btnShareFacebook.isChecked()) {
			facebookToken = whyqUtils.getFacebookToken(getApplicationContext());
			if (facebookToken == null || facebookToken == "") {
				whyqUtils.integateLoginFacebook(NewWhyqActivity.this,
						handleFbLogin);
			} else {
				// btnShareFacebook.setChecked(false);
				// permUtils.logOutFacebook(NewPermActivity.this);
			}
		} else {
			facebookToken = null;
		}

	}

	private void uploadPerm() {
		// TODO Auto-generated method stub
		// if (facebookToken == null && twitterAccessToken == null) {
		// Toast.makeText(getApplicationContext(),
		// "Please choose the share type!", Toast.LENGTH_LONG).show();
		// } else
		// {
		if (imagePath != "" || permID > 0) {
			// Logger.appendLog("\n== NewPermActivity uploadPerm function imagePath = "
			// + imagePath + "==", "NewPermActivity");
			showLoadingDialog("Processing", "Please wait...");
			new ImageUpload(imagePath).execute();

		} else if (isReperm) {
			showLoadingDialog("Processing", "Please wait...");
			new ImageUpload(imagePath).execute();
		}
		// }

	}

//	public class MyLocationListener implements LocationListener {
//
//		@Override
//		public void onLocationChanged(Location loc) {
//
//			loc.getLatitude();
//
//			loc.getLongitude();
//			lat = loc.getLatitude();
//			lon = loc.getLongitude();
//		}
//
//		@Override
//		public void onProviderDisabled(String provider) {
//
//		}
//
//		@Override
//		public void onProviderEnabled(String provider) {
//		}
//
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//
//		}
//
//	}/* End of Class MyLocationListener */

	public void gotoKakao() throws Exception {

		try {

			String strMessage = "permping.com/m/" + permId;// "카카오링크를 사용하여 메세지를 전달해 보세요.";
			String strURL = "Android: " + permAndroidLink + " & Iphone: "
					+ permIphoneLink;// "http://link.kakao.com";
			String strAppId = "com.kakao.android.image";
			String strAppVer = "2.0";
			String strAppName = context.getResources().getString(
					R.string.app_name_inkakao);// "Permping";// "[카카오톡]";
			String strInstallUrl = "Android: " + permAndroidLink + " &Iphone: "
					+ permIphoneLink;
			;
			ArrayList<Map<String, String>> arrMetaInfo = new ArrayList<Map<String, String>>();

			Map<String, String> metaInfoAndroid = new Hashtable<String, String>(
					1);
			metaInfoAndroid.put("os", "android");
			metaInfoAndroid.put("devicetype", "phone");
			metaInfoAndroid.put("installurl", strInstallUrl);
			metaInfoAndroid.put("executeurl", "perm://newperm");
			arrMetaInfo.add(metaInfoAndroid);
			WhyqMain.isKakao = true;
			KakaoLink link = new KakaoLink(NewWhyqActivity.this, strURL,
					strAppId, strAppVer, strMessage, strAppName, arrMetaInfo,
					"UTF-8");

			if (link.isAvailable()) {
				// startActivity(link.getIntent());
			}
			startActivity(link.getIntent());

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void showLoadingDialog(String title, String msg) {
		// loadingDialog = new ProgressDialog(context);
		// loadingDialog.setMessage(msg);
		// loadingDialog.setTitle(title);
		// loadingDialog.setCancelable(true);
		// loadingDialog.show();
		btnOk.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void dismissLoadingDialog() {
		// if (loadingDialog != null )
		// if(loadingDialog.isShowing())
		// loadingDialog.dismiss();
		if (progressBar.getVisibility() == View.VISIBLE) {
			progressBar.setVisibility(View.INVISIBLE);
			btnOk.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// PermpingMain.back();
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
