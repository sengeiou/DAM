package whyq.model;

import java.io.Serializable;

public class FriendFacebook implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String facebookId;
	private String ituneUrl;
	private String firstName;
	private String id;
	private int isFriend;
	private String avatar;
	private String last_name;
	private boolean is_join;

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public boolean getIs_join() {
		return is_join;
	}

	public void setIs_join(boolean is_join) {
		this.is_join = is_join;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public void setItuneUrl(String ituneUrl) {
		this.ituneUrl = ituneUrl;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIsFriend(int isFriend) {
		this.isFriend = isFriend;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public String getItuneUrl() {
		return ituneUrl;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getId() {
		return id;
	}

	public int getIsFriend() {
		return isFriend;
	}

	public String getAvatar() {
		return avatar;
	}

}
