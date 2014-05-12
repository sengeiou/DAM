package whyq.model;

public class FriendWhyq {

	private String id;
	private String email;
	private int role_id;
	private int is_active;
	private float total_money;
	private float total_saving_money;
	private int total_comment;
	private int total_comment_like;
	private int total_friend;
	private int total_favourite;
	private int total_check_bill;
	private String twitter_id;
	private String facebook_id;
	private String token;
	private String status;
	private String createdate;
	private String updatedate;
	private String first_name;
	private String last_name;
	private String gender;
	private String avatar;
	private StatusUser status_user;
	private int isFriend;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public int getIs_active() {
		return is_active;
	}
	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}
	public float getTotal_money() {
		return total_money;
	}
	public void setTotal_money(float total_money) {
		this.total_money = total_money;
	}
	public float getTotal_saving_money() {
		return total_saving_money;
	}
	public void setTotal_saving_money(float total_saving_money) {
		this.total_saving_money = total_saving_money;
	}
	public int getTotal_comment() {
		return total_comment;
	}
	public void setTotal_comment(int total_comment) {
		this.total_comment = total_comment;
	}
	public int getTotal_comment_like() {
		return total_comment_like;
	}
	public void setTotal_comment_like(int total_comment_like) {
		this.total_comment_like = total_comment_like;
	}
	public int getTotal_friend() {
		return total_friend;
	}
	public void setTotal_friend(int total_friend) {
		this.total_friend = total_friend;
	}
	public int getTotal_favourite() {
		return total_favourite;
	}
	public void setTotal_favourite(int total_favourite) {
		this.total_favourite = total_favourite;
	}
	public int getTotal_check_bill() {
		return total_check_bill;
	}
	public void setTotal_check_bill(int total_check_bill) {
		this.total_check_bill = total_check_bill;
	}
	public String getTwitter_id() {
		return twitter_id;
	}
	public void setTwitter_id(String twitter_id) {
		this.twitter_id = twitter_id;
	}
	public String getFacebook_id() {
		return facebook_id;
	}
	public void setFacebook_id(String facebook_id) {
		this.facebook_id = facebook_id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}
	public String getFirst_name() {
		return first_name==null?"":first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name==null?"":last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public StatusUser getStatus_user() {
		return status_user;
	}
	public void setStatus_user(StatusUser status_user) {
		this.status_user = status_user;
	}
	/**
	 * @return the isFriend
	 */
	public int getIsFriend() {
		return isFriend;
	}
	/**
	 * @param isFriend the isFriend to set
	 */
	public void setIsFriend(int isFriend) {
		this.isFriend = isFriend;
	}

}
