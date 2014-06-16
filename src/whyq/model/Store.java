package whyq.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable{

	private String id;
	private String storeId;
	private String address;
	private String longitude;
	private String latitude;
	private String status;
	private String radius;
	private String freeChargeOutRadiusDelieverPerKm;
	private String freeChargeOutRadiusDelieverPerOrder;
	private String createdate;
	private String updatedate;
	private String cateid;
	private String userId;
	private String nameStore;
	private String introStore;
	private String phoneStore;
	private String merchantNumber;
	private String logo;
	private String style;
	private String startTime;
	private String endTime;
	private boolean isHomeDeliver;
	private boolean isHotelDeliver;
	private boolean isTakeAway;
	private boolean isAtPlace;
	private String tableQuantity;
	private String countFavaouriteMember;
	private String coutBill;
	private String income;
	private String nameCate;
	private String distance;
	private String discription;
	private ArrayList<Photo> photos;
	private boolean isFavourite;
	private List<UserCheckBill> userCheckBillList;
	private UserCheckBill userCheckBill;
	private ArrayList<User> userList;
	private ArrayList<Menu> menuList;
	private ArrayList<Promotion> promotionList;

	private ArrayList<ProductTypeInfo> productTypeInfo;
	private ArrayList<Tag> tagList;
	private StoreInfo storeInfo;
	private String minimum;
	private boolean isPlace;
	private String countComment;
	private String hotelPlace;
	
	private boolean isCash;
	private boolean isEcoCash;
	private boolean isVisa;
	private boolean isVPayment;
	
	public String getCountComment() {
		return countComment;
	}
	public void setCountComment(String countComment) {
		this.countComment = countComment;
	}
	public String getHotelPlace() {
		return hotelPlace;
	}
	public void setHotelPlace(String hotelPlace) {
		this.hotelPlace = hotelPlace;
	}
	public Store() {
		// TODO Auto-generated constructor stub
	}
	public String getMinimum() {
		return minimum;
	}

	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}

	public boolean isPlace() {
		return isPlace;
	}

	public void setPlace(boolean isPlace) {
		this.isPlace = isPlace;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the radius
	 */
	public String getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(String radius) {
		this.radius = radius;
	}

	/**
	 * @return the freeChargeOutRadiusDelieverPerKm
	 */
	public String getFreeChargeOutRadiusDelieverPerKm() {
		return freeChargeOutRadiusDelieverPerKm;
	}

	/**
	 * @param freeChargeOutRadiusDelieverPerKm the freeChargeOutRadiusDelieverPerKm to set
	 */
	public void setFreeChargeOutRadiusDelieverPerKm(
			String freeChargeOutRadiusDelieverPerKm) {
		this.freeChargeOutRadiusDelieverPerKm = freeChargeOutRadiusDelieverPerKm;
	}

	/**
	 * @return the createdate
	 */
	public String getCreatedate() {
		return createdate;
	}

	/**
	 * @param createdate2 the createdate to set
	 */
	public void setCreatedate(String createdate2) {
		this.createdate = createdate2;
	}

	/**
	 * @return the updatedate
	 */
	public String getUpdatedate() {
		return updatedate;
	}

	/**
	 * @param updatedate2 the updatedate to set
	 */
	public void setUpdatedate(String updatedate2) {
		this.updatedate = updatedate2;
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
	 * @return the nameStore
	 */
	public String getNameStore() {
		return nameStore;
	}

	/**
	 * @param nameStore the nameStore to set
	 */
	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
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
	 * @return the isTakeAway
	 */
	public boolean isTakeAway() {
		return isTakeAway;
	}

	/**
	 * @param isTakeAway the isTakeAway to set
	 */
	public void setTakeAway(boolean isTakeAway) {
		this.isTakeAway = isTakeAway;
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
	 * @return the tableQuantity
	 */
	public String getTableQuantity() {
		return tableQuantity;
	}

	/**
	 * @param tableQuantity the tableQuantity to set
	 */
	public void setTableQuantity(String tableQuantity) {
		this.tableQuantity = tableQuantity;
	}

	/**
	 * @return the countFavaouriteMember
	 */
	public String getCountFavaouriteMember() {
		return countFavaouriteMember;
	}

	/**
	 * @param countFavouriteMemebr the countFavaouriteMember to set
	 */
	public void setCountFavaouriteMember(String countFavouriteMemebr) {
		this.countFavaouriteMember = countFavouriteMemebr;
	}

	/**
	 * @return the coutBill
	 */
	public String getCoutBill() {
		return coutBill;
	}

	/**
	 * @param coutBill2 the coutBill to set
	 */
	public void setCoutBill(String coutBill2) {
		this.coutBill = coutBill2;
	}

	/**
	 * @return the income
	 */
	public String getIncome() {
		return income;
	}

	/**
	 * @param inCome2 the income to set
	 */
	public void setIncome(String inCome2) {
		this.income = inCome2;
	}

	/**
	 * @return the nameCate
	 */
	public String getNameCate() {
		return nameCate;
	}

	/**
	 * @param nameCate the nameCate to set
	 */
	public void setNameCate(String nameCate) {
		this.nameCate = nameCate;
	}

	/**
	 * @return the userList
	 */
	public ArrayList<User> getUserList() {
		return userList;
	}

	/**
	 * @param userList the userList to set
	 */
	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

	/**
	 * @return the menuList
	 */
	public ArrayList<Menu> getMenuList() {
		return menuList;
	}

	/**
	 * @param menuList the menuList to set
	 */
	public void setMenuList(ArrayList<Menu> menuList) {
		this.menuList = menuList;
	}

	/**
	 * @return the productTypeInfo
	 */
	public ArrayList<ProductTypeInfo> getProductTypeInfo() {
		return productTypeInfo;
	}

	/**
	 * @param productTypeInfo the productTypeInfo to set
	 */
	public void setProductTypeInfo(ArrayList<ProductTypeInfo> productTypeInfo) {
		this.productTypeInfo = productTypeInfo;
	}

	/**
	 * @return the storeInfo
	 */
	public StoreInfo getStoreInfo() {
		return storeInfo;
	}

	/**
	 * @param storeInfo the storeInfo to set
	 */
	public void setStoreInfo(StoreInfo storeInfo) {
		this.storeInfo = storeInfo;
	}

	/**
	 * @return the tagList
	 */
	public ArrayList<Tag> getTagList() {
		return tagList;
	}

	/**
	 * @param tagList the tagList to set
	 */
	public void setTagList(ArrayList<Tag> tagList) {
		this.tagList = tagList;
	}
	/**
	 * @return the photos
	 */

	/**
	 * @return the isFavourite
	 */
	public boolean getIsFavourite() {
		return isFavourite;
	}
	/**
	 * @param isFavourite the isFavourite to set
	 */
	public void setIsFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}
	/**
	 * @return the userCheckBill
	 */
	public List<UserCheckBill> getUserCheckBillList() {
		return userCheckBillList;
	}
	/**
	 * @param userCheckBill the userCheckBill to set
	 */
	public void setUserCheckBillList(List<UserCheckBill> userchCheckBills) {
		this.userCheckBillList = userchCheckBills;
	}
	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}
	/**
	 * @param distance the distance to set
	 */
	public void setDistance(String distance) {
		this.distance = distance;
	}
	/**
	 * @return the discription
	 */
	public String getDiscription() {
		return discription;
	}
	/**
	 * @param discription the discription to set
	 */
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	/**
	 * @return the promotionList
	 */
	public ArrayList<Promotion> getPromotionList() {
		return promotionList;
	}
	/**
	 * @param promotionList the promotionList to set
	 */
	public void setPromotionList(ArrayList<Promotion> promotionList) {
		this.promotionList = promotionList;
	}
	/**
	 * @return the photos
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	/**
	 * @param photos the photos to set
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	/**
	 * @return the userCheckBill
	 */
	public UserCheckBill getUserCheckBill() {
		return userCheckBill;
	}
	/**
	 * @param userCheckBill the userCheckBill to set
	 */
	public void setUserCheckBill(UserCheckBill userCheckBill) {
		this.userCheckBill = userCheckBill;
	}
	public String getMerchantNumber() {
		return merchantNumber;
	}
	public void setMerchantNumber(String merchantNumber) {
		this.merchantNumber = merchantNumber;
	}
	public String getFreeChargeOutRadiusDelieverPerOrder() {
		return freeChargeOutRadiusDelieverPerOrder;
	}
	public void setFreeChargeOutRadiusDelieverPerOrder(
			String freeChargeOutRadiusDelieverPerOrder) {
		this.freeChargeOutRadiusDelieverPerOrder = freeChargeOutRadiusDelieverPerOrder;
	}

	public boolean isCash() {
		return isCash;
	}
	public void setCash(boolean isCash) {
		this.isCash = isCash;
	}
	public boolean isEcoCash() {
		return isEcoCash;
	}
	public void setEcoCash(boolean isEcoCash) {
		this.isEcoCash = isEcoCash;
	}
	public boolean isVisa() {
		return isVisa;
	}
	public void setVisa(boolean isVisa) {
		this.isVisa = isVisa;
	}
	public boolean isVPayment() {
		return isVPayment;
	}
	public void setVPayment(boolean isVPayment) {
		this.isVPayment = isVPayment;
	}
}
