/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import whyq.interfaces.FriendFacebookController;
import whyq.model.FriendFacebook;

/**
 * @author giangnguyen.tale@gmail.com
 * 
 */
public class FriendFacebookHandler extends BaseHandler implements
		FriendFacebookController {

	private static final String TAG_LIST_NOT_JOIN_WHYQ = "list_not_join_whyq";
	private static final String TAG_LIST_JOIN_WHYQ = "list_join_whyq";
	private static final String TAG_DATA = "data";
	private static final String TAG_FACEBOOK_ID = "facebook_id";
	private static final String TAG_ITUNE_URL = "ituneUrl";
	private static final String TAG_FIRST_NAME = "first_name";
	private static final String TAG_ID = "id";
	private static final String TAG_AVATAR = "avatar";
	private static final String TAG_LAST_NAME = "last_name";
	private static final String TAG_IS_JOIN = "is_join";
	private static final String TAG_IS_FRIEND = "is_friend";

	private FriendFacebook friendfacebook;
	private List<FriendFacebook> listNotJoinWhyq;
	private List<FriendFacebook> listJoinWhyq;
	private boolean isListNotJoinWhyq;

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (friendfacebook != null) {
			if (localName.equalsIgnoreCase(TAG_FACEBOOK_ID)) {
				friendfacebook.setFacebookId(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_FIRST_NAME)) {
				friendfacebook.setFirstName(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_ID)) {
				friendfacebook.setId(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_IS_FRIEND)) {
				friendfacebook.setIsFriend(Integer.parseInt(builder.toString()));
			} else if (localName.equalsIgnoreCase(TAG_AVATAR)) {
				friendfacebook.setAvatar(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_ITUNE_URL)) {
				friendfacebook.setItuneUrl(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_LAST_NAME)) {
				friendfacebook.setLast_name(getString());
			} else if (localName.equalsIgnoreCase(TAG_IS_JOIN)) {
				friendfacebook.setIs_join(getBoolean());
			} else if (localName.equalsIgnoreCase(TAG_DATA)) {
				if (isListNotJoinWhyq) {
					listNotJoinWhyq.add(friendfacebook);
				} else {
					listJoinWhyq.add(friendfacebook);
				}
			}
		}
		builder.setLength(0);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (localName.equalsIgnoreCase(TAG_DATA)) {
			friendfacebook = new FriendFacebook();
		} else if (localName.equalsIgnoreCase(TAG_LIST_NOT_JOIN_WHYQ)) {
			isListNotJoinWhyq = true;
		} else if (localName.equalsIgnoreCase(TAG_LIST_JOIN_WHYQ)) {
			isListNotJoinWhyq = false;
		}
	}

	@Override
	public List<FriendFacebook> getListNotJoinWhyq() {
		return listNotJoinWhyq;
	}

	@Override
	public List<FriendFacebook> getListWhyq() {
		return listJoinWhyq;
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		listJoinWhyq = new ArrayList<FriendFacebook>();
		listNotJoinWhyq = new ArrayList<FriendFacebook>();
	}

}
