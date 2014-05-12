/**
 * 
 */
package whyq.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Whyqold implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String id;
	private String name;
	private String description;
	private String permDateMessage;
	private WhyqImage image;
	private List<Comment> comments;
	private User author;
	private WhyqBoard board;
	private String category;
	
	private String permRepinCount = "0";
	private String permLikeCount = "0";
	private String permCommentCount = "0";
	private String permUserLikeCount = "0";
	private String permUrl = "";
	private String permAudio="";
	private float lat;
	private float lon;
	
	private String address;
	private String distant;
	private ArrayList<User> visitedUser;
	// TODO :This is not good but for now, I will do it
	// This is for keeping the value of <nextItem> in the response when get the list of Perm
	private String nextItem;
	
	private String previousItem;	
	
	/**
	 * Constructor
	 */
	
	public Whyqold(){
		
	}
	
	
	public Whyqold( String id){
		this.setId(id);
	}
	
	public Whyqold( String id, String name, String description, String permDateMessage, WhyqImage image, String permUrl, String permAduio ){
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setImage(image);
		this.setPermDateMessage(permDateMessage);
		this.setPermUrl(permUrl);
		this.setPermAudio(permAduio);
	}
	
	
	private void setPermDateMessage( String permDateMessage) {
		// TODO Auto-generated method stub
		this.permDateMessage = permDateMessage;
	}


	public Whyqold( String id, WhyqBoard board, String description, String permDateMessage, WhyqImage image , ArrayList<Comment> comments, String permUrl, String permAudio){
		this.setId(id);
		this.setBoard(board);
		this.setDescription(description);
		this.setImage(image);
		this.setComments( comments );
		this.setPermDateMessage(permDateMessage);
		this.setPermUrl(permUrl);
		this.setPermAudio(permAudio);
	}
	
	
	
	public String toString(){
		return this.id;
	}
	
	/**
	 * Getters / Setters
	 */
	
	public void setComments(List<Comment> comments){
		this.comments = comments;
	}
	
	public List<Comment> getComments(){
		return this.comments;
	}
	
	public void addCommnent(Comment comment) {
		this.comments.add(comment);
		this.permCommentCount += 1;
	}

	public WhyqImage getImage() {
		return image;
	}

	public void setImage(WhyqImage image) {
		this.image = image;
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
	public String getPermDatemessage() {
		return permDateMessage;
	}	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public WhyqBoard getBoard() {
		return board;
	}

	public void setBoard(WhyqBoard board) {
		this.board = board;
	}
	public void setPermUrl( String url) {
		this.permUrl = url;
	}
	public String getPermUrl(){
		return this.permUrl;
	}
	public void setPermAudio( String url) {
		this.permAudio = url;
	}
	public String getPermAudio(){
		return this.permAudio;
	}
	/**
	 * @return the permRepinCount
	 */
	public String getPermRepinCount() {
		return permRepinCount;
	}


	/**
	 * @param permRepinCount the permRepinCount to set
	 */
	public void setPermRepinCount(String permRepinCount) {
		this.permRepinCount = permRepinCount;
	}


	/**
	 * @return the permLikecount
	 */
	public String getPermLikeCount() {
		return permLikeCount;
	}


	/**
	 * @param permLikecount the permLikecount to set
	 */
	public void setPermLikeCount(String permLikecount) {
		this.permLikeCount = permLikecount;
	}


	/**
	 * @return the permCommentCount
	 */
	public String getPermCommentCount() {
		return permCommentCount;
	}


	/**
	 * @param permCommentCount the permCommentCount to set
	 */
	public void setPermCommentCount(String permCommentCount) {
		this.permCommentCount = permCommentCount;
	}


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}


	public String getNextItem() {
		return nextItem;
	}


	public void setNextItem(String nextItem) {
		this.nextItem = nextItem;
	}

	public String getPreviousItem() {
		return previousItem;
	}


	public void setPreviousItem(String previousItem) {
		this.previousItem = previousItem;
	}

	/**
	 * @return the permUserLikeCount
	 */
	public String getPermUserLikeCount() {
		return permUserLikeCount;
	}


	/**
	 * @param permUserLikeCount the permUserLikeCount to set
	 */
	public void setPermUserLikeCount(String permUserLikeCount) {
		this.permUserLikeCount = permUserLikeCount;
	}
	

	public void setLat(float value) {
		// TODO Auto-generated method stub
		this.lat = value;
	}
	public void setLon(float value) {
		// TODO Auto-generated method stub
		this.lon = value;
	}
	public float getLat(){
		return this.lat;
	}
	public float getLon(){
		return this.lon;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getDistant() {
		return distant;
	}


	public void setDistant(String distant) {
		this.distant = distant;
	}


	public ArrayList<User> getVisitedUser() {
		return visitedUser;
	}


	public void setVisitedUser(ArrayList<User> visitedUser) {
		this.visitedUser = visitedUser;
	}
}
