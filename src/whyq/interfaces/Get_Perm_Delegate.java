package whyq.interfaces;

import java.util.ArrayList;

import whyq.model.Store;


public interface Get_Perm_Delegate {
	void onSuccess(ArrayList<Store> stores);
	void onError();
}
