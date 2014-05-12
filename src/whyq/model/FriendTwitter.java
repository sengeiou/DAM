package whyq.model;

public class FriendTwitter {
	private String twitterId;
	private String id;
	private String ituneUrl;
	private String firstName;
	private String last_name;
	public String getItuneUrl() {
		return ituneUrl;
	}
	public void setItuneUrl(String ituneUrl) {
		this.ituneUrl = ituneUrl;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	private String screenName;
	private int isFriend;
	private String avatar;
	private boolean is_join;
	public String getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public int getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(int isFriend) {
		this.isFriend = isFriend;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public boolean getIs_join() {
		return is_join;
	}
	public void setIs_join(boolean is_join) {
		this.is_join = is_join;
	}
	
}
