/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import whyq.model.Comment;
import whyq.model.Store;
import whyq.model.User;
import whyq.model.WhyqImage;
import whyq.utils.Constants;


/**
 * @author Linh Nguyen
 *
 */
public class BoardHandler extends DefaultHandler {

	private StringBuffer buffer = new StringBuffer();
	
	private List<Store> stores;
	
	private Store store;
	
	private User user;
	
	private List<Comment> comments;
	
	private Comment comment;
	
	private User commentAuthor;
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length); 
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName != null && localName.equals(Constants.ITEM)) {
			stores.add(store);
		} else if (localName != null && localName.equals(Constants.PERM_ID)) {
			store.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_DESCRIPTION)) {
//			whyq.setDescription(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_CATEGORY)) {
//			whyq.setCategory(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_IMAGE)) {
			WhyqImage whyqImage = new WhyqImage(buffer.toString());
//			whyq.setImage(whyqImage);
		} else if (localName != null && localName.equals(Constants.PERM_COMMENTS)) {
//			whyq.setComments(comments);
		} else if (localName != null && localName.equals(Constants.COMMENT)) {
			comments.add(comment);
		} else if (localName != null && localName.equals(Constants.ID)) {
			comment.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.USER)) {
//			whyq.setAuthor(user);
		} else if (localName != null && localName.equals(Constants.L_USER)) {
			comment.setUser(commentAuthor);
		} else if (localName != null && localName.equals(Constants.USER_ID)) {
			user.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.L_USER_ID)) {
			commentAuthor.setId(buffer.toString());
		} else if (localName != null && localName.equals(Constants.USER_NAME)) {
			user.setName(buffer.toString());
		} else if (localName != null && localName.equals(Constants.L_USER_NAME)) {
			commentAuthor.setName(buffer.toString());
		} else if (localName != null && localName.equals(Constants.STATUS)) {
			user.setStatus(buffer.toString());
		} else if (localName != null && localName.equals(Constants.L_STATUS)) {
			commentAuthor.setStatus(buffer.toString());
		} else if (localName != null && localName.equals(Constants.USER_AVATAR)) {
			WhyqImage avatar = new WhyqImage(buffer.toString());
			user.setAvatar(avatar);
		} else if (localName != null && localName.equals(Constants.L_USER_AVATAR)) {
			WhyqImage avatar = new WhyqImage(buffer.toString());
			commentAuthor.setAvatar(avatar);
		} else if (localName != null && localName.equals(Constants.CONTENT)) {
			comment.setContent(buffer.toString());
		} else if (localName != null && localName.equals(Constants.IS_MORE)) {
			comment.setIsMore(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_REPINCOUNT)) {
//			whyq.setPermRepinCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_LIKECOUNT)) {
//			whyq.setPermLikeCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_COMMENTCOUNT)) {
//			whyq.setPermCommentCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.PERM_USERLIKECOUNT)) {
//			whyq.setPermUserLikeCount(buffer.toString());
		} else if (localName != null && localName.equals(Constants.NEXT_ITEM)) {
//			whyq.setNextItem(buffer.toString());
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		buffer.setLength(0);
		
		if (localName != null && localName.equals(Constants.PERMS)) {
			stores = new ArrayList<Store>();
		} else if (localName != null && localName.equals(Constants.ITEM)) {
			store = new Store();
		} else if (localName != null && localName.equals(Constants.USER)) {
			user = new User();
		} else if (localName != null && localName.equals(Constants.L_USER)) {
			commentAuthor = new User();
		} else if (localName != null && localName.equals(Constants.PERM_COMMENTS)) {
			comments = new ArrayList<Comment>();
		} else if (localName != null && localName.equals(Constants.COMMENT)) {
			comment = new Comment();			
		} else if (localName != null && localName.equals(Constants.USER)) {
			commentAuthor = new User();
		}
	}
	
	public List<Store> getPerms() {
		return stores;
	}
}