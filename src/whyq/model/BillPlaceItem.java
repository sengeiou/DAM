package whyq.model;

public class BillPlaceItem {

	private String storeId;
	private int countVisited;
	private float totalValue;
	private float totalDiscountValue;
	private String storeName;
	private String storeAddress;
	private String storeLogo;
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public int getCountVisited() {
		return countVisited;
	}
	public void setCountVisited(int countVisited) {
		this.countVisited = countVisited;
	}
	public float getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(float totalValue) {
		this.totalValue = totalValue;
	}
	public float getTotalDiscountValue() {
		return totalDiscountValue;
	}
	public void setTotalDiscountValue(float totalDiscountValue) {
		this.totalDiscountValue = totalDiscountValue;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	public String getStoreLogo() {
		return storeLogo;
	}
	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}
	
}
