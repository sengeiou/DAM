package whyq.model;

import java.io.Serializable;

public class ProductTypeInfo implements Serializable {
	private String id;
	private String nameProductType;
	private String createDate;
	private String sort;
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
	 * @return the nameProductType
	 */
	public String getNameProductType() {
		return nameProductType;
	}
	/**
	 * @param nameProductType the nameProductType to set
	 */
	public void setNameProductType(String nameProductType) {
		this.nameProductType = nameProductType;
	}
	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
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
}
