/**
 * 
 */
package whyq.controller;

import java.util.List;

import whyq.interfaces.Get_Perm_Delegate;
import whyq.model.Store;
import whyq.utils.API;
import whyq.utils.XMLParser;


/**
 * @author Linh Nguyen
 *
 */
public class MyDiaryController {

	/**
	 * Default constructor.
	 */
	public MyDiaryController() {
		
	}
	
	public List<Store> getPermsByDate(String date, Get_Perm_Delegate delegate) {
		if (date == null || "".equals(date))
			return null;
		XMLParser parser = new XMLParser(API.getPermsByDate + date, delegate,  XMLParser.GET_PERMS_BY_DATE);
		return parser.getPerms();
	}
}
