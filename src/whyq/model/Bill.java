package whyq.model;

import java.util.List;

public class Bill {
	private String id;
	private String thumb;
	private String productName;
	private String price;
	private String unit;
	private String discount;
	private String amount;
	private String productId;
	private List<OptionItem> optionList;
	private List<SizeItem> sizeList;
	private List<ExtraItem> extraList;
	private String deliveryFeeValue;
	private String note;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductName() {
		return productName != null ? productName : "";
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPrice() {
		return price != null ? price : "";
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	/**
	 * @return the thumb
	 */
	public String getThumb() {
		return thumb;
	}

	/**
	 * @param thumb
	 *            the thumb to set
	 */
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}

	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}

	/**
	 * @return the optionList
	 */
	public List<OptionItem> getOptionList() {
		return optionList;
	}

	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionList(List<OptionItem> optionList) {
		this.optionList = optionList;
	}

	/**
	 * @return the sizeList
	 */
	public List<SizeItem> getSizeList() {
		return sizeList;
	}

	/**
	 * @param sizeList the sizeList to set
	 */
	public void setSizeList(List<SizeItem> sizeList) {
		this.sizeList = sizeList;
	}

	/**
	 * @return the extraList
	 */
	public List<ExtraItem> getExtraList() {
		return extraList;
	}

	/**
	 * @param extraList the extraList to set
	 */
	public void setExtraList(List<ExtraItem> extraList) {
		this.extraList = extraList;
	}

	public String getNote() {
		return note == null? "": note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getDeliveryFeeValue() {
		return deliveryFeeValue;
	}

	public void setDeliveryFeeValue(String deliveryFeeValue) {
		this.deliveryFeeValue = deliveryFeeValue;
	}

}
