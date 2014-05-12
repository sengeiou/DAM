package whyq.model;

import java.io.Serializable;

public class Comment implements Serializable {

	private String id;
	private String user_id;
	private String store_id;
	private String content;
	private String parent_id;
	private int is_private;
	private int is_read;
	private int count_like;
	private int status;
	private String createdate;
	private String updatedate;
	private String duration_time;
	private Photo photos;
	private User user;

	private String isMore;

	private static final long serialVersionUID = 3234423477L;

	public Comment() {

	}

	public Comment(String id) {

	}

	public Comment(String id, String content) {
		this.id = id;
		this.content = content;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public String getId() {
		return this.id;
	}

	public String getContent() {
		return this.content;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsMore() {
		return isMore;
	}

	public void setIsMore(String isMore) {
		this.isMore = isMore;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public int getIs_private() {
		return is_private;
	}

	public void setIs_private(int is_private) {
		this.is_private = is_private;
	}

	public int getIs_read() {
		return is_read;
	}

	public void setIs_read(int is_read) {
		this.is_read = is_read;
	}

	public int getCount_like() {
		return count_like;
	}

	public void setCount_like(int count_like) {
		this.count_like = count_like;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
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

	public String getDuration_time() {
		return duration_time;
	}

	public void setDuration_time(String duration_time) {
		this.duration_time = duration_time;
	}

	public Photo getPhotos() {
		return photos;
	}

	public void setPhotos(Photo photos) {
		this.photos = photos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
