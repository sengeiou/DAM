package whyq.utils.share;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import whyq.model.ShareData;
import whyq.utils.SharedPreferencesManager;
import whyq.utils.Util;
import whyq.utils.facebook.BaseRequestListener;
import whyq.utils.facebook.sdk.FacebookError;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;


public class ShareHandler implements ShareListener {
	private String shareMessage;
	Twitter _twitter;
	SharedPreferencesManager _shareManager;
	private String linkshareFacebook = null;
	private String linkshareTwitter = null;
	public static int TWITTER = 1;
	public static int FACEBOOK = 2;
	public Context context;
	public static String errorMesage = "";
	private Context mContext;
	private StatusUpdate mStatus;
	private Status status;

	public ShareHandler(Context context) {
		// TODO Auto-generated constructor stub

		this.context = context;
		_shareManager = new SharedPreferencesManager(context);
		_twitter = _shareManager.loadTwitter();
	}

	@Override
	public void success() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail() {
		// TODO Auto-generated method stub

	}

	// public static Handler handlePostTwitter = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == 1) {
	// Toast.makeText(
	// WhyqApplication.Instance().getApplicationContext(),
	// "Share successful", Toast.LENGTH_SHORT).show();
	// } else {
	// Toast.makeText(
	// WhyqApplication.Instance().getApplicationContext(),
	// (String) msg.obj, Toast.LENGTH_SHORT).show();
	// }
	// }
	// };

	// public boolean postTwitter(Context context, String message, String
	// storyID) {
	// String msg = limitContent(message, "");
	// mStatus = new StatusUpdate(msg);
	// // mStatus.setMedia(Config.FILE_SHARE_THUMNAIL_GLOBAL);
	// String sdcard_thumb = "";
	// synchronized (IssueListActivity.db) {
	// IssueListActivity.db.open();
	// sdcard_thumb = IssueListActivity.db.getTwitterThumb(storyID);
	// IssueListActivity.db.close();
	// }
	// // File dir = Environment.getExternalStorageDirectory();
	// File yourFile = new File("", sdcard_thumb);
	// mStatus.setMedia(yourFile);
	// try {
	// status = _twitter.updateStatus(mStatus);
	// return true;
	// } catch (TwitterException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return false;
	// }
	// }

	public boolean postFacebook(String accessToken, ShareData data) {
		// message = IssueAdapter.currentDesription;
		exePostStory(accessToken, data);
		return false;
	}
	
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
//	private void publishStory(String accessToken) {
//		Log.d("publishStory","accessToken"+accessToken);
//	    Session session = Session.getActiveSession();
////        // Check for publish permissions    
////        List<String> permissions = session.getPermissions();
////        if (!isSubsetOf(PERMISSIONS, permissions)) {
////            pendingPublishReauthorization = true;
////            Session.NewPermissionsRequest newPermissionsRequest = new Session
////                    .NewPermissionsRequest(this, PERMISSIONS);
////        session.requestNewPublishPermissions(newPermissionsRequest);
////            return;
////        }
//	    if (session != null){
//	    	if(!session.isOpened()){
//	    		session = Session.openActiveSessionWithAccessToken(context, AccessToken.createFromExistingAccessToken(accessToken, null, null, null, null), new Session.StatusCallback() {
//					
//					@Override
//					public void call(Session session, SessionState state, Exception exception) {
//						// TODO Auto-generated method stub
//						exePostStory(session);
//					}
//				});
//	    	}else{
//	    		exePostStory(session);
//	    	}
//	    
//	    }else{
//	    	session = Session.openActiveSessionWithAccessToken(context, AccessToken.createFromExistingAccessToken(accessToken, null, null, null, null), new Session.StatusCallback() {
//				
//				@Override
//				public void call(Session session, SessionState state, Exception exception) {
//					// TODO Auto-generated method stub
//					exePostStory(a);
//				}
//			});
//	    }
//
//	}
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}
	

	
	private void exePostStory(String  accessToken, ShareData data) {
		// TODO Auto-generated method stub
        Bundle postParams = new Bundle();
        postParams.putString("name", ""+data.getName());
        postParams.putString("message", ""+data.getMessage());
        postParams.putString("caption", ""+data.getCaption());
        postParams.putString("description", ""+data.getDescription());
        postParams.putString("link", ""+data.getLink()); 
        postParams.putString("picture", ""+data.getPicture());

        Log.d("exePostStory","Link "+data.getLink());
        Request.Callback callback= new Request.Callback() {
            public void onCompleted(Response response) {
            	Log.d("publishStory","Response"+response);
                JSONObject graphResponse = response
                                           .getGraphObject()
                                           .getInnerJSONObject();
                String postId = null;
                try {
                    postId = graphResponse.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FacebookRequestError error = response.getError();
                if (error != null) {
                    Toast.makeText(context
                         .getApplicationContext(),
                         error.getErrorMessage(),
                         Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context
                             .getApplicationContext(), 
                             "Posted to faceboo!",
                             Toast.LENGTH_LONG).show();
                }
            }
        };
        Session session = Util.createSession();
        Request request = new Request(session, "me/feed", postParams, 
                              HttpMethod.POST, callback);

        RequestAsyncTask task = new RequestAsyncTask(request);
        task.execute();
	
//		try {
//			com.facebook.AccessToken token = AccessToken.createFromExistingAccessToken(accessToken, null, null	, null, null);
//
//            Session.openActiveSessionWithAccessToken(context, token,
//                    new Session.StatusCallback() {
//
//                        @SuppressWarnings("deprecation")
//						@Override
//                        public void call(Session session,
//                                SessionState state, Exception exception) {
//                            // TODO Auto-generated method stub
//
//                            if (session.isOpened()) {
//
//                                Request.executeGraphPathRequestAsync(
//                                        session, "me/home",
//                                        new Request.Callback() {
//
//                                            @Override
//                                            public void onCompleted(
//                                                    Response response) {
//                                                // TODO Auto-generated
//                                                // method stub
//
//                                                JSONObject jsonObject = null;
//                                                JSONArray jArray = null;
//
//                                                try {
//                                                    jsonObject = new JSONObject(
//                                                            response.getGraphObject()
//                                                                    .getInnerJSONObject()
//                                                                    .toString());
//                                                    jArray = jsonObject
//                                                            .getJSONArray("data");
//
//                                                    for (int i = 0; i < jArray
//                                                            .length(); i++) {
//                                                        JSONObject element = null;
//                                                        element = jArray
//                                                                .getJSONObject(i);
//                                                        System.out.println(element
//                                                                .get("id")
//                                                                + "\n");
//                                                    }
//                                                } catch (JSONException e) {
//                                                    // TODO: handle
//                                                    // exception,,
//
//                                                    System.out
//                                                            .println("JSON EXCEPTION:"
//                                                                    + e);
//
//                                                }
//
//                                            }
//                                        });
//
//                            } else {
//                                System.out
//                                        .println("KONEKCIJA NIJE OTVORENA");
//                            }
//
//                        }
//                    });
//
//        } catch (Exception e) {
//            // TODO: handle exception
//
//            System.out.println("Greska PRILIKOM vracanja grafa:"
//                    + e.toString());
//        }
	
	}
	private Handler mRunOnUi = new Handler();

	private final class WallPostListener extends BaseRequestListener {


		@Override
		public void onComplete(final String response) {

			// TODO Auto-generated method stub

			mRunOnUi.post(new Runnable() {
				@Override
				public void run() {
					Log.d("WallPostListener", "" + response);
					Toast.makeText(context, "Posted to Facebook",
							Toast.LENGTH_SHORT).show();
				}
			});

		
		}

		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub
			
		}
	}

	static public String limitContent(String message, String extra) {
		String result = message;
		String mark = "\"";
		Boolean endIsLyric = false;
		if (result.endsWith(mark))
			endIsLyric = true;
		int limit = (200000 - extra.length());
		if (result.length() > limit) {
			if (endIsLyric) {
				result = result.substring(0, limit - (3 + mark.length()));
				result += ("..." + mark);
			} else {
				result = result.substring(0, limit - 3);
				result += ("...");
			}
		}
		result += "\n" + extra;
		Log.v("Post Tweet", result);
		return result;
	}
}
