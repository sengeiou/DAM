package whyq.interfaces;

import java.util.ArrayList;

import whyq.model.Category;


public interface get_category_delegate {
	public void onCompletedGetCategory( ArrayList<Category> categories);
}
