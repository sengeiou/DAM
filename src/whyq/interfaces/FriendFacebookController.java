package whyq.interfaces;

import java.util.List;

import whyq.model.FriendFacebook;

public interface FriendFacebookController {

	List<FriendFacebook> getListNotJoinWhyq();
	
	List<FriendFacebook> getListWhyq();
	
}
