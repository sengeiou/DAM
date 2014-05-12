package whyq.model;

public class UserProfile {

	private String id;
	private String email;
	private String role_id;
	private String is_active;
	private String twitter_id;
	private String facebook_id;
	private String token;
	private String status;
	private String createdate;
	private String updatedate;
	private String first_name;
	private String last_name;
	private String bio;
	private String gender;
	private String avatar;
	private String city;
	private String address;
	private String phone_number;
	private String paypal_email;
	private String status_user;
	private TotalCount total_count;
	private boolean logined;
	private String messageLogin;
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
	public String getRole_id() {
		return role_id;
	}
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	public String getIs_active() {
		return is_active;
	}
	public void setIs_active(String is_active) {
		this.is_active = is_active;
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
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone_number() {
		return phone_number;
	}
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	public String getPaypal_email() {
		return paypal_email;
	}
	public void setPaypal_email(String paypal_email) {
		this.paypal_email = paypal_email;
	}
	public String getStatus_user() {
		return status_user;
	}
	public void setStatus_user(String status_user) {
		this.status_user = status_user;
	}
	public TotalCount getTotal_count() {
		return total_count;
	}
	public void setTotal_count(TotalCount total_count) {
		this.total_count = total_count;
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

}
