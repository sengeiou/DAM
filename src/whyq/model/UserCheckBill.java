package whyq.model;

import java.io.Serializable;

public class UserCheckBill implements Serializable{
	private String totalFriend;
	private String totalMember;
	private String id;
	private String firstName;
	private String lastName;
	private String avatar;
	public String getTotalFriend() {
		return totalFriend;
	}
	public void setTotalFriend(String totalFriend) {
		this.totalFriend = totalFriend;
	}
	public String getTotalMember() {
		return totalMember;
	}
	public void setTotalMember(String totalMember) {
		this.totalMember = totalMember;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStatusBill() {
		return statusBill;
	}
	public void setStatusBill(String statusBill) {
		this.statusBill = statusBill;
	}
	public String getInteractionPoint() {
		return interactionPoint;
	}
	public void setInteractionPoint(String interactionPoint) {
		this.interactionPoint = interactionPoint;
	}
	private String storeId;
	private String statusBill;
	private String interactionPoint;
}
