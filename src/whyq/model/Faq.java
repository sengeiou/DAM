package whyq.model;

public class Faq {
	private String id;
	private String contentQuestion;
	private String answerQuestion;
	private String isPublic;
	private String createDate;
	private String updateDate; 
	public Faq() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
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

	/**
	 * @return the isPublic
	 */
	public String getIsPublic() {
		return isPublic;
	}

	/**
	 * @param isPublic the isPublic to set
	 */
	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * @return the answerQuestion
	 */
	public String getAnswerQuestion() {
		return answerQuestion;
	}

	/**
	 * @param answerQuestion the answerQuestion to set
	 */
	public void setAnswerQuestion(String answerQuestion) {
		this.answerQuestion = answerQuestion;
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
	 * @return the contentQuestion
	 */
	public String getContentQuestion() {
		return contentQuestion;
	}

	/**
	 * @param contentQuestion the contentQuestion to set
	 */
	public void setContentQuestion(String contentQuestion) {
		this.contentQuestion = contentQuestion;
	}



}
