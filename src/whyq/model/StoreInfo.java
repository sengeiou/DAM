package whyq.model;

import java.io.Serializable;

public class StoreInfo implements Serializable{
	private String id;
	private String cateid;
	private String userId;
	private String nameSore;
	private String introStore;
	private String phoneStore;
	private String logo;
	private String style;
	private String startTime;
	private String endTime;
	private boolean isHomeDeliver;
	private boolean isHotelDeliver;
	private boolean isTakeAray;
	private boolean isAtPlace;
	private String startTimeDeliver;
	private String endtimeDeliver;
	private String minimunTimeDeliever;
	private String valueConditionDeliver;
	private String tableWuantity;
	private String countFavouriteMember;
	private String countBill;
	private String income;
	private String status;
	private String createDate;
	private String updateDate;
	public String getStartTimeDeliver() {
		return startTimeDeliver;
	}
	public void setStartTimeDeliver(String startTimeDeliver) {
		this.startTimeDeliver = startTimeDeliver;
	}
	public String getEndtimeDeliver() {
		return endtimeDeliver;
	}
	public void setEndtimeDeliver(String endtimeDeliver) {
		this.endtimeDeliver = endtimeDeliver;
	}
	public String getMinimunTimeDeliever() {
		return minimunTimeDeliever;
	}
	public void setMinimunTimeDeliever(String string) {
		this.minimunTimeDeliever = string;
	}
	public String getTableWuantity() {
		return tableWuantity;
	}
	public void setTableWuantity(String tableWuantity) {
		this.tableWuantity = tableWuantity;
	}
	public String getCountFavouriteMember() {
		return countFavouriteMember;
	}
	public void setCountFavouriteMember(String string) {
		this.countFavouriteMember = string;
	}
	public String getCountBill() {
		return countBill;
	}
	public void setCountBill(String string) {
		this.countBill = string;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String string) {
		this.income = string;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String string) {
		this.status = string;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the cateid
	 */
	public String getCateid() {
		return cateid;
	}
	/**
	 * @param cateid the cateid to set
	 */
	public void setCateid(String cateid) {
		this.cateid = cateid;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the nameSore
	 */
	public String getNameSore() {
		return nameSore;
	}
	/**
	 * @param nameSore the nameSore to set
	 */
	public void setNameSore(String nameSore) {
		this.nameSore = nameSore;
	}
	/**
	 * @return the introStore
	 */
	public String getIntroStore() {
		return introStore;
	}
	/**
	 * @param introStore the introStore to set
	 */
	public void setIntroStore(String introStore) {
		this.introStore = introStore;
	}
	/**
	 * @return the phoneStore
	 */
	public String getPhoneStore() {
		return phoneStore;
	}
	/**
	 * @param phoneStore the phoneStore to set
	 */
	public void setPhoneStore(String phoneStore) {
		this.phoneStore = phoneStore;
	}
	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}
	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}
	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the isHomeDeliver
	 */
	public boolean isHomeDeliver() {
		return isHomeDeliver;
	}
	/**
	 * @param isHomeDeliver the isHomeDeliver to set
	 */
	public void setHomeDeliver(boolean isHomeDeliver) {
		this.isHomeDeliver = isHomeDeliver;
	}
	/**
	 * @return the isHotelDeliver
	 */
	public boolean isHotelDeliver() {
		return isHotelDeliver;
	}
	/**
	 * @param isHotelDeliver the isHotelDeliver to set
	 */
	public void setHotelDeliver(boolean isHotelDeliver) {
		this.isHotelDeliver = isHotelDeliver;
	}
	/**
	 * @return the isTakeAray
	 */
	public boolean isTakeAray() {
		return isTakeAray;
	}
	/**
	 * @param isTakeAray the isTakeAray to set
	 */
	public void setTakeAray(boolean isTakeAray) {
		this.isTakeAray = isTakeAray;
	}
	/**
	 * @return the isAtPlace
	 */
	public boolean isAtPlace() {
		return isAtPlace;
	}
	/**
	 * @param isAtPlace the isAtPlace to set
	 */
	public void setAtPlace(boolean isAtPlace) {
		this.isAtPlace = isAtPlace;
	}
	/**
	 * @return the valueConditionDeliver
	 */
	public String getValueConditionDeliver() {
		return valueConditionDeliver;
	}
	/**
	 * @param string the valueConditionDeliver to set
	 */
	public void setValueConditionDeliver(String string) {
		this.valueConditionDeliver = string;
	}
}
