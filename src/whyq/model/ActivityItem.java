package whyq.model;

public class ActivityItem {

	private String id;
	private String user_id;
	private String active_id;
	private String optional_id;
	private String activity_type;
	private String message;
	private int is_read;
	private String createdate;
	private String updatedate;
	private BusinessInfo business_info;
	private String user_name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getActive_id() {
		return active_id;
	}

	public void setActive_id(String active_id) {
		this.active_id = active_id;
	}

	public String getOptional_id() {
		return optional_id;
	}

	public void setOptional_id(String optional_id) {
		this.optional_id = optional_id;
	}

	public String getActivity_type() {
		return activity_type!=null?activity_type:"";
	}

	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getIs_read() {
		return is_read;
	}

	public void setIs_read(int is_read) {
		this.is_read = is_read;
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

	public BusinessInfo getBusiness_info() {
		return business_info;
	}

	public void setBusiness_info(BusinessInfo business_info) {
		this.business_info = business_info;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
}
