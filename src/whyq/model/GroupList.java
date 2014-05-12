package whyq.model;

import java.util.List;

public class GroupList {
	public String name;
	public String color;
	public List<Store> groupListCollection;
	public void setMenuList(List<Store> list){
		this.groupListCollection = list;
	}
	public List<Store> getMenuList(){
		return this.groupListCollection;
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
