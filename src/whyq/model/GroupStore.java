package whyq.model;

import java.util.ArrayList;
import java.util.List;

public class GroupStore {
	private String urlThumb;
	private String name;
	private String numberResults;
	private int typeCatelogy;
	private List<Store> storiesList;
	
	public GroupStore(){
		
	}
	
	public String getUrlThumb() {
		return urlThumb;
	}
	public void setUrlThumb(String urlThumb) {
		this.urlThumb = urlThumb;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumberResults() {
		return numberResults;
	}
	public void setNumberResults(String numberResults) {
		this.numberResults = numberResults;
	}
	public int getTypeCatelogy() {
		return typeCatelogy;
	}
	public void setTypeCatelogy(int typeCatelogy) {
		this.typeCatelogy = typeCatelogy;
	}

	/**
	 * @return the storiesList
	 */
	public List<Store> getStoriesList() {
		return storiesList;
	}

	/**
	 * @param storiesList2 the storiesList to set
	 */
	public void setStoriesList(List<Store> storiesList2) {
		this.storiesList = storiesList2;
	}
}
