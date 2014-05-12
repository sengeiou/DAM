package whyq.model;

import java.io.Serializable;

public class Tag implements Serializable{
	private String id;
	private String nameTag;
	private int countUsed;
	private String storeId;
	private String createDate;
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
	 * @return the countUsed
	 */
	public int getCountUsed() {
		return countUsed;
	}
	/**
	 * @param countUsed the countUsed to set
	 */
	public void setCountUsed(int countUsed) {
		this.countUsed = countUsed;
	}
	/**
	 * @return the nameTag
	 */
	public String getNameTag() {
		return nameTag;
	}
	/**
	 * @param nameTag the nameTag to set
	 */
	public void setNameTag(String nameTag) {
		this.nameTag = nameTag;
	}
	/**
	 * @return the createData
	 */
	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
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

}
