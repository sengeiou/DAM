package whyq.interfaces;

import whyq.model.User;

public interface JoinPerm_Delegate {
	void onSuccess(User user);
	void onError();
}
