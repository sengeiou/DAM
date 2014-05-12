/**
 * 
 */
package whyq.controller;

//import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import whyq.interfaces.Login_delegate;
import whyq.model.User;
import whyq.utils.API;
import whyq.utils.XMLParser;
import android.content.Context;


/**
 * @author Linh Nguyen
 *
 */
public class AuthorizeController {
	
	public AuthorizeController() {
		
	}
	public static Login_delegate loginDelegate;
	public AuthorizeController(Login_delegate loginPermDelegate) {
		// TODO Auto-generated constructor stub
		loginDelegate = loginPermDelegate;
	}

	public static boolean authorize(Context context, List<NameValuePair> nameValuePairs) {
		// Send to server to check if the account is created
		// If existed => back to Home page (Popular screen)
		// If not existed => go to Create Account screen.
		boolean ret = false;
		XMLParser parser = new XMLParser( context,XMLParser.LOGIN, loginDelegate,API.authorizeURL, nameValuePairs);		
		return ret;

	}
	
	/**
	 * Get the profile of specified user.
	 * @param userId the user Id
	 * @return the User object.
	 */
	public User getUserProfileById(String userId) {
		if (userId != null && !"".equals(userId)) {
			XMLParser parser = new XMLParser(API.getProfileURL + userId);
			return parser.getUser();
		}
		return null;
	}
	
	public void updateUserProfileById(Context context, String userId, int type, Object login) {
		if (userId.length() > 0) {
			XMLParser parser = new XMLParser(context, API.getProfileURL + userId, login, type);
		}
	}
	
	public void logout(String userId) {
		if (userId != null && !"".equals(userId)) {
			XMLParser parser = new XMLParser(API.logoutURL + userId);			
		}
	}
}