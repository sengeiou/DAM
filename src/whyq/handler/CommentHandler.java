package whyq.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import whyq.model.Comment;
import whyq.model.Photo;
import whyq.model.User;

public class CommentHandler extends BaseHandler {

	// TAG_COMMENT
	private static final String COMMENT = "obj";

	private static final String TAG_CONTENT = "content";
	private static final String TAG_PARENT_ID = "parent_id";
	private static final String TAG_IS_PRIVATE = "is_private";
	private static final String TAG_IS_READ = "is_read";
	private static final String TAG_COUNT_LIKE = "count_like";
	private static final String TAG_DURATION_TIME = "duration_time";
	private static final String TAG_ISMORE = "isMore";

	// TAG_PHOTO
	private static final String TAG_ID = "id";
	private static final String TAG_STORE_ID = "store_id";
	private static final String TAG_USER_ID = "user_id";
	private static final String TAG_COMMENT_ID = "comment_id";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_THUMB = "thumb";
	private static final String TAG_STATUS = "status";
	private static final String TAG_TYPE_PHOTO = "type_photo";
	private static final String TAG_CREATEDATE = "createdate";
	private static final String TAG_UPDATEDATE = "updatedate";
	private static final String TAG_USER = "user";
	private static final String TAG_PHOTOS = "photos";
	
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ROLE_ID = "role_id";
	private static final String TAG_IS_ACTIVE = "is_active";
	private static final String TAG_TOTAL_MONEY = "total_money";
	private static final String TAG_TOTAL_SAVING_MONEY = "total_saving_money";
	private static final String TAG_TOTAL_COMMENT = "total_comment";
	private static final String TAG_TOTAL_COMMENT_LIKE = "total_comment_like";
	private static final String TAG_TOTAL_FRIEND = "total_friend";
	private static final String TAG_TOTAL_FAVOURITE = "total_favourite";
	private static final String TAG_TOTAL_CHECK_BILL = "total_check_bill";
	private static final String TAG_TOKEN = "token";
	private static final String TAG_FIRST_NAME = "first_name";
	private static final String TAG_LAST_NAME = "last_name";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_AVATAR = "avatar";
	private static final String TAG_STATUS_USER = "status_user";


	private List<Comment> comments;
	private Comment currentComment;
	private User currentUser;
	private Photo currentPhoto;

	public List<Comment> getComments() {
		return comments;
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		super.endElement(uri, localName, name);
		if (this.currentComment != null) {
			if (currentUser != null) {
				if (localName.equals(TAG_IS_ACTIVE)) {
					currentUser.setAcive(getBoolean());
				} else if (localName.equals(TAG_STATUS)) {
					currentUser.setStatus(getString());
				} else if (localName.equals(TAG_TOKEN)) {
					currentUser.setToken(getString());
				} else if (localName.equals(TAG_ID)) {
					currentUser.setId(getString());
				} else if (localName.equals(TAG_EMAIL)) {
					currentUser.setEmail(getString());
				} else if (localName.equals(TAG_ROLE_ID)) {
					currentUser.setRoleId(getString());
				} else if (localName.equals(TAG_TOTAL_MONEY)) {
					currentUser.setTotalMoney(getString());
				} else if (localName.equals(TAG_TOTAL_SAVING_MONEY)) {
					currentUser.setTotalSavingMoney(getString());
				} else if (localName.equals(TAG_TOTAL_COMMENT)) {
					currentUser.setTotalComment(getString());
				} else if (localName.equals(TAG_TOTAL_COMMENT_LIKE)) {
					currentUser.setTotalCommentLike(getString());
				} else if (localName.equals(TAG_TOTAL_FRIEND)) {
					currentUser.setTotalFriend(getString());
				} else if (localName.equals(TAG_TOTAL_FAVOURITE)) {
					currentUser.setTotalFavourite(getString());
				} else if (localName.equals(TAG_TOTAL_CHECK_BILL)) {
					currentUser.setTotalCheckBill(getString());
				} else if (localName.equals(TAG_CREATEDATE)) {
					currentUser.setCreateDate(getString());
				} else if (localName.equals(TAG_UPDATEDATE)) {
					currentUser.setUpdateDate(getString());
				} else if (localName.equals(TAG_FIRST_NAME)) {
					currentUser.setFirstName(getString());
				} else if (localName.equals(TAG_LAST_NAME)) {
					currentUser.setLastName(getString());
				} else if (localName.equals(TAG_GENDER)) {
					currentUser.setGender(getInt());
				} else if (localName.equals(TAG_AVATAR)) {
					currentUser.setUrlAvatar(getString());
				} else if (localName.equals(TAG_STATUS_USER)) {
					currentUser.setStatusUser(getString());
				} 
			}
			if (currentPhoto != null) {
				if (localName.equalsIgnoreCase(TAG_COMMENT_ID)) {
					currentPhoto.setComment_id(getString());
				} else if (localName.equalsIgnoreCase(TAG_CREATEDATE)) {
					currentPhoto.setCreatedate(getString());
				} else if (localName.equalsIgnoreCase(TAG_ID)) {
					currentPhoto.setId(getString());
				} else if (localName.equalsIgnoreCase(TAG_IMAGE)) {
					currentPhoto.setImage(getString());
				} else if (localName.equalsIgnoreCase(TAG_STATUS)) {
					currentPhoto.setStatus(getString());
				} else if (localName.equalsIgnoreCase(TAG_STORE_ID)) {
					currentPhoto.setStore_id(getString());
				} else if (localName.equalsIgnoreCase(TAG_THUMB)) {
					currentPhoto.setThumb(getString());
				}else if (localName.equalsIgnoreCase(TAG_TYPE_PHOTO)) {
					currentPhoto.setType_photo(getString());
				}else if (localName.equalsIgnoreCase(TAG_UPDATEDATE)) {
					currentPhoto.setUpdatedate(getString());
				}else if (localName.equalsIgnoreCase(TAG_USER_ID)) {
					currentPhoto.setUser_id(getString());
				}
			} 
			if (currentComment != null) {
				if (localName.equalsIgnoreCase(TAG_CONTENT)) {
					currentComment.setContent(getString());
				} else if (localName.equalsIgnoreCase(TAG_COUNT_LIKE)) {
					currentComment.setCount_like(getInt());
				} else  if (localName.equalsIgnoreCase(TAG_CREATEDATE)) {
					currentComment.setCreatedate(getString());
				} else  if (localName.equalsIgnoreCase(TAG_DURATION_TIME)) {
					currentComment.setDuration_time(getString());
				} else  if (localName.equalsIgnoreCase(TAG_ID)) {
					currentComment.setId(getString());
				} else  if (localName.equalsIgnoreCase(TAG_IS_PRIVATE)) {
					currentComment.setIs_private(getInt());
				} else  if (localName.equalsIgnoreCase(TAG_IS_READ)) {
					currentComment.setIs_read(getInt());
				} else  if (localName.equalsIgnoreCase(TAG_ISMORE)) {
					currentComment.setIsMore(getString());
				} else  if (localName.equalsIgnoreCase(TAG_PARENT_ID)) {
					currentComment.setParent_id(getString());
				} else  if (localName.equalsIgnoreCase(TAG_PHOTOS)) {
					currentComment.setPhotos(currentPhoto);
				} else  if (localName.equalsIgnoreCase(TAG_STATUS)) {
					currentComment.setStatus(getInt());
				} else  if (localName.equalsIgnoreCase(TAG_STORE_ID)) {
					currentComment.setStore_id(getString());
				} else  if (localName.equalsIgnoreCase(TAG_UPDATEDATE)) {
					currentComment.setUpdatedate(getString());
				} else  if (localName.equalsIgnoreCase(TAG_USER)) {
					currentComment.setUser(currentUser);
				} else  if (localName.equalsIgnoreCase(TAG_USER_ID)) {
					currentComment.setUser_id(getString());
				} 
			}
			if (localName.equalsIgnoreCase(COMMENT)) {
				comments.add(currentComment);
			}
			builder.setLength(0);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		comments = new ArrayList<Comment>();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);
		if (localName.equalsIgnoreCase(COMMENT)) {
			this.currentComment = new Comment();
		} else if (localName.equalsIgnoreCase(TAG_USER)) {
			this.currentUser = new User();
		} else if (localName.equalsIgnoreCase(TAG_PHOTOS)) {
			this.currentPhoto = new Photo();
		}
	}
}
