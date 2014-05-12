package whyq.model;

import java.io.Serializable;

public class WhyqBoard implements Serializable{
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1423424L;
	
	/**
	 * The board Id
	 */
	private String id;
	
	/**
	 * The board name
	 */
	private String name;
	
	/**
	 * The board description
	 */
	private String description;
	
	/**
	 * Number of board's followers 
	 */
	private int followers = 0;
	
	/**
	 * Number of board's pins;
	 */
	private int pins = 0;
	
	/**
	 * The category Id
	 */
	private String categoryId;
	
	/**
	 * Default constructor
	 */
	public WhyqBoard() {
		
	}
	
	/**
	 * Initialize a new Board with Id
	 * @param id the board Id
	 */
	public WhyqBoard( String id ){
		this.setId(id);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 */
	public WhyqBoard( String id, String name ){
		this.setId(id);
		this.setName(name);
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 */
	public WhyqBoard( String id, String name, String description){
		this.setId(id);
		this.setName( name );
		this.setDescription( description );
	}
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param followers
	 * @param pins
	 */
	public WhyqBoard(String id, String name, String description, int followers, int pins) {
		this.setId(id);
		this.setName( name );
		this.setDescription( description );
		this.setFollowers(followers);
		this.setPins(pins);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the followers
	 */
	public int getFollowers() {
		return followers;
	}

	/**
	 * @param followers the followers to set
	 */
	public void setFollowers(int followers) {
		this.followers = followers;
	}

	/**
	 * @return the pins
	 */
	public int getPins() {
		return pins;
	}

	/**
	 * @param pins the pins to set
	 */
	public void setPins(int pins) {
		this.pins = pins;
	}

	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
