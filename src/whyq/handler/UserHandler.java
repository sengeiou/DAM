/**
 * 
 */
package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import whyq.model.User;
import whyq.model.WhyqBoard;
import whyq.utils.Constants;


/**
 *
 */
public class UserHandler extends DefaultHandler {
	
	private User user = new User();
	private List<WhyqBoard> boards;
	private WhyqBoard whyqBoard;
	
	private StringBuffer buffer = new StringBuffer();
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length); 
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if(user!=null){
			String value  = buffer.toString();
			if (localName.equals("Version")) {
				user.setVersion(value);
			} else if (localName.equals("Status")) {
				user.setStatus(value);
			} else if (localName.equals("Message")) {
			} else if (localName.equals("Token")){
				user.setToken(value);
			} else if (localName.equals("id")) {
				if(user.getId()!=null)
					user.setId(value);
			} else if (localName.equals("email")) {
				user.setEmail(value);
			} else if (localName.equals("role_id")) {
				user.setRoleId(value);
			} else if (localName.equals("is_active")) {
				user.setIshowEmail(value.equals("1")?true:false);
			} else if (localName.equals("total_money")) {
				user.setTotalMoney(value);
			} else if (localName.equals("total_saving_money")) {
				user.setTotalSavingMoney(value);
			} else if (localName.equals("total_comment")) {
				user.setTotalComment(value);
			} else if (localName.equals("total_comment_like")) {
				user.setTotalCommentLike(value);
			} else if (localName.equals("total_friend")) {
				user.setTotalFriend(value);
			} else if (localName.equals("total_favourite")) {
				user.setTotalFavourite(value);
			} else if (localName.equals("total_check_bill")) {
				user.setTotalCheckBill(value);
			} else if (localName.equals("status")) {
				user.setStatus(value);
			} else if (localName.equals("createdate")) {
				user.setCreateDate(value);
			} else if (localName.equals("updatedate")) {
				user.setUpdateDate(value);
			} else if (localName.equals("first_name")) {
				user.setFirstName(value);
			} else if (localName.equals("last_name")) {
				user.setLastName(value);
			} else if (localName.equals("gender")) {
				user.setGender(Integer.parseInt(value));
			} else if (localName.equals("avatar")) {
				user.setUrlAvatar(value);
			} else if (localName.equals("status_user")) {
				user.setStatusUser(value);
			} else if (localName.equals("total_count")) {
				user.setTotalCount(value);
			} else if (localName.equals("id")) {
				user.setId(value);
			} else if (localName.equals("is_receive_notification")) {
				user.setReceiveNotification(value.equals("1")?true:false);
			} else if (localName.equals("is_show_email")) {
				user.setIshowEmail(value.equals("1")? true:false);
			} else if (localName.equals("is_show_favorite")) {
				user.setShowFavorite(value.equals("1")?true:false);
			} else if (localName.equals("is_show_friend")) {
				user.setShowFriend(value.equals("1")?true:false);
			} else if (localName.equals("is_receive_promotion_notification")) {
				user.setReceivePromotionNotification(value.equals("1")?true:false);
			} else if (localName.equals("createdate")) {
				user.setCreateDate(value);
			} else if (localName.equals("updatedate")) {
				user.setUpdateDate(value);
			} 
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		buffer.setLength(0);
		
		if (localName.equals(Constants.USER)) {
			user = new User();
		} else if (localName.equals(Constants.BOARDS)) {
			boards = new ArrayList<WhyqBoard>();
		} else if (localName.equals(Constants.ITEM)) {
			whyqBoard = new WhyqBoard();
		}
	}
	
	public User getUser() {
		return user;
	}
}
