/**
 * 
 */
package whyq.model;

import java.io.Serializable;
public class Category implements Serializable {
	private static final long serialVersionUID = 234234L;
	
	
	private String id;
	private String name;
	
	
	/**
	 * Constructor
	 */
	
	public Category(){
		
	}
	
	
	public Category( String id){
		this.setId(id);
	}
	
	public Category( String id, String name ){
		this.setId(id);
		this.setName(name);

	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
