package whyq.interfaces;

import java.util.ArrayList;

import whyq.model.Store;


public interface Get_Board_delegate {
	void onSuccess(ArrayList<Store> stores);
	void onError();
}
