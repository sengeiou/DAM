package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import whyq.interfaces.FriendTwitterController;
import whyq.model.FriendTwitter;

public class FriendTwitterHandler extends BaseHandler implements FriendTwitterController {
	private static final String TAG_LIST_NOT_JOIN_WHYQ = "list_not_join_whyq";
	private static final String TAG_LIST_JOIN_WHYQ = "list_join_whyq";
	private static final String TAG_DATA = "data";
	private static final String TAG_TWITTER_ID = "twitter_id";
	private static final String TAG_ITUNE_URL = "ituneUrl";
	private static final String TAG_FIRST_NAME = "first_name";
	private static final String TAG_SCREEN_NAME = "screen_name";
	private static final String TAG_ID = "id";
	private static final String TAG_AVATAR = "avatar";
	private static final String TAG_LAST_NAME = "last_name";
	private static final String TAG_IS_JOIN = "is_join";
	private static final String TAG_IS_FRIEND = "is_friend";	
	private FriendTwitter friendTwitter;
	private List<FriendTwitter> listNotJoinWhyq;
	private List<FriendTwitter> listJoinWhyq;
	private boolean isListNotJoinWhyq;
	
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if (friendTwitter != null) {
			if (localName.equalsIgnoreCase(TAG_TWITTER_ID)) {
				friendTwitter.setTwitterId(builder.toString());
			}else if(localName.equalsIgnoreCase(TAG_FIRST_NAME)){
				friendTwitter.setFirstName(builder.toString());
			}else if(localName.equalsIgnoreCase(TAG_ITUNE_URL)){
				friendTwitter.setItuneUrl(builder.toString());
			}else if(localName.equalsIgnoreCase(TAG_LAST_NAME)){
				friendTwitter.setLast_name(builder.toString());
			}else if (localName.equalsIgnoreCase(TAG_SCREEN_NAME)) {
				friendTwitter.setScreenName(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_ID)) {
				friendTwitter.setId(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_IS_FRIEND)) {
				friendTwitter.setIsFriend(Integer.parseInt(builder.toString()));
			} else if (localName.equalsIgnoreCase(TAG_AVATAR)) {
				friendTwitter.setAvatar(builder.toString());
			} else if (localName.equalsIgnoreCase(TAG_IS_JOIN)) {
				friendTwitter.setIs_join(getBoolean());
			} else if (localName.equalsIgnoreCase(TAG_DATA)) {
				if (isListNotJoinWhyq) {
					listNotJoinWhyq.add(friendTwitter);
				} else {
					listJoinWhyq.add(friendTwitter);
				}
			}
		}
		builder.setLength(0);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if (localName.equalsIgnoreCase(TAG_DATA)) {
			friendTwitter = new FriendTwitter();
		} else if (localName.equalsIgnoreCase(TAG_LIST_NOT_JOIN_WHYQ)) {
			isListNotJoinWhyq = true;
		} else if (localName.equalsIgnoreCase(TAG_LIST_JOIN_WHYQ)) {
			isListNotJoinWhyq = false;
		}
	}

	@Override
	public List<FriendTwitter> getListNotJoinWhyq() {
		// TODO Auto-generated method stub
		return listNotJoinWhyq;
	}

	@Override
	public List<FriendTwitter> getListWhyq() {
		// TODO Auto-generated method stub
		return listJoinWhyq;
	}
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		listJoinWhyq = new ArrayList<FriendTwitter>();
		listNotJoinWhyq = new ArrayList<FriendTwitter>();
	}

}
	