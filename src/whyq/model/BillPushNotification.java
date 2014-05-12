package whyq.model;

import java.io.Serializable;

public class BillPushNotification implements Serializable{
	private String storyId;
	private String deliverType;
	private String type;
	private String billId;
	private String timeDeliver;
	private String nameStore;
	private String alert;
	private String pushHash;
	public String getStoryId() {
		return storyId;
	}
	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}
	public String getDeliverType() {
		return deliverType;
	}
	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getTimeDeliver() {
		return timeDeliver;
	}
	public void setTimeDeliver(String timeDeliver) {
		this.timeDeliver = timeDeliver;
	}
	public String getNameStore() {
		return nameStore;
	}
	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}
	public String getAlert() {
		return alert;
	}
	public void setAlert(String alert) {
		this.alert = alert;
	}
	public String getPushHash() {
		return pushHash;
	}
	public void setPushHash(String pushHash) {
		this.pushHash = pushHash;
	}
}
