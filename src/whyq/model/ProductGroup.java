package whyq.model;

import java.util.ArrayList;

public class ProductGroup {
	public String groupName;
	public ArrayList<Product> productList;
	
	public ProductGroup() {
		productList = new ArrayList<Product>();
	}
}
