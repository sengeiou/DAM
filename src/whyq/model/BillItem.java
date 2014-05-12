package whyq.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class BillItem implements Parcelable {

	private String id;
	private String user_id;
	private String store_id;
	private float total_value;
	private float total_real_value;
	private float discount_value;
	private float  deliver_fee_value;
	private String transaction_id;
	private String code_bill;
	private String deliver_to;
	private String deliver_type;
	private int status_bill;
	private String parent_bill_id;
	private String createdate;
	private String updatedate;
	private BusinessInfo business_info;
	private String status_bill_old;
	private String text_status_bill;
	private String createdate_cv;
	private String time_deliver_cv;
	private String detail;
	
	public String getStatus_bill_old() {
		return status_bill_old;
	}
	public void setStatus_bill_old(String status_bill_old) {
		this.status_bill_old = status_bill_old;
	}
	public String getText_status_bill() {
		return text_status_bill;
	}
	public void setText_status_bill(String text_status_bill) {
		this.text_status_bill = text_status_bill;
	}
	public String getCreatedate_cv() {
		return createdate_cv;
	}
	public void setCreatedate_cv(String createdate_cv) {
		this.createdate_cv = createdate_cv;
	}
	public String getTime_deliver_cv() {
		return time_deliver_cv;
	}
	public void setTime_deliver_cv(String time_deliver_cv) {
		this.time_deliver_cv = time_deliver_cv;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
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
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public float getTotal_value() {
		return total_value;
	}
	public void setTotal_value(float total_value) {
		this.total_value = total_value;
	}
	public float getTotal_real_value() {
		return total_real_value;
	}
	public void setTotal_real_value(float total_real_value) {
		this.total_real_value = total_real_value;
	}
	public float getDiscount_value() {
		return discount_value;
	}
	public void setDiscount_value(float discount_value) {
		this.discount_value = discount_value;
	}
	public float getDeliver_fee_value() {
		return deliver_fee_value;
	}
	public void setDeliver_fee_value(float deliver_fee_value) {
		this.deliver_fee_value = deliver_fee_value;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getCode_bill() {
		return code_bill;
	}
	public void setCode_bill(String code_bill) {
		this.code_bill = code_bill;
	}
	public String getDeliver_to() {
		return deliver_to;
	}
	public void setDeliver_to(String deliver_to) {
		this.deliver_to = deliver_to;
	}
	public String getDeliver_type() {
		return deliver_type;
	}
	public void setDeliver_type(String deliver_type) {
		this.deliver_type = deliver_type;
	}
	public int getStatus_bill() {
		return status_bill;
	}
	public void setStatus_bill(int status_bill) {
		this.status_bill = status_bill;
	}
	public String getParent_bill_id() {
		return parent_bill_id;
	}
	public void setParent_bill_id(String parent_bill_id) {
		this.parent_bill_id = parent_bill_id;
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
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
