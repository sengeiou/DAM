package whyq.model;

import java.util.List;

public class GroupMenu {
	public String name;
	public String color;
	public List<Menu> groupMenuCollection;

//	public GroupMenu()
//	{
//		groupMenuCollection = new ArrayList<Menu>();
//	}
	public void setMenuList(List<Menu> list){
		this.groupMenuCollection = list;
	}
	public List<Menu> getMenuList(){
		return this.groupMenuCollection;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setColor(String name){
		this.color = name;
	}
	public String getColor(){
		return this.color;
	}
}
