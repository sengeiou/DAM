package whyq.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4778518614772031293L;
	private String id;
	private String name;
	private String status;
	private WhyqImage avatar;
	private int followings;
	private int friends;	
	private int pin;
	private int like;
	private int board;
	List<WhyqBoard> boards;
	private String version;
	private String email;
	private String roleId;
	private boolean isAcive;
	private String totalMoney;
	private String totalSavingMoney;
	private String totalComment;
	private String totalCommentLike;
	private String totalFriend;
	private String totalFavourite;
	private String totalCheckBill;
	private String token;
	private String createDate;
	private String updateDate;
	private String firstName;
	private String lastName;
	private int gender;
	private String urlAvatar;
	private String statusUser;
	private String city;
	private String phoneNumber;
	private String paypalEmail;
	private boolean isFriend;
	private String facebookId;
	private String twitterId;
	
	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPaypalEmail() {
		return paypalEmail;
	}

	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String totalCount;
	private String notificationId;
	private boolean isReceiveNotification;
	private boolean ishowEmail;
	private boolean isShowFavorite;
	private boolean isShowFriend;
	private boolean isReceivePromotionNotification;
	private int notiCreateDate;
	private int notiUpdateDate;
	private String address;
	private boolean logined;
	private String messageLogin;
	
	
	/**
	 * Default constructor
	 */
	public User() {
		
	}
	
	/**
	 * 
	 * @param id
	 */
	public User( String id){
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param avatar
	 * @param friends
	 * @param followings
	 * @param pin
	 * @param like
	 * @param board
	 * @param boards
	 */
	public User(String id, String name, WhyqImage avatar, int friends, int followings, 
			int pin, int like, int board, List<WhyqBoard> boards) {
		this.setId(id);
		this.setName(name);
		this.setAvatar(avatar);
		this.setFriends(friends);
		this.setFollowings(followings);
		this.setPin(pin);
		this.setLike(like);
		this.setBoard(board);
		this.setBoards(boards);
	}


	/**
	 * @return the boards
	 */
	public List<WhyqBoard> getBoards() {
		return boards;
	}

	/**
	 * @param boards the boards to set
	 */
	public void setBoards(List<WhyqBoard> boards) {
		this.boards = boards;
	}

	public String getName() {
		return name!=null?name:"";
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public WhyqImage getAvatar() {
		return avatar;
	}


	public void setAvatar(WhyqImage avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the friends
	 */
	public int getFriends() {
		return friends;
	}

	/**
	 * @param friends the friends to set
	 */
	public void setFriends(int friends) {
		this.friends = friends;
	}

	
	/**
	 * @return the pin
	 */
	public int getPin() {
		return pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(int pin) {
		this.pin = pin;
	}

	/**
	 * @return the like
	 */
	public int getLike() {
		return like;
	}

	/**
	 * @param like the like to set
	 */
	public void setLike(int like) {
		this.like = like;
	}

	/**
	 * @return the board
	 */
	public int getBoard() {
		return board;
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(int board) {
		this.board = board;
	}

	/**
	 * @return the followings
	 */
	public int getFollowings() {
		return followings;
	}

	/**
	 * @param followings the followings to set
	 */
	public void setFollowings(int followings) {
		this.followings = followings;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the isAcive
	 */
	public boolean isAcive() {
		return isAcive;
	}

	/**
	 * @param isAcive the isAcive to set
	 */
	public void setAcive(boolean isAcive) {
		this.isAcive = isAcive;
	}

	/**
	 * @return the totalMoney
	 */
	public String getTotalMoney() {
		return totalMoney;
	}

	/**
	 * @param totalMoney the totalMoney to set
	 */
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	/**
	 * @return the totalSavingMoney
	 */
	public String getTotalSavingMoney() {
		return totalSavingMoney;
	}

	/**
	 * @param totalSavingMoney the totalSavingMoney to set
	 */
	public void setTotalSavingMoney(String totalSavingMoney) {
		this.totalSavingMoney = totalSavingMoney;
	}

	/**
	 * @return the totalComment
	 */
	public String getTotalComment() {
		return totalComment;
	}

	/**
	 * @param totalComment the totalComment to set
	 */
	public void setTotalComment(String totalComment) {
		this.totalComment = totalComment;
	}

	/**
	 * @return the totalCommentLike
	 */
	public String getTotalCommentLike() {
		return totalCommentLike;
	}

	/**
	 * @param totalCommentLike the totalCommentLike to set
	 */
	public void setTotalCommentLike(String totalCommentLike) {
		this.totalCommentLike = totalCommentLike;
	}

	/**
	 * @return the totalFriend
	 */
	public String getTotalFriend() {
		return totalFriend;
	}

	/**
	 * @param totalFriend the totalFriend to set
	 */
	public void setTotalFriend(String totalFriend) {
		this.totalFriend = totalFriend;
	}

	/**
	 * @return the totalFavourite
	 */
	public String getTotalFavourite() {
		return totalFavourite;
	}

	/**
	 * @param totalFavourite the totalFavourite to set
	 */
	public void setTotalFavourite(String totalFavourite) {
		this.totalFavourite = totalFavourite;
	}

	/**
	 * @return the totalCheckBill
	 */
	public String getTotalCheckBill() {
		return totalCheckBill;
	}

	/**
	 * @param totalCheckBill the totalCheckBill to set
	 */
	public void setTotalCheckBill(String totalCheckBill) {
		this.totalCheckBill = totalCheckBill;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName==null?"":firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName==null?"":lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}

	/**
	 * @return the urlAvatar
	 */
	public String getUrlAvatar() {
		return urlAvatar;
	}

	/**
	 * @param urlAvatar the urlAvatar to set
	 */
	public void setUrlAvatar(String urlAvatar) {
		this.urlAvatar = urlAvatar;
	}

	/**
	 * @return the statusUser
	 */
	public String getStatusUser() {
		return statusUser;
	}

	/**
	 * @param statusUser the statusUser to set
	 */
	public void setStatusUser(String statusUser) {
		this.statusUser = statusUser;
	}

	/**
	 * @return the totalCount
	 */
	public String getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the notificationId
	 */
	public String getNotificationId() {
		return notificationId;
	}

	/**
	 * @param notificationId the notificationId to set
	 */
	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the isReceiveNotification
	 */
	public boolean isReceiveNotification() {
		return isReceiveNotification;
	}

	/**
	 * @param isReceiveNotification the isReceiveNotification to set
	 */
	public void setReceiveNotification(boolean isReceiveNotification) {
		this.isReceiveNotification = isReceiveNotification;
	}

	/**
	 * @return the ishowEmail
	 */
	public boolean isIshowEmail() {
		return ishowEmail;
	}

	/**
	 * @param ishowEmail the ishowEmail to set
	 */
	public void setIshowEmail(boolean ishowEmail) {
		this.ishowEmail = ishowEmail;
	}

	/**
	 * @return the isShowFavorite
	 */
	public boolean isShowFavorite() {
		return isShowFavorite;
	}

	/**
	 * @param isShowFavorite the isShowFavorite to set
	 */
	public void setShowFavorite(boolean isShowFavorite) {
		this.isShowFavorite = isShowFavorite;
	}

	/**
	 * @return the isReceivePromotionNotification
	 */
	public boolean isReceivePromotionNotification() {
		return isReceivePromotionNotification;
	}

	/**
	 * @param isReceivePromotionNotification the isReceivePromotionNotification to set
	 */
	public void setReceivePromotionNotification(
			boolean isReceivePromotionNotification) {
		this.isReceivePromotionNotification = isReceivePromotionNotification;
	}

	/**
	 * @return the notiCreateDate
	 */
	public int getNotiCreateDate() {
		return notiCreateDate;
	}

	/**
	 * @param notiCreateDate the notiCreateDate to set
	 */
	public void setNotiCreateDate(int notiCreateDate) {
		this.notiCreateDate = notiCreateDate;
	}

	/**
	 * @return the notiUpdateDate
	 */
	public int getNotiUpdateDate() {
		return notiUpdateDate;
	}

	/**
	 * @param notiUpdateDate the notiUpdateDate to set
	 */
	public void setNotiUpdateDate(int notiUpdateDate) {
		this.notiUpdateDate = notiUpdateDate;
	}

	/**
	 * @return the isShowFriend
	 */
	public boolean isShowFriend() {
		return isShowFriend;
	}

	/**
	 * @param isShowFriend the isShowFriend to set
	 */
	public void setShowFriend(boolean isShowFriend) {
		this.isShowFriend = isShowFriend;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the logined
	 */
	public boolean isLogined() {
		return logined;
	}

	/**
	 * @param logined the logined to set
	 */
	public void setLogined(boolean logined) {
		this.logined = logined;
	}

	/**
	 * @return the messageLogin
	 */
	public String getMessageLogin() {
		return messageLogin;
	}

	/**
	 * @param messageLogin the messageLogin to set
	 */
	public void setMessageLogin(String messageLogin) {
		this.messageLogin = messageLogin;
	}

	/**
	 * @return the isFriend
	 */
	public boolean isFriend() {
		return isFriend;
	}

	/**
	 * @param isFriend the isFriend to set
	 */
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	
	
}
