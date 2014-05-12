package whyq.model;

public class Image {
	private String id;
	private String url;
	private String address;
	private String numberFavourite;
	private String description;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the numberFavourite
	 */
	public String getNumberFavourite() {
		return numberFavourite;
	}
	/**
	 * @param numberFavourite the numberFavourite to set
	 */
	public void setNumberFavourite(String numberFavourite) {
		this.numberFavourite = numberFavourite;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
}
